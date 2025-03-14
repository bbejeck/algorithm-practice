package dev.bbejeck.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the ShortestPalindromeTwoPointer class
 */
public class ShortestPalindromeTwoPointerTest {

    private ShortestPalindromeTwoPointer palindromeFinder;

    @BeforeEach
    void setUp() {
        palindromeFinder = new ShortestPalindromeTwoPointer();
    }

    @Test
    void testEmptyString() {
        assertEquals("", palindromeFinder.shortestPalindrome(""));
    }

    @Test
    void testSingleCharacter() {
        assertEquals("a", palindromeFinder.shortestPalindrome("a"));
    }

    @Test
    void testAlreadyPalindrome() {
        assertEquals("aba", palindromeFinder.shortestPalindrome("aba"));
        assertEquals("racecar", palindromeFinder.shortestPalindrome("racecar"));
        assertEquals("aa", palindromeFinder.shortestPalindrome("aa"));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testShortestPalindrome(String input, String expected) {
        assertEquals(expected, palindromeFinder.shortestPalindrome(input));
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                // Example test cases
                Arguments.of("aacecaaa", "aaacecaaa"),
                Arguments.of("abcd", "dcbabcd"),

                // Cases where we add characters to the beginning
                Arguments.of("abc", "cbabc"),
                Arguments.of("aabba", "abbaabba"),
                Arguments.of("aabc", "cbaabc"),

                // Edge cases
                Arguments.of("a", "a"),
                Arguments.of("aa", "aa"),
                Arguments.of("ab", "bab"),

                // Longer examples
                Arguments.of("abcde", "edcbabcde"),
                Arguments.of("aaabaaaa", "aaaabaaaa"),
                Arguments.of("banana", "ananabanana"),

                // Cases with partial palindromes at the beginning
                Arguments.of("aabcd", "dcbaabcd"),
                Arguments.of("abacaba", "abacaba"),
                Arguments.of("abacabadabacaba", "abacabadabacaba"),

                // Special characters
                Arguments.of("a!b@c#", "#c@b!a!b@c#"),
                Arguments.of("12321abc", "cba12321abc")
        );
    }

    @Test
    void testWithRepeatedCharacters() {
        assertEquals("aaaaa", palindromeFinder.shortestPalindrome("aaaaa"));
        assertEquals("aaabaaa", palindromeFinder.shortestPalindrome("aaabaaa"));
        assertEquals("baaaaab", palindromeFinder.shortestPalindrome("aaaaab"));
    }

    @Test
    void testWithSpecialCases() {
        // Test with strings that have palindromic prefixes
        assertEquals("baab", palindromeFinder.shortestPalindrome("aab"));
        assertEquals("cbabc", palindromeFinder.shortestPalindrome("abc"));

        // Test with longer strings
        String longInput = "abcdefghijklmnopqrstuvwxyz";
        String expected = "zyxwvutsrqponmlkjihgfedcbabcdefghijklmnopqrstuvwxyz";
        assertEquals(expected, palindromeFinder.shortestPalindrome(longInput));
    }
}