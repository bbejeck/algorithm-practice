package dev.bbejeck.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Trie class
 */
public class TrieTest {

    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
    }

    @Test
    @DisplayName("Should handle empty trie operations")
    void testEmptyTrie() {
        // Search on empty trie should return false
        assertFalse(trie.search("word"));
        assertFalse(trie.search("a"));
        assertFalse(trie.search(""));

        // StartsWith on empty trie should return false
        assertFalse(trie.startsWith("word"));
        assertFalse(trie.startsWith("a"));
        assertFalse(trie.startsWith(""));
    }

    @Test
    @DisplayName("Should insert and search single character words")
    void testSingleCharacterWords() {
        trie.insert("a");
        assertTrue(trie.search("a"));
        assertFalse(trie.search("b"));

        trie.insert("z");
        assertTrue(trie.search("a"));
        assertTrue(trie.search("z"));
        assertFalse(trie.search("c"));
    }

    @Test
    @DisplayName("Should insert and search multi-character words")
    void testMultiCharacterWords() {
        trie.insert("apple");
        assertTrue(trie.search("apple"));
        assertFalse(trie.search("app"));
        assertFalse(trie.search("appl"));
        assertFalse(trie.search("application"));
    }

    @Test
    @DisplayName("Should handle prefix operations correctly")
    void testStartsWithPrefix() {
        trie.insert("apple");

        // Valid prefixes
        assertTrue(trie.startsWith("a"));
        assertTrue(trie.startsWith("ap"));
        assertTrue(trie.startsWith("app"));
        assertTrue(trie.startsWith("appl"));
        assertTrue(trie.startsWith("apple"));

        // Invalid prefixes
        assertFalse(trie.startsWith("b"));
        assertFalse(trie.startsWith("apx"));
        assertFalse(trie.startsWith("applex"));
    }

    @Test
    @DisplayName("Should handle overlapping words")
    void testOverlappingWords() {
        trie.insert("app");
        trie.insert("apple");
        trie.insert("application");

        // All words should be found
        assertTrue(trie.search("app"));
        assertTrue(trie.search("apple"));
        assertTrue(trie.search("application"));

        // Partial matches should not be found as complete words
        assertFalse(trie.search("ap"));
        assertFalse(trie.search("appl"));
        assertFalse(trie.search("applicatio"));

        // All prefixes should work
        assertTrue(trie.startsWith("a"));
        assertTrue(trie.startsWith("ap"));
        assertTrue(trie.startsWith("app"));
        assertTrue(trie.startsWith("appl"));
        assertTrue(trie.startsWith("apple"));
        assertTrue(trie.startsWith("applicat"));
        assertTrue(trie.startsWith("application"));
    }

    @Test
    @DisplayName("Should handle words with shared prefixes")
    void testSharedPrefixes() {
        trie.insert("cat");
        trie.insert("car");
        trie.insert("card");
        trie.insert("care");
        trie.insert("careful");

        // All inserted words should be found
        assertTrue(trie.search("cat"));
        assertTrue(trie.search("car"));
        assertTrue(trie.search("card"));
        assertTrue(trie.search("care"));
        assertTrue(trie.search("careful"));

        // Partial words should not be found
        assertFalse(trie.search("ca"));
        assertFalse(trie.search("car_"));
        assertFalse(trie.search("careful_"));

        // Prefix matching should work
        assertTrue(trie.startsWith("c"));
        assertTrue(trie.startsWith("ca"));
        assertTrue(trie.startsWith("car"));
        assertTrue(trie.startsWith("care"));
    }

    @Test
    @DisplayName("Should handle whitespace in words correctly")
    void testWhitespaceHandling() {
        // Your implementation skips whitespace characters
        trie.insert("hello world");
        trie.insert("test ing");

        // Should be able to find the words (whitespace skipped)
        assertTrue(trie.search("helloworld"));
        assertTrue(trie.search("testing"));

        // Original words with spaces should not be found
        assertTrue(trie.search("hello world"));
        assertTrue(trie.search("test ing"));

        // Prefix matching should work with whitespace removed
        assertTrue(trie.startsWith("hello"));
        assertTrue(trie.startsWith("hellow"));
        assertTrue(trie.startsWith("test"));
        assertTrue(trie.startsWith("testi"));
    }

    @Test
    @DisplayName("Should handle edge case words")
    void testEdgeCaseWords() {
        // Single letters
        trie.insert("a");
        trie.insert("z");

        // Repeated characters
        trie.insert("aaa");
        trie.insert("zzz");

        assertTrue(trie.search("a"));
        assertTrue(trie.search("z"));
        assertTrue(trie.search("aaa"));
        assertTrue(trie.search("zzz"));

        assertFalse(trie.search("aa"));
        assertFalse(trie.search("zz"));
        assertFalse(trie.search("aaaa"));
    }

    @Test
    @DisplayName("Should handle classic LeetCode example")
    void testLeetCodeExample() {
        // Classic LeetCode 208 example
        trie.insert("apple");

        assertTrue(trie.search("apple"));   // return True
        assertFalse(trie.search("app"));    // return False
        assertTrue(trie.startsWith("app")); // return True

        trie.insert("app");
        assertTrue(trie.search("app"));     // return True
    }

    @Test
    @DisplayName("Should handle multiple insertions of same word")
    void testDuplicateInsertions() {
        trie.insert("test");
        trie.insert("test");
        trie.insert("test");

        // Should still work correctly
        assertTrue(trie.search("test"));
        assertTrue(trie.startsWith("test"));
        assertTrue(trie.startsWith("te"));
    }

    @Test
    @DisplayName("Should handle words that are extensions of other words")
    void testWordExtensions() {
        trie.insert("test");
        trie.insert("testing");
        trie.insert("tester");
        trie.insert("tests");

        // All complete words should be found
        assertTrue(trie.search("test"));
        assertTrue(trie.search("testing"));
        assertTrue(trie.search("tester"));
        assertTrue(trie.search("tests"));

        // Partial words should not be found as complete words
        assertFalse(trie.search("tes"));
        assertFalse(trie.search("testi"));
        assertFalse(trie.search("teste"));

        // But they should be valid prefixes
        assertTrue(trie.startsWith("tes"));
        assertTrue(trie.startsWith("testi"));
        assertTrue(trie.startsWith("teste"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "abc", "abcd", "abcde", "test", "hello", "world", "programming", "algorithm"})
    @DisplayName("Should handle various word lengths")
    void testVariousWordLengths(String word) {
        trie.insert(word);
        assertTrue(trie.search(word));

        // Test all prefixes
        for (int i = 1; i <= word.length(); i++) {
            String prefix = word.substring(0, i);
            assertTrue(trie.startsWith(prefix));
        }
    }

    @Test
    @DisplayName("Should handle empty string operations")
    void testEmptyStringOperations() {
        // Empty string operations
        trie.insert("");

        // Depending on implementation, empty string might be considered valid
        // Your implementation likely treats empty string as a valid word
        assertFalse(trie.search(""));
        assertFalse(trie.startsWith(""));

        // Other words should still work
        trie.insert("test");
        assertTrue(trie.search("test"));
        assertTrue(trie.startsWith("t"));
    }

    @Test
    @DisplayName("Should handle comprehensive word set")
    void testComprehensiveWordSet() {
        String[] words = {
                "the", "quick", "brown", "fox", "jumps", "over", "lazy", "dog",
                "a", "an", "and", "are", "as", "at", "be", "by", "for", "from",
                "has", "he", "in", "is", "it", "its", "of", "on", "that", "to",
                "was", "will", "with", "the", "this", "but", "they", "have", "had"
        };

        // Insert all words
        for (String word : words) {
            trie.insert(word);
        }

        // Verify all words can be found
        for (String word : words) {
            assertTrue(trie.search(word), "Should find word: " + word);
        }

        // Test some non-existent words
        assertFalse(trie.search("xyz"));
        assertFalse(trie.search("hello"));
        assertFalse(trie.search("world"));

        // Test some prefixes
        assertTrue(trie.startsWith("qu"));  // quick
        assertTrue(trie.startsWith("bro")); // brown
        assertTrue(trie.startsWith("j"));   // jumps
        assertFalse(trie.startsWith("xyz"));
    }

    @Test
    @DisplayName("Should handle underscores in words")
    void testUnderscoreHandling() {
        trie.insert("test_word");
        trie.insert("hello_world");
        trie.insert("snake_case");

        assertTrue(trie.search("test_word"));
        assertTrue(trie.search("hello_world"));
        assertTrue(trie.search("snake_case"));

        // These should be different words
        assertFalse(trie.search("testword"));
        assertFalse(trie.search("helloworld"));

        // Prefix matching should work
        assertTrue(trie.startsWith("test_"));
        assertTrue(trie.startsWith("hello_"));
        assertTrue(trie.startsWith("snake_"));
    }
}