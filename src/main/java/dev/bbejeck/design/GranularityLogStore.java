package dev.bbejeck.design;

import java.util.List;

/**
 * Log Store with Granularity Queries (reported Datadog technical screen question, via PracHub)
 *
 * Timestamps are strings in the form "YYYY-MM-DD HH:MM:SS", always valid and zero-padded.
 *
 * add(id, timestamp) stores a log. IDs are unique. Logs can be added in any time order.
 *
 * query(start, end, granularity) returns the IDs of logs whose timestamp is in [start, end] when
 * every timestamp is cut down to the given granularity. With MONTH, "2020-05-20 10:20:30" is
 * treated as "2020-05". The bounds are cut down too, so start may be later than end at full
 * precision as long as start <= end at the granularity: a MONTH query from "2020-05-31 23:00:00"
 * to "2020-05-01 00:00:00" covers all of May.
 * Results are in chronological order. Logs with the same full timestamp keep the order they were
 * added in.
 *
 * Aim for O(log n + k) per query, where k is the number of results. Hint: fixed-width timestamps
 * sort the same way as strings and as times, so you can turn a query into a lower and upper bound
 * at full precision.
 *
 * Example 1:
 *   add(1, "2017-01-01 23:59:59"), add(2, "2017-01-02 00:00:00"), add(3, "2018-03-01 12:00:00")
 *   query("2017-01-01 00:00:00", "2017-12-31 23:59:59", YEAR)  -> [1, 2]
 *   query("2017-01-01 23:00:00", "2017-01-01 23:59:00", HOUR)  -> [1]
 *
 * Example 2:
 *   add(10, "2020-05-20 10:20:30"), add(11, "2020-05-01 00:00:00"), add(12, "2020-05-20 10:20:30")
 *   query("2020-05-01 00:00:00", "2020-05-31 23:59:59", MONTH) -> [11, 10, 12]
 *   query("2020-05-20 00:00:00", "2020-05-20 23:59:59", DAY)   -> [10, 12]
 *
 * Constraints: up to 10^5 operations.
 */
public class GranularityLogStore {

    public enum Granularity { YEAR, MONTH, DAY, HOUR, MINUTE, SECOND }

    public void add(int id, String timestamp) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Integer> query(String start, String end, Granularity granularity) {
        throw new UnsupportedOperationException("TODO");
    }
}
