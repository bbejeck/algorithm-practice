package dev.bbejeck.data_stream;

import dev.bbejeck.data_stream.MergeSortedRunsCompaction.Entry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MergeSortedRunsCompaction
 */
public class MergeSortedRunsCompactionTest {

    private final MergeSortedRunsCompaction merger = new MergeSortedRunsCompaction();

    private static Entry e(String key, long seq, String value) {
        return new Entry(key, seq, value);
    }

    private static <T> List<T> drain(Iterator<T> it) {
        List<T> result = new ArrayList<>();
        it.forEachRemaining(result::add);
        return result;
    }

    @SafeVarargs
    private static <T> List<Iterator<T>> iterators(List<T>... lists) {
        List<Iterator<T>> result = new ArrayList<>();
        for (List<T> list : lists) {
            result.add(list.iterator());
        }
        return result;
    }

    // Counts how many elements have been read, to check the merge streams
    private static final class CountingIterator<T> implements Iterator<T> {
        private final Iterator<T> delegate;
        int reads = 0;

        CountingIterator(Iterator<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public T next() {
            reads++;
            return delegate.next();
        }
    }

    // ---------------- part 1: mergeSorted ----------------

    @Test
    @DisplayName("Merges sorted iterators into one sorted iterator")
    void testMerge() {
        Iterator<Integer> merged = merger.mergeSorted(
                iterators(List.of(1, 4, 7), List.of(2, 5, 8), List.of(3, 6, 9)), Comparator.naturalOrder());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), drain(merged));
    }

    @Test
    @DisplayName("Handles no iterators and empty iterators")
    void testEmptyInputs() {
        assertFalse(merger.mergeSorted(new ArrayList<Iterator<Integer>>(), Comparator.naturalOrder()).hasNext());
        Iterator<Integer> merged = merger.mergeSorted(
                iterators(List.of(), List.of(2), List.of()), Comparator.<Integer>naturalOrder());
        assertEquals(List.of(2), drain(merged));
    }

    @Test
    @DisplayName("Keeps duplicates")
    void testDuplicates() {
        Iterator<Integer> merged = merger.mergeSorted(
                iterators(List.of(1, 1, 3), List.of(1, 3)), Comparator.naturalOrder());
        assertEquals(List.of(1, 1, 1, 3, 3), drain(merged));
    }

    @Test
    @DisplayName("Equal elements come out in input iterator order")
    void testStableAcrossInputs() {
        // Compare only the number, so "1b" and "1a" are equal
        Comparator<String> byNumber = Comparator.comparing(s -> s.charAt(0));
        Iterator<String> merged = merger.mergeSorted(
                iterators(List.of("1b", "1b2", "2b"), List.of("1a", "2a")), byNumber);
        assertEquals(List.of("1b", "1b2", "1a", "2b", "2a"), drain(merged));
    }

    @Test
    @DisplayName("Streams: reads only one element ahead per input")
    void testStreams() {
        CountingIterator<Integer> first = new CountingIterator<>(List.of(1, 3, 5, 7).iterator());
        CountingIterator<Integer> second = new CountingIterator<>(List.of(2, 4, 6, 8).iterator());
        Iterator<Integer> merged = merger.mergeSorted(List.of(first, second), Comparator.naturalOrder());
        assertTrue(first.reads <= 1 && second.reads <= 1, "read ahead before any next()");
        assertEquals(1, merged.next());
        assertEquals(2, merged.next());
        // After returning 1 and 2, each input needs at most its next element (3 and 4) buffered
        assertTrue(first.reads <= 2 && second.reads <= 2, "read too far ahead");
    }

    @Test
    @DisplayName("next() past the end throws")
    void testNextPastEnd() {
        Iterator<Integer> merged = merger.mergeSorted(iterators(List.of(1)), Comparator.naturalOrder());
        merged.next();
        assertThrows(NoSuchElementException.class, merged::next);
    }

    @Test
    @DisplayName("Merging 1,000 random runs matches sorting everything")
    void testManyRuns() {
        Random random = new Random(42);
        List<Integer> all = new ArrayList<>();
        List<Iterator<Integer>> runs = new ArrayList<>();
        for (int r = 0; r < 1_000; r++) {
            List<Integer> run = new ArrayList<>();
            int size = random.nextInt(200);
            for (int i = 0; i < size; i++) {
                run.add(random.nextInt(10_000));
            }
            run.sort(null);
            all.addAll(run);
            runs.add(run.iterator());
        }
        all.sort(null);
        assertEquals(all, drain(merger.mergeSorted(runs, Comparator.naturalOrder())));
    }

    // ---------------- part 2: compact ----------------

    private static List<List<Entry>> exampleRuns() {
        return List.of(
                List.of(e("a", 1, "x"), e("c", 2, "y")),
                List.of(e("a", 5, "x2"), e("b", 4, null)),
                List.of(e("b", 3, "z"), e("c", 6, null)));
    }

    private List<Entry> compact(List<List<Entry>> runs, boolean dropTombstones) {
        List<Iterator<Entry>> iterators = new ArrayList<>();
        for (List<Entry> run : runs) {
            iterators.add(run.iterator());
        }
        return drain(merger.compact(iterators, dropTombstones));
    }

    @Test
    @DisplayName("Example: keep tombstones")
    void testCompactKeepTombstones() {
        assertEquals(List.of(e("a", 5, "x2"), e("b", 4, null), e("c", 6, null)), compact(exampleRuns(), false));
    }

    @Test
    @DisplayName("Example: drop tombstones")
    void testCompactDropTombstones() {
        assertEquals(List.of(e("a", 5, "x2")), compact(exampleRuns(), true));
    }

    @Test
    @DisplayName("Highest sequence number wins no matter which run it's in")
    void testHighestSeqWins() {
        List<List<Entry>> runs = List.of(
                List.of(e("k", 3, "mid")),
                List.of(e("k", 9, "newest")),
                List.of(e("k", 1, "oldest")));
        assertEquals(List.of(e("k", 9, "newest")), compact(runs, false));
    }

    @Test
    @DisplayName("A put newer than a delete brings the key back")
    void testPutAfterDelete() {
        List<List<Entry>> runs = List.of(
                List.of(e("k", 2, null)),
                List.of(e("k", 1, "old"), e("m", 4, "m")),
                List.of(e("k", 3, "back")));
        assertEquals(List.of(e("k", 3, "back"), e("m", 4, "m")), compact(runs, true));
    }

    @Test
    @DisplayName("Dropping tombstones can leave nothing at all")
    void testAllDeleted() {
        List<List<Entry>> runs = List.of(
                List.of(e("a", 1, "x"), e("b", 2, "y")),
                List.of(e("a", 3, null), e("b", 4, null)));
        List<Entry> result = compact(runs, true);
        assertEquals(List.of(), result);
        assertFalse(merger.compact(List.of(runs.get(1).iterator()), true).hasNext());
    }

    @Test
    @DisplayName("A single run passes through unchanged")
    void testSingleRun() {
        List<Entry> run = List.of(e("a", 1, "x"), e("b", 2, null), e("c", 3, "z"));
        assertEquals(run, compact(List.of(run), false));
    }

    @Test
    @DisplayName("No runs and empty runs")
    void testEmptyRuns() {
        assertEquals(List.of(), compact(List.of(), false));
        assertEquals(List.of(e("a", 1, "x")), compact(List.of(List.of(), List.of(e("a", 1, "x")), List.of()), true));
    }

    @Test
    @DisplayName("Compacting random runs matches the latest write per key from a reference map")
    void testMatchesReference() {
        Random random = new Random(3);
        long seq = 0;
        java.util.TreeMap<String, Entry> latest = new java.util.TreeMap<>();
        List<List<Entry>> runs = new ArrayList<>();
        for (int r = 0; r < 50; r++) {
            java.util.TreeMap<String, Entry> run = new java.util.TreeMap<>();
            for (int i = 0; i < 100; i++) {
                String key = "key" + random.nextInt(500);
                Entry entry = e(key, ++seq, random.nextInt(4) == 0 ? null : "v" + seq);
                run.put(key, entry);
                latest.put(key, entry);
            }
            runs.add(new ArrayList<>(run.values()));
        }
        // Shuffle run order so newest data isn't always in the last run
        java.util.Collections.shuffle(runs, random);

        assertEquals(new ArrayList<>(latest.values()), compact(runs, false));
        List<Entry> live = latest.values().stream().filter(entry -> !entry.isTombstone()).toList();
        assertEquals(live, compact(runs, true));
    }
}
