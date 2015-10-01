package hihats.electricity.util;

import java.util.Date;

import hihats.electricity.model.Location;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class RmcConverter {

    static Location rmcToLocation(String rmc, String timestamp) {
        System.out.println(rmc);
        if (rmc.startsWith("$GPRMC")) {
            Location loc = new Location();
            String[] gpsValues = rmc.split(",");

            try {
                // Set latitude
                double latitude = Double.parseDouble(gpsValues[3].substring(2)) / 60.0;
                latitude += Double.parseDouble(gpsValues[3].substring(0, 2));
                if (gpsValues[4].charAt(0) == 'S') {
                    latitude = -latitude;
                }
                loc.setLatitude(latitude);

                // Set longitude
                double longitude = Double.parseDouble(gpsValues[5].substring(3)) / 60.0;
                longitude += Double.parseDouble(gpsValues[5].substring(0, 3));
                if (gpsValues[6].charAt(0) == 'W') {
                    longitude = -longitude;
                }
                loc.setLongitude(longitude);
            } catch (StringIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Illegal GPRMC format");
            }
            // Set time
            Date time = new Date(Long.parseLong(timestamp));
            loc.setTime(time);

            return loc;
        } else {
            throw new IllegalArgumentException("Needs to me in GPRMC format");
        }
    }

    static float rmcToSpeed(String rmc) {
        if (rmc.startsWith("$GPRMC")) {
            String[] gpsValues = rmc.split(",");
            return (Float.parseFloat(gpsValues[7])*1.85200f);
        } else {
            throw new IllegalArgumentException("Needs to me in GPRMC format");
        }
    }

    static float rmcToBearing(String rmc) {
        if (rmc.startsWith("$GPRMC")) {
            String[] gpsValues = rmc.split(",");
            return Float.parseFloat(gpsValues[8]);
        } else {
            throw new IllegalArgumentException("Needs to me in GPRMC format");
        }
    }
}
