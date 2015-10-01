package hihats.electricity.util;

import java.util.ArrayList;

import hihats.electricity.model.Bus;
import hihats.electricity.model.Location;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.ApiDataObject;
import hihats.electricity.net.HttpHandler;
import hihats.electricity.net.NoDataException;
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
    public Location getCurrentLocationForBus(String busId) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(busId, null, GPS_RMC, 5000);
        ArrayList<ApiDataObject> rawData = httpHandler.getResponse(url);
        ApiDataObject data = rawData.get(0);
        return RmcConverter.rmcToLocation(data.getValue(), data.getTimestamp());
    }

    public ArrayList<Bus> getCurrentDataForAllBuses() throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(null, null, GPS_RMC, 10000);
        ArrayList<ApiDataObject> rawData = httpHandler.getResponse(url);
        ArrayList<Bus> buses = new ArrayList<>();
        for (ApiDataObject o : rawData) {
            String busId = o.getGatewayId();
            Location loc = RmcConverter.rmcToLocation(o.getValue(), o.getTimestamp());
            float speed = RmcConverter.rmcToSpeed(o.getValue());
            float bearing = RmcConverter.rmcToBearing(o.getValue());
            buses.add(new Bus(busId, loc, speed, bearing));
        }
        return buses;
    }

    /**
     * Returns the total distance traveled for a certain bus.
     * @param busId The bus you want to get data for.
     * @return The total distance in meters.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     */
    public int getTotalDistanceForBus(String busId) throws AccessErrorException, NoDataException {
        String url = urlRetriever.getUrl(busId, null, TOTAL_VEHICLE_DISTANCE, 10000);
        ArrayList<ApiDataObject> rawData = httpHandler.getResponse(url);
        int data = Integer.parseInt(rawData.get(0).getValue());
        return data * 5;
    }
}
