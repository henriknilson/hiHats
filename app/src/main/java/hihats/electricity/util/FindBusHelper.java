package hihats.electricity.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.HttpHandler;

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

    public String askNetworkForId() throws AccessErrorException {
        String response = httpHandler.getResponse("http://feeds.feedburner.com/entrepreneur/startingabusiness.rss");
        String result = parseFromXML(response);
        if (result != null) {
            return result;
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
    }
}