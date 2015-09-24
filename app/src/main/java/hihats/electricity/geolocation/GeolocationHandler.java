package hihats.electricity.geolocation;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.List;

/**
 * Created by filip on 24/09/15.
 */
public class GeolocationHandler implements IGeolocationHandler {
    private static GeolocationHandler instance = new GeolocationHandler();
    private LocationManager locationManager;

    private static final String LOG_TAG = "GeolocationHandler";

    public static GeolocationHandler getInstance() {
        return instance;
    }

    private GeolocationHandler() {}

    @Override
    public List<String> getProviders() {

        List<String> providers = this.locationManager.getProviders(true);

        Log.d(this.LOG_TAG, providers.toString());

        return providers;
    }
}
