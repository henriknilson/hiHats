package hihats.electricity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hihats.electricity.R;

/**
 * Created by axel on 2015-10-13.
 */
public class DashboardListFragment extends Fragment {

    private static String TAG = "DashBoardListFragment";

    SimpleAdapter rideAdapter;
    List<HashMap<String, String>> rides;
    ListView rideListView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        rides = new ArrayList<>();
        fetchRides();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dlistview, container, false);

        String[] from = {
                "date",
                "busStopFrom",
                "busStopToo",
                "points",
                "distance",
                "owner"
        };

        int[] to = {
                R.id.rideDate,
                R.id.rideBusStopFrom,
                R.id.rideBusStopToo,
                R.id.ridePoints,
                R.id.rideDistance,
        };

        rideAdapter = new SimpleAdapter(getContext(), rides, R.layout.card_ride, from, to);

        rideListView = (ListView) view.findViewById(R.id.ridesListView);
        rideListView.setAdapter(rideAdapter);

        return view;
    }

    /*
    public void setHashRides(){
        for(Ride ride : rides){
            HashMap<String, String> hashRide = new HashMap<>();
            hashRide.put("date", ride.getDate().toString());
            hashRide.put("busStopFrom", ride.getBusStopFrom());
            hashRide.put("busStopTo", ride.getBusStopToo());
            hashRide.put("points", Integer.toString(ride.getPoints()));
            hashRide.put("distance", Double.toString(ride.getDistance()));

            hashRides.add(hashRide);
        }
        rideAdapter.notifyDataSetChanged();
    }*/

    public void fetchRides() {

        Log.i(TAG, "fetchRides()");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Deal");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseDeals, ParseException e) {
                if (e == null) {

                    Log.d(TAG, "Retrieved " + parseDeals.size() + " deals");

                    for (ParseObject parseObject : parseDeals) {
                        HashMap<String, String> ride = new HashMap<>();

                        // Create Deal HashMaps from the parse objects
                        ride.put("date", parseObject.getString("date"));
                        ride.put("busStopFrom", parseObject.getString("busStopFrom"));
                        ride.put("busStopTo", parseObject.getString("busStopTo"));
                        ride.put("points", Integer.toString(parseObject.getNumber("points").intValue()));
                        ride.put("distance", Double.toString(
                                        parseObject.getNumber("distance").intValue()
                                )
                        );

                        rides.add(ride);

                        // Notify the ListViews SimpleAdapter adapter to update UI
                        rideAdapter.notifyDataSetChanged();
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

}
