package hihats.electricity.util;

import com.parse.ParseUser;


/**
 * Created by Pertta on 15-10-07.
 * This is a help class for saving and updating data on the user
 */

public class ParseUserHelper {

    public int getPoints() {
        return ParseUser.getCurrentUser().getInt("points");
    }

    public void setPoints(int points) {
        ParseUser.getCurrentUser().put("points", points);
    }

}
