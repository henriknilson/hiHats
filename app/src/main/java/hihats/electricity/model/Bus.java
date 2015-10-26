package hihats.electricity.model;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */

public class Bus {

    private enum BusType {
        ELECTRIC, HYBRID
    }

    private String dgw;
    private String vin;
    private String regNr;
    private String systemId;
    private BusType busType;
    private DatedPosition datedPosition;
    private float speed;
    private float bearing;

    public Bus(String dgw, String vin, String regNr, String systemId, BusType busType) {
        this.dgw = dgw;
        this.vin = vin;
        this.regNr = regNr;
        this.systemId = systemId;
        this.busType = busType;
    }

    /*
    Getters
     */

    public String getDgw() {
        return dgw;
    }
    public String getVin() {
        return vin;
    }
    public String getRegNr() {
        return regNr;
    }
    public String getSystemId() {
        return systemId;
    }
    public BusType getBusType() {
        return busType;
    }
    public DatedPosition getDatedPosition() {
        return datedPosition;
    }
    public float getSpeed() {
        return speed;
    }
    public float getBearing() {
        return bearing;
    }

    /*
    Setters
     */

    public void setDatedPosition(DatedPosition datedPosition) {
        this.datedPosition = datedPosition;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }
}
