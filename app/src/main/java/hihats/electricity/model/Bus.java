package hihats.electricity.model;

import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;
import hihats.electricity.util.BusDataHelper;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */

public class Bus {

    private final BusDataHelper bdh;
    private final String busId;
    private Location location;
    private float speed;
    private float bearing;

    public Bus(String busId, Location location, float speed, float bearing) {
        bdh = new BusDataHelper();
        this.busId = busId;
        this.location = location;
        this.speed = speed;
        this.bearing = bearing;
    }

    public void updateLocation() throws AccessErrorException, NoDataException {
        location = bdh.getLastLocationForBus(busId);
    }
}
