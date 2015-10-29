package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 29/10/15.
 */
public class Ride implements IRide {

    private Date date;
    private String stopFrom;
    private String stopTo;
    private int points;
    private int distance;
    private String user;

    public Ride(Date date, String stopFrom, String stopTo, int points, int distance, String user) {
        this.date = date;
        this.stopFrom = stopFrom;
        this.stopTo = stopTo;
        this.points = points;
        this.distance = distance;
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public String getFrom() {
        return stopFrom;
    }

    public String getTo() {
        return stopTo;
    }

    public int getPoints() {
        return points;
    }

    public int getDistance() {
        return distance;
    }

    public String getUser() {
        return user;
    }
}
