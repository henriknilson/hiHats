package hihats.electricity.util;

import junit.framework.TestCase;

import java.util.Date;

import hihats.electricity.model.DatedPosition;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class RmcConverterTest extends TestCase {

    public void testRmcToLocation() throws Exception {
        boolean thrown = false;

        // Test1
        try {
            RmcConverter.rmcToPosition("$GPGSV,2,1,08", "100");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Input must be in GPRMC format")) {
                thrown = true;
            }
        }
        assertTrue(thrown);
        thrown = false;

        // Test2
        try {
            RmcConverter.rmcToPosition("$GPRMC,2,1,08", "100");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Illegal GPRMC format: $GPRMC,2,1,08")) {
                thrown = true;
            }
        }
        assertTrue(thrown);

        // Test3
        Long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        DatedPosition loc = RmcConverter.rmcToPosition("$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A", millis.toString());
        assertEquals(48.117300, loc.getLatitude());
        assertEquals(11.516666666666667, loc.getLongitude());
        assertEquals(date, loc.getDate());
    }

    public void testRmcToSpeed() throws Exception {
        float speed = RmcConverter.rmcToSpeed("$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A");
        assertEquals(41.4848f, speed);
    }

    public void testRmcToBearing() throws Exception {
        float bearing = RmcConverter.rmcToBearing("$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A");
        assertEquals(84.4f, bearing);
    }
}