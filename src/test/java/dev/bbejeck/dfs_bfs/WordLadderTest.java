package dev.bbejeck.dfs_bfs;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * WordLadderTest is designed to test the ladderLength method of the WordLadder class.
 * The ladderLength method calculates the shortest transformation sequence from the
 * beginWord to the endWord using words in the given wordList, following a specific
 * transformation rule - one letter change at a time, ensuring intermediate words are valid.
 */
public class WordLadderTest {

    @Test
    public void testLadderLength_BasicSuccessCase() {
        WordLadder wordLadder = new WordLadder();
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");

        int result = wordLadder.ladderLength(beginWord, endWord, wordList);

        assertEquals(5, result);
    }

    @Test
    public void testLadderLength_NoValidTransformation() {
        WordLadder wordLadder = new WordLadder();
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log");

        int result = wordLadder.ladderLength(beginWord, endWord, wordList);

        assertEquals(0, result);
    }

    @Test
    public void testLadderLength_SingleStepTransformation() {
        WordLadder wordLadder = new WordLadder();
        String beginWord = "a";
        String endWord = "c";
        List<String> wordList = Arrays.asList("a", "b", "c");

        int result = wordLadder.ladderLength(beginWord, endWord, wordList);

        assertEquals(2, result);
    }

    @Test
    public void testLadderLength_BeginWordNotInList() {
        WordLadder wordLadder = new WordLadder();
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");

        int result = wordLadder.ladderLength("invalid", endWord, wordList);

        assertEquals(0, result);
    }

    @Test
    public void testLadderLength_EndWordNotInList() {
        WordLadder wordLadder = new WordLadder();
        String beginWord = "hit";
        String endWord = "xyz";
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");

        int result = wordLadder.ladderLength(beginWord, endWord, wordList);

        assertEquals(0, result);
    }

    @Test
    public void testLadderLength_EmptyWordList() {
        WordLadder wordLadder = new WordLadder();
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = Collections.emptyList();

        int result = wordLadder.ladderLength(beginWord, endWord, wordList);

        assertEquals(0, result);
    }

    @Test
    public void testLadderLength_SingleLetterWords() {
        WordLadder wordLadder = new WordLadder();
        String beginWord = "a";
        String endWord = "c";
        List<String> wordList = Arrays.asList("a", "b", "c");

        int result = wordLadder.ladderLength(beginWord, endWord, wordList);

        assertEquals(2, result);
    }
    
}