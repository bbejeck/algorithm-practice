package dev.bbejeck.dynamic_programming;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LongestPalindromicSubstringTest {

    private final LongestPalindromicSubstring palindromeFinder = new LongestPalindromicSubstring();

    @Test
    void testNullInput() {
        assertEquals("", palindromeFinder.longestPalindrome(null));
    }

    @Test
    void testEmptyInput() {
        assertEquals("", palindromeFinder.longestPalindrome(""));
    }

    @Test
    void testSingleCharacter() {
        assertEquals("a", palindromeFinder.longestPalindrome("a"));
    }

    @ParameterizedTest
    @MethodSource("providePalindromeTestCases")
    void testLongestPalindrome(String input, String expected) {
        assertEquals(expected, palindromeFinder.longestPalindrome(input));
    }

    private static Stream<Arguments> providePalindromeTestCases() {
        return Stream.of(
                // Even length palindromes
                Arguments.of("babad", "aba"),
                Arguments.of("cbbd", "bb"),
                Arguments.of("abccba", "abccba"),
                Arguments.of("aabbcc", "cc"),  // First occurrence is returned

                // Odd length palindromes
                Arguments.of("racecar", "racecar"),
                Arguments.of("level", "level"),
                Arguments.of("hello", "ll"),
                Arguments.of("aacabdkacaa", "aca"),

                // Mixed cases
                Arguments.of("civilwartestingwhetherthatnaptionoranynartionsoconceivedandsodedicatedcanlongendureWeareqmetonagreatbattlefiemldoftzhatwarWehavecometodedicpateaportionofthatfieldasafinalrestingplaceforthosewhoheregavetheirlivesthatthatnationmightliveItisaltogetherfangandproperthatweshoulddothisButinalargersensewecannotdedicatewecannotconsecratewecannothallowthisgroundThebravelmenlivinganddeadwhostruggledherehaveconsecrateditfaraboveourpoorponwertoaddordetractTgheworldadswfilllittlenotlenorlongrememberwhatwesayherebutitcanneverforgetwhattheydidhereItisforusthelivingrathertobededicatedheretotheulnfinishedworkwhichtheywhofoughtherehavethusfarsonoblyadvancedItisratherforustobeherededicatedtothegreattdafskremainingbeforeusthatfromthesehonoreddeadwetakeincreaseddevotiontothatcauseforwhichtheygavethelastpfullmeasureofdevotionthatweherehighlyresolvethatthesedeadshallnothavediedinvainthatthisnationunsderGodshallhaveanewbirthoffreedomandthatgovernmentofthepeoplebythepeopleforthepeopleshallnotperishfromtheearth", "ranynar"),
                Arguments.of("aaaabbaaaa", "aaaabbaaaa")
        );
    }

    @Test
    void testAllSameCharacters() {
        assertEquals("aaaaa", palindromeFinder.longestPalindrome("aaaaa"));
    }

    @Test
    void testPalindromeAtBoundaries() {
        assertEquals("aba", palindromeFinder.longestPalindrome("abad"));
        assertEquals("aba", palindromeFinder.longestPalindrome("daba"));
    }

    @Test
    void testOverlappingPalindromes() {
        // In "aabaa", there are overlapping palindromes "aa" and "aba"
        // The longest one "aabaa" should be found
        assertEquals("aabaa", palindromeFinder.longestPalindrome("aabaa"));
    }
}