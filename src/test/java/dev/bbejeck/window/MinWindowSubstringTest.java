package dev.bbejeck.window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinWindowSubstringTest {

    private MinWindowSubstring minWindowSubstring;

    @BeforeEach
    void setUp() {
        minWindowSubstring = new MinWindowSubstring();
    }

    @Test
    @DisplayName("Test with standard LeetCode example")
    void testLeetCodeExample() {
        String s = "ADOBECODEBANC";
        String t = "ABC";
        String expected = "BANC";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should find the minimum window substring containing all characters from t");
    }

    @Test
    @DisplayName("Test when t is longer than s")
    void testTLongerThanS() {
        String s = "a";
        String t = "ab";
        String expected = "";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should return empty string when t is longer than s");
    }

    @Test
    @DisplayName("Test when s equals t")
    void testSEqualsT() {
        String s = "abc";
        String t = "abc";

        assertEquals(s, minWindowSubstring.minWindow(s, t),
                "Should return the entire string when s equals t");
    }

    @Test
    @DisplayName("Test with single character inputs")
    void testSingleCharacter() {
        String s = "a";
        String t = "a";

        assertEquals(s, minWindowSubstring.minWindow(s, t),
                "Should handle single character inputs correctly");
    }

    @Test
    @DisplayName("Test with no valid window")
    void testNoValidWindow() {
        String s = "ADOBECODEBANC";
        String t = "XYZ";
        String expected = "";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should return empty string when no valid window exists");
    }

    @Test
    @DisplayName("Test with repeated characters in t")
    void testRepeatedCharactersInT() {
        String s = "ADOBECODEBANC";
        String t = "AAC";
        String expected = "ADOBECODEBA";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should handle repeated characters in t correctly");
    }
    @Test
    @DisplayName("Test with LeetCode example 2")
    void testLeetCodeExample2() {
        String s = "a";
        String t = "a";
        String expected = "a";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should correctly handle single character match");
    }

    @Test
    @DisplayName("Test with larger t and multiple occurrences")
    void testLargerTWithMultipleOccurrences() {
        String s = "ADOBECODEBANC";
        String t = "AABC";
        String expected = "ADOBECODEBA";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should handle larger t with multiple character occurrences");
    }

    @Test
    @DisplayName("Test with window at start of string")
    void testWindowAtStart() {
        String s = "ABCDEFG";
        String t = "ABC";
        String expected = "ABC";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should find window at the start of the string");
    }

    @Test
    @DisplayName("Test with window at end of string")
    void testWindowAtEnd() {
        String s = "DEFGABC";
        String t = "ABC";
        String expected = "ABC";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should find window at the end of the string");
    }

    @Test
    @DisplayName("Test with all characters the same")
    void testAllCharactersSame() {
        String s = "AAAAAAA";
        String t = "AA";
        String expected = "AA";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should handle strings with all the same character");
    }

    @Test
    @DisplayName("Test with alternating characters")
    void testAlternatingCharacters() {
        String s = "ABABABABABA";
        String t = "AABB";
        String expected = "ABAB";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should handle strings with alternating patterns");
    }

    @Test
    @DisplayName("Test with exactly one occurrence of each character needed")
    void testExactlyOneOccurrence() {
        String s = "XAYBZC";
        String t = "ABC";
        String expected = "AYBZC";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should find minimum window when exactly one occurrence of each character is needed");
    }

    @Test
    @DisplayName("Test with characters in reverse order")
    void testCharactersInReverseOrder() {
        String s = "ZYXCBA";
        String t = "ABC";
        String expected = "CBA";

        assertEquals(expected, minWindowSubstring.minWindow(s, t),
                "Should find minimum window when characters appear in reverse order");
    }


}