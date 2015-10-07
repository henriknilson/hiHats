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
}
