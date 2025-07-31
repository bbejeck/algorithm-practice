package dev.bbejeck.strings;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class WordBreakTest {

    @Test
    void wordBreak_returnsTrue_whenStringCanBeSegmented() {
        WordBreak wordBreak = new WordBreak();
        String s = "leetcode";
        // "leet" + "code" exists in wordDict
        assertTrue(wordBreak.wordBreak(s, Arrays.asList("leet", "code")));
    }

    @Test
    void wordBreak_returnsFalse_whenStringCannotBeSegmented() {
        WordBreak wordBreak = new WordBreak();
        String s = "applepenapple";
        // cannot be split into valid words with only "cats", "dog", "sand", "and"
        assertFalse(wordBreak.wordBreak(s, Arrays.asList("cats", "dog", "sand", "and")));
    }

    @Test
    void wordBreak_returnsTrue_withOverlappingWords() {
        WordBreak wordBreak = new WordBreak();
        String s = "applepenapple";
        assertTrue(wordBreak.wordBreak(s, Arrays.asList("apple", "pen")));
    }

    @Test
    void wordBreak_returnsFalse_whenNoWordsMatch() {
        WordBreak wordBreak = new WordBreak();
        String s = "abc";
        assertFalse(wordBreak.wordBreak(s, Arrays.asList("a", "b")));
    }

    @Test
    void wordBreak_returnsTrue_forEmptyString() {
        WordBreak wordBreak = new WordBreak();
        String s = "";
        assertTrue(wordBreak.wordBreak(s, Collections.singletonList("anyword"))); // empty string is always "breakable"
    }

    @Test
    void wordBreak_returnsFalse_forEmptyWordDict() {
        WordBreak wordBreak = new WordBreak();
        String s = "anything";
        assertFalse(wordBreak.wordBreak(s, Collections.emptyList()));
    }
}