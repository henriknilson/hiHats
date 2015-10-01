package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class Location {

    private double latitude;
    private double longitude;
    private Date time;

    public Location() {}

    public Location(double latitude, double longitude, Date time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getTime() {
        return time;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
