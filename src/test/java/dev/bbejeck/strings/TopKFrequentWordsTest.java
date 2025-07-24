
package dev.bbejeck.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TopKFrequentWords
 * Tests various scenarios including edge cases and typical use cases
 */
class TopKFrequentWordsTest {

    private TopKFrequentWords topKFrequentWords;

    @BeforeEach
    void setUp() {
        topKFrequentWords = new TopKFrequentWords();
    }

    @Test
    @DisplayName("Test basic functionality - LeetCode example 1")
    void testBasicFunctionality() {
        String[] words = {"i", "love", "leetcode", "i", "love", "coding"};
        int k = 2;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        List<String> expected = List.of("i", "love");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test lexicographical ordering - LeetCode example 2")
    void testLexicographicalOrdering() {
        String[] words = {"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"};
        int k = 4;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        List<String> expected = List.of("the", "is", "sunny", "day");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test single word repeated")
    void testSingleWordRepeated() {
        String[] words = {"hello", "hello", "hello"};
        int k = 1;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        List<String> expected = List.of("hello");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test all words have same frequency")
    void testAllWordsSameFrequency() {
        String[] words = {"apple", "banana", "cherry", "date"};
        int k = 2;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // Should return first 2 in lexicographical order
        List<String> expected = List.of("apple", "banana");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test k equals array length")
    void testKEqualsArrayLength() {
        String[] words = {"a", "b", "c"};
        int k = 3;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        List<String> expected = List.of("a", "b", "c");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test k equals 1")
    void testKEqualsOne() {
        String[] words = {"word", "test", "word", "test", "word"};
        int k = 1;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        List<String> expected = List.of("word");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test mixed case words")
    void testMixedCaseWords() {
        String[] words = {"Apple", "apple", "APPLE", "banana", "Apple"};
        int k = 2;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // Each case variant is treated as different word
        // "Apple" appears 2 times, others appear 1 time each
        // So "Apple" should be first, then lexicographically first among others
        List<String> expected = List.of("Apple", "APPLE");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test single character words")
    void testSingleCharacterWords() {
        String[] words = {"a", "b", "a", "c", "a", "b"};
        int k = 2;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // "a" appears 3 times, "b" appears 2 times, "c" appears 1 time
        List<String> expected = List.of("a", "b");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test empty strings")
    void testEmptyStrings() {
        String[] words = {"", "hello", "", "world", ""};
        int k = 2;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // Empty string appears 3 times, others appear 1 time each
        // Empty string comes first lexicographically
        List<String> expected = List.of("", "hello");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test longer words with same frequency")
    void testLongerWordsWithSameFrequency() {
        String[] words = {"programming", "algorithm", "data", "structure", "programming", "algorithm"};
        int k = 3;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // "programming" and "algorithm" appear 2 times each
        // "data" and "structure" appear 1 time each
        // Expected: most frequent first, then lexicographical
        List<String> expected = List.of("algorithm", "programming", "data");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test result size matches k")
    void testResultSizeMatchesK() {
        String[] words = {"a", "b", "c", "d", "e", "f", "g"};
        int k = 3;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        assertEquals(k, result.size(), "Result size should match k");

        // All have same frequency, so should be first k in lexicographical order
        List<String> expected = List.of("a", "b", "c");
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("provideTopKTestCases")
    @DisplayName("Parameterized test cases")
    void testParameterizedCases(String[] words, int k, List<String> expected, String description) {
        List<String> result = topKFrequentWords.topKFrequent(words, k);
        assertEquals(expected, result, description);
    }

    private static Stream<Arguments> provideTopKTestCases() {
        return Stream.of(
                Arguments.of(
                        new String[]{"hello"},
                        1,
                        List.of("hello"),
                        "Single word"
                ),
                Arguments.of(
                        new String[]{"a", "a", "b"},
                        1,
                        List.of("a"),
                        "Most frequent only"
                ),
                Arguments.of(
                        new String[]{"x", "y", "z"},
                        2,
                        List.of("x", "y"),
                        "Lexicographical with same frequency"
                ),
                Arguments.of(
                        new String[]{"test", "case", "test"},
                        2,
                        List.of("test", "case"),
                        "Mixed frequencies"
                )
        );
    }

    @Test
    @DisplayName("Test frequency ordering is correct")
    void testFrequencyOrderingCorrect() {
        String[] words = {"rare", "common", "common", "frequent", "frequent", "frequent"};
        int k = 3;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // "frequent" (3), "common" (2), "rare" (1)
        List<String> expected = List.of("frequent", "common", "rare");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test lexicographical tie-breaking")
    void testLexicographicalTieBreaking() {
        String[] words = {"zebra", "apple", "zebra", "apple", "banana", "banana"};
        int k = 3;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // All have frequency 2, so lexicographical order: apple, banana, zebra
        List<String> expected = List.of("apple", "banana", "zebra");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test result integrity")
    void testResultIntegrity() {
        String[] words = {"test", "case", "test", "example", "case"};
        int k = 2;
        List<String> result = topKFrequentWords.topKFrequent(words, k);

        // Verify result is not null
        assertNotNull(result, "Result should not be null");

        // Verify result size
        assertEquals(k, result.size(), "Result size should match k");

        // Verify no null elements
        assertFalse(result.contains(null), "Result should not contain null elements");

        // Verify no duplicates
        assertEquals(result.size(), result.stream().distinct().count(),
                "Result should not contain duplicates");

        // Verify all elements are from input
        List<String> uniqueInputWords = Arrays.stream(words).distinct().toList();
        assertTrue(uniqueInputWords.containsAll(result),
                "All result words should be from input");
    }
}