package hihats.electricity.geolocation;

import android.location.Location;
import android.location.LocationManager;
import android.test.mock.MockContext;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by filip on 24/09/15.
 */
public class GeolocationHandlerTest extends TestCase {

    GeolocationHandler geolocationHandler1;
    GeolocationHandler geolocationHandler2;
    GeolocationHandler geolocationHandler3;
    GeolocationHandler geolocationHandler4;
    MockContext mockContext1;
    MockContext mockContext2;
    MockContext mockContext3;
    MockContext mockContext4;

    Location gpsLocation1;
    Location gpsLocation2;
    Location networkLocation1;
    Location networkLocation2;

    @Before
    public void setUp() throws Exception {

        mockContext1 = new MockContext();
        mockContext2 = new MockContext();
        mockContext3 = new MockContext();
        mockContext4 = new MockContext();

        geolocationHandler1 = new GeolocationHandler(mockContext1);
        geolocationHandler2 = new GeolocationHandler(mockContext2);
        geolocationHandler3 = new GeolocationHandler(mockContext3);
        geolocationHandler4 = new GeolocationHandler(mockContext4);

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