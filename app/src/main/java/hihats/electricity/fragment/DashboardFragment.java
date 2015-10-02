package hihats.electricity.fragment;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hihats.electricity.R;

public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


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
        int newTtlDistance = 20;

        TextView t = (TextView) view.findViewById(R.id.co2);
        t.setText(Double.toString(calcCo2(getRideDistance(oldTtlDistance, newTtlDistance))));

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
