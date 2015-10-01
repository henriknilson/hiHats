package hihats.electricity.model;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */

public class Bus {

    private String busId;
    private Location location;
    private float speed;
    private float bearing;

    public Bus(String busId, Location location, float speed, float bearing) {
        this.busId = busId;
        this.location = location;
        this.speed = speed;
        this.bearing = bearing;
    }
}
