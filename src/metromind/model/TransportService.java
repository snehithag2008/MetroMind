package metromind.model;

public class TransportService {
    private final String id, routeId, vehicleId, status, alert;
    private final String ticketCode;
    public TransportService(String id, String routeId, String vehicleId, String status,
                            String alert, String ticketCode) {
        this.id = id; this.routeId = routeId; this.vehicleId = vehicleId;
        this.status = status; this.alert = alert; this.ticketCode = ticketCode;
    }
    public String getId() { return id; }
    public String getRouteId() { return routeId; }
    public String getVehicleId() { return vehicleId; }
    public String getStatus() { return status; }
    public String getAlert() { return alert; }
    public String getTicketCode() { return ticketCode; }
    @Override public String toString() {
        return id + " | Route " + routeId + " | Vehicle " + vehicleId + " | " + status
                + " | Ticket/code " + ticketCode + " | Alert: " + alert;
    }
}
