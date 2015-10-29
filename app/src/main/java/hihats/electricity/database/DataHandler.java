package hihats.electricity.database;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hihats.electricity.model.BusStop;
import hihats.electricity.model.Deal;
import hihats.electricity.model.Ride;

/**
 * Created by filip on 29/10/15.
 */
public class DataHandler implements IDataHandler {

    private static final IDataHandler INSTANCE = new DataHandler();

    public static IDataHandler getInstance() {
        return DataHandler.INSTANCE;
    }

    private DataHandler() {};

    public static final String TAG = "DataHandler";

    public void getRides(IDataHandler.Callback callback) {
        new ParseTask(callback).fetchRides();
    }

    public void getBusStops(IDataHandler.Callback callback) {
        new ParseTask(callback).fetchBusStops();
    }

    public void getDeals(IDataHandler.Callback callback) {
        new ParseTask(callback).fetchDeals();
    }

    @Override
    public void save(Object o) {
        if(o instanceof ParseObject) {
            try {
                ((ParseObject) o).saveInBackground();
            } catch(Exception e) {
                Log.i(TAG, "save()" + e.getMessage());
            }
        }
    }

    private class ParseTask {

        final IDataHandler.Callback callback;

        public ParseTask(IDataHandler.Callback callback) {
            this.callback = callback;
        }

        protected void fetchBusStops() {

            ParseQuery<BusStop> stopsParseQuery = ParseQuery.getQuery(BusStop.class);
            stopsParseQuery.findInBackground(new FindCallback<BusStop>() {

                @Override
                public void done(List<BusStop> parseData, com.parse.ParseException e) {
                    if (e == null) {

                        // Sort
                        Collections.sort(parseData, new Comparator<BusStop>() {
                            @Override
                            public int compare(BusStop stop1, BusStop stop2) {
                                return stop1.compareTo(stop2);
                            }
                        });

                        // Callback
                        callback.callback(parseData);

                    } else {

                        Log.d(TAG, "fetchBusStops() Error: " + e.getMessage());

                    }

                }
            });


        }
        protected void fetchRides() {

            ParseQuery<Ride> query = ParseQuery.getQuery(Ride.class);
            query.findInBackground(new FindCallback<Ride>() {

                @Override
                public void done(List<Ride> parseData, com.parse.ParseException e) {
                    if (e == null) {

                        // Callback
                        callback.callback(parseData);

                    } else {
                        Log.d(TAG, "fetchRides() Error: " + e.getMessage());
                    }

                }
            });


        }
        protected void fetchDeals() {

            ParseQuery<Deal> stopsParseQuery = ParseQuery.getQuery(Deal.class);
            stopsParseQuery.findInBackground(new FindCallback<Deal>() {

                @Override
                public void done(List<Deal> parseData, com.parse.ParseException e) {
                    if (e == null) {

                        callback.callback(parseData);

                    } else {

                        Log.d(TAG, "fetchRides() Error: " + e.getMessage());

                    }

                }
            });


        }

    }

}
