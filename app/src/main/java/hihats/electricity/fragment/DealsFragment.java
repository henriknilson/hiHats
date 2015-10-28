package hihats.electricity.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hihats.electricity.R;
import hihats.electricity.model.ParseDeal;

public class DealsFragment extends Fragment {

    private static final String TAG = "DealsFragment";
    
    ListView dealsListView;
    SimpleAdapter dealsAdapter;

    /**
     * The data container for the ListView
     */
    List<HashMap<String, String>> deals;

    int[] dealImages = new int[] {
            R.drawable.forest,
            R.drawable.mountain,
            R.drawable.bananas
    };

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
                "points",
                "image"
        };

        // "... and put their values in these places..."
        int[] to = {
                R.id.dealName,
                R.id.dealAuthor,
                R.id.dealDescription,
                R.id.dealPoints,
                R.id.dealImage
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

        ParseQuery<ParseDeal> query = ParseQuery.getQuery(ParseDeal.class);
        query.findInBackground(new FindCallback<ParseDeal>() {
            public void done(List<ParseDeal> parseDeals, ParseException e) {
                if (e == null) {

                    for(ParseDeal parseDeal : parseDeals) {
                        HashMap<String, String> deal = new HashMap<String, String>();

                        // Create HashMaps from the Deals
                        deal.put("objectId", parseDeal.getObjectId());
                        deal.put("name", parseDeal.getName());
                        deal.put("author", parseDeal.getAuthor());
                        deal.put("description", parseDeal.getDescription());
                        deal.put("points", parseDeal.getPoints() + " GreenPoints");
                        deal.put("image", Integer.toString(
                                dealImages[parseDeal.getImage()]
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
