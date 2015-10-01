package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class Location {

    private double latitude;
    private double longitude;
    private Date time;

    public Location(double latitude, double longitude, Date time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }
}
