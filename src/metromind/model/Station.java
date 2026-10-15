package metromind.model;

import java.util.List;
import java.util.ArrayList;

public class Station {
    private final String id, name, area, status;
    private final List<String> routeIds;

    public Station(String id, String name, String area, String status, List<String> routeIds) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.status = status;
        this.routeIds = new ArrayList<>(routeIds);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getArea() { return area; }
    public String getStatus() { return status; }
    public List<String> getRouteIds() { return new ArrayList<>(routeIds); }

    @Override public String toString() {
        return id + " | " + name + " | Area: " + area + " | Routes: " + routeIds + " | " + status;
    }
}
