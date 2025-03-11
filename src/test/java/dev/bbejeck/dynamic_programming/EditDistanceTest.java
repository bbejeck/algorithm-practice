package dev.bbejeck.dynamic_programming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditDistanceTest {

    private EditDistance editDistance;

    @BeforeEach
    public void setup() {
        editDistance = new EditDistance();
    }

    @Test
    public void testIdenticalStrings() {
        assertEquals(0, editDistance.minDistance("hello", "hello"));
    }

    @Test
    public void testEmptyFirstString() {
        assertEquals(5, editDistance.minDistance("", "world"));
    }

    @Test
    public void testEmptySecondString() {
        assertEquals(5, editDistance.minDistance("hello", ""));
    }

    @Test
    public void testBothEmptyStrings() {
        assertEquals(0, editDistance.minDistance("", ""));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testEditDistance(String word1, String word2, int expected) {
        assertEquals(expected, editDistance.minDistance(word1, word2));
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                // Classic examples
                Arguments.of("horse", "ros", 3),
                Arguments.of("intention", "execution", 5),
                Arguments.of("kitten", "sitting", 3),
                Arguments.of("sunday", "saturday", 3),

                // Edge cases
                Arguments.of("a", "b", 1),
                Arguments.of("abc", "bcd", 2),

                // Cases with insertions
                Arguments.of("abc", "abcd", 1),
                Arguments.of("abc", "xabc", 1),

                // Cases with deletions
                Arguments.of("abcd", "abc", 1),
                Arguments.of("xabc", "abc", 1),

                // Cases with substitutions
                Arguments.of("abc", "abd", 1),
                Arguments.of("xbc", "abc", 1),

                // Case requiring mixed operations
                Arguments.of("algorithm", "rhythm", 6)
        );
    }
}