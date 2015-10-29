package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fredrikkindstrom on 28/10/15.
 */
public interface IBusStop extends Comparable<IBusStop> {

    LatLng getLatLng();
    String getName();
    int getOrder();
}
