package hihats.electricity;

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
 * Created by frellAn on 2015-09-22.
 */
public class ApiHttpHandler {

    /*
    Get methods
     */

    /**
     * Returns the last known location for a certain bus.
     * @param busId The bus you want to check location for.
     * @return The most recent location for said bus as an Android Location object.
     */
    public static String getMostRecentLocationForBus(String busId) {
        String url = prepareUrl(busId, null, "Ericsson$RMC_Value", 5000);
        String key = "Basic Z3JwNDU6RlozRWN1TFljag==";
        ApiDataObject fromHttp = getResponseFromHttp(url, key);
        if (fromHttp.getResourceSpec().equals("Ericsson$RMC_Value")) {
            String value = fromHttp.getValue();
        }
        return null;
    }

    /*
    Main call method
     */

    private static ApiDataObject getResponseFromHttp(String url, String key) {
        try {
            HttpURLConnection connection = establishConnection(url, key);
            if (connectionWasSuccessful(connection)) {
                return getDataObjectFromStream(connection);
            } else {
                System.out.print("Connection error!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
    private static HttpURLConnection establishConnection(String url, String key) throws IOException{
        HttpURLConnection connection;
        URL requestURL = new URL(url);
        connection = (HttpsURLConnection)requestURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", key);
        return connection;
    }
    private static boolean connectionWasSuccessful(HttpURLConnection connection) throws IOException{
        int responseCode = connection.getResponseCode();
        return responseCode == 200;
    }
    private static ApiDataObject getDataObjectFromStream(HttpURLConnection connection) throws IOException{
        InputStream stream = connection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        Reader reader = new BufferedReader(streamReader);
        Gson gson = new Gson();
        ApiDataObject[] data = gson.fromJson(reader, ApiDataObject[].class);
        return data[0];
    }
}
