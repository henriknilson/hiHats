package hihats.electricity.util;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hihats.electricity.model.BusStop;
import hihats.electricity.model.Ride;

/**
 * Created by axel on 2015-10-07.
 */
@ParseClassName("ParseRideHelper")
public class ParseRideHelper extends ParseObject {

    private ArrayList<Ride> rides;

    public ParseRideHelper(){}

    public Date getDate(){
        return getDate("date");
    }

    public String getBusStopFrom(){
        return getString("busStopFrom");
    }

    public String getBusStopToo(){
        return getString("busStopToo");
    }

    public int getPoints(){
        return getInt("points");
    }

    public double getDistance(){
        return getDouble("distance");}

    public String getOwner(){
        return getString("owner");}

    public ArrayList<Ride> getRides(final String owner){
        ParseQuery<ParseRideHelper> parseRides = ParseQuery.getQuery(ParseRideHelper.class);
        parseRides.findInBackground(new FindCallback<ParseRideHelper>() {
            @Override
            public void done(List<ParseRideHelper> objects, com.parse.ParseException e) {
                rides = new ArrayList<>();
                for (ParseRideHelper i : objects) {
                    Ride ride = new Ride(
                            i.getDate(),
                            i.getBusStopFrom(),
                            i.getBusStopToo(),
                            i.getPoints(),
                            i.getDistance(),
                            i.getOwner());
                    if (ride.getOwner().equals(owner)) {
                        rides.add(ride);
                    }
                }
            }

        });
        return rides;
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
     * @param owner The owner of the ride
     */
    public void uploadRide(Date date, String bsf, String bst, int points, double distance, String owner ){

        ParseObject ride = new ParseObject("ParseRideHelper");
        ride.add("date", date);
        ride.add("busStopFrom", bsf);
        ride.add("busStopToo", bst);
        ride.add("points", points);
        ride.add("distance", distance);
        ride.add("owner", owner);
        ride.saveInBackground();
    }
}
