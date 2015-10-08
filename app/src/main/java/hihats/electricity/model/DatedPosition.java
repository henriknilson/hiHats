package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class DatedPosition {

    private double latitude;
    private double longitude;
    private Date date;

    public DatedPosition() {}

    public DatedPosition(double latitude, double longitude, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        return "lat=" + latitude + " long=" + longitude + " date=" + date;
    }
}
