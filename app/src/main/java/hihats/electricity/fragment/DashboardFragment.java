package hihats.electricity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;

import hihats.electricity.R;

public class DashboardFragment extends Fragment {

    private static String TAG = "DashBoardFragment";

    TextView usernametxt;
    SimpleAdapter rideAdapter;
    List<HashMap<String, String>> rides;
    ListView rideListView;
    private LineChartView mChartOne;
    private final String[] mLabelsOne= {"", "Januari", "", "Februari", "", "Mars", "", "April", "", "Maj", ""};
    private final float[][] mValuesOne = {{3.5f, 4.7f, 4.3f, 8f, 6.5f, 10f, 7f, 8.3f, 7.0f, 7.3f, 5f}};

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    public DashboardFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rides = new ArrayList<>();
        fetchRides();

        //HashMap<String, String> ride = new HashMap<>();

        /*
        // Create Ride HashMaps from the parse objects
        ride.put("date", "date");
        ride.put("busStopFrom", "busStopFrom");
        ride.put("busStopTo", "busStopTo");
        ride.put("points", "points");
        ride.put("distance", "distance");
        */

        //rides.add(ride);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] from = {
                "date",
                "busStopFrom",
                "busStopToo",
                "points",
                "distance"
        };

        int[] to = {
                R.id.rideDate,
                R.id.rideBusStopFrom,
                R.id.rideBusStopToo,
                R.id.ridePoints,
                R.id.rideDistance,
        };
        View layout = inflater.inflate(R.layout.fragment_dashboard, container, false);
        rideAdapter = new SimpleAdapter(getContext(), rides, R.layout.card_ride, from, to);

        rideListView = (ListView) layout.findViewById(R.id.ridesListView);
        rideListView.setAdapter(rideAdapter);

        // Init first chart
        mChartOne = (LineChartView) layout.findViewById(R.id.linechart1);


        RelativeLayout dashBoardFragment = (RelativeLayout) layout.findViewById(R.id.dashboardFragment);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View buttonGroupView = layoutInflater.inflate(R.layout.buttongroup_dashboard, container, false);
        buttonGroupView.setLayoutParams(params);

        usernametxt = (TextView) layout.findViewById(R.id.username);
        usernametxt.setText(ParseUser.getCurrentUser().getUsername());

        //dashBoardFragment.addView(buttonGroupView,1);
        produceOne(mChartOne);


        return layout;
    }

    public void fetchRides() {

        Log.i(TAG, "fetchRides()");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRideHelper");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseRides, ParseException e) {
                if (e == null) {

                    Log.d(TAG, "Retrieved " + parseRides.size() + " rides");

                    for (ParseObject parseObject : parseRides) {
                        HashMap<String, String> ride = new HashMap<>();

                        // Create Ride HashMaps from the parse objects
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

    public void produceOne(LineChartView chart){

        LineSet dataset = new LineSet(mLabelsOne, mValuesOne[0]);
        dataset.setColor(Color.parseColor("#4CAF50"))
                .setFill(Color.parseColor("#A5D6A7"))
                .setSmooth(true);
        chart.addData(dataset);

        chart.setTopSpacing(Tools.fromDpToPx(15))
                .setBorderSpacing(Tools.fromDpToPx(0))
                .setAxisBorderValues(0, 10, 1)
                .setXLabels(AxisController.LabelPosition.INSIDE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#4CAF50"))
                .setXAxis(false)
                .setYAxis(false);
        chart.show();
    }

}
