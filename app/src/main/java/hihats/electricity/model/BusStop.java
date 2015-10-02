package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by axel on 2015-09-24.
 */
public class BusStop implements Comparable {

    String name;
    LatLng latLng;
    int order;

    public BusStop(Double lat, Double lng, String name, int order){

        latLng = new LatLng(lat,lng);
        this.name = name;
        this.order = order;
    }

    public LatLng getLatLng(){
        return latLng;
    }

    public String getName(){
        return name;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public int compareTo(Object another) {
        int compareOrder =((BusStop)another).getOrder();
        /* For Ascending order*/
        return this.order-compareOrder;

    }
}
