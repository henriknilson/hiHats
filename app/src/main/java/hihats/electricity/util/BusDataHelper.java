package hihats.electricity.util;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collection;

import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.ApiDataObject;
import hihats.electricity.net.HttpHandler;
import hihats.electricity.net.UrlRetriever;

/**
 * This class converts the data obtained by the net class
 * HttpHandler to useful formats to use further up in the application tree.
 * This is the class that gets called from outside the net/util package.
 */
public class BusDataHelper {

    private String GPS_RMC = "Ericsson$RMC_Value";
    private String TOTAL_VEHICLE_DISTANCE = "Ericsson$Total_Vehicle_Distance_Value";

    // Initialize the URLRetriever
    private UrlRetriever urlRetriever = new UrlRetriever();

    // Initialize the HttpHandler
    private HttpHandler httpHandler = new HttpHandler();

    /**
     * Returns the last known location for a certain bus.
     * @param busId The bus you want to get data for.
     * @return The most recent location for said bus as an Android Location object.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     */
    public Location getCurrentLocationForBus(String busId) throws AccessErrorException {
        String url = urlRetriever.getUrl(busId, null, GPS_RMC, 5000);
        ArrayList<ApiDataObject> rawData = httpHandler.getResponse(url);
        String gpsData = rawData.get(0).getValue();
        return RmcConverter.rmcToLocation(gpsData);
    }

    public ArrayList<String> getCurrentLocationForAllBuses() throws AccessErrorException {
        ArrayList<String> response = new ArrayList<>();
        String url = urlRetriever.getUrl(null, null, GPS_RMC, 10000);
        ArrayList<ApiDataObject> rawData = httpHandler.getResponse(url);
        System.out.println(rawData.size());
        for (ApiDataObject o : rawData) {
            response.add(o.toString());
        }
        return response;
    }

    /**
     * Returns the total distance traveled for a certain bus.
     * @param busId The bus you want to get data for.
     * @return The total distance in meters.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     */
    public int getTotalDistanceForBus(String busId) throws AccessErrorException {
        String url = urlRetriever.getUrl(busId, null, TOTAL_VEHICLE_DISTANCE, 10000);
        ArrayList<ApiDataObject> rawData = httpHandler.getResponse(url);
        int data = Integer.parseInt(rawData.get(0).getValue());
        return data * 5;
    }
}
