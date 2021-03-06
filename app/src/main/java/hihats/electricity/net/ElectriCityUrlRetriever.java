package hihats.electricity.net;

/**
 * This class creates a url to use with the Electricity API request.
 */
public class ElectriCityUrlRetriever {

    public String getUrl(String busId, String sensorId, String resourceId, int time) {
        long t2 = System.currentTimeMillis();
        long t1 = t2 - time;
        if (busId != null) {
            if (sensorId != null) {
                return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&sensorSpec=" + sensorId + "&t1=" + t1 + "&t2=" + t2;
            } else {
                return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&resourceSpec=" + resourceId + "&t1=" + t1 + "&t2=" + t2;
            }
        } else {
            if (sensorId != null) {
                return "https://ece01.ericsson.net:4443/ecity?sensorSpec=" + sensorId + "&t1=" + t1 + "&t2=" + t2;
            } else {
                return "https://ece01.ericsson.net:4443/ecity?resourceSpec=" + resourceId + "&t1=" + t1 + "&t2=" + t2;
            }
        }
    }
}
