package hihats.electricity.api;

import android.location.Location;

/**
 * Created by fredrikkindstrom on 24/09/15.
 */
public class ApiDataHandler {

    public static Location getMostRecentLocationForBus(String busId) throws AccessErrorException {
        ApiDataObject rawData = ApiHttpHandler.getMostRecentLocationForBus(busId);
        String data = rawData.getValue();
        System.out.println(data);
        if (data.startsWith("$GPRMC")) {
            String[] strValues = data.split(",");
            double latitude = Double.parseDouble(strValues[3].substring(2))/60.0f;
            latitude +=  Double.parseDouble(strValues[3].substring(0, 2));
            if (strValues[4].charAt(0) == 'S') {
                latitude = -latitude;
            }
            double longitude = Double.parseDouble(strValues[5].substring(3))/60.0f;
            longitude +=  Double.parseDouble(strValues[5].substring(0, 3));
            if (strValues[6].charAt(0) == 'W') {
                longitude = -longitude;
            }
            double course = Double.parseDouble(strValues[8]);
            System.out.println("latitude="+latitude+" ; longitude="+longitude+" ; course = "+course);
        }
        return null;
    }
}
