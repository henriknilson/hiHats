package hihats.electricity.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import hihats.electricity.model.BusFactory;
import hihats.electricity.model.DatedPosition;
import hihats.electricity.model.IBus;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.HttpHandler;
import hihats.electricity.net.NoDataException;
import hihats.electricity.net.ElectriCityUrlRetriever;

/**
 * This class converts the data obtained by the net class package
 * to useful formats to use further up in the application tree.
 * This is the class that gets called from outside the net/util package.
 */
public class BusDataHelper {

    private final String GPS_RMC = "Ericsson$RMC_Value";
    private final String TOTAL_VEHICLE_DISTANCE = "Ericsson$Total_Vehicle_Distance_Value";
    private final String AT_STOP = "Ericsson$At_Stop_Value";
    private final String NEXT_STOP = "Ericsson$Bus_Stop_Name_Value";
    private final float BUS_DISTANCE_METERS = 5000.0f;

    private final ElectriCityUrlRetriever urlRetriever = new ElectriCityUrlRetriever();
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
    public boolean isBusAtStop(IBus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, AT_STOP, 30000);
        String response = httpHandler.getResponse(url);
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
     * Returns a Bus object that is closest to the given location.
     * Calls the API to get all the active buses and then compare distances
     * to obtain the bus that is closest to the given location.
     * This will probably be the bus the user is riding.
     * @param location A location to compare to all the buses in the system.
     * @return A bus object if there is any buses within 20 meters from the given location, null if not.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public IBus getBusNearestLocation(Location location) throws AccessErrorException, NoDataException {
        ArrayList<IBus> allBuses = getLastDataForAllBuses();
        for (IBus bus : allBuses) {
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
    public ArrayList<IBus> getLastDataForAllBuses() throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(null, null, GPS_RMC, 8000);
        String response = httpHandler.getResponse(url);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ArrayList<IBus> buses = new ArrayList<>();
        for (ApiDataObject o : rawData) {
            String id = o.getGatewayId();
            // Ignore stupid test bus for now
            if (!id.equals("Vin_Num_001")) {
                DatedPosition loc;
                float bearing;
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
                IBus bus = BusFactory.getInstance().getBus("Ericsson$" + id);
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
    public DatedPosition getLastPositionForBus(IBus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, GPS_RMC, 5000);
        String response = httpHandler.getResponse(url);
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
    public String getNextStopForBus(IBus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, NEXT_STOP, 15000);
        String response = httpHandler.getResponse(url);
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
    public int getTotalDistanceForBus(IBus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, TOTAL_VEHICLE_DISTANCE, 10000);
        String response = httpHandler.getResponse(url);
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
    public void updateDataForBus(IBus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, GPS_RMC, 5000);
        String response = httpHandler.getResponse(url);
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