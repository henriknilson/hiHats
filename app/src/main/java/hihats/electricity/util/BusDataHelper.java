package hihats.electricity.util;

import android.location.Location;

import java.util.ArrayList;

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
        String data = rawData.get(0).getValue();
        Location loc = new Location(busId);

        if (data.startsWith("$GPRMC")) {
            String[] strValues = data.split(",");

            // Set latitude
            double latitude = Double.parseDouble(strValues[3].substring(2))/60.0;
            latitude +=  Double.parseDouble(strValues[3].substring(0, 2));
            if (strValues[4].charAt(0) == 'S') {
                latitude = -latitude;
            }
            loc.setLatitude(latitude);

            // Set longitude
            double longitude = Double.parseDouble(strValues[5].substring(3))/60.0;
            longitude +=  Double.parseDouble(strValues[5].substring(0, 3));
            if (strValues[6].charAt(0) == 'W') {
                longitude = -longitude;
            }
            loc.setLongitude(longitude);

            // Set speed
            float speed = (Float.parseFloat(strValues[7])*1.85200f);
            loc.setSpeed(speed);

            // Set bearing
            float bearing = Float.parseFloat(strValues[8]);
            loc.setBearing(bearing);
        }
        return loc;
    }

    public ArrayList<Location> getCurrentLocationForAllBusses() throws AccessErrorException {
        String url = urlRetriever.getUrl(null, null, GPS_RMC, 5000);
        return null;
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
