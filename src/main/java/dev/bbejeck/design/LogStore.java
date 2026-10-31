package dev.bbejeck.design;

import java.util.List;

/**
 * Log Storage and Querying (reported Datadog technical screen question, via PracHub)
 *
 * Each log has a millisecond timestamp, a severity, a serviceId and a message.
 *
 * Part 1, append / count / get:
 *   append(log) adds a log. Logs arrive in non-decreasing timestamp order. Throw
 *     IllegalArgumentException if a timestamp goes backwards.
 *   count(start, end, filter) returns the number of logs with start <= timestamp <= end that match
 *     the filter. A null severity or null serviceId in the filter means "any".
 *   get(start, end, filter, ascending, limit, offset) skips the first offset matching logs, then
 *     returns up to limit of them, ordered by timestamp. Ascending keeps logs with equal timestamps
 *     in append order; descending returns them in reverse append order.
 *     Don't build the full list of matches just to skip offset of them.
 *   Find the time window with binary search, not a scan from the start.
 *
 * Part 2, indexes:
 *   count must answer in O(log n) when the filter has no fields, only a severity, only a serviceId,
 *   or both. Scanning the time window is too slow for the performance tests.
 *   Be ready to discuss what each extra index costs on append (write amplification) and in memory.
 *
 * Example:
 *   append (100, INFO, "api", "start"), (200, ERROR, "db", "timeout"), (200, WARN, "api", "slow"),
 *          (300, ERROR, "api", "500")
 *   count(100, 250, any)                 -> 3
 *   count(0, 1000, severity ERROR)       -> 2
 *   get(0, 1000, service "api", descending, limit 2, offset 0)  -> the 300 log, then the (200, WARN) log
 *
 * Constraints: up to 2 * 10^5 operations, 0 <= timestamp <= 10^13.
 */
public class LogStore {

    public enum Severity { INFO, WARN, ERROR }

    public record Log(long timestamp, Severity severity, String serviceId, String message) { }

    public record Filter(Severity severity, String serviceId) {
        public static final Filter ANY = new Filter(null, null);
    }

    public void append(Log log) {
        throw new UnsupportedOperationException("TODO");
    }

    public int count(long start, long end, Filter filter) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Log> get(long start, long end, Filter filter, boolean ascending, int limit, int offset) {
        throw new UnsupportedOperationException("TODO");
    }
}
