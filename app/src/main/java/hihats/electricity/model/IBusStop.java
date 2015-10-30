package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * This interface represents how a BusStop should act and what it should be capable of.
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
