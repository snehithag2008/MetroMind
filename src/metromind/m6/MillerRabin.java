package metromind.m6; public final class MillerRabin { private MillerRabin() {} public static boolean isProbablyPrime(long n,int rounds){ return ScaleAlgorithms.millerRabin(n,rounds); } }
