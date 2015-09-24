package hihats.electricity.net;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * This is the backend class for all api http calls.
 * It sends ApiDataObjects that the class above ApiDataHelper then parses to more useful formats.
 */
public class ApiHttpHandler {

    private static String key = "Basic Z3JwNDU6RlozRWN1TFljag==";

    /*
    Get methods
     */

    public static ApiDataObject getMostRecentLocationForBus(String busId) throws AccessErrorException {
        String url = prepareUrl(busId, null, "Ericsson$RMC_Value", 5000);
        ApiDataObject data = getResponseFromHttp(url, key);
        if (data != null) {
            return data;
        } else {
            throw new AccessErrorException();
        }
    }
    public static ApiDataObject getTotalDistanceForBus(String busId) throws AccessErrorException {
        String url = prepareUrl(busId, null, "Ericsson$Total_Vehicle_Distance_Value", 10000);
        ApiDataObject data = getResponseFromHttp(url, key);
        if (data != null) {
            return data;
        } else {
            throw new AccessErrorException();
        }
    }

    /*
    Main call method
     */

    private static ApiDataObject getResponseFromHttp(String url, String key) throws AccessErrorException{
        try {
            HttpURLConnection connection = establishConnection(url, key);
            if (connectionWasSuccessful(connection)) {
                return getDataObjectFromStream(connection);
            } else {
                throw new AccessErrorException();
            }
        } catch (IOException e) {
            throw new AccessErrorException();
        }
    }

    /*
    Help methods
     */

    private static String prepareUrl(String busId, String sensorId, String resourceId, int time) {
        long t2 = System.currentTimeMillis();
        long t1 = t2 - time;
        if (sensorId != null) {
            return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&sensorSpec=" + sensorId + "&t1=" + t1 + "&t2=" + t2;
        } else {
            return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&resourceSpec=" + resourceId + "&t1=" + t1 + "&t2=" + t2;
        }
    }
    private static HttpURLConnection establishConnection(String url, String key) throws IOException {
        HttpURLConnection connection;
        URL requestURL = new URL(url);
        connection = (HttpsURLConnection)requestURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", key);
        return connection;
    }
    private static boolean connectionWasSuccessful(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        return responseCode == 200;
    }
    private static ApiDataObject getDataObjectFromStream(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        Reader reader = new BufferedReader(streamReader);
        Gson gson = new Gson();
        ApiDataObject[] data = gson.fromJson(reader, ApiDataObject[].class);
        return data[0];
    }
}
