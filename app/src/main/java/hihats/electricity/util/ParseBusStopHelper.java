package hihats.electricity.util;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Pertta on 15-10-01.
 */
@ParseClassName("ParseBusStopHelper")
public class ParseBusStopHelper extends ParseObject {

    public ParseBusStopHelper () {
    }

    /*
    Getters
     */

    public String getStopName() {
        return getString("Name");
    }

    public Double getLat() {
        return getDouble("Latitude");
    }

    public Double getLng() {
        return getDouble("Longitude");
    }

    public int getOrder() {
        return getInt("inOrder");
    }
}
