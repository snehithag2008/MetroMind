package metromind.m6; public final class ParallelReduce { private ParallelReduce() {} public static long sum(int[] values){ return ScaleAlgorithms.parallelPassengerTotal(values); } }
