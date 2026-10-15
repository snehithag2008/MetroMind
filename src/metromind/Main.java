package metromind;

import java.util.*;
import metromind.data.MetroData;
import metromind.model.*;
import metromind.m1.StringAlgorithms;
import metromind.m2.SuffixStructures;
import metromind.m3.DynamicProgramming;
import metromind.m4.FlowAlgorithms;
import metromind.m5.ApproximationAlgorithms;
import metromind.m6.ScaleAlgorithms;

public class Main {
    private static final Scanner IN = new Scanner(System.in);
    private static final List<Station> STATIONS = MetroData.stations();
    private static final List<Route> ROUTES = MetroData.routes();
    private static final List<TransportService> SERVICES = MetroData.services();
    private static final List<PassengerRecord> PASSENGERS = MetroData.passengerRecords();
    private static final List<TransportDocument> DOCUMENTS = MetroData.documents();

    public static void main(String[] args) {
        System.out.println("MetroMind uses illustrative sample data, not live transit feeds.");
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1: showNetworkData(); break;
                    case 2: searchStations(); break;
                    case 3: searchServiceAlerts(); break;
                    case 4: searchTicketCode(); break;
                    case 5: inspectRoutePattern(); break;
                    case 6: searchDocuments(); break;
                    case 7: compareRouteDescriptions(); break;
                    case 8: correctStationName(); break;
                    case 9: optimizeDestinationVisit(); break;
                    case 10: matrixChainDemo(); break;
                    case 11: optimalBstDemo(); break;
                    case 12: passengerFlowDemo(); break;
                    case 13: vehicleAssignmentDemo(); break;
                    case 14: schedulingAndConflictDemo(); break;
                    case 15: passengerAnalyticsDemo(); break;
                    case 0: System.out.println("MetroMind session ended."); return;
                    default: System.out.println("Please choose a listed option.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Input/problem: " + ex.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n================ METROMIND ================");
        System.out.println("Stations: " + STATIONS.size() + " | Routes: " + ROUTES.size()
                + " | Services: " + SERVICES.size() + " | Documents: " + DOCUMENTS.size());
        System.out.println(" String algorithms");
        System.out.println(" 1. Show sample transport records");
        System.out.println(" 2. Search station name (KMP)");
        System.out.println(" 3. Detect multiple service alerts (Aho-Corasick)");
        System.out.println(" 4. Search ticket/service code (Rabin-Karp)");
        System.out.println(" 5. Inspect route description pattern (Z-function)");
        System.out.println("Suffix structures");
        System.out.println(" 6. Search transport documents (suffix array)");
        System.out.println(" 7. Compare route descriptions (common prefix)");
        System.out.println("Dynamic programming");
        System.out.println(" 8. Correct misspelled station name");
        System.out.println(" 9. Find low-cost visit order for four destinations");
        System.out.println("10. Matrix-chain operation cost demo");
        System.out.println("11. Frequently accessed station index (Optimal BST)");
        System.out.println("Network flow");
        System.out.println("12. Passenger-capacity maximum flow and min-cut");
        System.out.println("13. Assign vehicles to services (bipartite matching)");
        System.out.println("NP-completeness/approximation");
        System.out.println("14. Scheduling constraint and conflict-cover demo");
        System.out.println("Randomized/parallel algorithms");
        System.out.println("15. Passenger analytics demo");
        System.out.println(" 0. Exit");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = IN.nextLine().trim();
            try { return Integer.parseInt(line); }
            catch (NumberFormatException e) { System.out.println("Enter a whole number."); }
        }
    }
    private static String readText(String prompt) {
        System.out.print(prompt);
        return IN.nextLine().trim();
    }

    private static void showNetworkData() {
        System.out.println("\n-- STATIONS --"); STATIONS.forEach(System.out::println);
        System.out.println("\n-- ROUTES --"); ROUTES.forEach(System.out::println);
        System.out.println("\n-- SERVICES --"); SERVICES.forEach(System.out::println);
        System.out.println("\n-- PASSENGER AGGREGATES (sample) --"); PASSENGERS.forEach(System.out::println);
    }

    private static void searchStations() {
        String query = readText("Enter a station name or part of a name: ").toLowerCase(Locale.ROOT);
        boolean found = false;
        for (Station s : STATIONS) {
            int position = StringAlgorithms.kmpIndexOf(s.getName().toLowerCase(Locale.ROOT), query);
            if (position >= 0) { System.out.println("MATCH at character " + position + ": " + s); found = true; }
        }
        if (!found) System.out.println("No station matched '" + query + "'. Try option 8 for spelling suggestions.");
        System.out.println("Algorithm: KMP; preprocessing LPS prevents rechecking matched characters.");
    }

    private static void searchServiceAlerts() {
        String message = readText("Enter an alert/message to analyse: ");
        List<String> keywords = Arrays.asList("delay","accident","maintenance","cancelled","traffic","normal");
        List<String> found = StringAlgorithms.ahoCorasick(message, keywords);
        System.out.println("Alert text: " + message);
        System.out.println("Detected known alert terms: " + (found.isEmpty() ? "none" : found));
        System.out.println("Algorithm: Aho-Corasick searches a set of keywords in one pass.");
    }

    private static void searchTicketCode() {
        String query = readText("Enter ticket/service code or code fragment: ");
        boolean found = false;
        for (TransportService s : SERVICES) {
            int pos = StringAlgorithms.rabinKarpIndexOf(s.getTicketCode(), query);
            if (pos >= 0) { System.out.println("Code match at position " + pos + ": " + s); found = true; }
        }
        for (PassengerRecord p : PASSENGERS) {
            int pos = StringAlgorithms.rabinKarpIndexOf(p.getTicketCode(), query);
            if (pos >= 0) { System.out.println("Passenger travel record match at position " + pos + ": " + p); found = true; }
        }
        if (!found) System.out.println("No ticket/service code contains '" + query + "'.");
        System.out.println("Algorithm: Rabin-Karp uses rolling hashes, then verifies candidate matches.");
    }

    private static void inspectRoutePattern() {
        System.out.println("Available route descriptions:");
        for (Route r : ROUTES) System.out.println(r.getId() + ": " + r.getDescription());
        String routeId = readText("Enter route ID (for example R101): ").toUpperCase(Locale.ROOT);
        Route selected = ROUTES.stream().filter(r -> r.getId().equals(routeId)).findFirst().orElse(null);
        if (selected == null) { System.out.println("Unknown route ID."); return; }
        String pattern = readText("Enter a prefix/pattern to check in this description: ");
        String text = selected.getDescription();
        String joined = pattern + "\u0001" + text;
        int[] z = StringAlgorithms.zFunction(joined);
        boolean found = false;
        for (int value : z) if (value >= pattern.length() && !pattern.isEmpty()) { found = true; break; }
        System.out.println(found ? "Pattern occurs in the route description." : "Pattern not found.");
        System.out.println("Z-array for pattern + separator + description: " + Arrays.toString(z));
    }

    private static void searchDocuments() {
        String query = readText("Enter phrase/keyword to find in transport documents: ");
        boolean found = false;
        for (TransportDocument d : DOCUMENTS) {
            String content = d.getTitle() + ". " + d.getContent();
            List<Integer> positions = SuffixStructures.findWithSuffixArray(content.toLowerCase(Locale.ROOT), query.toLowerCase(Locale.ROOT));
            if (!positions.isEmpty()) {
                System.out.println("\n" + d);
                System.out.println("Matching character position(s) in title+content: " + positions);
                found = true;
            }
        }
        if (!found) System.out.println("No document contained that phrase.");
        System.out.println("Algorithm: suffix array indexes suffixes; this demo checks matches against the index.");
    }

    private static void compareRouteDescriptions() {
        String aId = readText("First route ID (e.g. R101): ").toUpperCase(Locale.ROOT);
        String bId = readText("Second route ID (e.g. R102): ").toUpperCase(Locale.ROOT);
        Route a = ROUTES.stream().filter(r -> r.getId().equals(aId)).findFirst().orElse(null);
        Route b = ROUTES.stream().filter(r -> r.getId().equals(bId)).findFirst().orElse(null);
        if (a == null || b == null) { System.out.println("One or both route IDs are unknown."); return; }
        int len = SuffixStructures.commonPrefixLength(a.getDescription(), b.getDescription());
        System.out.println("Route A: " + a.getDescription());
        System.out.println("Route B: " + b.getDescription());
        System.out.println("Common prefix length: " + len);
        System.out.println("Common prefix: " + a.getDescription().substring(0, len));
        int[] sa = SuffixStructures.suffixArray(a.getDescription());
        int[] lcp = SuffixStructures.kasaiLcp(a.getDescription(), sa);
        System.out.println("Suffix-array size for first description: " + sa.length + "; Kasai LCP array: " + Arrays.toString(lcp));
    }


private static void correctStationName() {
    String input = readText(
        "Enter station name (try a spelling mistake): "
    ).trim().toLowerCase(Locale.ROOT);

    if (input.isEmpty()) {
        System.out.println("Please enter a station name.");
        return;
    }

    Station best = null;
    int bestDistance = Integer.MAX_VALUE;
    int bestDamerau = Integer.MAX_VALUE;
    String bestMatchedText = "";

    for (Station station : STATIONS) {
        String fullName = station.getName()
                                  .trim()
                                  .toLowerCase(Locale.ROOT);

        // Compare with the full station name.
        int distance = DynamicProgramming.levenshtein(
            input, fullName
        );

        int damerau = DynamicProgramming.damerauLevenshtein(
            input, fullName
        );

        String matchedText = fullName;

        // Also compare individual words, e.g. "Vijayawada"
        // inside "Vijayawada Central".
        String[] words = fullName.split("\\s+");

        for (String word : words) {
            int wordDistance = DynamicProgramming.levenshtein(
                input, word
            );

            if (wordDistance < distance) {
                distance = wordDistance;
                damerau = DynamicProgramming.damerauLevenshtein(
                    input, word
                );
                matchedText = word;
            }
        }

        if (distance < bestDistance) {
            bestDistance = distance;
            bestDamerau = damerau;
            best = station;
            bestMatchedText = matchedText;
        }
    }

    if (best == null) {
        System.out.println("No stations loaded.");
        return;
    }

    System.out.println(
        "Closest station suggestion: "
        + best.getName() + " (" + best.getId() + ")"
    );
    System.out.println("Matched station word: " + bestMatchedText);
    System.out.println("Levenshtein distance: " + bestDistance);
    System.out.println("Damerau-Levenshtein distance: " + bestDamerau);
    System.out.println(
        "Smaller edit distances generally indicate closer spelling."
    );
}


    private static void optimizeDestinationVisit() {
        String[] names = {"Central", "Railway", "Benz Circle", "Bus Stand"};
        int[][] costs = MetroData.sampleDistanceMatrix();
        System.out.println("Illustrative travel-cost matrix for destinations " + Arrays.toString(names));
        for (int[] row : costs) System.out.println(Arrays.toString(row));
        System.out.println("Start destination: Central");
        System.out.println("Minimum cost to visit all four once (not returning to start): "
                + DynamicProgramming.bitmaskTourCost(costs, 0));
        System.out.println("Bitmask DP is exponential, so this example intentionally uses only four destinations.");
    }

    private static void matrixChainDemo() {
        System.out.println("Example data-operation matrix dimensions: [10, 20, 30, 40].");
        System.out.println("Represents A(10x20), B(20x30), C(30x40).");
        System.out.println("Minimum scalar multiplication count: "
                + DynamicProgramming.matrixChainMinimumCost(new int[]{10,20,30,40}));
        System.out.println("This illustrates operation-order optimisation; these dimensions are a teaching example, not measured transit data.");
    }

    private static void optimalBstDemo() {
        int[] stationKeys = {101,102,103,104,105};
        int[] searchFrequency = {50,35,20,10,5};
        System.out.println("Station keys (sorted): " + Arrays.toString(stationKeys));
        System.out.println("Sample search frequencies: " + Arrays.toString(searchFrequency));
        System.out.println("Minimum weighted search cost: "
                + DynamicProgramming.optimalBstCost(stationKeys, searchFrequency));
        System.out.println("Higher-frequency station IDs are given more importance in the expected search cost.");
    }

    private static void passengerFlowDemo() {
        // Nodes: source=0, Central=1, Railway=2, Benz=3, BusStand=4, sink=5.
        FlowAlgorithms.Dinic flow = new FlowAlgorithms.Dinic(6);
        flow.addEdge(0,1,80); flow.addEdge(1,2,35); flow.addEdge(1,3,30);
        flow.addEdge(2,4,25); flow.addEdge(3,4,20); flow.addEdge(4,5,40);
        int max = flow.maxFlow(0,5);
        System.out.println("Illustrative capacity network: source -> Central -> (Railway/Benz) -> Bus Stand -> destination.");
        System.out.println("Maximum flow (capacity units): " + max);
        boolean[] side = flow.residualReachableFrom(0);
        List<Integer> reachable = new ArrayList<>();
        for (int i=0;i<side.length;i++) if(side[i]) reachable.add(i);
        System.out.println("Source-side vertices after max-flow (min-cut partition): " + reachable);
        System.out.println("Capacities are fictional teaching values, not actual passenger counts.");
    }

    private static void vehicleAssignmentDemo() {
        String[] vehicles = {"BUS-11","BUS-18","BUS-23"};
        String[] services = {"SV-A Central-Railway","SV-B Central-Benz","SV-C Railway-Bus"};
        boolean[][] eligible = {
            {true,true,false},
            {true,false,true},
            {false,true,true}
        };
        int[] assignment = FlowAlgorithms.maximumBipartiteMatching(eligible);
        int count = 0;
        System.out.println("Eligible assignment matrix (vehicle rows x service columns):");
        for (boolean[] row : eligible) System.out.println(Arrays.toString(row));
        System.out.println("Matching result:");
        for (int i=0;i<assignment.length;i++) {
            if(assignment[i]>=0){System.out.println(vehicles[i]+" -> "+services[assignment[i]]);count++;}
            else System.out.println(vehicles[i]+" -> not assigned");
        }
        System.out.println("Services assigned: " + count + " of " + services.length);
    }

    private static void schedulingAndConflictDemo() {
        // 3-SAT sample: (x1 OR !x2 OR x3) AND (!x1 OR x2 OR x3)
        int[][] clauses = {{1,-2,3},{-1,2,3}};
        boolean[] solution = ApproximationAlgorithms.solveSmall3Sat(3, clauses);
        System.out.println("Toy scheduling constraints: (x1 OR !x2 OR x3) AND (!x1 OR x2 OR x3)");
        System.out.println("Satisfying assignment found? " + (solution != null));
        if(solution!=null) System.out.println("Assignment [x1,x2,x3]: " + Arrays.toString(solution));
        List<int[]> compat = ApproximationAlgorithms.threeSatCompatibilityEdges(clauses);
        System.out.println("3-SAT-to-CLIQUE compatibility graph edges: " + compat.size());
        // Conflict edges: overlapping services represented by vertices 0..3.
        int[][] conflicts = {{0,1},{1,2},{2,3},{0,3}};
        System.out.println("Conflict edges: " + Arrays.deepToString(conflicts));
        System.out.println("2-approximate vertex cover (services to review/cover): "
                + ApproximationAlgorithms.vertexCoverTwoApprox(4, conflicts));
    }

    private static void passengerAnalyticsDemo() {
        int[] counts = PASSENGERS.stream().mapToInt(PassengerRecord::getPassengerCount).toArray();
        int[] sorted = counts.clone();
        ScaleAlgorithms.randomizedQuickSort(sorted);
        System.out.println("Passenger counts per sample travel record: " + Arrays.toString(counts));
        System.out.println("Randomized QuickSort ranking (ascending): " + Arrays.toString(sorted));
        System.out.println("Parallel reduction - total passengers represented: " + ScaleAlgorithms.parallelPassengerTotal(counts));
        List<PassengerRecord> sample = ScaleAlgorithms.reservoirSample(PASSENGERS, 3, new Random());
        System.out.println("Reservoir sample of 3 records from stream:");
        sample.forEach(System.out::println);
        System.out.println("Miller-Rabin test: is 1,000,003 probably prime? " + ScaleAlgorithms.millerRabin(1_000_003L, 8));
        System.out.println("Brent-style time upper-bound estimate for W=1000, S=80, P=4: "
                + ScaleAlgorithms.brentUpperBound(1000,80,4));
        System.out.println("These are demonstrations on a small sample; real-time ingestion/storage is outside this console prototype.");
    }
}
