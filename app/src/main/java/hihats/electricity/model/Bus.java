package hihats.electricity.model;

import android.location.Location;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class Bus {

    private String busId;
    private Location location;

    public Bus (String busId, Location location) {
        this.busId = busId;
        this.location = location;
    }
}
