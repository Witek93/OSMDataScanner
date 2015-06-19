package osmdatascanner;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import static osmdatascanner.OSMDataScanner.getOSMNodesInRange;

public class ScannerExamples {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

//        // okolice Lewiatana na Miasteczku Studenckim
//        printTags(50.068847, 19.907652, 0.0003);
//
//        // okolice B1
//        printTags(50.065976 , 19.919423, 0.0003);
//
//        // okolice A0
//        printTags(50.064681 , 19.923256, 0.0003);
//        
//        // Wawel
//        printTags(50.054594 , 19.936649, 0.001);
        
//        example_wawel_1km_range();
        
        example_walk_Cracow("amenity");

    }

    /**
     * Przykład, który przeszukuje dosyć duży obszar (1km kw.) z placem zamkowym
     * Wawelu jako punktem centralnym
     */
    private static void example_wawel_1km_range() throws IOException, ParserConfigurationException, SAXException {
        List<OSMNode> osmNodesInRange = getOSMNodesInRange(50.054594, 19.936649, 0.009);
        Map<String, Integer> values = NodeClassifier.getValuesForTag(osmNodesInRange, "historic");

        System.out.println("Historic values: ");
        for (Map.Entry<String, Integer> e : values.entrySet()) {
            System.out.println("\t" + e.getKey() + ": " + e.getValue());
        }
    }

    private static void example_walk_Cracow(String tag) throws IOException, ParserConfigurationException, SAXException {
        double range = 0.001;
        printTags(50.061719, 19.938798, range, tag); // Bazylika Mariacka
        printTags(50.061512, 19.937985, range, tag); // pomnik Mickiewicza
        printTags(50.060808, 19.937849, range, tag); // kościół Św. Wojciecha
        printTags(50.059099, 19.937892, range, tag); // Plac Dominikański/Grodzka
        printTags(50.054041, 19.938162, range, tag); // Plac Bernardyński
        printTags(50.052780, 19.935271, range, tag); // wejście na Wawel
        printTags(50.054351, 19.935469, range, tag); // Wawel: przy zamku
        printTags(50.054488, 19.936639, range, tag); // Wawel: plac zamkowy
    }

    private static void printTags(double lat, double lon, double range, String tag) 
            throws IOException, ParserConfigurationException, SAXException  {
        Map<String, Integer> values = NodeClassifier.getValuesForTag(
                getOSMNodesInRange(lat, lon, range), tag);
        for (Map.Entry<String, Integer> e : values.entrySet()) {
            System.out.println("\t" + e.getKey() + ": " + e.getValue());
        }
    }

    /* 
     range value accuracy:
     0.001  ~= 110m
     0.0001 ~= 11m
     */
    private static void printTags(double lat, double lon, double range)
            throws IOException, SAXException, ParserConfigurationException {

        List<OSMNode> osmNodesInRange = getOSMNodesInRange(lat, lon, range);
        for (OSMNode osmNode : osmNodesInRange) {
            if (osmNode.hasTags()) {
                System.out.println(osmNode);
            }
        }
    }

}
