package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 02/10/15.
 */
public class Ride {

    private int distance;
    private int points;
    private Date date;

    public Ride(int distance, int points, Date date) {
        this.distance = distance;
        this.points = points;
        this.date = date;
    }

    public void appendDistance(int distance) {
        this.distance += distance;
    }

    public void updatePoints() {
        // TODO fancy points algorithm
        points = distance * 12;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
