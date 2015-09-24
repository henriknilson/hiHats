package hihats.electricity.activity;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import hihats.electricity.R;

/**
 * Created by henriknilson on 18/09/15.
 */
public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    Button logout;

    //Map Variables
    boolean mShowMap;
    GoogleMap mMap;

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

        // Locate TextView in main_activity.xmlivity.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        // Locate Button in main_activity_activity.xml
        logout = (Button) findViewById(R.id.logout);

        // Store shit

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
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

        if(mShowMap){
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng,15);
            mMap.moveCamera(update);
            mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("You are here!")
                    .anchor(.5f,.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_ic_googleplayservices))
            );
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
