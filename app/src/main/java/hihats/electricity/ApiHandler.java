package hihats.electricity;

import android.location.Location;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by frellAn on 2015-09-22.
 */
public class ApiHandler {

    /*
    Get methods
     */

    /**
     * Returns the last known location for a certain bus.
     * @param busId The bus you want to check location for.
     * @param time How far back to look in the data to find the most recent one, in seconds.
     * @return The most recent location for said bus as an Android Location object.
     */
    public Location getCurrentLocationForBus(String busId, int time) {
        StringBuffer response = new StringBuffer();
        String url = prepareUrl(busId, null, "Ericsson$RMC_Value", time);
        String key = prepareKey();
        try {
            HttpURLConnection connection = establishConnection(url, key);
            if (checkIfRequestWasSuccessful(connection)) {
                response = readStreamFromConnection(connection);
            } else {
                System.out.print("Fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.toString());
    }

    /*
    Work Methods
     */

    private String prepareUrl(String busId, String sensorId, String resourceId, int time) {
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (time * 1000);
        if (sensorId != null) {
            return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&sensorSpec=" + sensorId + "&t1=" + t1 + "&t2=" + t2;
        } else {
            return  "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&resourceSpec=" + resourceId + "&t1=" + t1 + "&t2=" + t2;
        }
    }
    private String prepareKey() {
        String userNamePass = "grp45:FZ3EcuLYcj";
        String base64Encoded = Base64.encodeToString(userNamePass.getBytes(),Base64.NO_WRAP);
        return "Basic " + base64Encoded;
    }
    private HttpURLConnection establishConnection(String url, String key) throws IOException{
        HttpURLConnection connection;
        URL requestURL = new URL(url);
        connection = (HttpsURLConnection)requestURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", key);
        return connection;
    }
    private boolean checkIfRequestWasSuccessful(HttpURLConnection connection) throws IOException{
        int responseCode = connection.getResponseCode();
        return responseCode == 200;
    }
    private StringBuffer readStreamFromConnection(HttpURLConnection connection) throws IOException{
        StringBuffer response = new StringBuffer();
        InputStream stream = connection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();
        return response;
    }
}
