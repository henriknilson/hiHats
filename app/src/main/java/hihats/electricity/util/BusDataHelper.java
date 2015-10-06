package hihats.electricity.util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import hihats.electricity.model.Bus;
import hihats.electricity.model.SimpleLocation;
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

    private final UrlRetriever urlRetriever = new UrlRetriever();
    private final HttpHandler httpHandler = new HttpHandler();

    /*
    Util methods
     */

    /**
     * Updates the bus object with the last known complete data for the bus.
     * @param bus The bus you want to update data for.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public void updateDataForBus(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, GPS_RMC, 5000);
        String response = httpHandler.getResponse(url);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ApiDataObject data = rawData.get(0);
        SimpleLocation loc = RmcConverter.rmcToLocation(data.getValue(), data.getTimestamp());
        float speed = RmcConverter.rmcToSpeed(data.getValue());
        float bearing = RmcConverter.rmcToBearing(data.getValue());
        bus.setSimpleLocation(loc);
        bus.setSpeed(speed);
        bus.setBearing(bearing);
    }

    /**
     * Returns the last known complete data for every available bus.
     * @return A Bus object array containing all buses with all the data obtained.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public ArrayList<Bus> getLastDataForAllBuses() throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(null, null, GPS_RMC, 10000);
        String response = httpHandler.getResponse(url);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ArrayList<Bus> buses = new ArrayList<>();
        for (ApiDataObject o : rawData) {
            String id = o.getGatewayId();
            SimpleLocation loc = RmcConverter.rmcToLocation(o.getValue(), o.getTimestamp());
            Bus bus = new Bus(id);
            bus.setSimpleLocation(loc);
            buses.add(bus);
        }
        return buses;
    }

    /**
     * Returns the last known location for a certain bus.
     * @param bus The bus you want to get data for.
     * @return The most recent location for said bus as a SimpleLocation object.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public SimpleLocation getLastLocationForBus(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, GPS_RMC, 5000);
        String response = httpHandler.getResponse(url);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        ApiDataObject data = rawData.get(0);
        return RmcConverter.rmcToLocation(data.getValue(), data.getTimestamp());
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
        System.out.println(url);
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
    public int getTotalDistanceForBus(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, TOTAL_VEHICLE_DISTANCE, 10000);
        String response = httpHandler.getResponse(url);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        int data = Integer.parseInt(rawData.get(0).getValue());
        return data * 5;
    }

    /**
     * Returns if the bus is currently located at a bus stop or not.
     * @param bus The bus you want to get data for.
     * @return True if the bus is at a bus stop, false if not.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     * @throws NoDataException When the http request was successful but no data was found.
     */
    public boolean isBusAtStop(Bus bus) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(bus.getDgw(), null, AT_STOP, 30000);
        String response = httpHandler.getResponse(url);
        ArrayList<ApiDataObject> rawData = parseFromJSON(response);
        System.out.println(rawData.size());
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