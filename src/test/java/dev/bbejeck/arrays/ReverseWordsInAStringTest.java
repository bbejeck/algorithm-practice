package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReverseWordsInAStringTest {

    private ReverseWordsInAString reverser;

    @BeforeEach
    void setUp() {
        reverser = new ReverseWordsInAString();
    }

    @Test
    void testSingleCharacter() {
        assertEquals("a", reverser.reverseWords("a"));
    }

    @Test
    void testSingleWord() {
        assertEquals("hello", reverser.reverseWords("hello"));
    }

    @Test
    void testMultipleSpacesAtStart() {
        assertEquals("world hello", reverser.reverseWords("   hello world"));
    }

    @Test
    void testMultipleSpacesAtEnd() {
        assertEquals("world hello", reverser.reverseWords("hello world   "));
    }

    @Test
    void testMultipleSpacesBetweenWords() {
        assertEquals("world hello", reverser.reverseWords("hello    world"));
    }

    @Test
    void testAllSpaces() {
        assertEquals("", reverser.reverseWords("   "));
    }

    @Test
    void testMultipleWordsWithMixedSpacing() {
        assertEquals("blue is sky the",
                reverser.reverseWords("the   sky is      blue"));
    }

    @Test
    void testMultipleWordsWithSingleSpacing() {
        assertEquals("blue is sky the",
                reverser.reverseWords("the sky is blue"));
    }

    @Test
    void testWordsWithSpecialCharacters() {
        assertEquals("world! hello", reverser.reverseWords("hello world!"));
        assertEquals("123 hello", reverser.reverseWords("hello 123"));
    }

    @Test
    void testSingleLetterWords() {
        assertEquals("c b a", reverser.reverseWords("a b c"));
    }

    @Test
    void testLongSentence() {
        assertEquals(
                "example complex very a is this",
                reverser.reverseWords("this is a very complex example")
        );
    }

    @Test
    void testCaseThreeFromSite(){
        assertEquals(
                "example good a",
                reverser.reverseWords("a good   example ")
        );
    }

    @Test
    void testCaseFourFromSite(){
        assertEquals(
                "f df asdasd",
                reverser.reverseWords(" asdasd df f")
        );
    }

    @Test
    void testExtremeSpacing() {
        assertEquals(
                "d c b a",
                reverser.reverseWords("   a    b    c    d   ")
        );
    }

    @Test
    void testMixedCaseWords() {
        assertEquals(
                "World HELLO hello",
                reverser.reverseWords("hello HELLO World")
        );
    }
    }