package osmdatascanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeClassifier {

    private NodeClassifier() {
    }

    public static Map<String, Integer> getValuesForTag(List<OSMNode> nodes, String tagKey) {
        Map<String, Integer> tags = new HashMap<>();
        for (OSMNode node : nodes) {
            String key = node.getTagValue(tagKey);
            if (key != null) {
                if (tags.containsKey(key)) {
                    tags.put(key, tags.get(key) + 1);
                } else {
                    tags.put(key, 1);
                }
            }
        }
        return tags;
    }

}
