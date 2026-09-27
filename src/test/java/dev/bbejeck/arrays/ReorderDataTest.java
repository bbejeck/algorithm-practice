package dev.bbejeck.arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReorderData class
 */
public class ReorderDataTest {

    private final ReorderData reorderData = new ReorderData();

    @Test
    @DisplayName("Test with example 1: mixed letter and digit logs")
    void testExample1() {
        String[] logs = {"dig1 8 1 5 1", "let1 art can", "dig2 3 6", "let2 own kit dig", "let3 art zero"};
        String[] expected = {"let1 art can", "let3 art zero", "let2 own kit dig", "dig1 8 1 5 1", "dig2 3 6"};
        assertArrayEquals(expected, reorderData.reorderLogFiles(logs));
    }

    @Test
    @DisplayName("Test with example 2: mixed letter and digit logs")
    void testExample2() {
        String[] logs = {"a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"};
        String[] expected = {"g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"};
        assertArrayEquals(expected, reorderData.reorderLogFiles(logs));
    }

    @Test
    @DisplayName("Test letter logs with identical content are ordered by identifier")
    void testTieBrokenByIdentifier() {
        String[] logs = {"let3 art can", "let1 art can", "let2 art can"};
        String[] expected = {"let1 art can", "let2 art can", "let3 art can"};
        assertArrayEquals(expected, reorderData.reorderLogFiles(logs));
    }

    @Test
    @DisplayName("Test digit logs keep their original relative order")
    void testDigitLogsStable() {
        String[] logs = {"d3 9 9", "d1 1 1", "d2 5 5"};
        String[] expected = {"d3 9 9", "d1 1 1", "d2 5 5"};
        assertArrayEquals(expected, reorderData.reorderLogFiles(logs));
    }

    @Test
    @DisplayName("Test only letter logs are sorted by content")
    void testOnlyLetterLogs() {
        String[] logs = {"b zeta", "a beta", "c alpha"};
        String[] expected = {"c alpha", "a beta", "b zeta"};
        assertArrayEquals(expected, reorderData.reorderLogFiles(logs));
    }

    @Test
    @DisplayName("Test identifiers containing digits do not affect log type")
    void testIdentifierWithDigits() {
        String[] logs = {"123 1 2", "456 abc"};
        String[] expected = {"456 abc", "123 1 2"};
        assertArrayEquals(expected, reorderData.reorderLogFiles(logs));
    }

    @Test
    @DisplayName("Test single log is returned unchanged")
    void testSingleLog() {
        String[] logs = {"id1 hello world"};
        assertArrayEquals(logs, reorderData.reorderLogFiles(logs));
    }

    @Test
    @DisplayName("Test empty input returns empty array")
    void testEmptyInput() {
        assertEquals(0, reorderData.reorderLogFiles(new String[]{}).length);
    }
}
