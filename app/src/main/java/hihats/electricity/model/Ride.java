package hihats.electricity.model;

import java.util.Date;

/**
 * Created by axel on 2015-10-07.
 */
public class Ride {

    /*
    Instance Variables
     */
    private Date date;
    private BusStop busStopFrom;
    private BusStop busStopToo;
    private int points;

    /**
     *Constructor
     *
     * @param date When the ride was executed
     * @param busStopFrom The start position of the ride
     * @param busStopToo The end position of the ride
     * @param points The number of points the ride generated
     */
    public Ride(Date date, BusStop busStopFrom, BusStop busStopToo, int points ){
        this.date = date;
        this.busStopFrom = busStopFrom;
        this.busStopToo = busStopToo;
        this.points = points;
    }

    /*
    Getters
     */

    /**
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return busStopFrom
     */
    public BusStop getBusStopFrom() {
        return busStopFrom;
    }

    /**
     *
     * @return busStopToo
     */
    public BusStop getBusStopToo() {
        return busStopToo;
    }

    /**
     *
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /*
    Equals and Hashcode
     */

    /**
     * equals
     *
     * @param o the object to be compared
     * @return true if the object is actually a Ride, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ride))
            return false;

        Ride ride = (Ride) o;

        if (points != ride.points)
            return false;
        if (date != null ? !date.equals(ride.date) : ride.date != null)
            return false;
        if (busStopFrom != null ? !busStopFrom.equals(ride.busStopFrom) : ride.busStopFrom != null)
            return false;
        return !(busStopToo != null ? !busStopToo.equals(ride.busStopToo) : ride.busStopToo != null);

    }

    /**
     * hashCode
     *
     * @return the instance variables hashcode
     */
    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (busStopFrom != null ? busStopFrom.hashCode() : 0);
        result = 31 * result + (busStopToo != null ? busStopToo.hashCode() : 0);
        result = 31 * result + points;
        return result;
    }

    /*
    ToString
     */

    /**
     *
     * @return the value of the instance variables in string format
     */
    @Override
    public String toString() {
        return "Ride{" +
                "date=" + date +
                ", busStopFrom=" + busStopFrom +
                ", busStopToo=" + busStopToo +
                ", points=" + points +
                '}';
    }
}
