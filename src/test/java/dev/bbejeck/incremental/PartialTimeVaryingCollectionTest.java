package dev.bbejeck.incremental;

import dev.bbejeck.incremental.PartialTimeVaryingCollection.Update;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PartialTimeVaryingCollection
 */
public class PartialTimeVaryingCollectionTest {

    private static Update u(String data, long time, long diff) {
        return new Update(data, time, diff);
    }

    private static PartialTimeVaryingCollection example() {
        PartialTimeVaryingCollection c = new PartialTimeVaryingCollection();
        c.append(List.of(u("a", 0, 1), u("b", 1, 1), u("a", 2, -1), u("c", 3, 1)), 5);
        return c;
    }

    // Part 1

    @Test
    @DisplayName("New collection has since = upper = 0 and nothing is readable")
    void testEmpty() {
        PartialTimeVaryingCollection c = new PartialTimeVaryingCollection();
        assertEquals(0, c.since());
        assertEquals(0, c.upper());
        assertTrue(c.updates().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> c.read(0));
    }

    @Test
    @DisplayName("Reads accumulate diffs up to the read time")
    void testRead() {
        PartialTimeVaryingCollection c = example();
        assertEquals(5, c.upper());
        assertEquals(Map.of("a", 1L), c.read(0));
        assertEquals(Map.of("a", 1L, "b", 1L), c.read(1));
        assertEquals(Map.of("b", 1L), c.read(2));
        assertEquals(Map.of("b", 1L, "c", 1L), c.read(3));
        assertEquals(Map.of("b", 1L, "c", 1L), c.read(4));
    }

    @Test
    @DisplayName("Reads at or beyond upper throw")
    void testReadBeyondUpper() {
        PartialTimeVaryingCollection c = example();
        assertThrows(IllegalArgumentException.class, () -> c.read(5));
        assertThrows(IllegalArgumentException.class, () -> c.read(100));
    }

    @Test
    @DisplayName("Append rejects updates before upper and leaves state unchanged")
    void testAppendBeforeUpper() {
        PartialTimeVaryingCollection c = example();
        List<Update> before = c.updates();
        assertThrows(IllegalArgumentException.class,
                () -> c.append(List.of(u("d", 6, 1), u("e", 4, 1)), 8));
        assertEquals(5, c.upper());
        assertEquals(before, c.updates());
    }

    @Test
    @DisplayName("Append rejects updates at or beyond newUpper")
    void testAppendBeyondNewUpper() {
        PartialTimeVaryingCollection c = example();
        assertThrows(IllegalArgumentException.class, () -> c.append(List.of(u("d", 8, 1)), 8));
        assertEquals(5, c.upper());
    }

    @Test
    @DisplayName("Upper cannot move backwards; empty batches can advance it")
    void testUpperMovement() {
        PartialTimeVaryingCollection c = example();
        assertThrows(IllegalArgumentException.class, () -> c.append(List.of(), 4));
        c.append(List.of(), 5);
        assertEquals(5, c.upper());
        c.append(List.of(), 9);
        assertEquals(9, c.upper());
        assertEquals(Map.of("b", 1L, "c", 1L), c.read(8));
    }

    @Test
    @DisplayName("updates() is consolidated and sorted by time then data")
    void testConsolidation() {
        PartialTimeVaryingCollection c = new PartialTimeVaryingCollection();
        c.append(List.of(u("b", 1, 1), u("a", 1, 1), u("a", 1, 1), u("z", 0, 3), u("c", 1, 1), u("c", 1, -1)), 2);
        assertEquals(List.of(u("z", 0, 3), u("a", 1, 2), u("b", 1, 1)), c.updates());
    }

    @Test
    @DisplayName("Multiplicities above one")
    void testMultiplicity() {
        PartialTimeVaryingCollection c = new PartialTimeVaryingCollection();
        c.append(List.of(u("x", 0, 3)), 1);
        c.append(List.of(u("x", 1, -2)), 2);
        assertEquals(Map.of("x", 3L), c.read(0));
        assertEquals(Map.of("x", 1L), c.read(1));
    }

    // Part 2

    @Test
    @DisplayName("Compaction advances times and cancels updates")
    void testCompact() {
        PartialTimeVaryingCollection c = example();
        c.compact(2);
        assertEquals(2, c.since());
        assertEquals(List.of(u("b", 2, 1), u("c", 3, 1)), c.updates());
        assertEquals(Map.of("b", 1L), c.read(2));
        assertEquals(Map.of("b", 1L, "c", 1L), c.read(3));
    }

    @Test
    @DisplayName("Reads before since throw after compaction")
    void testReadBeforeSince() {
        PartialTimeVaryingCollection c = example();
        c.compact(2);
        assertThrows(IllegalArgumentException.class, () -> c.read(1));
        assertThrows(IllegalArgumentException.class, () -> c.read(0));
    }

    @Test
    @DisplayName("Compact to upper collapses everything to a single time")
    void testCompactToUpper() {
        PartialTimeVaryingCollection c = example();
        c.compact(5);
        assertEquals(List.of(u("b", 5, 1), u("c", 5, 1)), c.updates());
        assertThrows(IllegalArgumentException.class, () -> c.read(4));
        c.append(List.of(u("b", 5, -1)), 6);
        assertEquals(Map.of("c", 1L), c.read(5));
    }

    @Test
    @DisplayName("Since cannot move backwards or past upper")
    void testCompactBounds() {
        PartialTimeVaryingCollection c = example();
        c.compact(3);
        assertThrows(IllegalArgumentException.class, () -> c.compact(2));
        assertThrows(IllegalArgumentException.class, () -> c.compact(6));
        assertEquals(3, c.since());
    }

    @Test
    @DisplayName("Random appends and compactions preserve reads at or after since")
    void testRandomCompaction() {
        Random random = new Random(3);
        String[] keys = {"a", "b", "c", "d", "e"};
        for (int round = 0; round < 30; round++) {
            PartialTimeVaryingCollection c = new PartialTimeVaryingCollection();
            List<Update> all = new ArrayList<>();
            long upper = 0;
            for (int batch = 0; batch < 10; batch++) {
                long newUpper = upper + 1 + random.nextInt(4);
                List<Update> updates = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    long time = upper + random.nextInt((int) (newUpper - upper));
                    long diff = random.nextBoolean() ? 1 : -1;
                    updates.add(u(keys[random.nextInt(keys.length)], time, diff));
                }
                c.append(updates, newUpper);
                all.addAll(updates);
                upper = newUpper;
            }
            long since = random.nextInt((int) upper + 1);
            c.compact(since);
            assertEquals(since, c.since());
            for (long t = since; t < upper; t++) {
                assertEquals(bruteForceRead(all, t), c.read(t), "read " + t + " after compact " + since);
            }
            for (Update update : c.updates()) {
                assertTrue(update.time() >= since);
                assertNotEquals(0, update.diff());
            }
        }
    }

    private static Map<String, Long> bruteForceRead(List<Update> updates, long t) {
        Map<String, Long> result = new HashMap<>();
        for (Update update : updates) {
            if (update.time() <= t) {
                result.merge(update.data(), update.diff(), Long::sum);
            }
        }
        result.values().removeIf(v -> v == 0);
        return result;
    }
}
