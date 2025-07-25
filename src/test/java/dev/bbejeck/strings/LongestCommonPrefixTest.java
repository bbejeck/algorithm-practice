package dev.bbejeck.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LongestCommonPrefixTest {

    private LongestCommonPrefix longestCommonPrefix;

    @BeforeEach
    void setUp() {
        longestCommonPrefix = new LongestCommonPrefix();
    }

    @Test
    @DisplayName("Test classic LeetCode example - flower, flow, flight")
    void testClassicExample() {
        String[] strs = {"flower", "flow", "flight"};
        assertEquals("fl", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test no common prefix - dog, racecar, car")
    void testNoCommonPrefix() {
        String[] strs = {"dog", "racecar", "car"};
        assertEquals("", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test single string")
    void testSingleString() {
        String[] strs = {"hello"};
        assertEquals("hello", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test all identical strings")
    void testAllIdenticalStrings() {
        String[] strs = {"test", "test", "test"};
        assertEquals("test", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test one string is prefix of others")
    void testOnePrefixOfOthers() {
        String[] strs = {"abc", "abcdef", "abcd"};
        assertEquals("abc", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test shortest string is the common prefix")
    void testShortestIsPrefix() {
        String[] strs = {"programming", "program", "programmer"};
        assertEquals("program", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test empty string in array")
    void testEmptyStringInArray() {
        String[] strs = {"abc", "", "ab"};
        assertEquals("", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test single character strings - same")
    void testSingleCharactersSame() {
        String[] strs = {"a", "a", "a"};
        assertEquals("a", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test single character strings - different")
    void testSingleCharactersDifferent() {
        String[] strs = {"a", "b", "c"};
        assertEquals("", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test two strings with common prefix")
    void testTwoStringsWithPrefix() {
        String[] strs = {"prefix", "pretext"};
        assertEquals("pre", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test two strings with no common prefix")
    void testTwoStringsNoPrefix() {
        String[] strs = {"hello", "world"};
        assertEquals("", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test case sensitivity")
    void testCaseSensitivity() {
        String[] strs = {"Test", "test", "TEST"};
        assertEquals("", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test with numbers in strings")
    void testWithNumbers() {
        String[] strs = {"abc123", "abc456", "abc789"};
        assertEquals("abc", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test with special characters")
    void testWithSpecialCharacters() {
        String[] strs = {"prefix@", "prefix#", "prefix$"};
        assertEquals("prefix", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test long strings with small prefix")
    void testLongStringsSmallPrefix() {
        String[] strs = {
            "averylongstringwithlotsofcharacters",
            "averylongstringwithmanymorecharacters",
            "averylongstringwithdifferentending"
        };
        assertEquals("averylongstringwith", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test strings with varying lengths")
    void testVaryingLengths() {
        String[] strs = {"a", "ab", "abc", "abcd"};
        assertEquals("a", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test first character mismatch")
    void testFirstCharacterMismatch() {
        String[] strs = {"apple", "banana", "cherry"};
        assertEquals("", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test whitespace handling")
    void testWhitespaceHandling() {
        String[] strs = {" test", " test", " test"};
        assertEquals(" test", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test mixed whitespace")
    void testMixedWhitespace() {
        String[] strs = {" test", "test", " test"};
        assertEquals("", longestCommonPrefix.longestCommonPrefix(strs));
    }

    @Test
    @DisplayName("Test array bounds - should throw exception for null array")
    void testNullArray() {
        assertThrows(Exception.class, () -> {
            longestCommonPrefix.longestCommonPrefix(null);
        });
    }

    @Test
    @DisplayName("Test LeetCode example 2")
    void testLeetCodeExample2() {
        String[] strs = {"ab", "a"};
        assertEquals("a", longestCommonPrefix.longestCommonPrefix(strs));
    }
}
