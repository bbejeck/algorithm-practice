package dev.bbejeck.incremental;

import java.util.List;
import java.util.Map;

/**
 * Incremental Equi-Join over Arrangements
 *
 * Background: Materialize doc/developer/overview.md and the "Shared Arrangements" paper (McSherry et al.,
 * VLDB 2020). An arrangement is an index of a collection that also keeps its history, so it can be
 * read as of a past time. Joins read their inputs from arrangements.
 *
 * Part 1, Arrangement:
 *   add(key, value, time, diff) records that (key, value) gained diff copies at time.
 *   lookup(key, time) returns value -> multiplicity for that key, summing every diff at times <= time.
 *   Leave out values whose multiplicity is 0. An unknown key returns an empty map.
 *
 * Part 2, DeltaJoin.step(time, dA, dB):
 *   A and B are collections of (key, value) rows. Each step applies a batch of changes to both at
 *   the given time, and returns the change to A JOIN B ON key. Each output row is
 *   (key, leftValue, rightValue, diff), where diff is the product of the input multiplicities.
 *   Use:   d(A JOIN B) = dA JOIN B_before  +  A_after JOIN dB
 *   where B_before is B as of before this step and A_after includes dA. Look these up from the
 *   arrangements rather than scanning everything. Be ready to explain why this formula counts
 *   dA JOIN dB exactly once.
 *   Return consolidated output: no zero diffs, one row per (key, leftValue, rightValue), sorted by
 *   key, then leftValue, then rightValue.
 *   Times must strictly increase across calls, otherwise throw IllegalArgumentException.
 *   Input changes never make a row's multiplicity negative.
 *
 * Example:
 *   step(0, dA=[(k,a1,+1)], dB=[])          -> []
 *   step(1, dA=[],          dB=[(k,b1,+1)]) -> [(k,a1,b1,+1)]
 *   step(2, dA=[(k,a2,+2)], dB=[])          -> [(k,a2,b1,+2)]
 *   step(3, dA=[],          dB=[(k,b1,-1)]) -> [(k,a1,b1,-1), (k,a2,b1,-2)]
 *
 * Constraints: up to 10^4 steps, up to 10^3 changes per step.
 */
public class DeltaJoin {

    public record Change(String key, String value, long diff) { }

    public record Output(String key, String left, String right, long diff) { }

    public static class Arrangement {

        public void add(String key, String value, long time, long diff) {
            throw new UnsupportedOperationException("TODO");
        }

        public Map<String, Long> lookup(String key, long time) {
            throw new UnsupportedOperationException("TODO");
        }
    }

    public List<Output> step(long time, List<Change> dA, List<Change> dB) {
        throw new UnsupportedOperationException("TODO");
    }
}
