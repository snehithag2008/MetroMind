package metromind.model;

import java.util.List;
import java.util.ArrayList;

public class Route {
    private final String id, name, description;
    private final List<String> stationIds;

    public Route(String id, String name, String description, List<String> stationIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stationIds = new ArrayList<>(stationIds);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getStationIds() { return new ArrayList<>(stationIds); }

    @Override public String toString() {
        return id + " | " + name + " | " + description + " | Stops: " + stationIds;
    }
}
