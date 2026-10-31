package dev.bbejeck.incremental;

import java.util.List;

/**
 * Read Timestamp Selection and Reclocking
 *
 * Background: Materialize doc/developer/overview.md ("Timestamp determination", "Frontier tracking")
 * and the blog "Consistency and Operational Confidence" (McSherry, Sep 2023). This is a simplified
 * model for practice, not Materialize's actual algorithm.
 *
 * Part 1, select(inputs, isolation, now):
 *   Each input collection is readable at times in [since, upper): times before since are compacted
 *   away, and times at or beyond upper are not complete yet. A query reads every input at one common
 *   timestamp. That timestamp must be >= every since (for correctness), and ideally < every upper
 *   (so the answer is ready without waiting).
 *   SERIALIZABLE: choose the latest timestamp that is ready for all inputs. If no such timestamp is
 *     >= every since, choose the smallest valid one instead, which will not be ready yet.
 *   STRICT_SERIALIZABLE: choose the smallest timestamp that is >= every since, >= now (the wall-clock
 *     time of the request), and >= every timestamp previously returned by a strict read or recorded
 *     with recordWrite. Strict timestamps never go backwards, even if it means the answer is not
 *     ready yet. Serializable reads do not affect later strict reads.
 *   Return the timestamp and whether it is ready (< every upper).
 *   Throw IllegalArgumentException for no inputs, or an input with since > upper.
 *   Be ready to explain what a user could observe if strict reads were allowed to go backwards.
 *
 * Part 2, Reclocker:
 *   Upstream data (one Kafka partition, or a Postgres log) has its own offsets 0, 1, 2, ...
 *   Reclocking maps them onto Materialize timestamps with a remap table of bindings.
 *   mint(timestamp, offsetUpper) adds the binding "every offset < offsetUpper is visible at
 *     timestamp". Timestamps must strictly increase and offsetUpper must not decrease, otherwise
 *     throw IllegalArgumentException. A binding with the same offsetUpper means "no new data".
 *   reclock(offset) returns the timestamp of the first binding whose offsetUpper > offset.
 *     Throw IllegalStateException if no binding covers it yet, and IllegalArgumentException for a
 *     negative offset.
 *   registerTransaction(first, last) declares that offsets first..last (inclusive) are one upstream
 *     transaction. A later mint whose offsetUpper would split it (first < offsetUpper <= last) must
 *     throw IllegalArgumentException, so a whole transaction always gets one timestamp. Registering a
 *     transaction with first > last, or with first below the current offsetUpper, also throws.
 *
 * Example:
 *   mint(10, 3), mint(20, 7)
 *   reclock(0) = 10, reclock(2) = 10, reclock(3) = 20, reclock(6) = 20, reclock(7) throws
 *   registerTransaction(8, 11); mint(30, 9) throws; mint(30, 12) ok; reclock(8..11) = 30
 *
 * Constraints: up to 10^5 bindings. reclock should be faster than a linear scan.
 */
public class TimestampSelector {

    public enum Isolation { STRICT_SERIALIZABLE, SERIALIZABLE }

    public record Frontier(long since, long upper) { }

    public record Selection(long timestamp, boolean ready) { }

    public Selection select(List<Frontier> inputs, Isolation isolation, long now) {
        throw new UnsupportedOperationException("TODO");
    }

    public void recordWrite(long timestamp) {
        throw new UnsupportedOperationException("TODO");
    }

    public static class Reclocker {

        public void mint(long timestamp, long offsetUpper) {
            throw new UnsupportedOperationException("TODO");
        }

        public long reclock(long offset) {
            throw new UnsupportedOperationException("TODO");
        }

        public void registerTransaction(long first, long last) {
            throw new UnsupportedOperationException("TODO");
        }
    }
}
