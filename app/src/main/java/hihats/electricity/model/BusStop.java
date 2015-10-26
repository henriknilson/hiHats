package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by axel on 2015-09-24.
 */
@ParseClassName("BusStop")
public class BusStop extends ParseObject implements Comparable {

    LatLng latLng;
    int order;

    public BusStop() {

    }

    private LatLng setupLatLng() {
        this.latLng = new LatLng(getInt("latitude"), getInt("longitude"));
        return this.latLng;
    }

    public double getLat() {
        return this.getLatLng().latitude;
    }

    public double getLng() {
        return this.getLatLng().longitude;
    }

    public LatLng getLatLng() {
        if(this.latLng == null) {
            return this.setupLatLng();
        }
        return this.latLng;
    }

    public String getName() {
        return getString("name");
    }

    public int getOrder() {
        return getInt("order");
    }

    @Override
    public int compareTo(Object another) {
        int compareOrder =((BusStop)another).getOrder();
        
        /* For Ascending order*/
        return this.order - compareOrder;
    }
}
