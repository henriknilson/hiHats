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

    public String getBusId() {
        return busId;
    }

    public Location getLocation() {
        return location;
    }

    public float getSpeed() {
        return speed;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }
}
