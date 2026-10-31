package dev.bbejeck.incremental;

import java.util.List;
import java.util.Map;

/**
 * Incrementally Maintained GROUP BY Aggregates
 *
 * Background: Materialize blog "Freshness and Operational Autonomy" (McSherry, Oct 2023). Views are
 * kept current by turning input changes into output changes. MIN/MAX with retractions is called
 * out as a hard case.
 *
 * Input is a stream of batches, one batch per timestamp. Each Change(key, value, diff) means the
 * row (key, value) gained diff copies (negative diff = rows deleted). Within a batch, changes may
 * arrive in any order, but after the whole batch no (key, value) has a negative count.
 *
 * update(batch) applies the batch and returns how the aggregate result changed, as Output rows:
 *   - a group whose result changed from r1 to r2:  (key, r1, -1) then (key, r2, +1)
 *   - a group that appeared with result r:          (key, r, +1)
 *   - a group that disappeared (no rows left):      (key, r, -1)
 *   - a group whose result did not change emits nothing, even if its rows changed.
 * Outputs are sorted by key; for one key the -1 comes before the +1.
 * current() returns key -> result for every group that has at least one row.
 *
 * Part 1, COUNT and SUM:
 *   COUNT is the number of rows in the group. SUM is the sum of value * multiplicity.
 *   A group exists while it has rows, so a SUM of 0 is still a result.
 *
 * Part 2, MIN and MAX:
 *   Deleting the current minimum must find the next one without rescanning the whole group.
 *   Duplicate values count: deleting one of two copies of the minimum leaves it unchanged.
 *   Be ready to give the cost of update() per change.
 *
 * Example (MIN):
 *   update [(a,3,+1), (a,5,+1), (a,9,+1)]  ->  [(a,3,+1)]
 *   update [(a,3,-1)]                       ->  [(a,3,-1), (a,5,+1)]
 *   update [(a,9,-1)]                       ->  []
 *   update [(a,5,-1)]                       ->  [(a,5,-1)]
 *
 * Constraints: up to 10^5 changes per batch, up to 10^5 distinct values per group.
 */
public class IncrementalAggregates {

    public enum Kind { COUNT, SUM, MIN, MAX }

    public record Change(String key, long value, long diff) { }

    public record Output(String key, long result, long diff) { }

    public IncrementalAggregates(Kind kind) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Output> update(List<Change> batch) {
        throw new UnsupportedOperationException("TODO");
    }

    public Map<String, Long> current() {
        throw new UnsupportedOperationException("TODO");
    }
}
