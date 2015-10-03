package hihats.electricity.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.HttpHandler;
import hihats.electricity.net.NoDataException;

/**
 * Created by fredrikkindstrom on 03/10/15.
 */
public class FindBusHelper {

    // Initialize the HttpHandler
    private final HttpHandler httpHandler = new HttpHandler();

    public boolean isConnectedToWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetwork != null && wifiNetwork.isConnected();
    }

    public String askNetworkForId() throws AccessErrorException, NoDataException {
        String response;
        response = httpHandler.getXMLResponse("ombord.info/api/xml/system/");
        return response;
    }
}