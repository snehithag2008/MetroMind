
package metromind.m3;

import java.util.Arrays;

/** Dynamic-programming utilities for station correction and planning tasks. */
public final class DynamicProgramming {

    private DynamicProgramming() {}

    private static void validateStrings(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Strings cannot be null.");
        }
    }

    public static int levenshtein(String a, String b) {
        validateStrings(a, b);

        int m = a.length();
        int n = b.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1,
                             dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[m][n];
    }

    /** Optimal string-alignment Damerau-Levenshtein variant. */
    public static int damerauLevenshtein(String a, String b) {
        validateStrings(a, b);

        int m = a.length();
        int n = b.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1,
                             dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );

                if (i > 1 && j > 1
                    && a.charAt(i - 1) == b.charAt(j - 2)
                    && a.charAt(i - 2) == b.charAt(j - 1)) {
                    dp[i][j] = Math.min(
                        dp[i][j],
                        dp[i - 2][j - 2] + 1
                    );
                }
            }
        }

        return dp[m][n];
    }

    /**
     * Minimum-cost path starting at start and visiting every destination.
     * Does not return to the start.
     */
    public static int bitmaskTourCost(int[][] cost, int start) {
        if (cost == null) {
            throw new IllegalArgumentException("Cost matrix cannot be null.");
        }

        int n = cost.length;

        if (n == 0 || n > 20 || start < 0 || start >= n) {
            throw new IllegalArgumentException(
                "Use 1..20 destinations and a valid start."
            );
        }

        for (int[] row : cost) {
            if (row == null || row.length != n) {
                throw new IllegalArgumentException(
                    "Cost matrix must be square."
                );
            }
        }

        int states = 1 << n;
        int inf = Integer.MAX_VALUE / 4;
        int[][] dp = new int[states][n];

        for (int[] row : dp) Arrays.fill(row, inf);

        dp[1 << start][start] = 0;

        for (int mask = 0; mask < states; mask++) {
            for (int u = 0; u < n; u++) {
                if (dp[mask][u] >= inf) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0) {
                        int next = mask | (1 << v);
                        dp[next][v] = Math.min(
                            dp[next][v],
                            dp[mask][u] + cost[u][v]
                        );
                    }
                }
            }
        }

        int answer = inf;
        for (int v = 0; v < n; v++) {
            answer = Math.min(answer, dp[states - 1][v]);
        }

        return answer;
    }

    /** dimensions [p0, p1, ..., pn] represent n matrices. */
    public static long matrixChainMinimumCost(int[] dimensions) {
        if (dimensions == null || dimensions.length < 2) {
            return 0;
        }

        int n = dimensions.length - 1;
        long[][] dp = new long[n][n];

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                dp[i][j] = Long.MAX_VALUE / 4;

                for (int k = i; k < j; k++) {
                    long multiplyCost =
                        (long) dimensions[i]
                        * dimensions[k + 1]
                        * dimensions[j + 1];

                    long total = dp[i][k]
                               + dp[k + 1][j]
                               + multiplyCost;

                    dp[i][j] = Math.min(dp[i][j], total);
                }
            }
        }

        return dp[0][n - 1];
    }

    /** Keys must be sorted; frequencies are successful-search frequencies. */
    public static long optimalBstCost(int[] keys, int[] freq) {
        if (keys == null || freq == null || keys.length != freq.length) {
            throw new IllegalArgumentException(
                "Keys and frequencies must have the same length."
            );
        }

        int n = keys.length;
        if (n == 0) return 0;

        for (int i = 1; i < n; i++) {
            if (keys[i] < keys[i - 1]) {
                throw new IllegalArgumentException("Keys must be sorted.");
            }
        }

        long[][] dp = new long[n][n];
        long[] prefix = new long[n + 1];

        for (int i = 0; i < n; i++) {
            if (freq[i] < 0) {
                throw new IllegalArgumentException(
                    "Frequencies cannot be negative."
                );
            }

            dp[i][i] = freq[i];
            prefix[i + 1] = prefix[i] + freq[i];
        }

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                long sum = prefix[j + 1] - prefix[i];
                dp[i][j] = Long.MAX_VALUE / 4;

                for (int root = i; root <= j; root++) {
                    long left = root > i ? dp[i][root - 1] : 0;
                    long right = root < j ? dp[root + 1][j] : 0;

                    dp[i][j] = Math.min(
                        dp[i][j],
                        left + right + sum
                    );
                }
            }
        }

        return dp[0][n - 1];
    }
}
