package hihats.electricity.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import hihats.electricity.model.Bus;
import hihats.electricity.model.BusFactory;
import hihats.electricity.model.DatedPosition;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.HttpHandler;
import hihats.electricity.net.NoDataException;
import hihats.electricity.net.UrlRetriever;

/**
 * This class converts the data obtained by the net class
 * HttpHandler to useful formats to use further up in the application tree.
 * This is the class that gets called from outside the net/util package.
 */
public class BusDataHelper {

    private final String GPS_RMC = "Ericsson$RMC_Value";
    private final String TOTAL_VEHICLE_DISTANCE = "Ericsson$Total_Vehicle_Distance_Value";
    private final String AT_STOP = "Ericsson$At_Stop_Value";
    private final String NEXT_STOP = "Ericsson$Bus_Stop_Name_Value";
    private final float BUS_DISTANCE_METERS = 50.0f;
    private final String ICOMERA = "https://ombord.info/api/xml/system/";

    private final UrlRetriever urlRetriever = new UrlRetriever();
    private final HttpHandler httpHandler = new HttpHandler();

    /*
    Boolean methods
     */

    /**
     * Returns if the bus is currently located at a bus stop or not.
     * @param bus The bus you want to get data for.
     * @return True if the bus is at a bus stop, false if not.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public boolean isBusAtStop(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, AT_STOP, 30000);
        String response = httpHandler.getResponse(url, true);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ApiDataObject data = rawData.get(0);
        switch (data.getValue()) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new NoDataException();
        }
    }

    /**
     * Returns if the device is connected to a wifi network or not.
     * @param context The context of the application, get this from MainActivity.
     * @return True if the device is connected to wifi, false if not
     */
    public boolean isConnectedToWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetwork != null && wifiNetwork.isConnected();
    }

    /**
     * Returns if the device is able to use the GPS or not.
     * @param context The context of the application, get this from MainActivity.
     * @return True if the device is able to use GPS, false if not
     */
    public boolean isGPSEnabled(Context context) {
        LocationManager locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /*
    Get data methods
     */

    /**
     * Returns a Bus object that matches a System Id obtained from the bus onboard network API.
     * This method can only be successful when the device is connected to the bus onboard wifi.
     * @return A bus matching the SystemId if its valid, null if not.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public Bus getBusFromSystemId() throws AccessErrorException, NoDataException {
        String response = httpHandler.getResponse(ICOMERA, false);
        String result = parseFromXML(response);
        System.out.println(response);
        if (result != null) {
            try {
                return BusFactory.getInstance().getBus(result);
            } catch (IllegalArgumentException e) {
                throw new NoDataException();
            }
        } else {
            throw new AccessErrorException();
        }
    }

    /**
     * Returns a Bus object that is closest to the given location.
     * Calls the API to get all the active buses and then compare distances
     * to obtain the bus that is closest to the given location.
     * This will probably be the bus the user is riding.
     * @param location A location to compare to all the buses in the system.
     * @return A bus object if there is any buses within 20 meters from the given location, null if not.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public Bus getBusNearestLocation(Location location) throws AccessErrorException, NoDataException {
        ArrayList<Bus> allBuses = getLastDataForAllBuses();
        for (Bus bus : allBuses) {
            if (bus.getDatedPosition() != null && location != null) {
                float[] distanceBetweenBuses = new float[1];
                Location.distanceBetween(
                        bus.getDatedPosition().getLatitude(),
                        bus.getDatedPosition().getLongitude(),
                        location.getLatitude(),
                        location.getLongitude(),
                        distanceBetweenBuses);
                System.out.println("Distance between 'DEVICE' and '" + bus.getRegNr() + "' is " + distanceBetweenBuses[0] + " meters");
                if (distanceBetweenBuses[0] < BUS_DISTANCE_METERS) {
                    System.out.println("FOUND BUS '" + bus.getRegNr() + "' is " + distanceBetweenBuses[0] + " meters away");
                    return bus;
                }
            }
        }
        return null;
    }

    /**
     * Returns the last known complete data for every available bus.
     * @return A Bus object array containing all buses with all the data obtained.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public ArrayList<Bus> getLastDataForAllBuses() throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(null, null, GPS_RMC, 8000);
        String response = httpHandler.getResponse(url, true);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ArrayList<Bus> buses = new ArrayList<>();
        for (ApiDataObject o : rawData) {
            String id = o.getGatewayId();
            // Ignore stupid test bus for now
            if (!id.equals("Vin_Num_001")) {
                DatedPosition loc;
                float bearing = 0f;
                try {
                    loc = RmcConverter.rmcToPosition(o.getValue(), o.getTimestamp());
                } catch (IllegalArgumentException e) {
                    loc = null;
                }
                try {
                    bearing = RmcConverter.rmcToBearing(o.getValue());
                } catch (IllegalArgumentException e) {
                    bearing = 0f;
                }
                Bus bus = BusFactory.getInstance().getBus("Ericsson$" + id);
                bus.setDatedPosition(loc);
                bus.setBearing(bearing);
                buses.add(bus);
            }
        }
        return buses;
    }

    /**
     * Returns the last known position for a certain bus.
     * @param bus The bus you want to get data for.
     * @return The most recent location for said bus as a DatedPosition object.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public DatedPosition getLastPositionForBus(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, GPS_RMC, 5000);
        String response = httpHandler.getResponse(url, true);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ApiDataObject data = rawData.get(0);
        return RmcConverter.rmcToPosition(data.getValue(), data.getTimestamp());
    }

    /**
     * Returns the name of the next bus stop for a bus.
     * @param bus The bus you want to get data for.
     * @return A String with the name of the bus stop.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public String getNextStopForBus(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, NEXT_STOP, 15000);
        String response = httpHandler.getResponse(url, true);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ApiDataObject data = rawData.get(0);
        return data.getValue();
    }

    /**
     * Returns the total distance traveled for a certain bus.
     * @param bus The bus you want to get data for.
     * @return The total distance in meters.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public int getTotalDistanceForBus(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, TOTAL_VEHICLE_DISTANCE, 10000);
        String response = httpHandler.getResponse(url, true);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        int data = Integer.parseInt(rawData.get(0).getValue());
        return data * 5;
    }

    /*
    Void update methods
     */

    /**
     * Updates the bus object with the last known complete data for the bus.
     * @param bus The bus you want to update data for.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public void updateDataForBus(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, GPS_RMC, 5000);
        String response = httpHandler.getResponse(url, true);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ApiDataObject data = rawData.get(0);
        DatedPosition loc = RmcConverter.rmcToPosition(data.getValue(), data.getTimestamp());
        float speed = RmcConverter.rmcToSpeed(data.getValue());
        float bearing = RmcConverter.rmcToBearing(data.getValue());
        bus.setDatedPosition(loc);
        bus.setSpeed(speed);
        bus.setBearing(bearing);
    }

    /*
    Help methods
     */

    private String parseFromXML(String xml) throws NoDataException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        final String GROUP = "system";
        final String VALUE = "system_id";
        String result = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new NoDataException();
        }
        if (document != null) {
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName(GROUP);
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    result = eElement.getElementsByTagName(VALUE).item(0).getTextContent();
                }
            }
            return result;
        } else {
            return null;
        }
    }
    private ArrayList<ApiDataObject> parseFromJSON(String s) throws NoDataException {
        Gson gson = new Gson();
        ApiDataObject[] fromStream = gson.fromJson(s, ApiDataObject[].class);
        ArrayList<ApiDataObject> dataObjects = new ArrayList<>();
        try {
            Collections.addAll(dataObjects, fromStream);
        } catch (NullPointerException e) {
            throw new NoDataException();
        }
        return dataObjects;
    }
    private class ApiDataObject {

        private final String resourceSpec;
        private final String timestamp;
        private final String value;
        private final String gatewayId;

        public ApiDataObject(String resourceSpec, String timestamp, String value, String gatewayId) {
            this.resourceSpec = resourceSpec;
            this.timestamp = timestamp;
            this.value = value;
            this.gatewayId = gatewayId;
        }

        public String toString() {
            return "resourceSpec=" + resourceSpec + " timestamp=" + timestamp + " value=" + value + " gatewayId=" + gatewayId;
        }

        public String getResourceSpec() {
            return resourceSpec;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getValue() {
            return value;
        }

        public String getGatewayId() {
            return gatewayId;
        }
    }

}