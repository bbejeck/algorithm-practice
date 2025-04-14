package dev.bbejeck.window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubstringWithConcatenationOfAllWordsTest {

    private SubstringWithConcatenationOfAllWords solution;

    @BeforeEach
    void setUp() {
        solution = new SubstringWithConcatenationOfAllWords();
    }

    @Test
    @DisplayName("Test with example 1: barfoothefoobarman with [foo, bar]")
    void testExample1() {
        String s = "barfoothefoobarman";
        String[] words = {"foo", "bar"};
        List<Integer> expected = List.of(0, 9);

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result, "Should find indices where all words concatenate");
    }

    @Test
    @DisplayName("Test with example 2: wordgoodgoodgoodbestword with [word, good, best, word]")
    void testExample2() {
        String s = "wordgoodgoodgoodbestword";
        String[] words = {"word", "good", "best", "word"};
        List<Integer> expected = List.of();

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result, "Should return empty list when no valid concatenation exists");
    }

    @Test
    @DisplayName("Test with example 3: barfoofoobarthefoobarman with [bar, foo, the]")
    void testExample3() {
        String s = "barfoofoobarthefoobarman";
        String[] words = {"bar", "foo", "the"};
        List<Integer> expected = List.of(6, 9, 12);

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result, "Should find all valid starting indices");
    }

    @Test
    @DisplayName("Test with duplicate words")
    void testWithDuplicateWords() {
        String s = "wordgoodgoodgoodbestword";
        String[] words = {"word", "good", "good", "best"};
        List<Integer> expected = List.of(8);

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result, "Should handle duplicate words correctly");
    }
    

    @Test
    @DisplayName("Test with input shorter than required length")
    void testWithShortInput() {
        String s = "short";
        String[] words = {"longer", "than", "input"};

        List<Integer> result = solution.findSubstring(s, words);

        assertTrue(result.isEmpty(), "Should return empty list when input is too short");
    }

    @Test
    @DisplayName("Test with exact match (no extra characters)")
    void testWithExactMatch() {
        String s = "foobar";
        String[] words = {"foo", "bar"};
        List<Integer> expected = List.of(0);

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result, "Should find match when input is exactly the concatenation");
    }

    @Test
    @DisplayName("Test with repeated pattern")
    void testWithRepeatedPattern() {
        String s = "ababababab";
        String[] words = {"ab", "ab"};
        List<Integer> expected = List.of(0, 2, 4, 6);

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result, "Should find all occurrences in repeating pattern");
    }

    @Test
    @DisplayName("Test with empty result")
    void testWithNoMatch() {
        String s = "mississippi";
        String[] words = {"is", "pp"};

        List<Integer> result = solution.findSubstring(s, words);

        assertTrue(result.isEmpty(), "Should return empty list when no matches exist");
    }

    @Test
    @DisplayName("Test with large number of identical single-character words")
    void testLargeIdenticalSingleCharWords() {
        // Generate a string of 500 'a' characters (using smaller size for unit test)
        int size = 500;
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append('a');
        }
        String s = sb.toString();

        // Generate an array of 500 "a" strings
        String[] words = new String[size];
        for (int i = 0; i < size; i++) {
            words[i] = "a";
        }

        // Generate expected result (indices 0 to size-1)
        List<Integer> expected = Arrays.asList(0);

        // Measure execution time
        long startTime = System.currentTimeMillis();
        List<Integer> result = solution.findSubstring(s, words);
        long endTime = System.currentTimeMillis();

        System.out.println("Large identical words test execution time: " + (endTime - startTime) + " ms");

        assertEquals(expected, result, "Should efficiently find all valid starting positions with identical characters");
    }
    

    @Test
    @DisplayName("Test with small repeated identical words for performance optimization")
    void testSmallRepeatedIdenticalWords() {
        // Small test to verify the optimization logic works correctly
        String s = "aaaaaa";
        String[] words = {"a", "a", "a"};
        List<Integer> expected = List.of(0, 1, 2, 3);

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result, "Should correctly handle small repeated identical words");
    }

    @Test
    @DisplayName("Test with repeated substrings: aaaaaa with [aaa, aaa]")
    void testRepeatedSubstrings() {
        String s = "aaaaaa";
        String[] words = {"aaa", "aaa"};
        List<Integer> expected = List.of(0);

        List<Integer> result = solution.findSubstring(s, words);

        assertEquals(expected, result,
                "Should find index 0 as the only valid position for concatenation of two 'aaa's");
    }

}