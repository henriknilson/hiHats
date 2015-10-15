package hihats.electricity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hihats.electricity.R;
import hihats.electricity.activity.MainActivity;
import hihats.electricity.model.Bus;
import hihats.electricity.model.BusStop;
import hihats.electricity.model.DatedPosition;
import hihats.electricity.model.Ride;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;
import hihats.electricity.util.BusDataHelper;
import hihats.electricity.util.BusPositionService;
import hihats.electricity.util.ParseBusStopHelper;

public class RideFragment extends Fragment implements OnMapReadyCallback {

    // System variables
    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private RelativeLayout fragmentViewLayout;
    private View statusBarView;
    private Intent serviceIntent;
    private GoogleApiClient googleApiClient;

    // Buttons
    private Button findBusButton;
    private Button stopRideButton;

    // Map variables
    private MapView mapView;
    private GoogleMap googleMap;
    private final LatLng startMapOverview = new LatLng(57.69999167, 11.96330168);
    private Polyline line;
    private ArrayList<BusStop> busStops;
    private Boolean mapReady = false;
    private Boolean busStopsReady = false;

    // Active objects variables
    private Bus activeBus;
    private LatLng activeBusPosition;
    private Marker activeBusMarker;
    Ride activeRide;

    public static RideFragment newInstance() {
        return new RideFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        serviceIntent = new Intent(getActivity(), BusPositionService.class);
        googleApiClient = ((MainActivity)getActivity()).googleApiClient;
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        // Create the whole fragment view
        view = inflater.inflate(R.layout.fragment_ride, container, false);
        fragmentViewLayout = (RelativeLayout) view.findViewById(R.id.rideFragment);

        // Create the map view and fetch the map from Google
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Create the "Find My Bus" button and set its properties
        findBusButton = (Button) view.findViewById(R.id.find_bus_button);
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
        // Set map center to start and zoom level
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(startMapOverview, 13);
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
        /*
        // Place the bus stops on the map
        for (BusStop i : busStops){
            googleMap.addMarker(new MarkerOptions()
                            .position(i.getLatLng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop_small))
                            .title(i.getName())
            );
        }
        */

        // Draw a line along the bus path
        PolylineOptions options = new PolylineOptions().width(15).color(getResources().getColor(R.color.primary)).geodesic(true);
        line = googleMap.addPolyline(options);
        AssetManager am = getContext().getAssets();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(am.open("stops.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] s = line.split(" ");
                options.add(new LatLng(Double.parseDouble(s[0].substring(4)), Double.parseDouble(s[1].substring(5))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        line = googleMap.addPolyline(options);
    }

    /*
    Action methods for map
     */

    private void engageRideMode() {
        ((ViewGroup) view).removeView(findBusButton);

        // Inflate the status bar view and set the correct gravity
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        statusBarView = inflater.inflate(R.layout.statusbar_ride, container, false);

        // Add the status bar view to ride fragment
        fragmentViewLayout.addView(statusBarView, params);

        // Create the "Stop Ride" button and set its properties
        stopRideButton = (Button) view.findViewById(R.id.stop_ride_button);
        stopRideButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                System.out.println("PRESSED");
                stopRideMode();
            }
        });

        // Zoom in the camera on the active bus
        if (mapReady && busStopsReady && googleMap != null) {
            activeBusPosition = new LatLng(activeBus.getDatedPosition().getLatitude(), activeBus.getDatedPosition().getLongitude());

            activeBusMarker = googleMap.addMarker(new MarkerOptions()
                    .position(activeBusPosition)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                    .title(activeBus.getRegNr()));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(activeBusPosition)
                    .zoom(17)
                    .tilt(70)
                    .bearing(activeBus.getBearing())
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1500, null);
        }

        // Start looking for new bus positions and update the map when received
        serviceIntent.putExtra("busDgw", activeBus.getDgw());
        getActivity().startService(serviceIntent);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BusPositionService.BROADCAST_ACTION));
    }
    private void updateMap() {
        activeBusMarker.setPosition(activeBusPosition);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(activeBusPosition)
                .zoom(17)
                .tilt(70)
                .bearing(activeBus.getBearing())
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }
    private void stopRideMode() {
        ((ViewGroup) view).removeView(statusBarView);

        // Add the status bar view to ride fragment
        fragmentViewLayout.addView(findBusButton);

        // Reset camera to start position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(startMapOverview)
                .zoom(13)
                .tilt(0)
                .bearing(0)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1500, null);
        activeBusMarker.remove();

        // Stop looking for new bus positions
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(serviceIntent);
    }

    /*
    Action methods for ride object
     */

    private void createRideObject() {

    }
    private void updateRideObject() {

    }

    /*
    Asynchronous tasks
     */

    /**
     * Finds bus either via Network or via GPS, then sets the 'activeBus'
     * variable and runs the 'engageRideMode' method
     */
    private class AsyncFindBusTask extends AsyncTask<Void, Bus, Bus> implements LocationListener{

        private final BusDataHelper helper = new BusDataHelper();
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
                engageRideMode();
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
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long time = intent.getLongExtra("newTime", activeBus.getDatedPosition().getDate().getTime());
            double latitude = intent.getDoubleExtra("newLat", activeBus.getDatedPosition().getLatitude());
            double longitude = intent.getDoubleExtra("newLong", activeBus.getDatedPosition().getLongitude());
            float bearing = intent.getFloatExtra("newBearing", activeBus.getBearing());
            DatedPosition newPos = new DatedPosition(latitude, longitude, new Date(time));
            activeBus.setDatedPosition(newPos);
            activeBus.setBearing(bearing);
            activeBusPosition = new LatLng(activeBus.getDatedPosition().getLatitude(), activeBus.getDatedPosition().getLongitude());
            updateMap();
        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapReady = true;
        setupMap();
    }
}