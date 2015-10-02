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

    public String getStopName() {
        return getString("Name");
    }

    public Number getLat() {
        return getDouble("Latitude");
    }

    public Number getLng() {
        return getDouble("Longitude");
    }

}
