
package osmdatascanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OSMNode {
    private String id;
    private String lat;
    private String lon;
    private final Map<String, String> tags;
    private String version;

    
    public OSMNode() {
        this.tags = new HashMap();
    }

    public OSMNode(String id, String lat, String lon, String version, Map<String, String> tags) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.tags = tags;
        this.version = version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        for(Map.Entry<String, String> e: tags.entrySet()) {
            sb.append('\t')
              .append(e.getKey())
              .append(": ")
              .append(e.getValue())
              .append('\n');
        }
        return this.id + " " + this.lat + " " + this.lon + sb.toString();
                
    }
    
    public boolean hasTags() {
        return !tags.isEmpty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTagValue(String key) {
        return tags.get(key);
    }
      
}