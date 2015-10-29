package hihats.electricity.database;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import hihats.electricity.model.IRide;

/**
 * Created by fredrikkindstrom on 29/10/15.
 */
public class ParseDatabase {

    private static final String TAG = "ParseDatabase";

    /*
    Upload methods
     */

    public static void uploadRide(IRide ride) {
        ParseObject r = ParseObject.create("Ride");
        r.add("date", ride.getDate());
        r.add("from", ride.getFrom());
        r.add("to", ride.getTo());
        r.add("points", ride.getPoints());
        r.add("distance", ride.getDistance());
        r.add("user", ride.getUser());
        r.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }
}
