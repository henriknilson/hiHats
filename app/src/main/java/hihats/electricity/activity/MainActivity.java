package hihats.electricity.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import hihats.electricity.R;
import hihats.electricity.adapter.TabsAdapter;

/**
 * This is the main activity where all the content in the app is
 * displayed and the absolute largest part of the application lives.
 * This holds all fragments and all views related to the app content.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TabLayout tabLayout;
    public GoogleApiClient googleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a GoogleApiClient instance
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);

        // Create the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        // Locate Button in activity_main.xml
       // logout = (Button) findViewById(R.id.logout);

        // Logout Button Click Listener
        // logout.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View arg0) {
        //        // Logout current user
        //        ParseUser.logOut();
        //        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //        startActivity(intent);
        //        finish();
        //    }
        // });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                break;
            case R.id.logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Connected to Google Play services!
        // The good stuff goes here.
        Log.d(TAG, "GOOGLE API CONNECTED");
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.
    }
}
