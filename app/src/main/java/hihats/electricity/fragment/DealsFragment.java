package hihats.electricity.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hihats.electricity.R;
import hihats.electricity.util.ParseDealHelper;

public class DealsFragment extends Fragment {

    private static String TAG = "DealsFragment";
    
    ListView dealsListView;
    SimpleAdapter dealsAdapter;

    List<HashMap<String, String>> deals;

    public static DealsFragment newInstance() {
        DealsFragment fragment = new DealsFragment();
        return fragment;
    }

    public DealsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.deals = new ArrayList<HashMap<String, String>>();

//        fetchDeals();

        for(int i = 0; i < 10; i++) {
            HashMap<String, String> deal = new HashMap<String, String>();

            deal.put("objectId", "object" + i);
            deal.put("name", "name" + 1);
            deal.put("author", "author" + i);
            deal.put("description", "description" + 1);
            deal.put("points", "points" + i);

            deals.add(deal);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        String[] from = {
                "name",
                "author",
                "description",
                "points"
        };

        int[] to = {
                R.id.dealName,
                R.id.dealAuthor,
                R.id.dealDescription,
                R.id.dealPoints
        };

        this.dealsListView = (ListView) view.findViewById(R.id.dealsListView);
        this.dealsAdapter = new SimpleAdapter(getContext(), deals, R.layout.card_deal, from, to);

        this.dealsListView.setAdapter(dealsAdapter);

        return view;

    }

    public void fetchDeals() {

        Log.i(TAG, "fetchDeals()");

        ParseQuery<ParseDealHelper> parseDeals = ParseQuery.getQuery(ParseDealHelper.class);

        Log.i(TAG, "ParseQuery.getQuery()");

        parseDeals.findInBackground(new FindCallback<ParseDealHelper>() {
            @Override
            public void done(List<ParseDealHelper> objects, com.parse.ParseException e) {

                Log.i(TAG, "FindCallback.done()");

                for (ParseDealHelper i : objects) {

                    HashMap<String, String> deal = new HashMap<String, String>();

                    deal.put("objectId", i.getObjectId());
                    deal.put("name", i.getName());
                    deal.put("author", i.getAuthor());
                    deal.put("description", i.getDescription());
                    deal.put("points", Integer.toString(i.getPoints()));

                    deals.add(deal);
                }

            }
        });
    }

}
