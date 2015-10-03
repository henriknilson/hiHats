package hihats.electricity.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hihats.electricity.R;
import hihats.electricity.model.BusStop;
import hihats.electricity.util.LocationTracker;
import hihats.electricity.util.ParseBusStopHelper;

public class RideFragment extends Fragment implements OnMapReadyCallback {

    View view;
    Button findBusButton;
    LocationTracker gps;

    // Map variables
    MapView mapView;
    GoogleMap googleMap;
    Polyline line;
    ArrayList<BusStop> busStops;

    // Promise variables
    private Boolean mapReady = false;
    private Boolean busStopsReady = false;

    public static RideFragment newInstance() {
        return new RideFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ride, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        findBusButton = (Button) view.findViewById(R.id.findBusButton);

        findBusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if (gps == null) {
                    gps = new LocationTracker(getContext());
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
        fetchBusStops();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Add markers, setup the camera, and all map settings in general.
     */
    private void setupMap() {

        // Make sure both Google and Parse requests are done
        if(!(mapReady && busStopsReady)) {
            return;
        }

        // To be replaced with device current position
        LatLng currentPosition = new LatLng(57.68857167,11.97830168);

        // Set map center and zoom level
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentPosition, 10);
        googleMap.moveCamera(update);

        // Set map type (terrain, normal, hybrid etc.)
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Create markers for each bus stop
        for(BusStop i : busStops){
            googleMap.addMarker(new MarkerOptions()
                            .position(i.getLatLng())
                            .title(i.getName())
            );
        }

        drawPath();

    }
    private void drawPath() {
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLACK).geodesic(true);
        for(BusStop i : busStops){
            LatLng point = i.getLatLng();
            options.add(point);
        }
        line = googleMap.addPolyline(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapReady = true;

        setupMap();
    }

    // Fetches data on bus stops from parse cloud
    public void fetchBusStops() {
        ParseQuery<ParseBusStopHelper> stops = ParseQuery.getQuery(ParseBusStopHelper.class);
        stops.findInBackground(new FindCallback<ParseBusStopHelper>() {
            @Override
            public void done(List<ParseBusStopHelper> objects, com.parse.ParseException e) {

                busStops = new ArrayList<>();
                for (ParseBusStopHelper i : objects) {
                    //Add all to list of stops
                    BusStop stop = new BusStop(i.getLat(), i.getLng(), i.getStopName(), i.getOrder());
                    busStops.add(stop);
                }

                // Sorts bus stops in right order
                Collections.sort(busStops, new Comparator<BusStop>() {
                    @Override
                    public int compare(BusStop stop1, BusStop stop2) {
                        return stop1.compareTo(stop2);
                    }
                });
                busStopsReady = true;
                setupMap();
            }
        });
    }
}