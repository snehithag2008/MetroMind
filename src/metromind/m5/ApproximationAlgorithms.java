package metromind.m5;

import java.util.*;

/** Small educational examples for scheduling constraints and conflict coverage. */
public final class ApproximationAlgorithms {
    private ApproximationAlgorithms() {}

    public static boolean satisfies3Sat(int variables, int[][] clauses, boolean[] assignment) {
        if(assignment.length!=variables) throw new IllegalArgumentException("Assignment length mismatch.");
        for(int[] clause:clauses) {
            boolean clauseTrue=false;
            for(int literal:clause) {
                int variable=Math.abs(literal)-1;
                if(variable<0||variable>=variables) throw new IllegalArgumentException("Literal outside variable range.");
                boolean value=assignment[variable];
                if(literal<0)value=!value;
                clauseTrue |= value;
            }
            if(!clauseTrue)return false;
        }
        return true;
    }

    /** Brute-force SAT solver, suitable only for small teaching examples (e.g. <=20 variables). */
    public static boolean[] solveSmall3Sat(int variables, int[][] clauses) {
        if(variables>20) throw new IllegalArgumentException("Demo solver is limited to 20 variables.");
        long limit=1L<<variables;
        for(long mask=0;mask<limit;mask++) {
            boolean[] a=new boolean[variables];
            for(int i=0;i<variables;i++)a[i]=(mask&(1L<<i))!=0;
            if(satisfies3Sat(variables,clauses,a))return a;
        }
        return null;
    }

    /** Greedy 2-approximation for minimum vertex cover of an undirected conflict graph. */
    public static Set<Integer> vertexCoverTwoApprox(int vertices, int[][] edges) {
        boolean[] covered=new boolean[vertices];
        Set<Integer> cover=new LinkedHashSet<>();
        for(int[] e:edges) {
            int u=e[0],v=e[1];
            if(!covered[u]&&!covered[v]) {
                cover.add(u);cover.add(v);covered[u]=covered[v]=true;
            }
        }
        return cover;
    }

    /**
     * Converts a 3-SAT instance into a compatibility graph for the standard
     * clause-literal CLIQUE reduction. Each node is one literal occurrence.
     * Nodes from same clause are not connected; complementary literals are not connected.
     * A satisfying assignment exists iff the graph has a clique of size clauseCount.
     */
    public static List<int[]> threeSatCompatibilityEdges(int[][] clauses) {
        List<int[]> edges=new ArrayList<>();
        for(int i=0;i<clauses.length;i++)for(int j=i+1;j<clauses.length;j++)
            for(int a=0;a<clauses[i].length;a++)for(int b=0;b<clauses[j].length;b++)
                if(clauses[i][a] != -clauses[j][b])
                    edges.add(new int[]{i*3+a,j*3+b});
        return edges;
    }
}
