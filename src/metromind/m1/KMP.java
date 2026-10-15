package metromind.m1; public final class KMP { private KMP() {} public static int search(String text,String pattern){ return StringAlgorithms.kmpIndexOf(text,pattern); } }
