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
    private Location location;
    private float speed;
    private float bearing;

    public Bus(String dgwOrSystemId) {
        if (dgwOrSystemId.startsWith("Ericsson")) {
            this.dgw = dgwOrSystemId;
            switch (dgwOrSystemId) {
                case "Ericsson$100020":
                    this.vin = "YV3U0V222FA100020";
                    this.regNr = "EPO 131";
                    this.systemId = "2501069301";
                    this.busType = BusType.ELECTRIC;
                    break;
                case "Ericsson$100021":
                    this.vin = "YV3U0V222FA100021";
                    this.regNr = "EPO 136";
                    this.systemId = "2501069758";
                    this.busType = BusType.ELECTRIC;
                    break;
                case "Ericsson$100022":
                    this.vin = "YV3U0V222FA100022";
                    this.regNr = "EPO 143";
                    this.systemId = "2501131248";
                    this.busType = BusType.ELECTRIC;
                    break;
                case "Ericsson$171164":
                    this.vin = "YV3T1U22XF1171164";
                    this.regNr = "EOG 604";
                    this.systemId = "2501142922";
                    this.busType = BusType.HYBRID;
                    break;
                case "Ericsson$171234":
                    this.vin = "YV3T1U225F1171234";
                    this.regNr = "EOG 606";
                    this.systemId = "2501069303";
                    this.busType = BusType.HYBRID;
                    break;
                case "Ericsson$171235":
                    this.vin = "YV3T1U227F1171235";
                    this.regNr = "EOG 616";
                    this.systemId = "2500825764";
                    this.busType = BusType.HYBRID;
                    break;
                case "Ericsson$171327":
                    this.vin = "YV3T1U221F1171327";
                    this.regNr = "EOG 622";
                    this.systemId = "2501075606";
                    this.busType = BusType.HYBRID;
                    break;
                case "Ericsson$171328":
                    this.vin = "YV3T1U223F1171328";
                    this.regNr = "EOG 627";
                    this.systemId = "2501069756";
                    this.busType = BusType.HYBRID;
                    break;
                case "Ericsson$171329":
                    this.vin = "YV3T1U225F1171329";
                    this.regNr = "EOG 631";
                    this.systemId = "2501131250";
                    this.busType = BusType.HYBRID;
                    break;
                case "Ericsson$171330":
                    this.vin = "YV3T1U223F1171330";
                    this.regNr = "EOG 634";
                    this.systemId = "2501074720";
                    this.busType = BusType.HYBRID;
                    break;
                default:
                    throw new IllegalArgumentException("No such bus in database");
            }
        } else {
            this.systemId = dgwOrSystemId;
            switch (dgwOrSystemId) {
                case "2501069301":
                    this.dgw = "Ericsson$100020";
                    this.vin = "YV3U0V222FA100020";
                    this.regNr = "EPO 131";
                    this.busType = BusType.ELECTRIC;
                    break;
                case "2501069758":
                    this.dgw = "Ericsson$100021";
                    this.vin = "YV3U0V222FA100021";
                    this.regNr = "EPO 136";
                    this.busType = BusType.ELECTRIC;
                    break;
                case "2501131248":
                    this.dgw = "Ericsson$100022";
                    this.vin = "YV3U0V222FA100022";
                    this.regNr = "EPO 143";
                    this.busType = BusType.ELECTRIC;
                    break;
                case "2501142922":
                    this.dgw = "Ericsson$171164";
                    this.vin = "YV3T1U22XF1171164";
                    this.regNr = "EOG 604";
                    this.busType = BusType.HYBRID;
                    break;
                case "2501069303":
                    this.dgw = "Ericsson$171234";
                    this.vin = "YV3T1U225F1171234";
                    this.regNr = "EOG 606";
                    this.busType = BusType.HYBRID;
                    break;
                case "2500825764":
                    this.dgw = "Ericsson$171235";
                    this.vin = "YV3T1U227F1171235";
                    this.regNr = "EOG 616";
                    this.busType = BusType.HYBRID;
                    break;
                case "2501075606":
                    this.dgw = "Ericsson$171327";
                    this.vin = "YV3T1U221F1171327";
                    this.regNr = "EOG 622";
                    this.busType = BusType.HYBRID;
                    break;
                case "2501069756":
                    this.dgw = "Ericsson$171328";
                    this.vin = "YV3T1U223F1171328";
                    this.regNr = "EOG 627";
                    this.busType = BusType.HYBRID;
                    break;
                case "2501131250":
                    this.dgw = "Ericsson$171329";
                    this.vin = "YV3T1U225F1171329";
                    this.regNr = "EOG 631";
                    this.busType = BusType.HYBRID;
                    break;
                case "2501074720":
                    this.dgw = "Ericsson$171330";
                    this.vin = "YV3T1U223F1171330";
                    this.regNr = "EOG 634";
                    this.busType = BusType.HYBRID;
                    break;
                default:
                    throw new IllegalArgumentException("No such bus in database");
            }
        }
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
    public Location getLocation() {
        return location;
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

    public void setLocation(Location location) {
        this.location = location;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }
}
