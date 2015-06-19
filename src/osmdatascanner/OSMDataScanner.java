package osmdatascanner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class OSMDataScanner {

    private static final String OVERPASS_API = "http://www.overpass-api.de/api/interpreter";
    private static final String OPENSTREETMAP_API_06 = "http://www.openstreetmap.org/api/0.6/";


    public static OSMNode getNode(String nodeId) throws IOException, ParserConfigurationException, SAXException {
            URL osm = new URL("http://www.openstreetmap.org/api/0.6/node/" + nodeId);
            HttpURLConnection connection = openNewURLConnection(osm);

            Document document = initDocumentBuilder().parse(connection.getInputStream());
            List<OSMNode> nodes = getNodes(document);
            if (!nodes.isEmpty()) {
                    return nodes.iterator().next();
            }
            return null;
    }

    private static HttpURLConnection openNewURLConnection(URL osm) throws IOException {
        return (HttpURLConnection) osm.openConnection();       
    }

    /* 
       range value accuracy:
       0.001  ~= 110m
       0.0001 ~= 11m
    */
    @SuppressWarnings("nls")
    private static Document getXML(double lat, double lon, double range) throws IOException, SAXException,
                    ParserConfigurationException {

            DecimalFormat format = new DecimalFormat("##0.0000000", DecimalFormatSymbols.getInstance(Locale.ENGLISH)); //$NON-NLS-1$
            String left = format.format(lon - range);
            String bottom = format.format(lat - range);
            String right = format.format(lon + range);
            String top = format.format(lat + range);

            String string = OPENSTREETMAP_API_06 + "map?bbox=" + left + "," + bottom + "," + right + ","
                            + top;
            URL osm = new URL(string);
            HttpURLConnection connection = openNewURLConnection(osm);

            return initDocumentBuilder().parse(connection.getInputStream());
    }

    private static DocumentBuilder initDocumentBuilder() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return docBuilder;
    }

    public static Document getXMLFile(String location) throws ParserConfigurationException, SAXException, IOException {
        return initDocumentBuilder().parse(location);
    }

    /**
     * 
     * @param xmlDocument 
     * @return a list of openseamap nodes extracted from xml
     */
    @SuppressWarnings("nls")
    public static List<OSMNode> getNodes(Document xmlDocument) {
        List<OSMNode> osmNodes = new ArrayList<>();

        // Document xml = getXML(8.32, 49.001);
        Node osmRoot = xmlDocument.getFirstChild();
        NodeList osmXMLNodes = osmRoot.getChildNodes();
        for (int i = 1; i < osmXMLNodes.getLength(); i++) {
            Node item = osmXMLNodes.item(i);
            if (item.getNodeName().equals("node")) {
                NamedNodeMap attributes = item.getAttributes();
                NodeList tagXMLNodes = item.getChildNodes();
                
                Map<String, String> tags = getTags(tagXMLNodes);
                
                Node namedItemID = attributes.getNamedItem("id");
                Node namedItemLat = attributes.getNamedItem("lat");
                Node namedItemLon = attributes.getNamedItem("lon");
                Node namedItemVersion = attributes.getNamedItem("version");

                String id = namedItemID.getNodeValue();
                String latitude = namedItemLat.getNodeValue();
                String longitude = namedItemLon.getNodeValue();
                String version = "0";
                if (namedItemVersion != null) {
                    version = namedItemVersion.getNodeValue();
                }

                    osmNodes.add(new OSMNode(id, latitude, longitude, version, tags));
            }

        }
        return osmNodes;
    }

    private static Map<String, String> getTags(NodeList tagXMLNodes) throws DOMException {
        Map<String, String> tags = new HashMap<>();
        for (int j = 1; j < tagXMLNodes.getLength(); j++) {
            Node tagItem = tagXMLNodes.item(j);
            NamedNodeMap tagAttributes = tagItem.getAttributes();
            if (tagAttributes != null) {
                tags.put(tagAttributes.getNamedItem("k").getNodeValue(), tagAttributes.getNamedItem("v")
                        .getNodeValue());
            }
        }
        return tags;
    }
    
    /**
     * 
     * @param lat
     * @param lon
     * @param range 0.001  ~= 110m, 0.0001 ~= 11m
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException 
     */
    public static List<OSMNode> getOSMNodesInRange(double lat, double lon, double range) throws IOException,
            SAXException, ParserConfigurationException {
        return OSMDataScanner.getNodes(getXML(lat, lon, range));
    }

    /**
     * 
     * @param query the overpass query
     * @return the nodes in the formulated query
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document getNodesViaOverpass(String query) throws IOException, ParserConfigurationException, SAXException {
        String hostname = OVERPASS_API;
        String queryString = readFileAsString(query);

        URL osm = new URL(hostname);
        HttpURLConnection connection = openNewURLConnection(osm);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        return initDocumentBuilder().parse(connection.getInputStream());
    }

    /**
     * 
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    private static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }	
}
