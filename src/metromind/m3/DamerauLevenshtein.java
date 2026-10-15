
package metromind.m3;

public final class DamerauLevenshtein {

    private DamerauLevenshtein() {}

    public static int distance(String a, String b) {
        return DynamicProgramming.damerauLevenshtein(a, b);
    }
}
