package hihats.electricity.util;

import com.parse.ParseUser;

import hihats.electricity.model.CurrentUser;

/**
 * Created by Pertta on 15-10-07.
 * This is a help class for saving and updating data on the user
 */

public class ParseUserHelper {

    private static ParseUserHelper userHelper = null;

    protected ParseUserHelper() {
    }

    public static ParseUserHelper getInstance() {
        if (userHelper == null) {
            userHelper = new ParseUserHelper();
        }
        return userHelper;
    }

    public int getPoints() {
        return ParseUser.getCurrentUser().getInt("points");
    }

    //Sets the new points on parse and local
    public void setPoints(int points) {
        ParseUser.getCurrentUser().put("points", points);
        CurrentUser.getInstance().setPoints(points);
    }

}
