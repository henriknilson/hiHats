package hihats.electricity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hihats.electricity.R;

/**
 * Created by axel on 2015-10-13.
 */
public class DashboardListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
                R.id
        }

    }
}
