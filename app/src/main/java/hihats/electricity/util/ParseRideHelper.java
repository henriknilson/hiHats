package hihats.electricity.util;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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

    /*
    Variables
     */
    private ArrayList<Ride> rides;

    /*
    Constructor
     */
    public ParseRideHelper(){}

    /*
    Getters
     */

    /**
     *
     * @return date, from parse
     */
    public Date getDate(){
        return getDate("date");
    }

    /**
     *
     * @return busStopFrom, from parse
     */
    public String getBusStopFrom(){
        return getString("busStopFrom");
    }

    /**
     *
     * @return busStopToo, from parse
     */
    public String getBusStopToo(){
        return getString("busStopToo");
    }

    /**
     *
     * @return points, from parse
     */
    public int getPoints(){
        return getInt("points");
    }

    /**
     *
     * @return distance, from parse
     */
    public double getDistance(){
        return getDouble("distance");
    }

    /**
     *
     * @return owner, from parse
     */
    public String getOwner(){
        return getString("owner");
    }

    /**
     * Fetches rides from parse and puts them in an arraylist, if they belong to the owner
     *
     * @param owner the owner of the rides
     * @return an arraylist containing rides, from parse
     */
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

        ParseObject ride = ParseObject.create("ParseRideHelper");
        ride.add("date", date);
        ride.add("busStopFrom", bsf);
        ride.add("busStopToo", bst);
        ride.add("points", points);
        ride.add("distance", distance);
        ride.add("owner", owner);
        ride.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){

                }else{
                    e.printStackTrace();
                }
            }
        });
    }
}
