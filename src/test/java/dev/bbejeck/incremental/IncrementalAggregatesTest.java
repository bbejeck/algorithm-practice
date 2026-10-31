package dev.bbejeck.incremental;

import dev.bbejeck.incremental.IncrementalAggregates.Change;
import dev.bbejeck.incremental.IncrementalAggregates.Kind;
import dev.bbejeck.incremental.IncrementalAggregates.Output;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for IncrementalAggregates
 */
public class IncrementalAggregatesTest {

    private static Change c(String key, long value, long diff) {
        return new Change(key, value, diff);
    }

    private static Output o(String key, long result, long diff) {
        return new Output(key, result, diff);
    }

    // Part 1: COUNT and SUM

    @Test
    @DisplayName("COUNT: groups appear, change, and disappear")
    void testCount() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.COUNT);
        assertEquals(List.of(o("a", 2, 1), o("b", 1, 1)),
                agg.update(List.of(c("b", 5, 1), c("a", 10, 1), c("a", 20, 1))));
        assertEquals(List.of(o("a", 2, -1), o("a", 1, 1)), agg.update(List.of(c("a", 10, -1))));
        assertEquals(List.of(o("b", 1, -1)), agg.update(List.of(c("b", 5, -1))));
        assertEquals(Map.of("a", 1L), agg.current());
    }

    @Test
    @DisplayName("COUNT: unchanged results emit nothing")
    void testCountNoChange() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.COUNT);
        assertEquals(List.of(), agg.update(List.of(c("a", 1, 1), c("a", 1, -1))));
        assertEquals(Map.of(), agg.current());
        agg.update(List.of(c("a", 1, 1)));
        assertEquals(List.of(), agg.update(List.of(c("a", 7, 1), c("a", 1, -1))));
        assertEquals(List.of(), agg.update(List.of()));
        assertEquals(Map.of("a", 1L), agg.current());
    }

    @Test
    @DisplayName("COUNT: multiplicities greater than one")
    void testCountMultiplicity() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.COUNT);
        assertEquals(List.of(o("a", 3, 1)), agg.update(List.of(c("a", 1, 3))));
        assertEquals(List.of(o("a", 3, -1), o("a", 1, 1)), agg.update(List.of(c("a", 1, -2))));
    }

    @Test
    @DisplayName("SUM: value times multiplicity")
    void testSum() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.SUM);
        assertEquals(List.of(o("a", 50, 1)), agg.update(List.of(c("a", 10, 1), c("a", 20, 2))));
        assertEquals(List.of(o("a", 50, -1), o("a", 40, 1)), agg.update(List.of(c("a", 10, -1))));
        assertEquals(Map.of("a", 40L), agg.current());
    }

    @Test
    @DisplayName("SUM: a group summing to zero still exists")
    void testSumZero() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.SUM);
        assertEquals(List.of(o("z", 0, 1)), agg.update(List.of(c("z", 5, 1), c("z", -5, 1))));
        assertEquals(Map.of("z", 0L), agg.current());
        assertEquals(List.of(o("z", 0, -1)), agg.update(List.of(c("z", 5, -1), c("z", -5, -1))));
        assertEquals(Map.of(), agg.current());
    }

    @Test
    @DisplayName("SUM: retraction inside the same batch as insertion, out of order")
    void testSumOutOfOrderBatch() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.SUM);
        agg.update(List.of(c("a", 4, 1)));
        assertEquals(List.of(o("a", 4, -1), o("a", 6, 1)),
                agg.update(List.of(c("a", 4, -1), c("a", 6, 1))));
    }

    @Test
    @DisplayName("Random COUNT and SUM agree with full recompute")
    void testRandomCountSum() {
        randomAgainstBruteForce(Kind.COUNT, 21);
        randomAgainstBruteForce(Kind.SUM, 22);
    }

    // Part 2: MIN and MAX

    @Test
    @DisplayName("MIN: deleting the minimum finds the next one")
    void testMin() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.MIN);
        assertEquals(List.of(o("a", 3, 1)), agg.update(List.of(c("a", 3, 1), c("a", 5, 1), c("a", 9, 1))));
        assertEquals(List.of(o("a", 3, -1), o("a", 5, 1)), agg.update(List.of(c("a", 3, -1))));
        assertEquals(List.of(), agg.update(List.of(c("a", 9, -1))));
        assertEquals(List.of(o("a", 5, -1)), agg.update(List.of(c("a", 5, -1))));
        assertEquals(Map.of(), agg.current());
    }

    @Test
    @DisplayName("MIN: duplicate values")
    void testMinDuplicates() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.MIN);
        agg.update(List.of(c("a", 2, 2), c("a", 8, 1)));
        assertEquals(List.of(), agg.update(List.of(c("a", 2, -1))));
        assertEquals(List.of(o("a", 2, -1), o("a", 8, 1)), agg.update(List.of(c("a", 2, -1))));
    }

    @Test
    @DisplayName("MIN: a new smaller value")
    void testMinNewSmaller() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.MIN);
        agg.update(List.of(c("a", 5, 1)));
        assertEquals(List.of(o("a", 5, -1), o("a", -1, 1)), agg.update(List.of(c("a", -1, 1))));
    }

    @Test
    @DisplayName("MAX: deleting the maximum finds the next one")
    void testMax() {
        IncrementalAggregates agg = new IncrementalAggregates(Kind.MAX);
        assertEquals(List.of(o("a", 9, 1), o("b", 1, 1)),
                agg.update(List.of(c("a", 3, 1), c("b", 1, 1), c("a", 9, 1))));
        assertEquals(List.of(o("a", 9, -1), o("a", 3, 1)), agg.update(List.of(c("a", 9, -1))));
        assertEquals(List.of(), agg.update(List.of(c("a", 1, 1))));
        assertEquals(Map.of("a", 3L, "b", 1L), agg.current());
    }

    @Test
    @DisplayName("Random MIN and MAX agree with full recompute")
    void testRandomMinMax() {
        randomAgainstBruteForce(Kind.MIN, 23);
        randomAgainstBruteForce(Kind.MAX, 24);
    }

    @Test
    @DisplayName("MIN: deleting minimums one at a time does not rescan the group")
    void testMinPerformance() {
        int n = 100_000;
        IncrementalAggregates agg = new IncrementalAggregates(Kind.MIN);
        List<Change> inserts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            inserts.add(c("big", i, 1));
        }
        Collections.shuffle(inserts, new Random(5));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            agg.update(inserts);
            for (int i = 0; i < n - 1; i++) {
                List<Output> out = agg.update(List.of(c("big", i, -1)));
                assertEquals(i + 1, out.get(1).result());
            }
        });
    }

    private static void randomAgainstBruteForce(Kind kind, long seed) {
        Random random = new Random(seed);
        String[] keys = {"a", "b", "c", "d"};
        IncrementalAggregates agg = new IncrementalAggregates(kind);
        // key -> value -> multiplicity
        Map<String, Map<Long, Long>> rows = new HashMap<>();
        Map<String, Long> fromOutputs = new HashMap<>();
        for (int batchNo = 0; batchNo < 300; batchNo++) {
            List<Change> batch = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                String key = keys[random.nextInt(keys.length)];
                Map<Long, Long> group = rows.computeIfAbsent(key, k -> new HashMap<>());
                List<Long> present = new ArrayList<>(group.keySet());
                if (!present.isEmpty() && random.nextInt(3) == 0) {
                    long value = present.get(random.nextInt(present.size()));
                    batch.add(c(key, value, -1));
                    group.merge(value, -1L, Long::sum);
                    group.values().removeIf(v -> v == 0);
                } else {
                    long value = random.nextInt(21) - 10;
                    batch.add(c(key, value, 1));
                    group.merge(value, 1L, Long::sum);
                }
            }
            Collections.shuffle(batch, random);
            Map<String, Long> before = new HashMap<>(fromOutputs);
            List<Output> outputs = agg.update(batch);

            for (int i = 1; i < outputs.size(); i++) {
                assertTrue(outputs.get(i - 1).key().compareTo(outputs.get(i).key()) <= 0, "outputs sorted by key");
            }
            for (Output out : outputs) {
                if (out.diff() == -1) {
                    assertEquals(out.result(), fromOutputs.remove(out.key()), "retracted a result that was not current");
                } else {
                    assertEquals(1, out.diff());
                    assertNull(fromOutputs.put(out.key(), out.result()), "two results for " + out.key());
                }
            }
            Map<String, Long> expected = recompute(kind, rows);
            assertEquals(expected, fromOutputs, kind + " outputs after batch " + batchNo);
            assertEquals(expected, agg.current(), kind + " current after batch " + batchNo);
            for (Output out : outputs) {
                assertNotEquals(before.get(out.key()), expected.get(out.key()), "emitted an unchanged result for " + out.key());
            }
        }
    }

    private static Map<String, Long> recompute(Kind kind, Map<String, Map<Long, Long>> rows) {
        Map<String, Long> result = new HashMap<>();
        rows.forEach((key, group) -> {
            if (group.isEmpty()) {
                return;
            }
            long r = switch (kind) {
                case COUNT -> group.values().stream().mapToLong(Long::longValue).sum();
                case SUM -> group.entrySet().stream().mapToLong(e -> e.getKey() * e.getValue()).sum();
                case MIN -> Collections.min(group.keySet());
                case MAX -> Collections.max(group.keySet());
            };
            result.put(key, r);
        });
        return result;
    }
}
