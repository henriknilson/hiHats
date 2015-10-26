package hihats.electricity.model;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by axel on 2015-10-07.
 */
@ParseClassName("Ride")
public class Ride extends ParseObject {

    public Ride() {

    }

    public Date getDate() {
        return getDate("date");
    }

    public String getFrom() {
        return getString("from");
    }

    public String getTo() {
        return getString("to");
    }

    public int getPoints() {
        return getInt("points");
    }

    public double getDistance() {
        return getDouble("distance");
    }

    public String getUser() {
        return getString("user");
    }

    /*
    ClassMethod
     */

    /**
     *Call this method to easily upload a ride to parse!
     *
     * @param date The date the ride was initialized
     * @param bsf The initial bus stop
     * @param bst The final bus stop
     * @param points The points generated
     * @param distance The distance covered
     * @param user The user of the ride
     */
    public void uploadRide(Date date, String bsf, String bst, int points, int distance, String user ){

        ParseObject ride = ParseObject.create("Ride");
        ride.add("date", date);
        ride.add("from", bsf);
        ride.add("to", bst);
        ride.add("points", points);
        ride.add("distance", distance);
        ride.add("user", user);
        ride.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
