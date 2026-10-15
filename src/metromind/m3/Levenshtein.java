
package metromind.m3;

public final class Levenshtein {

    private Levenshtein() {}

    public static int distance(String a, String b) {
        return DynamicProgramming.levenshtein(a, b);
    }
}
