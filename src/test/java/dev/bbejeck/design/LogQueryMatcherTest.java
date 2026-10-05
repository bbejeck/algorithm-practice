package dev.bbejeck.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LogQueryMatcher
 */
public class LogQueryMatcherTest {

    private final LogQueryMatcher matcher = new LogQueryMatcher();

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        List<String> output = matcher.process(List.of(
                "Q: database error",
                "L: Database ERROR on host1",
                "Q: host1",
                "L: host1 database ok",
                "L: error database host1"));

        assertEquals(List.of(
                "ACK: database error; ID=1",
                "M: Database ERROR on host1; Q=1",
                "ACK: host1; ID=2",
                "M: host1 database ok; Q=2",
                "M: error database host1; Q=1,2"), output);
    }

    @Test
    @DisplayName("Empty input produces no output")
    void testEmptyInput() {
        assertEquals(List.of(), matcher.process(List.of()));
    }

    @Test
    @DisplayName("Queries are acknowledged with IDs starting at 1")
    void testQueryIdsStartAtOne() {
        List<String> output = matcher.process(List.of("Q: a", "Q: b", "Q: c"));
        assertEquals(List.of("ACK: a; ID=1", "ACK: b; ID=2", "ACK: c; ID=3"), output);
    }

    @Test
    @DisplayName("Logs with no registered queries produce no output")
    void testLogBeforeAnyQuery() {
        assertEquals(List.of(), matcher.process(List.of("L: something happened")));
    }

    @Test
    @DisplayName("A log that matches no query produces no output")
    void testNoMatch() {
        List<String> output = matcher.process(List.of("Q: disk full", "L: disk ok"));
        assertEquals(List.of("ACK: disk full; ID=1"), output);
    }

    @Test
    @DisplayName("Every query word must appear in the log")
    void testAllWordsRequired() {
        List<String> output = matcher.process(List.of(
                "Q: timeout retry",
                "L: timeout on request",
                "L: retry after timeout"));
        assertEquals(List.of(
                "ACK: timeout retry; ID=1",
                "M: retry after timeout; Q=1"), output);
    }

    @Test
    @DisplayName("Matching is case-insensitive in both directions")
    void testCaseInsensitive() {
        List<String> output = matcher.process(List.of(
                "Q: Hello WORLD",
                "L: hello world",
                "L: HELLO World again"));
        assertEquals(List.of(
                "ACK: Hello WORLD; ID=1",
                "M: hello world; Q=1",
                "M: HELLO World again; Q=1"), output);
    }

    @Test
    @DisplayName("Only whole words match, not substrings")
    void testWholeWordsOnly() {
        List<String> output = matcher.process(List.of(
                "Q: err",
                "L: error happened",
                "L: terrible day",
                "L: err happened"));
        assertEquals(List.of(
                "ACK: err; ID=1",
                "M: err happened; Q=1"), output);
    }

    @Test
    @DisplayName("Word order does not matter")
    void testWordOrderIgnored() {
        List<String> output = matcher.process(List.of("Q: a b c", "L: c b a"));
        assertEquals(List.of("ACK: a b c; ID=1", "M: c b a; Q=1"), output);
    }

    @Test
    @DisplayName("A repeated query word only needs to appear once in the log")
    void testDuplicateQueryWords() {
        List<String> output = matcher.process(List.of("Q: fail fail", "L: fail now"));
        assertEquals(List.of("ACK: fail fail; ID=1", "M: fail now; Q=1"), output);
    }

    @Test
    @DisplayName("A query does not match logs that came before it")
    void testQueryOnlyMatchesLaterLogs() {
        List<String> output = matcher.process(List.of(
                "L: cpu high",
                "Q: cpu",
                "L: cpu high"));
        assertEquals(List.of("ACK: cpu; ID=1", "M: cpu high; Q=1"), output);
    }

    @Test
    @DisplayName("Matched query IDs are listed in ascending order")
    void testIdsAscending() {
        List<String> output = matcher.process(List.of(
                "Q: x y",
                "Q: y",
                "Q: x",
                "L: y x"));
        assertEquals(List.of(
                "ACK: x y; ID=1",
                "ACK: y; ID=2",
                "ACK: x; ID=3",
                "M: y x; Q=1,2,3"), output);
    }

    @Test
    @DisplayName("Query words don't need to be next to each other in the log")
    void testQueryWordsNotAdjacent() {
        List<String> output = matcher.process(List.of("Q: disk timeout", "L: disk read timeout"));
        assertEquals(List.of("ACK: disk timeout; ID=1", "M: disk read timeout; Q=1"), output);
    }

    @Test
    @DisplayName("IDs stay ascending when identical queries are interleaved with others")
    void testIdsAscendingWithRepeatedQueries() {
        List<String> output = matcher.process(List.of("Q: a", "Q: b", "Q: a", "L: a b"));
        assertEquals(List.of("ACK: a; ID=1", "ACK: b; ID=2", "ACK: a; ID=3", "M: a b; Q=1,2,3"), output);
    }

    @Test
    @DisplayName("Identical queries get separate IDs and both match")
    void testIdenticalQueries() {
        List<String> output = matcher.process(List.of("Q: oom", "Q: oom", "L: oom killer"));
        assertEquals(List.of("ACK: oom; ID=1", "ACK: oom; ID=2", "M: oom killer; Q=1,2"), output);
    }

    @Test
    @DisplayName("Multiple spaces between words; original text is echoed unchanged")
    void testMultipleSpaces() {
        List<String> output = matcher.process(List.of("Q: a  b", "L: b   a"));
        assertEquals(List.of("ACK: a  b; ID=1", "M: b   a; Q=1"), output);
    }

    @Test
    @DisplayName("Words with digits match exactly")
    void testDigits() {
        List<String> output = matcher.process(List.of("Q: host1", "L: host10 down", "L: host1 down"));
        assertEquals(List.of("ACK: host1; ID=1", "M: host1 down; Q=1"), output);
    }

    @Test
    @DisplayName("Many queries: a log containing every word matches all of them")
    void testManyQueries() {
        List<String> lines = new ArrayList<>();
        StringBuilder log = new StringBuilder("L:");
        StringBuilder ids = new StringBuilder();
        for (int i = 1; i <= 1_000; i++) {
            lines.add("Q: w" + i);
            log.append(" w").append(i);
            ids.append(i == 1 ? "" : ",").append(i);
        }
        lines.add(log.toString());

        List<String> output = matcher.process(lines);
        assertEquals(1_001, output.size());
        assertEquals("ACK: w1000; ID=1000", output.get(999));
        assertEquals("M: " + log.substring(3) + "; Q=" + ids, output.get(1_000));
    }
}
