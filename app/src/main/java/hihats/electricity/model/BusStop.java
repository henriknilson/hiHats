package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fredrikkindstrom on 29/10/15.
 */
public class BusStop implements IBusStop {

    private String name;
    private LatLng latLng;
    private int order;

    public BusStop(String name, LatLng latLng, int order) {
        this.name = name;
        this.latLng = latLng;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getOrder() {
        return order;
    }
}
