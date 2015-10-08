package hihats.electricity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import java.util.concurrent.ExecutionException;

import hihats.electricity.R;
import hihats.electricity.activity.MainActivity;
import hihats.electricity.model.Bus;
import hihats.electricity.model.BusStop;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;
import hihats.electricity.util.BusDataHelper;
import hihats.electricity.util.ParseBusStopHelper;

public class RideFragment extends Fragment implements OnMapReadyCallback {

    View view;
    Button findBusButton;
    ViewGroup container;

    MapView mapView;
    GoogleMap googleMap;
    Polyline line;
    ArrayList<BusStop> busStops;
    Location currentLocation;
    LatLng currentPosition;

    // Promise/async variables
    private GoogleApiClient googleApiClient;
    private Boolean mapReady = false;
    private Boolean busStopsReady = false;

    public static RideFragment newInstance() {
        return new RideFragment();
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        googleApiClient = ((MainActivity)getActivity()).googleApiClient;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        // Create the whole fragment view
        view = inflater.inflate(R.layout.fragment_ride, container, false);
        this.container = container;

        // Create the map view and fetch the map from Google
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Create the "Find My Bus" button and set its properties
        findBusButton = (Button) view.findViewById(R.id.findBusButton);
        findBusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                AsyncFindBusTask task = new AsyncFindBusTask();
                task.execute();
            }
        });

        fetchBusStops();

        // Return the finished view
        return view;
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

    /*
    Help methods for map
     */

    private void fetchBusStops() {
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
                setupBusStops();
            }
        });
    }
    private void setupMap() {
        // To be replaced with device current position
        currentPosition = new LatLng(57.68857167,11.97830168);

        // Set map center and zoom level
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentPosition, 12);
        googleMap.moveCamera(update);

        // Configure the Google Map
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    private void setupBusStops() {
        // Place the bus stops on the map
        for (BusStop i : busStops){
            googleMap.addMarker(new MarkerOptions()
                            .position(i.getLatLng())
                            .title(i.getName())
            );
        }

        // Draw a line between the bus stops
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLACK).geodesic(true);
        for(BusStop i : busStops){
            LatLng point = i.getLatLng();
            options.add(point);
        }
        line = googleMap.addPolyline(options);
    }

    /*
    Asynchronous tasks
     */

    private class AsyncFindBusTask extends AsyncTask<Void, Bus, Bus> implements LocationListener{

        private BusDataHelper helper = new BusDataHelper();
        private LocationRequest locationRequest;
        private Location location;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000)
                    .setMaxWaitTime(3 * 1000);
        }

        @Override
        protected Bus doInBackground(Void... params) {
            System.out.println("INSIDE TASK");
            if (helper.isConnectedToWifi(getContext())) {
                return getBusFromNetwork();
            } else if (helper.isGPSEnabled(getContext())) {
                return getBusFromLocation();
            }
            return null;
        }

        private Bus getBusFromNetwork() {
            try {
                return helper.getBusFromSystemId();
            } catch (AccessErrorException | NoDataException e) {
                //TODO GUI Alert
                if (helper.isGPSEnabled(getContext())) {
                    return getBusFromLocation();
                }
            }
            return null;
        }
        private Bus getBusFromLocation() {
            // Request GPS updates
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            // Start waiting... when this is done, we'll have the location in this.location.
            Looper.loop();
            // Now go use the location to load some data.
            try {
                Bus bus = helper.getBusNearestLocation(location);
                if (bus != null) {
                    return bus;
                }
            } catch (AccessErrorException e) {
                //TODO GUI Alert
                System.out.println("NO INTERNET CONNECTION");
            } catch (NoDataException e) {
                //TODO GUI Alert
                System.out.println("ELECTRICITY SERVER DOWN");
            }
            currentLocation = location;
            return null;
        }

        @Override
        protected void onPostExecute(Bus bus) {
            super.onPostExecute(bus);
            //TODO GUI Alert
            if (bus == null) {
                System.out.println("NO NEARBY BUS FOUND");
            } else {
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

                if (mapReady && busStopsReady && googleMap != null) {

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentPosition)
                            .zoom(17)
                            .tilt(70)
                            .build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                }
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            this.location = location;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            System.out.println(location.toString());
            Looper.myLooper().quit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapReady = true;
        setupMap();
    }
}