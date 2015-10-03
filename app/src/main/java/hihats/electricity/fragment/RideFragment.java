package hihats.electricity.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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

public class RideFragment extends Fragment {

    View view;
    Button test1;
    Button test2;
    LocationTracker gps;

    //Map Variables
    MapView mapView;
    GoogleMap googleMap;
    Polyline line;
    ArrayList<BusStop> busStops;

    /*
    Fragment standard methods
     */

    public static RideFragment newInstance() {
        return new RideFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ride, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Locate Buttons in fragment_ride.xml
        test1 = (Button) view.findViewById(R.id.test1Button);
//        test2 = (Button) view.findViewById(R.id.test2Button);

//        // Test Button Click Listener
//        test1.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                if (gps == null) {
//                    gps = new LocationTracker(getContext());
//                }
//            }
//        });
//        // Test Button Click Listener
//        test2.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                if (gps != null) {
//                    //gps.stopUsingGPS();
//                }
//            }
//        });

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
        googleMap = mapView.getMap();
        // latitude and longitude
        getBusStopsFromParse();
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
    Map related methods
     */

    private void setupMap(GoogleMap googleMap) {
        //temp. latlng, later to be replaced with cellphone latlng
        LatLng latlng = new LatLng(57.68857167,11.97830168);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 10);
        googleMap.moveCamera(update);
        googleMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("You are here!")
        );
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        for(BusStop i : busStops){
            googleMap.addMarker(new MarkerOptions()
                            .position(i.getLatLng())
                            .title(i.getName())
            );
        }
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

    private class GetGpsPosition extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }

    //Fetches data on bus stops from parse cloud
    public void getBusStopsFromParse() {
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
                //Sorts bus stops in right order
                Collections.sort(busStops, new Comparator<BusStop>() {
                    @Override
                    public int compare(BusStop stop1, BusStop  stop2)
                    {
                        return  stop1.compareTo(stop2);
                    }
                });
                setupMap(googleMap);
                drawPath();
            }
        });
    }
}