package hihats.electricity.model;

/**
 * This class creates buses. It does so by receiving a bus ID
 * and depending on which ID it is, creates a bus with matching values.
 */

public class BusFactory {

    private static BusFactory instance;

    private BusFactory(){}

    public static BusFactory getInstance() {
        if (instance == null) {
            instance = new BusFactory();
        }
        return instance;
    }

    public Bus getBus(String dgwOrSystemId) {
        String dgw;
        String vin;
        String regNr;
        String systemId;
        BusType busType;
        if (dgwOrSystemId.startsWith("Ericsson")) {
            dgw = dgwOrSystemId;
            switch (dgwOrSystemId) {
                case "Ericsson$100020":
                    vin = "YV3U0V222FA100020";
                    regNr = "EPO 131";
                    systemId = "2501069301";
                    busType = BusType.ELECTRIC;
                    break;
                case "Ericsson$100021":
                    vin = "YV3U0V222FA100021";
                    regNr = "EPO 136";
                    systemId = "2501069758";
                    busType = BusType.ELECTRIC;
                    break;
                case "Ericsson$100022":
                    vin = "YV3U0V222FA100022";
                    regNr = "EPO 143";
                    systemId = "2501131248";
                    busType = BusType.ELECTRIC;
                    break;
                case "Ericsson$171164":
                    vin = "YV3T1U22XF1171164";
                    regNr = "EOG 604";
                    systemId = "2501142922";
                    busType = BusType.HYBRID;
                    break;
                case "Ericsson$171234":
                    vin = "YV3T1U225F1171234";
                    regNr = "EOG 606";
                    systemId = "2501069303";
                    busType = BusType.HYBRID;
                    break;
                case "Ericsson$171235":
                    vin = "YV3T1U227F1171235";
                    regNr = "EOG 616";
                    systemId = "2500825764";
                    busType = BusType.HYBRID;
                    break;
                case "Ericsson$171327":
                    vin = "YV3T1U221F1171327";
                    regNr = "EOG 622";
                    systemId = "2501075606";
                    busType = BusType.HYBRID;
                    break;
                case "Ericsson$171328":
                    vin = "YV3T1U223F1171328";
                    regNr = "EOG 627";
                    systemId = "2501069756";
                    busType = BusType.HYBRID;
                    break;
                case "Ericsson$171329":
                    vin = "YV3T1U225F1171329";
                    regNr = "EOG 631";
                    systemId = "2501131250";
                    busType = BusType.HYBRID;
                    break;
                case "Ericsson$171330":
                    vin = "YV3T1U223F1171330";
                    regNr = "EOG 634";
                    systemId = "2501074720";
                    busType = BusType.HYBRID;
                    break;
                case "Ericsson$Vin_Num_001":
                    vin = "TEST";
                    regNr = "TEST";
                    systemId = "TEST";
                    busType = BusType.HYBRID;
                    break;
                default:
                    throw new IllegalArgumentException("No such bus in database");
            }
        } else {
            systemId = dgwOrSystemId;
            switch (dgwOrSystemId) {
                case "2501069301":
                    dgw = "Ericsson$100020";
                    vin = "YV3U0V222FA100020";
                    regNr = "EPO 131";
                    busType = BusType.ELECTRIC;
                    break;
                case "2501069758":
                    dgw = "Ericsson$100021";
                    vin = "YV3U0V222FA100021";
                    regNr = "EPO 136";
                    busType = BusType.ELECTRIC;
                    break;
                case "2501131248":
                    dgw = "Ericsson$100022";
                    vin = "YV3U0V222FA100022";
                    regNr = "EPO 143";
                    busType = BusType.ELECTRIC;
                    break;
                case "2501142922":
                    dgw = "Ericsson$171164";
                    vin = "YV3T1U22XF1171164";
                    regNr = "EOG 604";
                    busType = BusType.HYBRID;
                    break;
                case "2501069303":
                    dgw = "Ericsson$171234";
                    vin = "YV3T1U225F1171234";
                    regNr = "EOG 606";
                    busType = BusType.HYBRID;
                    break;
                case "2500825764":
                    dgw = "Ericsson$171235";
                    vin = "YV3T1U227F1171235";
                    regNr = "EOG 616";
                    busType = BusType.HYBRID;
                    break;
                case "2501075606":
                    dgw = "Ericsson$171327";
                    vin = "YV3T1U221F1171327";
                    regNr = "EOG 622";
                    busType = BusType.HYBRID;
                    break;
                case "2501069756":
                    dgw = "Ericsson$171328";
                    vin = "YV3T1U223F1171328";
                    regNr = "EOG 627";
                    busType = BusType.HYBRID;
                    break;
                case "2501131250":
                    dgw = "Ericsson$171329";
                    vin = "YV3T1U225F1171329";
                    regNr = "EOG 631";
                    busType = BusType.HYBRID;
                    break;
                case "2501074720":
                    dgw = "Ericsson$171330";
                    vin = "YV3T1U223F1171330";
                    regNr = "EOG 634";
                    busType = BusType.HYBRID;
                    break;
                default:
                    throw new IllegalArgumentException("No such bus in database");
            }
        }
        return new Bus(dgw, vin, regNr, systemId, busType);
    }
}
