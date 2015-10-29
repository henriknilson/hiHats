package hihats.electricity.database;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by filip on 29/10/15.
 */
public class ParseDataHandler implements IDataHandler {

    private static final IDataHandler INSTANCE = new ParseDataHandler();

    public static IDataHandler getInstance() {
        return ParseDataHandler.INSTANCE;
    }

    private ParseDataHandler() {};

    public static final String TAG = "ParseDataHandler";

    public void getRides(IDataHandler.Callback callback) {
        new ParseTask(callback).fetchRides();
    }

    public void getBusStops(IDataHandler.Callback callback) {
        new ParseTask(callback).fetchBusStops();
    }

    public void getDeals(IDataHandler.Callback callback) {
        new ParseTask(callback).fetchDeals();
    }

    private class ParseTask {

        final IDataHandler.Callback callback;

        public ParseTask(IDataHandler.Callback callback) {
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

            ParseQuery<ParseRide> query = ParseQuery.getQuery(ParseRide.class);
            query.findInBackground(new FindCallback<ParseRide>() {

                @Override
                public void done(List<ParseRide> parseData, com.parse.ParseException e) {
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

            ParseQuery<ParseDeal> stopsParseQuery = ParseQuery.getQuery(ParseDeal.class);
            stopsParseQuery.findInBackground(new FindCallback<ParseDeal>() {

                @Override
                public void done(List<ParseDeal> parseData, com.parse.ParseException e) {
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
