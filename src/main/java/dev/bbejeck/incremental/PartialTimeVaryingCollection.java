package dev.bbejeck.incremental;

import java.util.List;
import java.util.Map;

/**
 * Partial Time-Varying Collection (since / upper frontiers)
 *
 * Background: Materialize doc/developer/platform/formalism.md. A collection is stored as updates
 * (data, time, diff). It is only accurate for times in [since, upper). Appending advances upper,
 * compaction advances since.
 *
 * Times here are totally ordered longs. A new collection has since = 0 and upper = 0, so no time is
 * readable until something is appended.
 *
 * Part 1, append / read / updates:
 *   append(updates, newUpper) adds a batch and moves upper to newUpper.
 *     Throw IllegalArgumentException if newUpper < upper, or if any update's time is outside
 *     [upper, newUpper). A rejected batch must leave the collection unchanged.
 *     An empty batch is a legal way to advance upper ("nothing changed up to here").
 *   read(t) returns data -> multiplicity, summing the diffs of every update with time <= t.
 *     Leave out data whose multiplicity is 0. Throw IllegalArgumentException unless since <= t < upper.
 *   updates() returns the consolidated updates: at most one update per (data, time), no zero diffs,
 *     sorted by time and then data.
 *
 * Part 2, compact(newSince):
 *   Advance every update's time to max(time, newSince), then consolidate. Set since = newSince.
 *   Reads at any t >= newSince must return exactly what they returned before. Reads before newSince
 *   must now throw. Throw IllegalArgumentException if newSince < since or newSince > upper.
 *   Be ready to explain why this loses no information for t >= since, and why it saves memory.
 *
 * Example:
 *   append [(a,0,+1), (b,1,+1), (a,2,-1), (c,3,+1)], newUpper 5
 *   read(1) = {a=1, b=1}   read(2) = {b=1}   read(5) throws
 *   compact(2) -> updates() = [(b,2,+1), (c,3,+1)]   ((a,2,+1) and (a,2,-1) cancel)
 *   read(2) = {b=1}   read(1) throws
 *
 * Constraints: up to 10^5 updates. Diffs can be any non-zero long.
 */
public class PartialTimeVaryingCollection {

    public record Update(String data, long time, long diff) { }

    public long since() {
        throw new UnsupportedOperationException("TODO");
    }

    public long upper() {
        throw new UnsupportedOperationException("TODO");
    }

    public void append(List<Update> updates, long newUpper) {
        throw new UnsupportedOperationException("TODO");
    }

    public Map<String, Long> read(long t) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Update> updates() {
        throw new UnsupportedOperationException("TODO");
    }

    public void compact(long newSince) {
        throw new UnsupportedOperationException("TODO");
    }
}
