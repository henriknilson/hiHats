package hihats.electricity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;

import hihats.electricity.R;
import hihats.electricity.database.IDataHandler;
import hihats.electricity.database.ParseDataHandler;
import hihats.electricity.model.IRide;
import hihats.electricity.model.CurrentUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();

    private TextView usernametxt;
    private TextView pointstxt;
    private TextView nbrRidestxt;
    private TextView distancetxt;

    private SimpleAdapter rideAdapter;
    private List<HashMap<String, String>> rides;
    private ListView rideListView;
    private int distance = 0;
    private int points = 0;
    private LineChartView mChartOne;
    private final String[] mLabelsOne = {"", getChartMonth(7), getChartMonth(6), getChartMonth(5), getChartMonth(4), getChartMonth(3), getChartMonth(2), getChartMonth(1), getChartMonth(0),""};
    private final float[][] mValuesOne = new float[1][10];
    private final IDataHandler dataHandler = ParseDataHandler.getInstance();
    private ArrayList<IRide> iRides;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rides = new ArrayList<>();
        fetchRides();
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

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View buttonGroupView = layoutInflater.inflate(R.layout.buttongroup_dashboard, container, false);
        buttonGroupView.setLayoutParams(params);

        usernametxt = (TextView) layout.findViewById(R.id.username);
        usernametxt.setText(CurrentUser.getInstance().getUserName());

        pointstxt = (TextView) layout.findViewById(R.id.points);

        nbrRidestxt = (TextView) layout.findViewById(R.id.nbrRides);

        distancetxt = (TextView) layout.findViewById(R.id.distance);

        return layout;
    }

    private void fetchRides() {

        dataHandler.getRides(new IDataHandler.Callback<IRide>() {
            @Override
            public void callback(List<IRide> data) {
                iRides = new ArrayList<>();
                for (IRide r : data) {

                    if (r.getUser().equals(CurrentUser.getInstance().getUserName())) {
                        iRides.add(r);
                        distance += r.getDistance();
                        points += r.getPoints();

                        HashMap<String, String> ride = new HashMap<>();
                        // Create Ride HashMaps from the parse objects
                        ride.put("busStopFrom", r.getFrom());
                        ride.put("busStopToo", r.getTo());
                        ride.put("points", Integer.toString(r.getPoints()));
                        ride.put("distance", Double.toString(
                                r.getDistance()));

                        rides.add(ride);

                        // Notify the ListViews SimpleAdapter adapter to update UI
                        rideAdapter.notifyDataSetChanged();
                    }
                }
                pointstxt.setText(points + "");
                nbrRidestxt.setText(rides.size() + "");
                distancetxt.setText(distance + "");
                calculateChartValues();
            }
        });

    }

    private void produceOne(LineChartView chart) {

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
    private String getChartMonth(int i) {
        Calendar cal = Calendar.getInstance();
        int currentMonth = (cal.get(Calendar.MONTH) - i);

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
    private String getMonthString(int currentMonth) {

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
    private void calculateChartValues() {
        float[] chartValues = new float[8];

        for (IRide r : iRides) {
            int rideMonth = r.getDate().getMonth();
            if (getMonthString(rideMonth).equals(getChartMonth(7))) {
                chartValues[0] = chartValues[0] + (float) (r.getDistance()/5);
            } else if (getMonthString(rideMonth).equals(getChartMonth(6))) {
                chartValues[1] = chartValues[1] + (float) (r.getDistance()/5);
            } else if (getMonthString(rideMonth).equals(getChartMonth(5))) {
                chartValues[2] = chartValues[2] + (float) (r.getDistance()/5);
            } else if (getMonthString(rideMonth).equals(getChartMonth(4))) {
                chartValues[3] = chartValues[3] + (float) (r.getDistance()/5);
            } else if (getMonthString(rideMonth).equals(getChartMonth(3))) {
                chartValues[4] = chartValues[4] + (float) (r.getDistance()/5);
            } else if (getMonthString(rideMonth).equals(getChartMonth(2))) {
                chartValues[5] = chartValues[5] + (float) (r.getDistance()/5);
            } else if (getMonthString(rideMonth).equals(getChartMonth(1))) {
                chartValues[6] = chartValues[6] + (float) (r.getDistance()/5);
            } else if (getMonthString(rideMonth).equals(getChartMonth(0))) {
                chartValues[7] = chartValues[7] + (float) (r.getDistance()/5);
            }
        }
        setChartValues(chartValues);
    }

    private void setChartValues(float[] chartValues) {
        System.arraycopy(chartValues, 0, mValuesOne[0], 1, 8);
        produceOne(mChartOne);
    }
}
