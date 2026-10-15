package metromind.m2; public final class LCP { private LCP() {} public static int commonPrefix(String a,String b){ return SuffixStructures.commonPrefixLength(a,b); } }
