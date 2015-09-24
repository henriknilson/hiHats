package hihats.electricity.geolocation;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by filip on 24/09/15.
 */
public class GeolocationHandler implements LocationListener {

    private Context mContext;
    private LocationManager locationManager;
    private Location currentLocation = null;

    private static final String LOG_TAG = "geolocation";
    private static final float MIN_MOVING_SPEED = 3; // 10.8 km/h
    private static final int ACCURACY = Criteria.ACCURACY_FINE;
    private static final int POWER = Criteria.POWER_LOW;

    public GeolocationHandler(Context mContext) {
        this.mContext = mContext;
        this.locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Returns the most accurate current location if providers are available.
     * @return Location
     */
    public Location getCurrentLocation() {

        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(ACCURACY);
            criteria.setPowerRequirement(POWER);

            String locationProvider = locationManager.getBestProvider(criteria, true);

            // Start listening for location
            locationManager.requestLocationUpdates(locationProvider, 0, 0, this);

            // Set current location
            this.currentLocation = locationManager.getLastKnownLocation(locationProvider);

            // Stop listening for location
            locationManager.removeUpdates(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.currentLocation;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    /**
     * Decides if the device is on a bus, based on MIN_MOVING_SPEED.
     * @return boolean
     */
    public boolean isTravelling() {
        return this.getCurrentLocation().getSpeed() > MIN_MOVING_SPEED;
    }

    public boolean isGPSEnabled() {
        return this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkEnabled() {
        return this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public LocationManager getLocationManager() {
        return this.locationManager;
    }

    public float getMinMovingSpeed() {
        return MIN_MOVING_SPEED;
    }

    public int getAccuracy() {
        return ACCURACY;
    }

    public int getPower() {
        return POWER;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}