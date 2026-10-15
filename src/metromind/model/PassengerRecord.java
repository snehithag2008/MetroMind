package metromind.model;

public class PassengerRecord {
    private final String recordId, ticketCode, source, destination;
    private final int passengerCount;
    public PassengerRecord(String recordId, String ticketCode, String source,
                           String destination, int passengerCount) {
        this.recordId = recordId; this.ticketCode = ticketCode; this.source = source;
        this.destination = destination; this.passengerCount = passengerCount;
    }
    public String getRecordId() { return recordId; }
    public String getTicketCode() { return ticketCode; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public int getPassengerCount() { return passengerCount; }
    @Override public String toString() {
        return recordId + " | " + ticketCode + " | " + source + " -> " + destination
                + " | passengers=" + passengerCount;
    }
}
