package dev.bbejeck.strings;

import dev.bbejeck.strings.PrefixMatchFilter.Cost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PrefixMatchFilter
 */
public class PrefixMatchFilterTest {

    private static PrefixMatchFilter of(String... prefixes) {
        PrefixMatchFilter f = new PrefixMatchFilter();
        for (String p : prefixes) {
            f.addPrefix(p);
        }
        return f;
    }

    // Part 1

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        assertEquals(List.of("abc", "prefix", "cater"),
                of("ab", "cat", "pre").filter(List.of("abc", "dog", "prefix", "cater", "zoo")));
    }

    @Test
    @DisplayName("Matching strings appear once, in first-appearance order")
    void testDuplicates() {
        assertEquals(List.of("ab", "abacus"), of("a", "ab").filter(List.of("ab", "abacus", "ab", "b")));
        assertEquals(List.of("abc", "abd"), of("a", "ab", "abc").filter(List.of("abc", "abd", "b", "abc")));
    }

    @Test
    @DisplayName("A string equal to a prefix matches; a shorter one doesn't")
    void testExactAndShorter() {
        assertEquals(List.of("cat"), of("cat").filter(List.of("ca", "cat", "c", "")));
    }

    @Test
    @DisplayName("The empty prefix matches everything")
    void testEmptyPrefix() {
        assertEquals(List.of("", "x", "yz"), of("").filter(List.of("", "x", "yz", "x")));
    }

    @Test
    @DisplayName("No prefixes, no matches")
    void testNoPrefixes() {
        assertEquals(List.of(), new PrefixMatchFilter().filter(List.of("a", "", "abc")));
        assertEquals(List.of(), of("a").filter(List.of()));
    }

    @Test
    @DisplayName("Prefixes can be added between filter calls")
    void testIncremental() {
        PrefixMatchFilter f = of("x");
        assertEquals(List.of(), f.filter(List.of("abc")));
        f.addPrefix("ab");
        assertEquals(List.of("abc"), f.filter(List.of("abc")));
    }

    @Test
    @DisplayName("Random inputs agree with brute force")
    void testRandom() {
        Random random = new Random(17);
        for (int round = 0; round < 200; round++) {
            List<String> prefixes = randomStrings(random, 1 + random.nextInt(6), 3);
            List<String> inputs = randomStrings(random, 20, 5);
            PrefixMatchFilter f = new PrefixMatchFilter();
            prefixes.forEach(f::addPrefix);
            LinkedHashSet<String> expected = new LinkedHashSet<>();
            for (String s : inputs) {
                if (prefixes.stream().anyMatch(s::startsWith)) {
                    expected.add(s);
                }
            }
            assertEquals(new ArrayList<>(expected), f.filter(inputs), "prefixes " + prefixes);
        }
    }

    @Test
    @DisplayName("Large input runs in linear time")
    void testPerformance() {
        Random random = new Random(3);
        List<String> prefixes = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            prefixes.add(randomString(random, 6 + random.nextInt(4)));
        }
        List<String> inputs = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            inputs.add(randomString(random, 10));
        }
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            PrefixMatchFilter f = new PrefixMatchFilter();
            prefixes.forEach(f::addPrefix);
            for (int i = 0; i < 20; i++) {
                f.filter(inputs);
            }
        });
    }

    // Part 2

    @Test
    @DisplayName("Costs for the first example")
    void testCostsExample1() {
        assertEquals(new Cost(27, 10, 8, 8, "trie", "tie"),
                PrefixMatchFilter.compareCosts(List.of("ab", "cat", "pre"),
                        List.of("abc", "dog", "prefix", "cater", "zoo")));
    }

    @Test
    @DisplayName("Costs with duplicate prefixes and an empty input")
    void testCostsExample2() {
        assertEquals(new Cost(3, 3, 6, 3, "tie", "trie"),
                PrefixMatchFilter.compareCosts(List.of("a", "ab", "abc", "ab"), List.of("abc", "abd", "b", "")));
    }

    @Test
    @DisplayName("Costs when the input runs out before a prefix ends")
    void testCostsInputEndsEarly() {
        // hash: "ab" -> 1 + 2 = 3, trie: inspects 'a', 'b' then runs out -> 2
        assertEquals(new Cost(3, 2, 3, 3, "trie", "tie"),
                PrefixMatchFilter.compareCosts(List.of("abc"), List.of("ab")));
    }

    @Test
    @DisplayName("Costs with the empty prefix are zero time")
    void testCostsEmptyPrefix() {
        // trie: "x" and "xy" share a node -> 2 nodes; hash: 0 + 1 + 2 = 3 characters
        assertEquals(new Cost(0, 0, 3, 2, "tie", "trie"),
                PrefixMatchFilter.compareCosts(List.of("", "x", "xy"), List.of("abc", "xyz", "")));
    }

    @Test
    @DisplayName("Hash can use less space when prefixes share nothing and are short")
    void testCostsNoSharing() {
        // hash: "a" -> 1, "b" -> 1, "zz" -> 1 + 2 = 3 => 5; trie: 1 + 1 + 1 ('z' missing edge) => 3
        assertEquals(new Cost(5, 3, 2, 2, "trie", "tie"),
                PrefixMatchFilter.compareCosts(List.of("a", "b"), List.of("a", "b", "zz")));
    }

    private static List<String> randomStrings(Random random, int n, int maxLen) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(randomString(random, random.nextInt(maxLen + 1)));
        }
        return result;
    }

    private static String randomString(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(3)));
        }
        return sb.toString();
    }
}
