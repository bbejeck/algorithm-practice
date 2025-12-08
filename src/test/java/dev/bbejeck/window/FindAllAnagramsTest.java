package dev.bbejeck.window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FindAllAnagrams class
 */
class FindAllAnagramsTest {

    private FindAllAnagrams findAllAnagrams;

    @BeforeEach
    void setUp() {
        findAllAnagrams = new FindAllAnagrams();
    }

    @Test
    @DisplayName("Test with example 1: cbaebabacd with pattern 'abc'")
    void testExample1() {
        String s = "cbaebabacd";
        String p = "abc";
        List<Integer> expected = List.of(0, 6);

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertEquals(expected, result, "Should find anagrams at indices 0 and 6");
    }

    @Test
    @DisplayName("Test with example 2: abab with pattern 'ab'")
    void testExample2() {
        String s = "abab";
        String p = "ab";
        List<Integer> expected = List.of(0, 1, 2);

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertEquals(expected, result, "Should find anagrams at indices 0, 1, and 2");
    }

    @Test
    @DisplayName("Should return empty list when pattern is longer than original")
    void testPatternLongerThanOriginal() {
        String s = "ab";
        String p = "abc";

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertTrue(result.isEmpty(), "Should return empty list when pattern is longer");
    }

    @Test
    @DisplayName("Should return empty list when no anagrams found")
    void testNoAnagramsFound() {
        String s = "hello";
        String p = "xyz";

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertTrue(result.isEmpty(), "Should return empty list when no anagrams exist");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacter() {
        String s = "a";
        String p = "a";
        List<Integer> expected = List.of(0);

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertEquals(expected, result, "Should find single character match at index 0");
    }

    @Test
    @DisplayName("Should handle pattern equal to original string")
    void testPatternEqualsOriginal() {
        String s = "abc";
        String p = "bca";
        List<Integer> expected = List.of(0);

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertEquals(expected, result, "Should find anagram when pattern length equals original");
    }

    @Test
    @DisplayName("Should handle repeated characters in pattern")
    void testRepeatedCharactersInPattern() {
        String s = "aabaa";
        String p = "aa";
        List<Integer> expected = List.of(0, 3);

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertEquals(expected, result, "Should correctly handle repeated characters");
    }

    @Test
    @DisplayName("Should handle all same characters")
    void testAllSameCharacters() {
        String s = "aaaa";
        String p = "aa";
        List<Integer> expected = List.of(0, 1, 2);

        List<Integer> result = findAllAnagrams.findAnagrams(s, p);

        assertEquals(expected, result, "Should find all overlapping anagrams with same characters");
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    @DisplayName("Parameterized test cases for findAnagrams")
    void testFindAnagramsParameterized(String original, String pattern, List<Integer> expected) {
        List<Integer> result = findAllAnagrams.findAnagrams(original, pattern);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                // Standard cases
                Arguments.of("cbaebabacd", "abc", List.of(0, 6)),
                Arguments.of("abab", "ab", List.of(0, 1, 2)),

                // Edge cases
                Arguments.of("a", "a", List.of(0)),
                Arguments.of("ab", "ba", List.of(0)),
                Arguments.of("", "a", List.of()),

                // No matches
                Arguments.of("abcdef", "xyz", List.of()),
                Arguments.of("aaaaa", "b", List.of()),

                // Multiple matches
                Arguments.of("abcabcabc", "abc", List.of(0,1,2,3,4,5,6)),
                Arguments.of("baa", "aa", List.of(1)),

                // Longer patterns
                Arguments.of("abcdabcdabcd", "dcba", List.of(0,1,2,3,4,5,6,7,8))
        );
    }
}