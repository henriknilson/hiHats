package hihats.electricity.database;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import hihats.electricity.model.IBusStop;

/**
 * Created by axel on 2015-09-24.
 */
@ParseClassName("BusStop")
public class ParseBusStop extends ParseObject implements IBusStop, Comparable {

    public ParseBusStop() {}

    public LatLng getLatLng() {
        return new LatLng(getInt("latitude"), getInt("longitude"));
    }

    public String getName() {
        return getString("name");
    }

    public int getOrder() {
        return getInt("order");
    }

    @Override
    public int compareTo(Object o) {
        return getOrder() - ((ParseBusStop)o).getOrder();
    }
}
