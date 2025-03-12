package dev.bbejeck.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstIndexOfFirstOccurrenceTest {

    private FirstIndexOfFirstOccurrence finder;

    @BeforeEach
    public void setup() {
        finder = new FirstIndexOfFirstOccurrence();
    }

    @Test
    public void testNeedleLongerThanHaystack() {
        assertEquals(-1, finder.strStr("abc", "abcde"));
    }

    @Test
    public void testEqualStrings() {
        assertEquals(0, finder.strStr("abc", "abc"));
    }

    @Test
    public void testEmptyNeedle() {
        assertEquals(0, finder.strStr("abc", ""));
    }

    @Test
    public void testEmptyHaystack() {
        assertEquals(-1, finder.strStr("", "abc"));
    }

    @Test
    public void testBothEmpty() {
        assertEquals(0, finder.strStr("", ""));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testStrStr(String haystack, String needle, int expectedIndex) {
        assertEquals(expectedIndex, finder.strStr(haystack, needle));
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                // Basic test cases
                Arguments.of("hello", "ll", 2),
                Arguments.of("aaaaa", "bba", -1),
                Arguments.of("sadbutsad", "sad", 0),

                // Edge cases
                Arguments.of("a", "a", 0),
                Arguments.of("abc", "c", 2),
                Arguments.of("abc", "ab", 0),

                // Multiple occurrences - should find first
                Arguments.of("mississippi", "issi", 1),
                Arguments.of("aabaaabaaac", "aabaaac", 4),

                // Partial matches that fail
                Arguments.of("ababac", "abac", 2),
                Arguments.of("aabaabaacaabaabaac", "aabaabaabaac", -1),

                // Cases with repeated characters
                Arguments.of("aaaa", "aa", 0),
                Arguments.of("abababa", "aba", 0),

                // No match cases
                Arguments.of("hello world", "goodbye", -1),
                Arguments.of("needle", "haystack", -1)
        );
    }
}