package dev.bbejeck.window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestSubstringWithoutRepeatTest {

    private LongestSubstringWithoutRepeat solution;

    @BeforeEach
    void setUp() {
        solution = new LongestSubstringWithoutRepeat();
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyString() {
        assertEquals(0, solution.lengthOfLongestSubstring(""));
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacter() {
        assertEquals(1, solution.lengthOfLongestSubstring("a"));
    }

    @Test
    @DisplayName("Should handle strings with all unique characters")
    void testAllUniqueCharacters() {
        assertEquals(5, solution.lengthOfLongestSubstring("abcde"));
    }

    @Test
    @DisplayName("Should handle strings with repeated characters")
    void testRepeatedCharacters() {
        assertEquals(3, solution.lengthOfLongestSubstring("abcabcbb"));
    }

    @Test
    @DisplayName("Should handle strings with all same characters")
    void testAllSameCharacters() {
        assertEquals(1, solution.lengthOfLongestSubstring("bbbbb"));
    }

    @Test
    @DisplayName("Should handle complex patterns")
    void testComplexPattern() {
        assertEquals(6, solution.lengthOfLongestSubstring("aabcdef"));
        assertEquals(3, solution.lengthOfLongestSubstring("pwwkew"));
        assertEquals(7, solution.lengthOfLongestSubstring("abcdefg"));
    }

    @ParameterizedTest(name = "Input: {0}, Expected Output: {1}")
    @MethodSource("provideTestCases")
    void testMultipleCases(String input, int expected) {
        assertEquals(expected, solution.lengthOfLongestSubstring(input));
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("abcabcbb", 3),
                Arguments.of("bbbbb", 1),
                Arguments.of("pwwkew", 3),
                Arguments.of("", 0),
                Arguments.of("a", 1),
                Arguments.of("aab", 2),
                Arguments.of("dvdf", 3),
                Arguments.of("anviaj", 5),
                Arguments.of("abcdeafbdc", 6),
                Arguments.of("tmmzuxt", 5)
        );
    }
}