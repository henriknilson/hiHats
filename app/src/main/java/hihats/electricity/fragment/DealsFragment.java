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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hihats.electricity.R;

public class DealsFragment extends Fragment {

    private static String TAG = "DealsFragment";
    
    ListView dealsListView;
    SimpleAdapter dealsAdapter;

    /**
     * The data container for the ListView
     */
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

        fetchDeals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        // "Take these keys from our data array..."
        String[] from = {
                "name",
                "author",
                "description",
                "points"
        };

        // "... and put their values in these places..."
        int[] to = {
                R.id.dealName,
                R.id.dealAuthor,
                R.id.dealDescription,
                R.id.dealPoints
        };

        // ...and use these arrays arrays to build a ListView adapter.
        this.dealsAdapter = new SimpleAdapter(getContext(), deals, R.layout.card_deal, from, to);

        this.dealsListView = (ListView) view.findViewById(R.id.dealsListView);
        this.dealsListView.setAdapter(dealsAdapter);

        return view;

    }

    /**
     * Fetches Deals from Parse and adds the returned deals to the UI (via the deals array).
     */
    public void fetchDeals() {

        Log.i(TAG, "fetchDeals()");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Deal");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseDeals, ParseException e) {
                if (e == null) {

                    Log.d(TAG, "Retrieved " + parseDeals.size() + " deals");

                    for(ParseObject parseObject : parseDeals) {
                        HashMap<String, String> deal = new HashMap<String, String>();

                        // Create Deal HashMaps from the parse objects
                        deal.put("objectId", parseObject.getString("objectId"));
                        deal.put("name", parseObject.getString("name"));
                        deal.put("author", parseObject.getString("author"));
                        deal.put("description", parseObject.getString("description"));
                        deal.put("points", Integer.toString(
                                        parseObject.getNumber("points").intValue()
                                )
                        );

                        deals.add(deal);

                        // Notify the ListViews SimpleAdapter adapter to update UI
                        dealsAdapter.notifyDataSetChanged();

                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

}
