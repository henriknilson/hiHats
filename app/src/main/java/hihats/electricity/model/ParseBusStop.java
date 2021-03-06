package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * This class is a representation of a BusStop in the application, designed to work
 * with the parse database.
 */

@ParseClassName("BusStop")
public class ParseBusStop extends ParseObject implements IBusStop, Comparable<IBusStop> {

    public ParseBusStop() {}

    public LatLng getLatLng() {
        return new LatLng(getDouble("latitude"), getDouble("longitude"));
    }

    public String getName() {
        return getString("name");
    }

    public int getOrder() {
        return getInt("order");
    }

    public void setLat(double lat) {
        put("latitude", lat);
    }

    public void setLng(double lng) {
        put("longitude", lng);
    }

    public void setLatLng(double lat, double lng) {
        setLat(lat);
        setLng(lng);
    }

    public void setLatLng(LatLng latLng) {
        setLat(latLng.latitude);
        setLng(latLng.longitude);
    }

    public void setName(String name) {
        if(name == null) {
            name = "";
        }
        put("name", name);
    }

    public void setOrder(int order) {
        put("order", order);
    }

    @Override
    public int compareTo(IBusStop iBusStop) {
        return getOrder() - iBusStop.getOrder();
    }

}
