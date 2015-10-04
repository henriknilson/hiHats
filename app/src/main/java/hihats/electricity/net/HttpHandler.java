package hihats.electricity.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This is the backend class for all api http calls.
 * It returns a String with data that the util classes then parses to more useful formats.
 */
public class HttpHandler {

    private static String KEY = "Basic Z3JwNDU6RlozRWN1TFljag==";

    /*
    Main call method
     */

    /**
     * Returns a String with data if the request was successful, null if not.
     * This can be XML or JSON so parsing is needed later.
     * @param url The url query to access the api with.
     * @return A raw String with data when successful, null if not successful.
     * @throws AccessErrorException If there was an error connection to the server.
     */
    public String getResponse(String url) throws AccessErrorException {
        try {
            HttpURLConnection connection = establishConnection(url, KEY);
            if (connectionWasSuccessful(connection)) {
                return getResponseFromStream(connection);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new AccessErrorException();
        }
    }

    /*
    Help methods
     */

    private HttpURLConnection establishConnection(String url, String key) throws IOException {
        HttpURLConnection connection;
        URL requestURL = new URL(url);
        connection = (HttpURLConnection)requestURL.openConnection();
        connection.setRequestMethod("GET");
        if (key != null) {
            connection.setRequestProperty("Authorization", key);
        }
        return connection;
    }
    private boolean connectionWasSuccessful(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        return responseCode == 200;
    }
    private String getResponseFromStream(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        InputStream stream = connection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return response.toString();
    }
}