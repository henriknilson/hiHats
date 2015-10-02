package hihats.electricity.util;

import java.util.Date;

import hihats.electricity.model.Location;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class RmcConverter {

    /**
     * Parses the GPRMC string to a Location object.
     * @param rmc The string to parse.
     * @param timestamp The timestamp milliseconds.
     * @return A location object with latitude, longitude and date.
     */
    public static Location rmcToLocation(String rmc, String timestamp) {
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
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                throw new IllegalArgumentException("Illegal GPRMC format: " + rmc);
            }

            // Set time
            Date time = new Date(Long.parseLong(timestamp));
            loc.setDate(time);

            return loc;
        } else {
            throw new IllegalArgumentException("Input must be in GPRMC format");
        }
    }

    /**
     * Parses the GPRMC string to a float speed number.
     * @param rmc The string to parse.
     * @return A float object representing the speed.
     */
    public static float rmcToSpeed(String rmc) {
        if (rmc.startsWith("$GPRMC")) {
            String[] gpsValues = rmc.split(",");
            return (Float.parseFloat(gpsValues[7])*1.85200f);
        } else {
            throw new IllegalArgumentException("Illegal GPRMC format: " + rmc);
        }
    }

    /**
     * Parses the GPRMC string to a float bearing number.
     * @param rmc The string to parse.
     * @return A float object representing the bearing.
     */
    public static float rmcToBearing(String rmc) {
        if (rmc.startsWith("$GPRMC")) {
            String[] gpsValues = rmc.split(",");
            return Float.parseFloat(gpsValues[8]);
        } else {
            throw new IllegalArgumentException("Illegal GPRMC format: " + rmc);
        }
    }
}
