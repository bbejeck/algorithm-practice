package dev.bbejeck.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CamelCase class
 */
public class CamelCaseTest {

    private final CamelCase camelCase = new CamelCase();

    private static final String[] QUERIES = {"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"};

    @Test
    @DisplayName("Test with example 1: pattern FB")
    void testExample1() {
        List<Boolean> expected = List.of(true, false, true, true, false);
        assertEquals(expected, camelCase.camelMatch(QUERIES, "FB"));
    }

    @Test
    @DisplayName("Test with example 2: pattern FoBa")
    void testExample2() {
        List<Boolean> expected = List.of(true, false, true, false, false);
        assertEquals(expected, camelCase.camelMatch(QUERIES, "FoBa"));
    }

    @Test
    @DisplayName("Test with example 3: pattern FoBaT")
    void testExample3() {
        List<Boolean> expected = List.of(false, true, false, false, false);
        assertEquals(expected, camelCase.camelMatch(QUERIES, "FoBaT"));
    }

    @Test
    @DisplayName("Test query identical to pattern")
    void testExactMatch() {
        assertTrue(camelCase.isMatch("FooBar".toCharArray(), "FooBar".toCharArray()));
    }

    @Test
    @DisplayName("Test pattern longer than query")
    void testPatternLongerThanQuery() {
        assertFalse(camelCase.isMatch("Foo".toCharArray(), "FooBar".toCharArray()));
    }

    @Test
    @DisplayName("Test pattern characters not all present in query")
    void testPatternNotFullyConsumed() {
        // lowercase 'x' never appears, so the pattern is never fully matched
        assertFalse(camelCase.isMatch("FooBar".toCharArray(), "FxB".toCharArray()));
    }

    @Test
    @DisplayName("Test extra uppercase letter in query fails")
    void testExtraUppercaseInQuery() {
        assertFalse(camelCase.isMatch("FooBarBaz".toCharArray(), "FB".toCharArray()));
    }

    @Test
    @DisplayName("Test extra uppercase letter at start of query fails")
    void testLeadingExtraUppercase() {
        assertFalse(camelCase.isMatch("XFooBar".toCharArray(), "FB".toCharArray()));
    }

    @Test
    @DisplayName("Test all lowercase query and pattern")
    void testAllLowercase() {
        assertTrue(camelCase.isMatch("abcde".toCharArray(), "ace".toCharArray()));
        assertFalse(camelCase.isMatch("abcde".toCharArray(), "aec".toCharArray()));
    }

    @Test
    @DisplayName("Test empty pattern matches only queries without uppercase")
    void testEmptyPattern() {
        List<Boolean> expected = List.of(true, false);
        assertEquals(expected, camelCase.camelMatch(new String[]{"foobar", "fooBar"}, ""));
    }

    @Test
    @DisplayName("Test empty queries array returns empty list")
    void testEmptyQueries() {
        assertTrue(camelCase.camelMatch(new String[]{}, "FB").isEmpty());
    }
}
