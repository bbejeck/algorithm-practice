package dev.bbejeck.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Log Query Matcher
 *
 * You receive a stream of lines. Each line is either a query or a log:
 *
 *   "Q: <words>"   registers a query
 *   "L: <words>"   a log message
 *
 * Words are separated by one or more spaces and contain only letters and digits.
 *
 * Implement process(lines), which handles the lines in order and returns the output lines:
 *
 *   - For each query, output "ACK: <query text>; ID=<id>"
 *       Query IDs start at 1 and go up by 1 for each query.
 *   - For each log, output "M: <log text>; Q=<id>,<id>,..."
 *       listing every query registered so far whose words ALL appear in the log.
 *       IDs are in ascending order. If no query matches, output nothing for that log.
 *
 * <query text> and <log text> are everything after "Q: " / "L: ", unchanged.
 *
 * Matching rules:
 *   - Case-insensitive: "Hello" matches "hello".
 *   - Whole words only: "err" does not match "error".
 *   - Word order does not matter, and a log may contain extra words.
 *   - A word repeated in a query only needs to appear once in the log.
 *   - A log only matches queries that came before it.
 *
 * Example
 *   input:
 *     "Q: database error"
 *     "L: Database ERROR on host1"
 *     "Q: host1"
 *     "L: host1 database ok"
 *     "L: error database host1"
 *   output:
 *     "ACK: database error; ID=1"
 *     "M: Database ERROR on host1; Q=1"
 *     "ACK: host1; ID=2"
 *     "M: host1 database ok; Q=2"
 *     "M: error database host1; Q=1,2"
 */
public class LogQueryMatcher {

    public List<String> process(List<String> lines) {
        String qInd = "Q: ";

        List<Set<String>> queries = new ArrayList<>();
        List<String> results = new ArrayList<>();
        for (String line : lines) {
            if (line.contains(qInd)) {
                String qText = line.substring(3);
                Set<String> query = toSet(qText);;
                queries.add(query);
                results.add("ACK: " + qText + "; ID=" + queries.size());
            } else {
                String logText = line.substring(3);
                Set<String> normalizedLog = toSet(logText);
                List<String> ids = new ArrayList<>();


                for (int i = 0; i < queries.size(); i++) {
                    if (normalizedLog.containsAll(queries.get(i))) {
                        ids.add(String.valueOf(i + 1));
                    }
                }
                if (!ids.isEmpty()) {
                    results.add("M: " + logText +"; Q=" + String.join(",", ids));
                }
            }
        }

        return results;
    }


    public Set<String> toSet(String str) {
        String lower = str.toLowerCase();
        String[] words = lower.split("\\s+");
        return new HashSet<>(Arrays.asList(words));
    }
}
