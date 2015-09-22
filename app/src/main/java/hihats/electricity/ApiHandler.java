package hihats.electricity;

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

    static StringBuffer response = new StringBuffer();
    static HttpURLConnection connection;

    private String prepareUrl() {
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 1);
        String url = "https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Next_Stop&t1=" + t1 + "&t2=" + t2;
        return url;
    }

    private String prepareKey() {
        String userNamePass = "grp0:password";
        String base64Encoded = Base64.encodeToString(userNamePass.getBytes(), Base64.NO_WRAP);
        return "Basic " + base64Encoded;
    }

    private void establishConnection(String url, String key) throws IOException{
        URL requestURL = new URL(url);
        connection = (HttpsURLConnection)requestURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", key);
    }

    private void checkRequest() throws IOException{
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);
    }

    private void readStreamFromConnection(HttpURLConnection connection) throws IOException{
        InputStream stream = connection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();
    }
}
