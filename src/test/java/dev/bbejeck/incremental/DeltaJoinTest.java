package dev.bbejeck.incremental;

import dev.bbejeck.incremental.DeltaJoin.Arrangement;
import dev.bbejeck.incremental.DeltaJoin.Change;
import dev.bbejeck.incremental.DeltaJoin.Output;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DeltaJoin
 */
public class DeltaJoinTest {

    private static Change c(String key, String value, long diff) {
        return new Change(key, value, diff);
    }

    private static Output o(String key, String left, String right, long diff) {
        return new Output(key, left, right, diff);
    }

    // Part 1: Arrangement

    @Test
    @DisplayName("Lookup accumulates diffs as of a time")
    void testArrangementLookup() {
        Arrangement arr = new Arrangement();
        arr.add("k", "x", 1, 1);
        arr.add("k", "y", 2, 2);
        arr.add("k", "x", 3, -1);
        arr.add("j", "z", 1, 1);
        assertEquals(Map.of(), arr.lookup("k", 0));
        assertEquals(Map.of("x", 1L), arr.lookup("k", 1));
        assertEquals(Map.of("x", 1L, "y", 2L), arr.lookup("k", 2));
        assertEquals(Map.of("y", 2L), arr.lookup("k", 3));
        assertEquals(Map.of("y", 2L), arr.lookup("k", 100));
        assertEquals(Map.of("z", 1L), arr.lookup("j", 5));
    }

    @Test
    @DisplayName("Unknown keys and fully cancelled values")
    void testArrangementEmpty() {
        Arrangement arr = new Arrangement();
        assertEquals(Map.of(), arr.lookup("missing", 10));
        arr.add("k", "x", 1, 1);
        arr.add("k", "x", 1, -1);
        assertEquals(Map.of(), arr.lookup("k", 1));
    }

    @Test
    @DisplayName("Updates added out of time order")
    void testArrangementOutOfOrder() {
        Arrangement arr = new Arrangement();
        arr.add("k", "x", 5, 1);
        arr.add("k", "y", 2, 1);
        assertEquals(Map.of("y", 1L), arr.lookup("k", 3));
        assertEquals(Map.of("x", 1L, "y", 1L), arr.lookup("k", 5));
    }

    // Part 2: DeltaJoin

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        DeltaJoin join = new DeltaJoin();
        assertEquals(List.of(), join.step(0, List.of(c("k", "a1", 1)), List.of()));
        assertEquals(List.of(o("k", "a1", "b1", 1)), join.step(1, List.of(), List.of(c("k", "b1", 1))));
        assertEquals(List.of(o("k", "a2", "b1", 2)), join.step(2, List.of(c("k", "a2", 2)), List.of()));
        assertEquals(List.of(o("k", "a1", "b1", -1), o("k", "a2", "b1", -2)),
                join.step(3, List.of(), List.of(c("k", "b1", -1))));
    }

    @Test
    @DisplayName("Both sides change in the same step: counted once")
    void testSameStep() {
        DeltaJoin join = new DeltaJoin();
        assertEquals(List.of(o("j", "x", "y", 1)), join.step(0, List.of(c("j", "x", 1)), List.of(c("j", "y", 1))));
        assertEquals(List.of(o("j", "x", "y", -1)), join.step(1, List.of(c("j", "x", -1)), List.of(c("j", "y", -1))));
    }

    @Test
    @DisplayName("Multiplicities multiply")
    void testMultiplicities() {
        DeltaJoin join = new DeltaJoin();
        assertEquals(List.of(o("k", "a", "b", 6)), join.step(0, List.of(c("k", "a", 2)), List.of(c("k", "b", 3))));
    }

    @Test
    @DisplayName("Keys only match themselves; output is sorted")
    void testKeysAndOrdering() {
        DeltaJoin join = new DeltaJoin();
        join.step(0, List.of(c("p", "a2", 1), c("p", "a1", 1), c("q", "a3", 1)), List.of());
        assertEquals(List.of(o("p", "a1", "b1", 1), o("p", "a1", "b2", 1), o("p", "a2", "b1", 1), o("p", "a2", "b2", 1)),
                join.step(1, List.of(), List.of(c("p", "b2", 1), c("p", "b1", 1), c("r", "b3", 1))));
    }

    @Test
    @DisplayName("Changes that cancel within a step produce nothing")
    void testCancellation() {
        DeltaJoin join = new DeltaJoin();
        join.step(0, List.of(), List.of(c("k", "b", 1)));
        assertEquals(List.of(), join.step(1, List.of(c("k", "a", 1), c("k", "a", -1)), List.of()));
    }

    @Test
    @DisplayName("Times must strictly increase")
    void testTimeOrder() {
        DeltaJoin join = new DeltaJoin();
        join.step(5, List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> join.step(5, List.of(), List.of()));
        assertThrows(IllegalArgumentException.class, () -> join.step(4, List.of(), List.of()));
        join.step(6, List.of(), List.of());
    }

    @Test
    @DisplayName("Random steps: accumulated output always equals the full join")
    void testRandomAgainstRecompute() {
        Random random = new Random(42);
        String[] keys = {"k1", "k2", "k3"};
        String[] values = {"v1", "v2", "v3", "v4"};
        DeltaJoin join = new DeltaJoin();
        Map<List<String>, Long> a = new HashMap<>();
        Map<List<String>, Long> b = new HashMap<>();
        Map<Output, Long> accumulated = new HashMap<>();
        for (long time = 0; time < 300; time++) {
            List<Change> dA = randomChanges(random, keys, values, a);
            List<Change> dB = randomChanges(random, keys, values, b);
            List<Output> out = join.step(time, dA, dB);
            for (int i = 1; i < out.size(); i++) {
                Output p = out.get(i - 1);
                Output q = out.get(i);
                int cmp = p.key().compareTo(q.key());
                if (cmp == 0) cmp = p.left().compareTo(q.left());
                if (cmp == 0) cmp = p.right().compareTo(q.right());
                assertTrue(cmp < 0, "output sorted and consolidated at time " + time);
            }
            for (Output row : out) {
                assertNotEquals(0, row.diff());
                accumulated.merge(o(row.key(), row.left(), row.right(), 0), row.diff(), Long::sum);
            }
            accumulated.values().removeIf(v -> v == 0);
            assertEquals(fullJoin(a, b), accumulated, "join contents at time " + time);
        }
    }

    private static List<Change> randomChanges(Random random, String[] keys, String[] values, Map<List<String>, Long> state) {
        List<Change> changes = new ArrayList<>();
        int n = random.nextInt(4);
        for (int i = 0; i < n; i++) {
            List<List<String>> present = new ArrayList<>(state.keySet());
            if (!present.isEmpty() && random.nextInt(3) == 0) {
                List<String> row = present.get(random.nextInt(present.size()));
                changes.add(c(row.get(0), row.get(1), -1));
                state.merge(row, -1L, Long::sum);
                state.values().removeIf(v -> v == 0);
            } else {
                List<String> row = List.of(keys[random.nextInt(keys.length)], values[random.nextInt(values.length)]);
                long diff = 1 + random.nextInt(2);
                changes.add(c(row.get(0), row.get(1), diff));
                state.merge(row, diff, Long::sum);
            }
        }
        return changes;
    }

    private static Map<Output, Long> fullJoin(Map<List<String>, Long> a, Map<List<String>, Long> b) {
        Map<Output, Long> result = new HashMap<>();
        a.forEach((ra, ma) -> b.forEach((rb, mb) -> {
            if (ra.get(0).equals(rb.get(0))) {
                result.merge(o(ra.get(0), ra.get(1), rb.get(1), 0), ma * mb, Long::sum);
            }
        }));
        return result;
    }
}
