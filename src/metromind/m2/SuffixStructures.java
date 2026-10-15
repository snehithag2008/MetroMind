package metromind.m2;

import java.util.*;

/** Suffix-array, LCP/Kasai and suffix-automaton utilities for transport documents. */
public final class SuffixStructures {
    private SuffixStructures() {}

    /**
     * Prefix-doubling suffix-array construction. O(n log^2 n) here due to comparison sorting.
     * This is deliberately named accurately; it is not the SA-IS algorithm.
     */
    public static int[] suffixArray(String text) {
        int n = text.length();
        Integer[] sa = new Integer[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) { sa[i] = i; rank[i] = text.charAt(i); }
        for (int step = 1; step < n; step *= 2) {
            final int k = step;
            final int[] r = rank;
            Arrays.sort(sa, (a,b) -> {
                if (r[a] != r[b]) return Integer.compare(r[a], r[b]);
                int ra = a + k < n ? r[a + k] : -1;
                int rb = b + k < n ? r[b + k] : -1;
                return Integer.compare(ra, rb);
            });
            int[] nextRank = new int[n];
            for (int i = 1; i < n; i++) {
                int a = sa[i-1], b = sa[i];
                boolean different = rank[a] != rank[b]
                    || (a + k < n ? rank[a+k] : -1) != (b + k < n ? rank[b+k] : -1);
                nextRank[b] = nextRank[a] + (different ? 1 : 0);
            }
            rank = nextRank;
            if (rank[sa[n-1]] == n-1) break;
            if (step > n / 2) break; // avoids integer overflow on step *= 2
        }
        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = sa[i];
        return result;
    }

    /** Kasai algorithm: lcp[i] is LCP(sa[i], sa[i+1]); last entry is zero. */
    public static int[] kasaiLcp(String text, int[] sa) {
        int n = text.length();
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;
        int[] lcp = new int[n];
        int h = 0;
        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == n - 1) { h = 0; continue; }
            int j = sa[r + 1];
            while (i + h < n && j + h < n && text.charAt(i+h) == text.charAt(j+h)) h++;
            lcp[r] = h;
            if (h > 0) h--;
        }
        return lcp;
    }

    public static List<Integer> findWithSuffixArray(String text, String pattern) {
        int[] sa = suffixArray(text);
        List<Integer> positions = new ArrayList<>();
        for (int start : sa) {
            if (start + pattern.length() <= text.length()
                    && text.regionMatches(start, pattern, 0, pattern.length())) positions.add(start);
        }
        Collections.sort(positions);
        return positions;
    }

    public static int commonPrefixLength(String a, String b) {
        int i = 0, limit = Math.min(a.length(), b.length());
        while (i < limit && a.charAt(i) == b.charAt(i)) i++;
        return i;
    }

    /** Suffix automaton substring-existence query. */
    public static boolean suffixAutomatonContains(String text, String pattern) {
        class State {
            int len, link = -1;
            Map<Character,Integer> next = new HashMap<>();
        }
        List<State> st = new ArrayList<>();
        st.add(new State());
        int last = 0;
        for (char ch : text.toCharArray()) {
            int cur = st.size(); st.add(new State()); st.get(cur).len = st.get(last).len + 1;
            int p = last;
            while (p != -1 && !st.get(p).next.containsKey(ch)) {
                st.get(p).next.put(ch, cur); p = st.get(p).link;
            }
            if (p == -1) st.get(cur).link = 0;
            else {
                int q = st.get(p).next.get(ch);
                if (st.get(p).len + 1 == st.get(q).len) st.get(cur).link = q;
                else {
                    int clone = st.size(); st.add(new State());
                    st.get(clone).len = st.get(p).len + 1;
                    st.get(clone).next = new HashMap<>(st.get(q).next);
                    st.get(clone).link = st.get(q).link;
                    while (p != -1 && st.get(p).next.getOrDefault(ch, -1) == q) {
                        st.get(p).next.put(ch, clone); p = st.get(p).link;
                    }
                    st.get(q).link = clone; st.get(cur).link = clone;
                }
            }
            last = cur;
        }
        int state = 0;
        for (char ch : pattern.toCharArray()) {
            Integer next = st.get(state).next.get(ch);
            if (next == null) return false;
            state = next;
        }
        return true;
    }
}
