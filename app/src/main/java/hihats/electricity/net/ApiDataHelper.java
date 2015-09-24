package hihats.electricity.net;

import android.location.Location;

/**
 * This class converts the data obtained by the backend class
 * ApiHttpHandler to useful formats to use further up in the application.
 * This is the class that gets called from outside the api package.
 */
public class ApiDataHelper {

    /**
     * Returns the last known location for a certain bus.
     * @param busId The bus you want to get data for.
     * @return The most recent location for said bus as an Android Location object.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     */
    public static Location getMostRecentLocationForBus(String busId) throws AccessErrorException {
        ApiDataObject rawData = ApiHttpHandler.getMostRecentLocationForBus(busId);
        String data = rawData.getValue();
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

    /**
     * Returns the total distance traveled for a certain bus.
     * @param busId The bus you want to get data for.
     * @return The total distance in meters.
     * @throws AccessErrorException When the http request failed and the data can not be obtained.
     */
    public static int getTotalDistanceForBus(String busId) throws AccessErrorException {
        ApiDataObject rawData = ApiHttpHandler.getTotalDistanceForBus(busId);
        int data = Integer.parseInt(rawData.getValue());
        return data*5;
    }
}
