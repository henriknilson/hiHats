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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;

import hihats.electricity.R;
import hihats.electricity.model.Ride;
import hihats.electricity.model.User;

public class ProfileFragment extends Fragment {

    private static String TAG = "DashBoardFragment";

    TextView usernametxt;
    SimpleAdapter rideAdapter;
    List<HashMap<String, String>> rides;
    ListView rideListView;
    private LineChartView mChartOne;
    private final String[] mLabelsOne = {"", getChartMonth(0), getChartMonth(0), getChartMonth(0), getChartMonth(0), getChartMonth(0), getChartMonth(0), getChartMonth(0), getChartMonth(0),""};
    private final float[][] mValuesOne = new float[1][10]; //{{3.5f, 4.7f, 4.3f, 0f, 0f, 0f, 7f, 8.3f, 7.0f, 0f, 0f, 0f, 3.5f, 4.1f, 2.2f, 3.5f, 5.6f, 5.8f, 6.2f}};

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rides = new ArrayList<>();
        fetchRides();
        calculateChartValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] from = {
                "busStopFrom",
                "busStopToo",
                "points",
                "distance"
        };

        int[] to = {
                R.id.rideBusStopFrom,
                R.id.rideBusStopToo,
                R.id.ridePoints,
                R.id.rideDistance,
        };

        View layout = inflater.inflate(R.layout.fragment_dlistview, container, false);
        View header = inflater.inflate(R.layout.fragment_profile, null, false);

        rideAdapter = new SimpleAdapter(getContext(), rides, R.layout.card_ride, from, to);

        rideListView = (ListView) layout.findViewById(R.id.ridesListView);
        rideListView.addHeaderView(header);
        rideListView.setAdapter(rideAdapter);

        // Init first chart
        mChartOne = (LineChartView) layout.findViewById(R.id.linechart1);
        produceOne(mChartOne);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View buttonGroupView = layoutInflater.inflate(R.layout.buttongroup_dashboard, container, false);
        buttonGroupView.setLayoutParams(params);

        usernametxt = (TextView) layout.findViewById(R.id.username);
        usernametxt.setText(ParseUser.getCurrentUser().getUsername());

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
                        ride.put("busStopFrom", parseObject.getString("busStopFrom"));
                        ride.put("busStopToo", parseObject.getString("busStopToo"));
                        ride.put("points", Integer.toString(parseObject.getNumber("points").intValue()));
                        ride.put("distance", Double.toString(
                                        parseObject.getNumber("distance").doubleValue()
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

    public void produceOne(LineChartView chart) {

        LineSet dataset = new LineSet(mLabelsOne, mValuesOne[0]);
        dataset.setColor(this.getResources().getColor(R.color.primary))
                .setFill(this.getResources().getColor(R.color.accent))
                .setSmooth(true)
                .setThickness(2);
        chart.addData(dataset);

        chart.setTopSpacing(Tools.fromDpToPx(16))
                .setBorderSpacing(Tools.fromDpToPx(0))
                .setAxisBorderValues(0, 10, 1)
                .setXLabels(AxisController.LabelPosition.INSIDE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#1B5E20"))
                .setXAxis(false)
                .setYAxis(false);
        chart.show();
    }

    /**
     * This method makes it possible to get for example
     * the month before the last month
     * with sending an int of 2 with the call
     * @param i
     * @return
     */
    public String getChartMonth(int i) {
        int currentMonth = Calendar.MONTH - i;

        if (currentMonth < 0) {
            currentMonth = currentMonth + 12;
        }

        return getMonthString(currentMonth);
    }

    /**
     * Month to String
     * @param currentMonth
     * @return
     */
    public String getMonthString(int currentMonth) {

        switch (currentMonth) {
            case 0:  return "Jan";
            case 1:  return "Feb";
            case 2:  return "Mar";
            case 3:  return "Apr";
            case 4:  return "May";
            case 5:  return "Jun";
            case 6:  return "Jul";
            case 7:  return "Aug";
            case 8:  return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return "Invalid month";
        }
    }

    /**
     * Sets the values in the chart depending on users rides
     */
    public void calculateChartValues() {
        ArrayList<Ride> rides = User.getInstance().getRides();
        float[] chartValues = new float[8];

        for (Ride r : rides) {
            int rideMonth = r.getDate().getMonth();
            if (r.getDate().getYear() == Calendar.YEAR) {
                if (getMonthString(rideMonth) == getChartMonth(7)) {
                    chartValues[0] = chartValues[0] + (float) (r.getDistance() / 10);
                } else if (getMonthString(rideMonth) == getChartMonth(6)) {
                    chartValues[1] = chartValues[1] + (float) (r.getDistance() / 10);
                } else if (getMonthString(rideMonth) == getChartMonth(5)) {
                    chartValues[2] = chartValues[2] + (float) (r.getDistance() / 10);
                } else if (getMonthString(rideMonth) == getChartMonth(4)) {
                    chartValues[3] = chartValues[3] + (float) (r.getDistance() / 10);
                } else if (getMonthString(rideMonth) == getChartMonth(3)) {
                    chartValues[4] = chartValues[4] + (float) (r.getDistance() / 10);
                } else if (getMonthString(rideMonth) == getChartMonth(2)) {
                    chartValues[5] = chartValues[5] + (float) (r.getDistance());
                } else if (getMonthString(rideMonth) == getChartMonth(1)) {
                    chartValues[6] = chartValues[6] + (float) (r.getDistance());
                } else if (getMonthString(rideMonth) == getChartMonth(0)) {
                    chartValues[7] = chartValues[7] + (float) (r.getDistance());
                }
            }
        }
        setChartValues(chartValues);
    }

    public void setChartValues(float[] chartValues) {
        for (int i = 1; i < 9; i++) {
            mValuesOne[0][i] = chartValues[(i-1)];
        }
    }
}
