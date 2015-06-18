package osmdatascanner;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import static osmdatascanner.OSMDataScanner.getOSMNodesInVicinity;

public class ScannerExamples {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        // okolice Lewiatana na Miasteczku Studenckim
        printTags(19.907, 50.0688, 0.0003);

        // okolice B1
        printTags(19.919, 50.06606, 0.0005);

        // okolice A0
        printTags(19.9232, 50.0644, 0.0004);

    }

    private static void printTags(double lat, double lon, double range)
            throws IOException, SAXException, ParserConfigurationException {

        List<OSMNode> osmNodesInVicinity = getOSMNodesInVicinity(lat, lon, range);
        for (OSMNode osmNode : osmNodesInVicinity) {
            if (osmNode.hasTags()) {
                System.out.println(osmNode);
            }
        }
    }

}
