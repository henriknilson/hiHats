package hihats.electricity.database;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import hihats.electricity.model.IRide;

/**
 * Created by axel on 2015-10-07.
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
}
