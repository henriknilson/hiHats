package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by axel on 2015-09-24.
 */
public class BusStop {

    String name;
    LatLng latLng;

    public BusStop(Double lat, Double lng, String name){
        latLng = new LatLng(lat,lng);
        this.name = name;
    }
    public LatLng getLatLng(){
        return latLng;
    }
    public String getName(){
        return name;
    }
}
