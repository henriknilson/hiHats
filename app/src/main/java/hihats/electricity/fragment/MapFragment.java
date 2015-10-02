package hihats.electricity.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
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

import java.util.ArrayList;

import hihats.electricity.R;
import hihats.electricity.model.BusStop;

public class MapFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {

    private OnFragmentInteractionListener mListener;
    private static String TAG = "MapFragment";

    BusStop svenHultin;
    BusStop chalmersPlatsen;
    BusStop kapellPlatsen;
    BusStop gotaPlatsen;
    BusStop valand;
    BusStop kungsPortsPlatsen;
    BusStop brunnsParken;
    BusStop lillaBommen;
    BusStop friHamnen;
    BusStop pumpGatan;
    BusStop regnBagsGatan;
    BusStop lindHolmen;
    BusStop lindHolmsPlatsen;
    BusStop teknikGatan;

    //Map Variables
    MapView mMapView;
    GoogleMap googleMap;
    Polyline line;
    ArrayList<BusStop> busStops;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.gMap);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();
        // latitude and longitude
        setupMap(googleMap);
        drawPath();

        return view;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void setupMap(GoogleMap googleMap) {
        //temp. latlng, later to be replaced with cellphone latlng
        LatLng latlng = new LatLng(57.68857167,11.97830168);

        //Busstop variables
        svenHultin = new BusStop(57.685825, 11.977261, "Sven Hultins Gata");
        chalmersPlatsen = new BusStop(57.689312, 11.973452, "Chalmersplatsen");
        kapellPlatsen = new BusStop(57.693730, 11.973338, "Kapellplatsen");
        gotaPlatsen = new BusStop(57.697652, 11.978949, "Götaplatsen");
        valand = new BusStop(57.700347, 11.974572, "Valand");
        kungsPortsPlatsen = new BusStop(57.704038, 11.969529, "Kungsportsplatsen");
        brunnsParken = new BusStop(57.706962, 11.967620, "Brunnsparken");
        lillaBommen = new BusStop(57.709549, 11.965952, "Lilla Bommen");
        friHamnen = new BusStop(57.718204, 11.959474, "Frihamnsporten");
        pumpGatan = new BusStop(57.712793, 11.946173, "Pumpgatan");
        regnBagsGatan = new BusStop(57.710765, 11.942761, "Regnbågsgatan");
        lindHolmen = new BusStop(57.708105, 11.938089, "Lindholmen");
        teknikGatan = new BusStop(57.706907, 11.937150, "Teknikgatan");
        lindHolmsPlatsen = new BusStop(57.706993, 11.938464, "Lindholmsplatsen");

        //Array containing the busstops
        busStops = new ArrayList<>();
        busStops.add(svenHultin);
        busStops.add(chalmersPlatsen);
        busStops.add(kapellPlatsen);
        busStops.add(gotaPlatsen);
        busStops.add(valand);
        busStops.add(kungsPortsPlatsen);
        busStops.add(brunnsParken);
        busStops.add(lillaBommen);
        busStops.add(friHamnen);
        busStops.add(pumpGatan);
        busStops.add(regnBagsGatan);
        busStops.add(lindHolmen);
        busStops.add(teknikGatan);
        busStops.add(lindHolmsPlatsen);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 15);
        googleMap.moveCamera(update);
        googleMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("You are here!")
        );
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        for(int i = 0; i < busStops.size(); i++){
            googleMap.addMarker(new MarkerOptions()
                            .position(busStops.get(i).getLatLng())
                            .title(busStops.get(i).getName())
            );
        }
    }

    private void drawPath(){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLACK).geodesic(true);
        for(int i=0; i < busStops.size(); i++){
            LatLng point = busStops.get(i).getLatLng();
            options.add(point);
        }
        line = googleMap.addPolyline(options);
    }

}