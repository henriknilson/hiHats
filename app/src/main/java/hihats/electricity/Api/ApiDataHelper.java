package hihats.electricity.api;

import android.location.Location;
import java.util.Date;

/**
 * Created by fredrikkindstrom on 24/09/15.
 */
public class ApiDataHelper {

    /**
     * Returns the last known location for a certain bus.
     * @param busId The bus you want to get current location for.
     * @return The most recent location for said bus as an Android Location object.
     */
    public static Location getMostRecentLocationForBus(String busId) throws AccessErrorException {
        ApiDataObject rawData = ApiHttpHandler.getMostRecentLocationForBus(busId);
        String data = rawData.getValue();
        //Location location = new Location(busId);
        double latitude;
        double longitude;
        System.out.println(data);
        if (data.startsWith("$GPRMC")) {
            String[] strValues = data.split(",");

            // Set latitude
            latitude = Double.parseDouble(strValues[3].substring(2))/60.0;
            latitude +=  Double.parseDouble(strValues[3].substring(0, 2));
            if (strValues[4].charAt(0) == 'S') {
                latitude = -latitude;
            }
            //location.setLatitude(latitude);

            // Set longitude
            longitude = Double.parseDouble(strValues[5].substring(3))/60.0;
            longitude +=  Double.parseDouble(strValues[5].substring(0, 3));
            if (strValues[6].charAt(0) == 'W') {
                longitude = -longitude;
            }
            //location.setLongitude(longitude);

            // Set speed
            float speed = (Float.parseFloat(strValues[7])*1.85200f);
            //location.setSpeed(speed);

            // Set bearing
            float bearing = Float.parseFloat(strValues[8]);
            //location.setBearing(bearing);

            System.out.println(new Date(Long.parseLong(rawData.getTimestamp())));
            System.out.println("latitude="+latitude+" ; longitude="+longitude+" ; speed = "+speed+" ; bearing = "+bearing);
        }
        return null;
    }
}
