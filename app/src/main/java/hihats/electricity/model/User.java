package hihats.electricity.model;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pertta on 15-10-07.
 * This class represents the user
 */
public class User {

    private static User user = null;

    String userName;
    String userId;
    int points;
    ArrayList<Ride> rides = new ArrayList<>();

    protected User() {
    }

    public static User getInstance() {
        if(user == null) {
            user = new User();
        }
        return user;
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

        ParseQuery<Ride> parseRides = ParseQuery.getQuery(Ride.class);
        parseRides.findInBackground(new FindCallback<Ride>() {
            @Override
            public void done(List<Ride> objects, com.parse.ParseException e) {
                rides = new ArrayList<>();
                for (Ride ride : objects) {

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
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (points != user.points) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        return !(rides != null ? !rides.equals(user.rides) : user.rides != null);

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + points;
        result = 31 * result + (rides != null ? rides.hashCode() : 0);
        return result;
    }
}
