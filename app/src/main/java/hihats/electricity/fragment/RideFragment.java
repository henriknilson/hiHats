package hihats.electricity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
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
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.util.FindBusHelper;
import hihats.electricity.util.ParseBusStopHelper;

public class RideFragment extends Fragment implements OnMapReadyCallback {

    View view;
    Button findBusButton;
    TableLayout statusLayout;

    MapView mapView;
    GoogleMap googleMap;
    Polyline line;
    ArrayList<BusStop> busStops;
    LatLng currentPosition;

    // Promise/async variables
    private Boolean mapReady = false;
    private Boolean busStopsReady = false;

    // Animations
    private Animation animFlyout;
    private Animation animFlyin;

    public static RideFragment newInstance() {
        return new RideFragment();
    }

    public GoogleMap getMap() {
        return this.googleMap;
    }

    public void setMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public LatLng getCurrentPosition() {
        return this.currentPosition;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ride, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        findBusButton = (Button) view.findViewById(R.id.findBusButton);
        findBusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                new FindBusIdTask().execute();

                ((ViewGroup) findBusButton.getParent()).removeView(findBusButton);

                RelativeLayout rideFragment = (RelativeLayout) view.findViewById(R.id.rideFragment);

                // Inflate the status bar view and set the correct gravity
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                LayoutInflater layoutInflater = (LayoutInflater)
                        getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View statusBarView = layoutInflater.inflate(R.layout.statusbar_ride, container, false);
                statusBarView.setLayoutParams(params);

                // Add the status bar view to ride fragment
                rideFragment.addView(statusBarView, 1);

                if (mapReady && busStopsReady && getMap() != null) {

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(getCurrentPosition())
                            .zoom(17)
                            .tilt(70)
                            .build();
                    getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                }

            }
        });

        mapView.getMapAsync(this);
        fetchBusStops();

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
        currentPosition = new LatLng(57.68857167,11.97830168);

        // Set map center and zoom level
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentPosition, 12);
        googleMap.moveCamera(update);

        // Configure the Google Map
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Create markers for each bus stop
        for(BusStop i : busStops){
            googleMap.addMarker(new MarkerOptions()
                            .position(i.getLatLng())
                            .title(i.getName())
            );
        }

        // Draw a line between the markers
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

    /*
    AsynkTasks
     */

    private class FindBusIdTask extends AsyncTask<Void, String, String> {

        private FindBusHelper helper = new FindBusHelper();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (helper.isConnectedToWifi(getContext())) {
                try {
                    return helper.askNetworkForId();
                } catch (AccessErrorException e) {
                    e.printStackTrace();
                }
            } else {
                return "Wifi not connected";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
        }
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