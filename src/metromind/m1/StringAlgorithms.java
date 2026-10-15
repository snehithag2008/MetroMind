package metromind.m1;

import java.util.*;

/** String algorithms used for station, route, ticket-code and alert search. */
public final class StringAlgorithms {
    private StringAlgorithms() {}

    public static int kmpIndexOf(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] lps = new int[pattern.length()];
        for (int i = 1, len = 0; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(len)) lps[i++] = ++len;
            else if (len > 0) len = lps[len - 1];
            else lps[i++] = 0;
        }
        for (int i = 0, j = 0; i < text.length();) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++; j++;
                if (j == pattern.length()) return i - j;
            } else if (j > 0) j = lps[j - 1];
            else i++;
        }
        return -1;
    }

    public static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];
        int left = 0, right = 0;
        for (int i = 1; i < n; i++) {
            if (i <= right) z[i] = Math.min(right - i + 1, z[i - left]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] - 1 > right) { left = i; right = i + z[i] - 1; }
        }
        return z;
    }

    public static int rabinKarpIndexOf(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        if (pattern.length() > text.length()) return -1;
        final long mod = 1_000_000_007L, base = 257L;
        long pHash = 0, tHash = 0, power = 1;
        int m = pattern.length();
        for (int i = 0; i < m; i++) {
            pHash = (pHash * base + pattern.charAt(i)) % mod;
            tHash = (tHash * base + text.charAt(i)) % mod;
            if (i < m - 1) power = (power * base) % mod;
        }
        for (int i = 0; i <= text.length() - m; i++) {
            if (pHash == tHash && text.regionMatches(i, pattern, 0, m)) return i;
            if (i < text.length() - m) {
                tHash = (tHash - text.charAt(i) * power % mod + mod) % mod;
                tHash = (tHash * base + text.charAt(i + m)) % mod;
            }
        }
        return -1;
    }

    /** Trie + failure links for reporting all occurrences of multiple patterns. */
    public static List<String> ahoCorasick(String text, List<String> patterns) {
        class Node {
            Map<Character, Integer> next = new HashMap<>();
            int fail;
            Set<String> output = new LinkedHashSet<>();
        }
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node());
        for (String p : patterns) {
            if (p.isEmpty()) continue;
            int state = 0;
            for (char ch : p.toLowerCase(Locale.ROOT).toCharArray()) {
                Integer to = nodes.get(state).next.get(ch);
                if (to == null) {
                    to = nodes.size();
                    nodes.get(state).next.put(ch, to);
                    nodes.add(new Node());
                }
                state = to;
            }
            nodes.get(state).output.add(p);
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int child : nodes.get(0).next.values()) q.add(child);
        while (!q.isEmpty()) {
            int v = q.remove();
            for (Map.Entry<Character,Integer> e : nodes.get(v).next.entrySet()) {
                char ch = e.getKey();
                int u = e.getValue();
                int f = nodes.get(v).fail;
                while (f != 0 && !nodes.get(f).next.containsKey(ch)) f = nodes.get(f).fail;
                if (nodes.get(f).next.containsKey(ch)) f = nodes.get(f).next.get(ch);
                nodes.get(u).fail = f;
                nodes.get(u).output.addAll(nodes.get(f).output);
                q.add(u);
            }
        }
        Set<String> found = new LinkedHashSet<>();
        int state = 0;
        for (char ch : text.toLowerCase(Locale.ROOT).toCharArray()) {
            while (state != 0 && !nodes.get(state).next.containsKey(ch)) state = nodes.get(state).fail;
            state = nodes.get(state).next.getOrDefault(ch, 0);
            found.addAll(nodes.get(state).output);
        }
        return new ArrayList<>(found);
    }
}
