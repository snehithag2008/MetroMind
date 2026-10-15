package metromind.data;

import java.util.*;
import metromind.model.*;

public final class MetroData {
    private MetroData() {}

    public static List<Station> stations() {
        return Arrays.asList(
            new Station("ST101", "Vijayawada Central", "Central", "ACTIVE", Arrays.asList("R101","R102")),
            new Station("ST102", "Railway Station", "Railway Area", "ACTIVE", Arrays.asList("R101","R103")),
            new Station("ST103", "Benz Circle", "Benz Circle", "ACTIVE", Arrays.asList("R102","R104")),
            new Station("ST104", "Bus Stand", "Bus Stand Area", "ACTIVE", Arrays.asList("R103","R104")),
            new Station("ST105", "Airport Terminal", "Gannavaram", "ACTIVE", Arrays.asList("R105")),
            new Station("ST106", "Governorpet", "Governorpet", "ACTIVE", Arrays.asList("R102"))
        );
    }

    public static List<Route> routes() {
        return Arrays.asList(
            new Route("R101", "Central-Railway", "Vijayawada Central connects to Railway Station", Arrays.asList("ST101","ST102")),
            new Route("R102", "Central-Benz-Governorpet", "Vijayawada Central connects to Benz Circle and Governorpet", Arrays.asList("ST101","ST103","ST106")),
            new Route("R103", "Railway-Bus", "Railway Station connects to Bus Stand", Arrays.asList("ST102","ST104")),
            new Route("R104", "Benz-Bus", "Benz Circle connects to Bus Stand", Arrays.asList("ST103","ST104")),
            new Route("R105", "Airport Link", "Bus Stand connects to Airport Terminal", Arrays.asList("ST104","ST105"))
        );
    }

    public static List<TransportService> services() {
        return Arrays.asList(
            new TransportService("SV201","R101","BUS-11","DELAYED","Service delay due to traffic near Central Station","MTR1001"),
            new TransportService("SV202","R102","BUS-18","ACTIVE","No major delay reported","MTR1002"),
            new TransportService("SV203","R103","BUS-23","MAINTENANCE","Maintenance work at Railway Station","MTR1003"),
            new TransportService("SV204","R104","BUS-07","CANCELLED","Service cancelled due to road accident","MTR1004"),
            new TransportService("SV205","R105","BUS-31","ACTIVE","Airport service operating normally","MTR1005")
        );
    }

    public static List<PassengerRecord> passengerRecords() {
        return Arrays.asList(
            new PassengerRecord("PR001","MTR1001","Vijayawada Central","Railway Station",42),
            new PassengerRecord("PR002","MTR1002","Vijayawada Central","Benz Circle",35),
            new PassengerRecord("PR003","MTR1003","Railway Station","Bus Stand",28),
            new PassengerRecord("PR004","MTR1004","Benz Circle","Bus Stand",19),
            new PassengerRecord("PR005","MTR1005","Bus Stand","Airport Terminal",24),
            new PassengerRecord("PR006","MTR1006","Governorpet","Benz Circle",31)
        );
    }

    public static List<TransportDocument> documents() {
        return Arrays.asList(
            new TransportDocument("DOC101","Central Station Route","ROUTE",
                "Vijayawada Central connects to Railway Station. Passengers can change at Central Station."),
            new TransportDocument("DOC102","Railway Service Notice","SERVICE",
                "Railway Station connects to Bus Stand. Maintenance work is scheduled near Railway Station."),
            new TransportDocument("DOC103","Traffic Alert","ALERT",
                "Service delay reported near Central Station due to traffic congestion."),
            new TransportDocument("DOC104","Airport Route Guide","ROUTE",
                "Bus Stand connects to Airport Terminal. Airport service is operating normally.")
        );
    }

    public static int[][] sampleDistanceMatrix() {
        // Approximate illustrative travel costs between four destinations:
        // Central, Railway, Benz Circle, Bus Stand.
        return new int[][] {
            {0, 4, 3, 7},
            {4, 0, 6, 2},
            {3, 6, 0, 4},
            {7, 2, 4, 0}
        };
    }
}
