package metromind.m2; public final class Kasai { private Kasai() {} public static int[] lcp(String text,int[] suffixArray){ return SuffixStructures.kasaiLcp(text,suffixArray); } }
