package dev.bbejeck.counting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SubdomainVisits class
 */
public class SubdomainVisitsTest {

    private final SubdomainVisits subdomainVisits = new SubdomainVisits();

    // Results may come back in any order, so compare sorted copies
    private static void assertSameElements(List<String> expected, List<String> actual) {
        List<String> sortedExpected = new ArrayList<>(expected);
        List<String> sortedActual = new ArrayList<>(actual);
        Collections.sort(sortedExpected);
        Collections.sort(sortedActual);
        assertEquals(sortedExpected, sortedActual);
    }

    @Test
    @DisplayName("Test with example 1: single three-level domain")
    void testExample1() {
        String[] cpdomains = {"9001 discuss.leetcode.com"};
        List<String> expected = List.of("9001 leetcode.com", "9001 discuss.leetcode.com", "9001 com");
        assertSameElements(expected, subdomainVisits.subdomainVisits(cpdomains));
    }

    @Test
    @DisplayName("Test with example 2: overlapping subdomains are summed")
    void testExample2() {
        String[] cpdomains = {"900 google.mail.com", "50 yahoo.com", "1 intel.mail.com", "5 wiki.org"};
        List<String> expected = List.of("901 mail.com", "50 yahoo.com", "900 google.mail.com", "5 wiki.org",
                "5 org", "1 intel.mail.com", "951 com");
        assertSameElements(expected, subdomainVisits.subdomainVisits(cpdomains));
    }

    @Test
    @DisplayName("Test same domain listed twice is summed")
    void testDuplicateDomain() {
        String[] cpdomains = {"10 a.b.com", "5 a.b.com"};
        List<String> expected = List.of("15 a.b.com", "15 b.com", "15 com");
        assertSameElements(expected, subdomainVisits.subdomainVisits(cpdomains));
    }

    @Test
    @DisplayName("Test top-level domain only")
    void testTopLevelOnly() {
        String[] cpdomains = {"7 com", "3 org"};
        List<String> expected = List.of("7 com", "3 org");
        assertSameElements(expected, subdomainVisits.subdomainVisits(cpdomains));
    }

    @Test
    @DisplayName("Test two-level domain")
    void testTwoLevelDomain() {
        String[] cpdomains = {"42 leetcode.com"};
        List<String> expected = List.of("42 leetcode.com", "42 com");
        assertSameElements(expected, subdomainVisits.subdomainVisits(cpdomains));
    }

    @Test
    @DisplayName("Test same subdomain name under different parents is counted separately")
    void testSameLabelDifferentParents() {
        String[] cpdomains = {"4 mail.google.com", "6 mail.yahoo.com"};
        List<String> expected = List.of("4 mail.google.com", "4 google.com", "6 mail.yahoo.com",
                "6 yahoo.com", "10 com");
        assertSameElements(expected, subdomainVisits.subdomainVisits(cpdomains));
    }

    @Test
    @DisplayName("Test large visit counts are summed correctly")
    void testLargeCounts() {
        String[] cpdomains = {"10000 a.com", "10000 b.com"};
        List<String> expected = List.of("10000 a.com", "10000 b.com", "20000 com");
        assertSameElements(expected, subdomainVisits.subdomainVisits(cpdomains));
    }

    @Test
    @DisplayName("Test empty input returns empty list")
    void testEmptyInput() {
        assertTrue(subdomainVisits.subdomainVisits(new String[]{}).isEmpty());
    }
}
