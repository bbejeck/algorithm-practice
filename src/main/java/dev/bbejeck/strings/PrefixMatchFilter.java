package dev.bbejeck.strings;

import java.util.List;

/**
 * Prefix Match Filter (reported Datadog onsite question, via PracHub)
 *
 * Part 1, trie filter:
 *   addPrefix(prefix) stores a prefix in a trie.
 *   filter(inputs) returns every input that starts with at least one stored prefix.
 *     Each matching string appears once, in the order of its first appearance in inputs.
 *     If "" was added, every string matches, including "".
 *   Walk each input one character at a time and stop at the first node that ends a stored prefix,
 *   or at the first missing edge.
 *
 * Part 2, compareCosts(prefixes, inputs):
 *   Compare a hash-set approach against the trie using this cost model. Duplicate prefixes count once.
 *     hashSpace: total characters across the distinct prefixes.
 *     trieSpace: number of trie nodes, not counting the root.
 *     hashTime:  for each input, build s[0..1), s[0..2), ... and look each one up. Building a prefix
 *                of length k costs k. A first match at length k costs 1 + 2 + ... + k. No match costs
 *                1 + 2 + ... + len(s).
 *     trieTime:  for each input, 1 per character inspected, stopping at a node that ends a prefix,
 *                a missing edge (that character still counts), or the end of the input.
 *     If "" is a prefix, both times are 0 for every input.
 *   fasterTime / smallerSpace are "hash", "trie", or "tie" (the lower cost wins).
 *
 * Example:
 *   prefixes ["ab", "cat", "pre"], inputs ["abc", "dog", "prefix", "cater", "zoo"]
 *   filter -> ["abc", "prefix", "cater"]
 *   costs  -> hashTime 27, trieTime 10, hashSpace 8, trieSpace 8, faster "trie", smaller "tie"
 *
 * Constraints: up to 10^4 prefixes and inputs, at most 2 * 10^5 characters in total, lowercase
 * letters only, strings may be empty.
 */
public class PrefixMatchFilter {

    public record Cost(long hashTime, long trieTime, long hashSpace, long trieSpace,
                       String fasterTime, String smallerSpace) { }

    public void addPrefix(String prefix) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<String> filter(List<String> inputs) {
        throw new UnsupportedOperationException("TODO");
    }

    public static Cost compareCosts(List<String> prefixes, List<String> inputs) {
        throw new UnsupportedOperationException("TODO");
    }
}
