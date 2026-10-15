package metromind.m4; public final class MinCut { private MinCut() {} public static boolean[] sourceSide(FlowAlgorithms.Dinic network,int source){ return network.residualReachableFrom(source); } }
