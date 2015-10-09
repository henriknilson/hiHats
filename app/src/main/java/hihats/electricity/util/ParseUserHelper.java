package hihats.electricity.util;

import com.parse.ParseUser;

import static hihats.electricity.model.User.getInstance;


/**
 * Created by Pertta on 15-10-07.
 * This is a help class for saving and updating data on the user
 */

public class ParseUserHelper {

    public int getPoints() {
        return ParseUser.getCurrentUser().getInt("points");
    }

    //Sets the new points on parse and local
    public void setPoints(int points) {
        ParseUser.getCurrentUser().put("points", points);
        getInstance().setNewPoints(points);
    }

}
