package hihats.electricity.geolocation;

import android.location.Location;
import android.location.LocationManager;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by filip on 24/09/15.
 */
public class GeolocationHandlerTest {

    GeolocationHandler geolocationHandler1;
    GeolocationHandler geolocationHandler2;
    GeolocationHandler geolocationHandler3;
    GeolocationHandler geolocationHandler4;
    MockContext mockContext;

    Location gpsLocation1;
    Location gpsLocation2;
    Location networkLocation1;
    Location networkLocation2;

    @Before
    public void setUp() throws Exception {

        mockContext = new MockContext();

        geolocationHandler1 = new GeolocationHandler(mockContext);
        geolocationHandler2 = new GeolocationHandler(mockContext);
        geolocationHandler3 = new GeolocationHandler(mockContext);
        geolocationHandler4 = new GeolocationHandler(mockContext);

        gpsLocation1 = new Location(LocationManager.GPS_PROVIDER);
        gpsLocation2 = new Location(LocationManager.GPS_PROVIDER);
        networkLocation1 = new Location(LocationManager.NETWORK_PROVIDER);
        networkLocation2 = new Location(LocationManager.NETWORK_PROVIDER);

        gpsLocation1.setSpeed((float)13.37);
        gpsLocation1.setSpeed((float)-0.1337);
        networkLocation1.setSpeed((float)13.37);
        networkLocation1.setSpeed((float)-0.1337);

        geolocationHandler1.setCurrentLocation(gpsLocation1);
        geolocationHandler2.setCurrentLocation(gpsLocation2);
        geolocationHandler3.setCurrentLocation(networkLocation1);
        geolocationHandler4.setCurrentLocation(networkLocation2);

    }

    @Test
    public void testIsTravelling() throws Exception {
        assertTrue(geolocationHandler1.isTravelling());
        assertFalse(geolocationHandler2.isTravelling());
        assertTrue(geolocationHandler3.isTravelling());
        assertFalse(geolocationHandler4.isTravelling());
    }
}