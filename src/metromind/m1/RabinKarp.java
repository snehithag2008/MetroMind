package metromind.m1; public final class RabinKarp { private RabinKarp() {} public static int search(String text,String pattern){ return StringAlgorithms.rabinKarpIndexOf(text,pattern); } }
