package hihats.electricity.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import hihats.electricity.model.IRide;

/**
 * This class is a representation of a Ride in the application, designed to work
 * with the parse database.
 */

@ParseClassName("Ride")
public class ParseRide extends ParseObject implements IRide {

    public ParseRide() {

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

    public int getDistance() {
        return getInt("distance");
    }

    public String getUser() {
        return getString("user");
    }





    public void setDate(Date date) {
        put("date", date);
    }

    public void setFrom(String from) {
        if (from == null) {
            from = "";
        }
        put("from", from); }

    public void setTo(String to) {
        if (to == null) {
            to = "";
        }
        put("to", to); }

    public void setPoints(int points) {
        put("points", points); }

    public void setDistance(int distance) {
        put("distance", distance); }

    public void setUser(String user) {
        if (user == null) {
            user = "";
        }
        put("user", user); }


}
