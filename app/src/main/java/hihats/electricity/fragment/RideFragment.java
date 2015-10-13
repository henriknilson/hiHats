package hihats.electricity.fragment;

import android.content.Context;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import hihats.electricity.activity.MainActivity;
import hihats.electricity.model.Bus;
import hihats.electricity.model.BusStop;
import hihats.electricity.model.DatedPosition;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;
import hihats.electricity.util.BusDataHelper;
import hihats.electricity.util.ParseBusStopHelper;

public class RideFragment extends Fragment implements OnMapReadyCallback {

    LayoutInflater inflater;
    ViewGroup container;
    View fragmentView;
    RelativeLayout fragmentViewLayout;
    View statusBarView;

    Button findBusButton;
    Button stopRideButton;

    MapView mapView;
    GoogleMap googleMap;
    Polyline line;
    ArrayList<BusStop> busStops;

    Bus activeBus;
    DatedPosition activeBusPosition;

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

        this.inflater = inflater;
        this.container = container;

        // Create the whole fragment fragmentView
        fragmentView = inflater.inflate(R.layout.fragment_ride, container, false);
        fragmentViewLayout = (RelativeLayout) fragmentView.findViewById(R.id.rideFragment);

        // Create the map view and fetch the map from Google
        mapView = (MapView) fragmentView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Create the "Find My Bus" button and set its properties
        findBusButton = (Button) fragmentView.findViewById(R.id.find_bus_button);
        findBusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                AsyncFindBusTask task = new AsyncFindBusTask();
                task.execute();
            }
        });

        fetchBusStops();

        // Return the finished fragmentView
        return fragmentView;
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
    Setup methods for map
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
        LatLng startPos = new LatLng(57.69999167, 11.96330168);

        // Set map center and zoom level
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(startPos, 13);
        googleMap.moveCamera(update);

        // Configure the Google Map
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings mapUi = googleMap.getUiSettings();
        mapUi.setMapToolbarEnabled(false);
        mapUi.setCompassEnabled(false);
        mapUi.setTiltGesturesEnabled(false);
        mapUi.setScrollGesturesEnabled(false);
        mapUi.setZoomControlsEnabled(false);
        mapUi.setZoomGesturesEnabled(false);
    }
    private void setupBusStops() {
        // Place the bus stops on the map
        for (BusStop i : busStops){
            googleMap.addMarker(new MarkerOptions()
                            .position(i.getLatLng())
                            .icon(BitmapDescriptorFactory.defaultMarker(359))
                            .title(i.getName())
            );
        }

        // Draw a line between the bus stops
        PolylineOptions options = new PolylineOptions().width(16).color(getResources().getColor(R.color.primary)).geodesic(true);
        for (BusStop i : busStops){
            LatLng point = i.getLatLng();
            options.add(point);
        }
        line = googleMap.addPolyline(options);
    }

    /*
    Action methods for map
     */

    private void engageRidingMode() {
        ((ViewGroup) fragmentView).removeView(findBusButton);

        // Inflate the status bar view and set the correct gravity
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        statusBarView = inflater.inflate(R.layout.statusbar_ride, container, false);

        // Add the status bar fragmentView to ride fragment
        fragmentViewLayout.addView(statusBarView, params);

        // Create the "Stop Ride" button and set its properties
        stopRideButton = (Button) fragmentView.findViewById(R.id.stop_ride_button);
        stopRideButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                System.out.println("PRESSED");
            }
        });

        // Zoom in the camera on the active bus
        if (mapReady && busStopsReady && googleMap != null) {

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(activeBusPosition.getLatitude(), activeBusPosition.getLongitude()))
                    .zoom(17)
                    .tilt(70)
                    .bearing(activeBus.getBearing())
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
        }
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
                    .setFastestInterval(1 * 1000);
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
            return null;
        }

        @Override
        protected void onPostExecute(Bus bus) {
            super.onPostExecute(bus);
            if (bus == null) {
                //TODO GUI Alert
                System.out.println("NO NEARBY BUS FOUND");
            } else {
                activeBus = bus;
                activeBusPosition = bus.getDatedPosition();
                engageRidingMode();
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