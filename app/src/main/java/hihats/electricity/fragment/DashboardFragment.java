package hihats.electricity.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import hihats.electricity.R;

public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView t;

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    public DashboardFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        int oldTtlDistance = 15;
        int newTtlDistance = 30;

        t = (TextView) view.findViewById(R.id.co2);
        t.setText("You have saved " + Double.toString(calcCo2(getRideDistance(oldTtlDistance, newTtlDistance))) + " kg CO2 today!");

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
           new DataPoint (0,1),
           new DataPoint (1,5),
           new DataPoint (2,3),
           new DataPoint (3,2),
           new DataPoint (4,6),
        });

        graph.addSeries(series);

        RelativeLayout dashBoardFragment = (RelativeLayout) view.findViewById(R.id.dashboardFragment);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View buttonGroupView = layoutInflater.inflate(R.layout.buttongroup_dashboard, container, false);
        buttonGroupView.setLayoutParams(params);

        dashBoardFragment.addView(buttonGroupView,1);


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public int getRideDistance(int busPreviousTtlDistance, int busNewTtlDistance){
        return busNewTtlDistance - busPreviousTtlDistance;
    }

    public double calcCo2(int distance){
        double co2 = 0.3;
        return distance * co2;
    }
}
