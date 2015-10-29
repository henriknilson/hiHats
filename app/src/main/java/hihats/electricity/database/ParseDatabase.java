package hihats.electricity.database;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hihats.electricity.model.BusStop;
import hihats.electricity.model.IBusStop;
import hihats.electricity.model.IRide;

/**
 * Created by fredrikkindstrom on 29/10/15.
 */
public class ParseDatabase implements DBClient{

    private static final String TAG = "ParseDatabase";
    private static ParseDatabase instance;
    private static ArrayList<DBListener> listeners = new ArrayList<>();

    public static ParseDatabase getInstance() {
        if (instance == null) {
            instance = new ParseDatabase();
        }
        return instance;
    }

    @Override
    public void startListen(DBListener listener) {
        listeners.add(listener);
    }

    @Override
    public void stopListen(DBListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void publish(DBEvent event, Object object) {
        for (int i = 0; i < listeners.size(); i++) {
            DBListener listener = listeners.get(i);
            Log.d(TAG, "Published " + event + " to " + listener.getClass().getSimpleName());
            listener.onDBAction(event, object);
        }
    }

    /*
    Download methods
     */

    public void downloadBusStops() {
        ParseQuery<ParseBusStop> q = ParseQuery.getQuery(ParseBusStop.class);
        q.findInBackground(new BusStopsDownloadCall());
    }

    /*
    Upload methods
     */

    public void uploadRide(IRide ride) {
        ParseObject r = ParseObject.create("Ride");
        r.add("date", ride.getDate());
        r.add("from", ride.getFrom());
        r.add("to", ride.getTo());
        r.add("points", ride.getPoints());
        r.add("distance", ride.getDistance());
        r.add("user", ride.getUser());
        r.saveInBackground(new UploadCall(DBEvent.RIDE_UPLOADED));
    }

    /*
    Help classes
     */

    class BusStopsDownloadCall implements FindCallback<ParseBusStop> {

        /**
         * Override this function with the code you want to run after the fetch is complete.
         *
         * @param objects The objects that were retrieved, or null if it did not succeed.
         * @param e
         */
        @Override
        public void done(List<ParseBusStop> objects, ParseException e) {
            Log.d(TAG, "Retrieved " + objects.size() + " bus stops");
            Collections.sort(objects, new Comparator<ParseBusStop>() {
                @Override
                public int compare(ParseBusStop stop1, ParseBusStop stop2) {
                    return stop1.compareTo(stop2);
                }
            });
            ArrayList<IBusStop> response = new ArrayList<>();
            for (ParseBusStop o : objects) {
                IBusStop b = new BusStop(o.getName(), o.getLatLng(), o.getOrder());
                response.add(b);
            }
            publish(DBEvent.BUSSTOPS_DOWNLOADED, response);
        }
    }

    class UploadCall implements SaveCallback {

        DBEvent event;

        UploadCall(DBEvent event) {
            this.event = event;
        }

        /**
         * Override this function with the code you want to run after the save is complete.
         *
         * @param e The exception raised by the save, or {@code null} if it succeeded.
         */
        @Override
        public void done(ParseException e) {
            publish(event, null);
        }
    }
}
