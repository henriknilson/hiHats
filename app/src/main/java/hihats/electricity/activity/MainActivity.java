package hihats.electricity.activity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;


import android.content.Intent;
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

    //Map Variables
    boolean mShowMap;
    GoogleMap mMap;
    ArrayList<BusStop> busStops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupToolbar();
        setupTablayout();
        mShowMap = initMap();
        setupMap();

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

        //temp. latlng
        LatLng latlng = new LatLng(57.68857167,11.97830168);

        svenHultin = new BusStop(57.685825, 11.977261, "Sven Hultins Gata");
        chalmersPlatsen = new BusStop(57.689312, 11.973452, "Chalmersplatsen");

        busStops = new ArrayList<>();
        busStops.add(svenHultin);
        busStops.add(chalmersPlatsen);

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
}
