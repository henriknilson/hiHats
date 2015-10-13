package hihats.electricity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hihats.electricity.R;
import hihats.electricity.model.Ride;
import hihats.electricity.util.ParseRideHelper;

/**
 * Created by axel on 2015-10-13.
 */
public class DashboardListFragment extends Fragment {

    SimpleAdapter rideAdapter;
    ArrayList<Ride> rides;
    List<HashMap<String,String>> hashRides;
    ParseRideHelper rideHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rides = rideHelper.getRides(ParseUser.getCurrentUser().toString());
        hashRides = new ArrayList<>();
        setHashRides();
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
                R.id.rideName,
                R.id.rideBusStopFrom,
                R.id.rideBusStopToo,
                R.id.ridePoints,
                R.id.rideDistance,
        };

        this.rideAdapter = new SimpleAdapter(getContext(), hashRides, R.layout.card_ride, from, to)

    }

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
    }
}
