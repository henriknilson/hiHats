package hihats.electricity.database;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by filip on 29/10/15.
 */
public class ParseDatabase {

    private static final ParseDatabase INSTANCE = new ParseDatabase();

    public static ParseDatabase getInstance() {
        return ParseDatabase.INSTANCE;
    }

    private ParseDatabase() {};

    public static final String TAG = "ParseDatabase";

    public interface Callback {
        public void callback(List data);
    }

    public void getRides(Callback callback) {
        new ParseTask(callback).fetchRides();
    }

    public void getBusStops(Callback callback) {
        new ParseTask(callback).fetchBusStops();
    }

    public void getDeals(Callback callback) {
        new ParseTask(callback).fetchDeals();
    }

    private class ParseTask {

        final Callback callback;

        public ParseTask(Callback callback) {
            this.callback = callback;
        }

        protected void fetchBusStops() {

            ParseQuery<ParseBusStop> stopsParseQuery = ParseQuery.getQuery(ParseBusStop.class);
            stopsParseQuery.findInBackground(new FindCallback<ParseBusStop>() {

                @Override
                public void done(List<ParseBusStop> parseData, com.parse.ParseException e) {
                    if (e == null) {

                        // Sort
                        Collections.sort(parseData, new Comparator<ParseBusStop>() {
                            @Override
                            public int compare(ParseBusStop stop1, ParseBusStop stop2) {
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

            ParseQuery<ParseBusStop> stopsParseQuery = ParseQuery.getQuery(ParseBusStop.class);
            stopsParseQuery.findInBackground(new FindCallback<ParseBusStop>() {

                @Override
                public void done(List<ParseBusStop> parseData, com.parse.ParseException e) {
                    if (e == null) {

                        // Sort
                        Collections.sort(parseData, new Comparator<ParseBusStop>() {
                            @Override
                            public int compare(ParseBusStop stop1, ParseBusStop stop2) {
                                return stop1.compareTo(stop2);
                            }
                        });

                        // Callback
                        callback.callback(parseData);

                    } else {

                        Log.d(TAG, "fetchRides() Error: " + e.getMessage());

                    }

                }
            });


        }
        protected void fetchDeals() {

            ParseQuery<ParseBusStop> stopsParseQuery = ParseQuery.getQuery(ParseBusStop.class);
            stopsParseQuery.findInBackground(new FindCallback<ParseBusStop>() {

                @Override
                public void done(List<ParseBusStop> parseData, com.parse.ParseException e) {
                    if (e == null) {

                        // Sort
                        Collections.sort(parseData, new Comparator<ParseBusStop>() {
                            @Override
                            public int compare(ParseBusStop stop1, ParseBusStop stop2) {
                                return stop1.compareTo(stop2);
                            }
                        });

                        // Callback
                        callback.callback(parseData);

                    } else {

                        Log.d(TAG, "fetchRides() Error: " + e.getMessage());

                    }

                }
            });


        }

    }

}
