package hihats.electricity.util;

import android.location.Location;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class RmcConverter {

    static Location rmcToLocation(String rmc) {

        Location loc = new Location("");

        if (rmc.startsWith("$GPRMC")) {
            String[] gpsValues = rmc.split(",");

            // Set latitude
            double latitude = Double.parseDouble(gpsValues[3].substring(2))/60.0;
            latitude +=  Double.parseDouble(gpsValues[3].substring(0, 2));
            if (gpsValues[4].charAt(0) == 'S') {
                latitude = -latitude;
            }
            loc.setLatitude(latitude);

            // Set longitude
            double longitude = Double.parseDouble(gpsValues[5].substring(3))/60.0;
            longitude +=  Double.parseDouble(gpsValues[5].substring(0, 3));
            if (gpsValues[6].charAt(0) == 'W') {
                longitude = -longitude;
            }
            loc.setLongitude(longitude);

            // Set speed
            float speed = (Float.parseFloat(gpsValues[7])*1.85200f);
            loc.setSpeed(speed);

            // Set bearing
            float bearing = Float.parseFloat(gpsValues[8]);
            loc.setBearing(bearing);
        }
        return loc;
    }
}
