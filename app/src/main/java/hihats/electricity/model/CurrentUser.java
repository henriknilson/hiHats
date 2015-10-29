package hihats.electricity.model;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pertta on 15-10-07.
 * This class represents the user
 */
public class CurrentUser {

    private static CurrentUser instance;

    String userName;
    String userId;
    int points;
    ArrayList<ParseRide> rides = new ArrayList<>();

    protected CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }


    /*
    Getters
     */

    public String getUserName() {
        return userName;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList getRides() {
        return rides;
    }

    /*
    Setters
     */

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRides() {

        ParseQuery<ParseRide> parseRides = ParseQuery.getQuery(ParseRide.class);
        parseRides.findInBackground(new FindCallback<ParseRide>() {
            @Override
            public void done(List<ParseRide> objects, com.parse.ParseException e) {
                rides = new ArrayList<>();
                for (ParseRide ride : objects) {

                    if (ride.getUser().equals(userName)) {
                        rides.add(ride);
                    }
                }
            }

        });

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrentUser)) return false;

        CurrentUser currentUser = (CurrentUser) o;

        if (points != currentUser.points) return false;
        if (userName != null ? !userName.equals(currentUser.userName) : currentUser.userName != null) return false;
        return !(rides != null ? !rides.equals(currentUser.rides) : currentUser.rides != null);

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + points;
        result = 31 * result + (rides != null ? rides.hashCode() : 0);
        return result;
    }
}
