package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fredrikkindstrom on 28/10/15.
 */
public interface IBusStop extends Comparable<IBusStop> {

    /**
     * @return The coordinates of the bus stop.
     */
    LatLng getLatLng();

    /**
     * @return The name of the bus stop.
     */
    String getName();

    /**
     * @return The order of the bus stop, compared to all the other bus stops.
     */
    int getOrder();
}
