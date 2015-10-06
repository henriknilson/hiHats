package hihats.electricity.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import hihats.electricity.model.Bus;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.HttpHandler;
import hihats.electricity.net.NoDataException;

/**
 * Created by fredrikkindstrom on 03/10/15.
 */
public class FindBusHelper {

    private final HttpHandler httpHandler = new HttpHandler();

    /*
    Util methods
     */

    public boolean isConnectedToWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetwork != null && wifiNetwork.isConnected();
    }

    public Bus getBusFromSystemId() throws AccessErrorException, NoDataException {
        String response = httpHandler.getResponse("http://feeds.feedburner.com/entrepreneur/startingabusiness.rss");
        String result = parseFromXML(response);
        if (result != null) {
            try {
                return new Bus(result);
            } catch (IllegalArgumentException e) {
                throw new NoDataException();
            }
        } else {
            throw new AccessErrorException();
        }
    }

    /*
    Help methods
     */

    private String parseFromXML(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        final String GROUP = "channel";
        final String VALUE = "title";
        String result = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName(GROUP);
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    result = eElement.getElementsByTagName(VALUE).item(0).getTextContent();
                }
            }
            return result;
        } else {
            return null;
        }
    }
}