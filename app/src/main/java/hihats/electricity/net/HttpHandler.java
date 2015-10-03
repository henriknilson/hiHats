package hihats.electricity.net;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

/**
 * This is the backend class for all api http calls.
 * It returns ApiDataObjects that the util classes then parses to more useful formats.
 */
public class HttpHandler {

    private static String KEY = "Basic Z3JwNDU6RlozRWN1TFljag==";

    /*
    Main call method
     */

    /**
     * Returns an ApiDataObject with data if the request was successful, null if not.
     * @param url The url query to access the api with.
     * @return An ApiDataObject with data when successful, null if not successful.
     * @throws AccessErrorException If there was an error connection to the server.
     */
    public ArrayList<ApiDataObject> getJSONResponse(String url) throws AccessErrorException, NoDataException {
        try {
            HttpURLConnection connection = establishConnection(url, KEY);
            if (connectionWasSuccessful(connection)) {
                return getJSONObjectFromStream(connection);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new AccessErrorException();
        }
    }

    /**
     * Returns an XML String with data if the request was successful, null if not.
     * @param url The url query to access the api with.
     * @return An XML String with data when successful, null if not successful.
     * @throws AccessErrorException If there was an error connection to the server.
     */
    public String getXMLResponse(String url) throws AccessErrorException, NoDataException {
        try {
            HttpURLConnection connection = establishConnection(url, null);
            if (connectionWasSuccessful(connection)) {
                return getXMLObjectFromStream(connection);
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
        connection = (HttpsURLConnection)requestURL.openConnection();
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
    private ArrayList<ApiDataObject> getJSONObjectFromStream(HttpURLConnection connection) throws IOException, NoDataException {
        InputStream stream = connection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        Reader reader = new BufferedReader(streamReader);
        Gson gson = new Gson();
        ApiDataObject[] fromStream = gson.fromJson(reader, ApiDataObject[].class);
        ArrayList<ApiDataObject> dataObjects = new ArrayList<>();
        try {
            Collections.addAll(dataObjects, fromStream);
        } catch (NullPointerException e) {
            throw new NoDataException();
        }
        return dataObjects;
    }
    private String getXMLObjectFromStream(HttpURLConnection connection) throws IOException, NoDataException {
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