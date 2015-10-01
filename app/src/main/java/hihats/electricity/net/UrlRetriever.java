package hihats.electricity.net;

/**
 * Created by fredrikkindstrom on 30/09/15.
 */
public class UrlRetriever {

    public String getUrl(String busId, String sensorId, String resourceId, int time) {
        long t2 = System.currentTimeMillis();
        long t1 = t2 - time;
        if (sensorId != null) {
            return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&sensorSpec=" + sensorId + "&t1=" + t1 + "&t2=" + t2;
        } else {
            return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&resourceSpec=" + resourceId + "&t1=" + t1 + "&t2=" + t2;
        }
    }
}
