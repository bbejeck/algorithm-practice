package dev.bbejeck.data_stream;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Merge Sorted Runs and Compaction (LSM-tree style)
 *
 * Part 1, mergeSorted(iterators, comparator):
 *   Each iterator returns elements in ascending order. Return one iterator over all of them in
 *   ascending order. It must stream: hold at most one element from each input at a time, never
 *   collect everything into a list. Equal elements come out in the order of their input iterators.
 *   next() past the end throws NoSuchElementException.
 *
 * Part 2, compact(runs, dropTombstones):
 *   Each run is a sorted file from a key-value store. An entry has a key, a sequence number and a
 *   value. A null value is a tombstone that records a delete.
 *   Within a run, keys are unique and ascending. Across runs a key can appear many times, and the
 *   entry with the highest sequence number is the current one.
 *   Return a streaming iterator, ascending by key, with only the current entry for each key.
 *   If dropTombstones is true, keys whose current entry is a tombstone are left out entirely.
 *   Be ready to explain when it's safe to drop tombstones, and what goes wrong if you drop them too early.
 *
 * Example:
 *   run 1: (a, 1, "x"),  (c, 2, "y")
 *   run 2: (a, 5, "x2"), (b, 4, null)
 *   run 3: (b, 3, "z"),  (c, 6, null)
 *   keep tombstones: (a, 5, "x2"), (b, 4, null), (c, 6, null)
 *   drop tombstones: (a, 5, "x2")
 *
 * Constraints: up to 1,000 runs, each of any length. Sequence numbers are unique.
 */
public class MergeSortedRunsCompaction {

    public record Entry(String key, long seq, String value) {
        public boolean isTombstone() {
            return value == null;
        }
    }

    public <T> Iterator<T> mergeSorted(List<Iterator<T>> iterators, Comparator<? super T> comparator) {
        throw new UnsupportedOperationException("TODO");
    }

    public Iterator<Entry> compact(List<Iterator<Entry>> runs, boolean dropTombstones) {
        throw new UnsupportedOperationException("TODO");
    }
}
