package hihats.electricity.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hihats.electricity.R;
import hihats.electricity.database.DataHandler;
import hihats.electricity.database.IDataHandler;
import hihats.electricity.model.Deal;
import hihats.electricity.model.IDeal;

public class DealsFragment extends Fragment {

    private static final String TAG = DealsFragment.class.getSimpleName();
    
    private ListView dealsListView;
    private SimpleAdapter dealsAdapter;

    private final IDataHandler dataHandler = DataHandler.getInstance();

    /**
     * The data container for the ListView
     */
    private List<HashMap<String, String>> deals;

    private final int[] dealImages = new int[] {
            R.drawable.forest,
            R.drawable.mountain,
            R.drawable.bananas
    };

    public static DealsFragment newInstance() {
        return new DealsFragment();
    }

    public DealsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.deals = new ArrayList<>();

        dataHandler.getDeals(new IDataHandler.Callback<IDeal>() {
            @Override
            public void callback(List<IDeal> data) {
                for (IDeal parseDeal : data) {
                    HashMap<String, String> deal = new HashMap<>();

                    // Create HashMaps from the Deals
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
            }
        });
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

}
