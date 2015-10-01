package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 02/10/15.
 */
public class Ride {

    private Bus bus;
    private int distance;

    public Ride(Bus bus) {
        this.bus = bus;
    }

    public void startRide() {

    }

    public void appendDistance(int distance) {
        this.distance += distance;
    }
}
