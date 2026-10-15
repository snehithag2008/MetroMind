METROMIND - Intelligent Public Transport Platform
=================================================

This is a Java console prototype for a DSA project. It uses clearly labelled sample/demo data;
it is not connected to a live transport agency or real passenger database.

ECLIPSE SETUP
1. Extract MetroMind_Full_Project.zip.
2. In Eclipse: File > New > Java Project > MetroMind.
3. If Eclipse creates module-info.java, you may delete it for this simple classpath project.
4. Copy the extracted `src/metromind` folder into the project's `src` folder.
5. Ensure package names match folder names.
6. Open metromind.Main and choose Run As > Java Application.

FEATURES
M1 / CO1: KMP station/alert search, Z-function prefix analysis, Rabin-Karp code search,
          Aho-Corasick multiple alert detection.
M2 / CO2: Suffix array using prefix-doubling, Kasai LCP, substring search using suffix array,
          suffix automaton substring search. SA-IS is explained in the report as a future
          replacement/extension; this project does NOT falsely label the doubling algorithm SA-IS.
M3 / CO3: Levenshtein and Damerau-Levenshtein spelling correction, bitmask DP for a small
          destination tour, matrix-chain multiplication cost, optimal BST expected search cost.
M4 / CO4: Dinic maximum flow for passenger-capacity network; bipartite matching for vehicle
          to service assignment; min-cut reachable side after max flow.
M5 / CO5: small-instance 3-SAT brute-force demonstration, 3-SAT clause-to-clique compatibility
          graph demo, conflict graph vertex-cover 2-approximation.
M6 / CO6: randomized quicksort, reservoir sampling, Miller-Rabin primality test,
          parallel reduction using Java parallel streams, simple Brent-style work/span estimate.

IMPORTANT ALGORITHM NOTES
- Data is illustrative and uses fictional/sample station records.
- Bitmask DP is exponential, so the menu uses only four destinations.
- 3-SAT brute force is intended only for small teaching examples.
- The prefix-doubling suffix-array implementation is O(n log^2 n) with comparison sorting.
  It is not SA-IS. SA-IS can be added as a separate implementation if your faculty explicitly
  requires a working linear-time SA-IS implementation.
- The transport route-tour demo assumes supplied pairwise costs; it is not a live route planner.

SEPARATE ALGORITHM CLASSES
Each algorithm now has its own class file (KMP.java, ZFunction.java, etc.) for the Eclipse structure.
Some classes are thin entry points that delegate to the shared implementation classes. For course submission,
be transparent that SAIS, FordFulkerson, and EdmondsKarp are documented entry points, not full implementations.
