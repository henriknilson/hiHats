package hihats.electricity.database;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by filip on 29/10/15.
 */
public class ParseDataHandler implements IDataHandler {

    public static final String TAG = ParseDataHandler.class.getSimpleName();

    private static IDataHandler instance;

    private ParseDataHandler() {}

    public static IDataHandler getInstance() {
        if (ParseDataHandler.instance == null) {
            ParseDataHandler.instance = new ParseDataHandler();
        }
        return ParseDataHandler.instance;
    }

    @Override
    public void getRides(IDataHandler.Callback callback) {
        new ParseFetcher(callback).fetch("Ride");
    }

    @Override
    public void getBusStops(IDataHandler.Callback callback) {
        new ParseFetcher(callback).fetch("BusStop");
    }

    @Override
    public void getDeals(IDataHandler.Callback callback) {
        new ParseFetcher(callback).fetch("Deal");
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

    private class ParseFetcher {

        final IDataHandler.Callback callback;

        public ParseFetcher(IDataHandler.Callback callback) {
            this.callback = callback;
        }

        protected void fetch(final String parseObject) {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(parseObject);
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseData, com.parse.ParseException e) {
                    if (e == null) {
                        callback.callback(parseData);
                    } else {
                        Log.d(TAG, "fetch(\"" + parseObject + "\") Error: " + e.getMessage());
                    }
                }
            });
        }

    }

}
