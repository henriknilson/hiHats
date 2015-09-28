package hihats.electricity.activity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseUser;


import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.ArrayList;

import hihats.electricity.R;
import hihats.electricity.model.BusStop;

/**
 * Created by henriknilson on 18/09/15.
 */
public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    Button logout;
    Button changeMapView;

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
    boolean mShowMap;
    GoogleMap mMap;
    Polyline line;
    ArrayList<BusStop> busStops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupToolbar();
        setupTablayout();
        mShowMap = initMap();
        setupMap();
        drawPath();

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        String struser = currentUser.getUsername().toString();

        // Locate TextView in main_activity.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        // Locate Button in main_activity.xml
        logout = (Button) findViewById(R.id.logout);
        changeMapView = (Button) findViewById(R.id.changeMapView);

        // Store shit

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                Intent intent = new Intent(MainActivity.this, LogInAndSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        changeMapView.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
            // Change map style
                if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }else
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
    }

    private boolean initMap() {
        if(mMap == null){
            MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }
        return mMap != null;
    }

    private void setupMap() {
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

        //Direct camera to given position and add markers
        if(mShowMap){
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng,15);
            mMap.moveCamera(update);
            mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("You are here!")
            );
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            for(int i = 0; i < busStops.size(); i++){
                mMap.addMarker(new MarkerOptions()
                        .position(busStops.get(i).getLatLng())
                        .title(busStops.get(i).getName())
                );
            }
        }
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupTablayout(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.title_section1)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.title_section2)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.title_section3)));
    }

    private void drawPath(){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLACK).geodesic(true);
        for(int i=0; i < busStops.size(); i++){
            LatLng point = busStops.get(i).getLatLng();
            options.add(point);
        }
        line = mMap.addPolyline(options);
    }
}
