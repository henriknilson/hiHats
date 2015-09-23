package hihats.electricity;

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
    public static String getCurrentLocationForBus(String busId, int time) {
        String response = "";
        String url = prepareUrl(busId, null, "Ericsson$RMC_Value", time);
        String key = prepareKey();
        try {
            HttpURLConnection connection = establishConnection(url, key);
            if (conectionWasSuccessful(connection)) {
                response = readStreamFromConnection(connection);
            } else {
                System.out.print("Fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*
    Work Methods
     */

    private static String prepareUrl(String busId, String sensorId, String resourceId, int time) {
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (time * 1000);
        if (sensorId != null) {
            return "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&sensorSpec=" + sensorId + "&t1=" + t1 + "&t2=" + t2;
        } else {
            return  "https://ece01.ericsson.net:4443/ecity?dgw=" + busId + "&resourceSpec=" + resourceId + "&t1=" + t1 + "&t2=" + t2;
        }
    }
    private static String prepareKey() {
        // TODO use Base64 algorithm in Android
        /*
        String userNamePass = "grp45:FZ3EcuLYcj";
        String base64Encoded = Base64.encodeToString(userNamePass.getBytes(),Base64.NO_WRAP);
        return "Basic " + base64Encoded;
        */
        return "Basic Z3JwNDU6RlozRWN1TFljag==";
    }
    private static HttpURLConnection establishConnection(String url, String key) throws IOException{
        HttpURLConnection connection;
        URL requestURL = new URL(url);
        connection = (HttpsURLConnection)requestURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", key);
        return connection;
    }
    private static boolean conectionWasSuccessful(HttpURLConnection connection) throws IOException{
        int responseCode = connection.getResponseCode();
        return responseCode == 200;
    }
    private static String readStreamFromConnection(HttpURLConnection connection) throws IOException{
        InputStream stream = connection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        stream.close();
        return result;
    }
}
