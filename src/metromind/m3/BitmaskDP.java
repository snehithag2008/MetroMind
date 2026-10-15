package metromind.m3; public final class BitmaskDP { private BitmaskDP() {} public static int minimumVisitCost(int[][] costs,int start){ return DynamicProgramming.bitmaskTourCost(costs,start); } }
