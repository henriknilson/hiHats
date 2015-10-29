package hihats.electricity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hihats.electricity.R;
import hihats.electricity.activity.MainActivity;
import hihats.electricity.database.IDataHandler;
import hihats.electricity.database.ParseDataHandler;
import hihats.electricity.model.CurrentUser;
import hihats.electricity.model.IBusStop;
import hihats.electricity.model.DatedPosition;
import hihats.electricity.model.IBus;
import hihats.electricity.model.Ride;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;
import hihats.electricity.service.RideDataService;
import hihats.electricity.util.BusDataHelper;
import hihats.electricity.service.BusPositionService;
import hihats.electricity.util.GreenPointsCalculator;

public class RideFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = RideFragment.class.getSimpleName();

    // System variables
    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private RelativeLayout fragmentViewLayout;
    private Intent positionServiceIntent;
    private Intent rideServiceIntent;
    private GoogleApiClient googleApiClient;
    private IDataHandler dataHandler = ParseDataHandler.getInstance();

    // Buttons
    private ActionProcessButton getOnBusButton;
    private Button getOffBusButton;

    // Status bar view
    private View statusBarView;
    private TextView statusBarBusLabel;
    private TextView statusBarNextStopLabel;
    private TextView statusBarPointsLabel;

    // Map variables
    private MapView mapView;
    private GoogleMap googleMap;
    private final LatLng startMapOverview = new LatLng(57.69999167, 11.96330168);
    private ArrayList<IBusStop> busStops;
    private Boolean mapReady = false;
    private Boolean busStopsReady = false;

    // Active bus variables
    private IBus activeBus;
    private LatLng activeBusPosition;
    private boolean isActiveBusAtStop;
    private String activeBusNextStop;
    private int activeBusTotalDistance = 0;
    private int activeBusNewTotalDistance;
    private long activeBusTotalTime = 0;
    private long activeBusNewTotalTime;
    private Marker activeBusMarker;

    // Active ride variables
    private Date rideDate;
    private String rideBusStopFrom;
    private String rideBusStopToo;
    private double activeRidePoints;
    private int ridePoints;
    private int rideDistance;

    public static RideFragment newInstance() {
        return new RideFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        positionServiceIntent = new Intent(getActivity(), BusPositionService.class);
        rideServiceIntent = new Intent(getActivity(), RideDataService.class);
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
        getOnBusButton = (ActionProcessButton) view.findViewById(R.id.find_bus_button);
        getOnBusButton.setMode(ActionProcessButton.Mode.ENDLESS);
        getOnBusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                AsyncFindBusFromLocationTask task = new AsyncFindBusFromLocationTask();
                if (task.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    task.cancel(true);
                } else {
                    task.execute();
                }
            }
        });

        dataHandler.getBusStops(new IDataHandler.Callback() {
            @Override
            public void callback(List data) {
                busStops = new ArrayList<>();
                busStops.addAll(data);
                busStopsReady = true;
                setupBusStops();
            }
        });

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
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
    Setup methods for map
     */

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
        // Place the bus stops on the map
        for (IBusStop i : busStops){
            googleMap.addMarker(new MarkerOptions()
                            .position(i.getLatLng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop_small))
                            .title(i.getName())
            );
        }

        // Draw a line along the bus path
        PolylineOptions options = new PolylineOptions().width(15).color(getResources().getColor(R.color.primary)).geodesic(true);
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
        googleMap.addPolyline(options);
    }

    /*
    Main action methods for UI
     */
    private void engageRideMode() {
        ((ViewGroup) view).removeView(getOnBusButton);

        // Inflate the status bar view and set the correct gravity
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        statusBarView = inflater.inflate(R.layout.statusbar_ride, container, false);
        statusBarBusLabel = (TextView) statusBarView.findViewById(R.id.statusview_current_bus);
        statusBarNextStopLabel = (TextView) statusBarView.findViewById(R.id.statusview_current_next_stop);
        statusBarPointsLabel = (TextView) statusBarView.findViewById(R.id.statusview_current_points);

        // Add the status bar view to ride fragment
        fragmentViewLayout.addView(statusBarView, params);
        statusBarBusLabel.setText(activeBus.getRegNr());

        // Create the "Stop Ride" button and set its properties
        getOffBusButton = (Button) view.findViewById(R.id.stop_ride_button);
        getOffBusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Log.d(TAG, "STOP RIDE");
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

        // Start looking for new bus positions and update the ui when received
        positionServiceIntent.putExtra("busDgw", activeBus.getDgw());
        rideServiceIntent.putExtra("busDgw", activeBus.getDgw());
        getActivity().startService(positionServiceIntent);
        getActivity().startService(rideServiceIntent);
        getActivity().registerReceiver(positionBroadcastReceiver, new IntentFilter(BusPositionService.BROADCAST_ACTION));
        getActivity().registerReceiver(rideBroadcastReceiver, new IntentFilter(RideDataService.BROADCAST_ACTION));

        // Start logging the ride data
        startLoggingRide();
    }
    private void updateMap() {
        activeBusMarker.setPosition(activeBusPosition);
        CameraPosition cameraPosition;
        if (!isActiveBusAtStop) {
            cameraPosition = new CameraPosition.Builder()
                    .target(activeBusPosition)
                    .zoom(17)
                    .tilt(70)
                    .bearing(activeBus.getBearing())
                    .build();
        } else {
            cameraPosition = new CameraPosition.Builder()
                    .target(activeBusPosition)
                    .zoom(17)
                    .tilt(70)
                    .build();
        }
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }
    private void updateStatusBar() {
        statusBarNextStopLabel.setText(activeBusNextStop);
        statusBarPointsLabel.setText(String.format("%,d",
                GreenPointsCalculator.getInstance().getPoints(activeRidePoints)));
    }
    private void stopRideMode() {
        ((ViewGroup) view).removeView(statusBarView);

        // Add the status bar view to ride fragment
        fragmentViewLayout.addView(getOnBusButton);

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
        getActivity().unregisterReceiver(positionBroadcastReceiver);
        getActivity().unregisterReceiver(rideBroadcastReceiver);
        getActivity().stopService(positionServiceIntent);
        getActivity().stopService(rideServiceIntent);

        // Stop logging the ride data
        stopLoggingRide();

        // Show success dialog for the user
        showSuccessDialog();
    }
    private void showSuccessDialog() {
        SuccessFragment success = new SuccessFragment();
        Bundle bundle = new Bundle(10);
        bundle.putInt("Points", ridePoints);
        bundle.putString("StopFrom", rideBusStopFrom);
        bundle.putString("StopToo", rideBusStopToo);
        bundle.putLong("Time", activeBus.getDatedPosition().getDate().getTime() - rideDate.getTime());
        success.setArguments(bundle);
        //TODO Co2 stuff
        success.show(getActivity().getSupportFragmentManager(), "");
    }

    /*
    Action methods for ride object
     */

    private void startLoggingRide() {
        rideDate = activeBus.getDatedPosition().getDate();
        rideBusStopFrom = getClosestBusStop();
        rideBusStopToo = null;
        ridePoints = 0;
        activeRidePoints = 0;
        rideDistance = 0;
    }
    private void updateRide() {
        if (isActiveBusAtStop) {
            rideBusStopToo = getClosestBusStop();
        }
        activeRidePoints +=
                GreenPointsCalculator.getInstance().getLivePoints(
                        getTraveledDistance(),
                        getTraveledTime());
        rideDistance += getTraveledDistance();
    }
    private void stopLoggingRide() {
        activeBusTotalDistance = 0;
        activeBusTotalTime = 0;
        ridePoints = GreenPointsCalculator.getInstance().getPoints(activeRidePoints);
        Ride ride = new Ride(
                rideDate,
                rideBusStopFrom,
                rideBusStopToo,
                ridePoints,
                rideDistance,
                CurrentUser.getInstance().getUserName());
        //ParseDataHandler.uploadRide(ride);
    }

    /*
    Help methods
     */

    private String getClosestBusStop() {
        float[] distance = new float[1];
        for (IBusStop stop : busStops) {
            Location.distanceBetween(activeBusPosition.latitude, activeBusPosition.longitude, stop.getLatLng().latitude, stop.getLatLng().longitude, distance);
            if (distance[0] < 50.0f) {
                return stop.getName();
            }
        }
        return null;
    }
    private int getTraveledDistance() {
        if (activeBusTotalDistance == 0) {
            activeBusTotalDistance = activeBusNewTotalDistance;
        }
        int distance = activeBusNewTotalDistance - activeBusTotalDistance;
        activeBusTotalDistance = activeBusNewTotalDistance;
        return distance;
    }
    private long getTraveledTime() {
        if (activeBusTotalTime == 0) {
            activeBusTotalTime = activeBusNewTotalTime;
        }
        long time = activeBusNewTotalTime - activeBusTotalTime;
        activeBusTotalTime = activeBusNewTotalTime;
        return time;
    }

    /*
    Asynchronous tasks
     */

    /**
     * Finds bus either via Network or via GPS, then sets the 'activeBus'
     * variable and runs the 'engageRideMode' method
     */
    private class AsyncFindBusFromLocationTask extends AsyncTask<Void, IBus, IBus> implements LocationListener{

        private final BusDataHelper helper = new BusDataHelper();
        private LocationRequest locationRequest;
        private Location location;

        /**
         * Sets the "get on my bus" button status to loading and prepares
         * the LocationRequest that will be used during the task execution.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getOnBusButton.setProgress(50);
            locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000)
                    .setNumUpdates(1);
        }

        /**
         * Checks first if the device has location services enabled.
         * Then starts waiting for a location.
         * When the location is recieved its used to find a nearby bus via
         * the BusDataHelper class.
         * If all goes well it returns a bus object, if not it returns null.
         * @param params Not used since this is a void method.
         * @return A bus object with the bus being nearest the device,
         * null if no nearby bus is found.
         */
        @Override
        protected IBus doInBackground(Void... params) {
            Log.d(TAG, "GET ON BUS TASK EXECUTED");
            if (helper.isGPSEnabled(getContext())) {
                // Request GPS updates
                Looper.prepare();
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                // Start waiting... when this is done, we'll have the location in this.location.
                Looper.loop();
                // Now go use the location to load some data.
                if (location != null) {
                    try {
                        IBus bus = helper.getBusNearestLocation(location);
                        if (bus != null) {
                            return bus;
                        }
                    } catch (AccessErrorException e) {
                        Log.d(TAG, "NO INTERNET CONNECTION");
                    } catch (NoDataException e) {
                        Log.d(TAG, "ELECTRICITY SERVER DOWN");
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(IBus bus) {
            super.onPostExecute(bus);
            if (bus == null) {
                Log.d(TAG, "NO NEARBY BUS FOUND");
                getOnBusButton.setProgress(-1);
                getOnBusButton.setText(R.string.error_no_bus_found);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOnBusButton.setProgress(0);
                        getOnBusButton.setText(R.string.get_on_bus_button_text);
                    }
                }, 2000);
            } else {
                getOnBusButton.setProgress(0);
                activeBus = bus;
                engageRideMode();
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            this.location = location;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            Looper.myLooper().quit();
        }
    }
    private final BroadcastReceiver positionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long time = intent.getLongExtra("newTime", activeBus.getDatedPosition().getDate().getTime());
            double latitude = intent.getDoubleExtra("newLat", activeBus.getDatedPosition().getLatitude());
            double longitude = intent.getDoubleExtra("newLong", activeBus.getDatedPosition().getLongitude());
            float bearing = intent.getFloatExtra("newBearing", activeBus.getBearing());
            DatedPosition newPos = new DatedPosition(latitude, longitude, new Date(time));
            activeBus.setDatedPosition(newPos);
            activeBus.setBearing(bearing);
            activeBusPosition = new LatLng(
                    activeBus.getDatedPosition().getLatitude(),
                    activeBus.getDatedPosition().getLongitude());
            updateMap();
        }
    };
    private final BroadcastReceiver rideBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isBusAtStop = intent.getBooleanExtra("isBusAtStop", isActiveBusAtStop);
            String nextStop = intent.getStringExtra("nextStop");
            int totalDistance = intent.getIntExtra("totalDistance", activeBusTotalDistance);
            isActiveBusAtStop = isBusAtStop;
            activeBusNextStop = nextStop;
            activeBusNewTotalDistance = totalDistance;
            activeBusNewTotalTime = activeBus.getDatedPosition().getDate().getTime();
            updateRide();
            updateStatusBar();
        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapReady = true;
        setupMap();
    }


}