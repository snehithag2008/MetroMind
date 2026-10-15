package metromind.m4;

import java.util.*;

/** Flow and matching algorithms for transport capacity and vehicle allocation demos. */
public final class FlowAlgorithms {
    private FlowAlgorithms() {}

    public static final class Dinic {
        private static final class Edge { int to, rev, cap, original; Edge(int t,int r,int c){to=t;rev=r;cap=c;original=c;} }
        private final List<Edge>[] graph; private int[] level, ptr;
        @SuppressWarnings("unchecked")
        public Dinic(int n) { graph=(List<Edge>[])new List<?>[n]; for(int i=0;i<n;i++)graph[i]=new ArrayList<>(); level=new int[n]; ptr=new int[n]; }
        public void addEdge(int u,int v,int cap) { Edge a=new Edge(v,graph[v].size(),cap), b=new Edge(u,graph[u].size(),0); graph[u].add(a); graph[v].add(b); }
        private boolean bfs(int s,int t) {
            Arrays.fill(level,-1); Queue<Integer> q=new ArrayDeque<>(); q.add(s); level[s]=0;
            while(!q.isEmpty()){int v=q.remove();for(Edge e:graph[v])if(e.cap>0&&level[e.to]<0){level[e.to]=level[v]+1;q.add(e.to);}}
            return level[t]>=0;
        }
        private int dfs(int v,int t,int pushed) {
            if(pushed==0)return 0;if(v==t)return pushed;
            for(;ptr[v]<graph[v].size();ptr[v]++){Edge e=graph[v].get(ptr[v]);if(e.cap<=0||level[e.to]!=level[v]+1)continue;
                int tr=dfs(e.to,t,Math.min(pushed,e.cap));if(tr==0)continue;e.cap-=tr;graph[e.to].get(e.rev).cap+=tr;return tr;}
            return 0;
        }
        public int maxFlow(int s,int t) { int flow=0;while(bfs(s,t)){Arrays.fill(ptr,0);int pushed;while((pushed=dfs(s,t,Integer.MAX_VALUE))>0)flow+=pushed;}return flow; }
        public boolean[] residualReachableFrom(int s) {
            boolean[] seen=new boolean[graph.length];Queue<Integer>q=new ArrayDeque<>();q.add(s);seen[s]=true;
            while(!q.isEmpty()){int v=q.remove();for(Edge e:graph[v])if(e.cap>0&&!seen[e.to]){seen[e.to]=true;q.add(e.to);}}
            return seen;
        }
    }

    /** Kuhn augmenting-path matching. leftToRight[u] is the assigned right-side vertex or -1. */
    public static int[] maximumBipartiteMatching(boolean[][] canAssign) {
        int left=canAssign.length, right=left==0?0:canAssign[0].length;
        int[] matchRight=new int[right];Arrays.fill(matchRight,-1);
        for(int u=0;u<left;u++) augment(u,canAssign,new boolean[right],matchRight);
        int[] result=new int[left];Arrays.fill(result,-1);
        for(int v=0;v<right;v++)if(matchRight[v]>=0)result[matchRight[v]]=v;
        return result;
    }
    private static boolean augment(int u,boolean[][] g,boolean[] seen,int[] matchRight) {
        for(int v=0;v<g[u].length;v++)if(g[u][v]&&!seen[v]){seen[v]=true;if(matchRight[v]<0||augment(matchRight[v],g,seen,matchRight)){matchRight[v]=u;return true;}}
        return false;
    }
}
