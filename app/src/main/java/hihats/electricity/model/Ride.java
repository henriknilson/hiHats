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
    private String busStopFrom;
    private String busStopToo;
    private int points;
    private int distance;
    private String owner;

    /**
     *Constructor
     *
     * @param date When the ride was executed
     * @param busStopFrom The start position of the ride
     * @param busStopToo The end position of the ride
     * @param points The number of points the ride generated
     */
    public Ride(Date date, String busStopFrom, String busStopToo,
                int points, int distance, String userId){
        this.date = date;
        this.busStopFrom = busStopFrom;
        this.busStopToo = busStopToo;
        this.points = points;
        this.distance = distance;
        this.owner = userId;

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
    public String getBusStopFrom() {
        return busStopFrom;
    }

    /**
     *
     * @return busStopToo
     */
    public String getBusStopToo() {
        return busStopToo;
    }

    /**
     *
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     *
     * @return distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     *
     * @return owner
     */
    public String getOwner() {
        return owner;
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
        if (!(o instanceof Ride)) return false;

        Ride ride = (Ride) o;

        if (points != ride.points) return false;
        if (Double.compare(ride.distance, distance) != 0) return false;
        if (date != null ? !date.equals(ride.date) : ride.date != null) return false;
        if (busStopFrom != null ? !busStopFrom.equals(ride.busStopFrom) : ride.busStopFrom != null)
            return false;
        if (busStopToo != null ? !busStopToo.equals(ride.busStopToo) : ride.busStopToo != null)
            return false;
        return !(owner != null ? !owner.equals(ride.owner) : ride.owner != null);

    }

    /**
     * hashCode
     *
     * @return the instance variables hashcode
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date != null ? date.hashCode() : 0;
        result = 31 * result + (busStopFrom != null ? busStopFrom.hashCode() : 0);
        result = 31 * result + (busStopToo != null ? busStopToo.hashCode() : 0);
        result = 31 * result + points;
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
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
                ", distance=" + distance +
                '}';
    }
}
