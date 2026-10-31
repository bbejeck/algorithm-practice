package dev.bbejeck.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VersionedKeyValueStore
 */
public class VersionedKeyValueStoreTest {

    private final VersionedKeyValueStore store = new VersionedKeyValueStore();

    private void loadExample() {
        assertEquals(1, store.put("a", "1"));
        assertEquals(2, store.put("b", "2"));
        assertEquals(3, store.put("a", "3"));
        assertEquals(4, store.delete("b"));
    }

    // ---------------- get / put / delete ----------------

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        loadExample();
        assertEquals("3", store.get("a"));
        assertEquals("1", store.get("a", 2));
        assertEquals("2", store.get("b", 3));
        assertNull(store.get("b"));
        assertEquals(4, store.currentVersion());
    }

    @Test
    @DisplayName("Empty store")
    void testEmpty() {
        assertEquals(0, store.currentVersion());
        assertNull(store.get("a"));
        assertNull(store.get("a", 0));
    }

    @Test
    @DisplayName("Reading before a key's first write returns null")
    void testBeforeFirstWrite() {
        store.put("x", "x");
        store.put("a", "1");
        assertNull(store.get("a", 1));
        assertEquals("1", store.get("a", 2));
    }

    @Test
    @DisplayName("Reading between writes returns the earlier write")
    void testBetweenWrites() {
        store.put("a", "1");   // 1
        store.put("b", "b");   // 2
        store.put("b", "b2");  // 3
        store.put("a", "2");   // 4
        assertEquals("1", store.get("a", 1));
        assertEquals("1", store.get("a", 3));
        assertEquals("2", store.get("a", 4));
    }

    @Test
    @DisplayName("Reading at a future version returns the current value")
    void testFutureVersion() {
        store.put("a", "1");
        assertEquals("1", store.get("a", 100));
    }

    @Test
    @DisplayName("Deleting a missing key still takes a version")
    void testDeleteMissingKey() {
        assertEquals(1, store.delete("nope"));
        assertEquals(2, store.put("a", "1"));
        assertNull(store.get("nope"));
    }

    @Test
    @DisplayName("Put after delete brings the key back, history stays readable")
    void testPutAfterDelete() {
        store.put("a", "1");   // 1
        store.delete("a");     // 2
        store.put("a", "2");   // 3
        assertEquals("1", store.get("a", 1));
        assertNull(store.get("a", 2));
        assertEquals("2", store.get("a"));
    }

    @Test
    @DisplayName("Empty strings are values, not deletes")
    void testEmptyValue() {
        store.put("", "");
        assertEquals("", store.get(""));
    }

    @Test
    @DisplayName("Null keys and values are rejected")
    void testNulls() {
        assertThrows(IllegalArgumentException.class, () -> store.put(null, "v"));
        assertThrows(IllegalArgumentException.class, () -> store.put("k", null));
        assertThrows(IllegalArgumentException.class, () -> store.delete(null));
        assertEquals(0, store.currentVersion());
    }

    @Test
    @DisplayName("Negative versions are rejected")
    void testNegativeVersion() {
        assertThrows(IllegalArgumentException.class, () -> store.get("a", -1));
    }

    @Test
    @DisplayName("100,000 writes to one key, every version reads correctly")
    void testManyVersions() {
        int n = 100_000;
        for (int i = 1; i <= n; i++) {
            store.put("hot", "v" + i);
        }
        for (int i = 1; i <= n; i += 997) {
            assertEquals("v" + i, store.get("hot", i));
        }
        assertEquals("v" + n, store.get("hot"));
    }

    // ---------------- follow-up 1: snapshot ----------------

    @Test
    @DisplayName("Snapshot: example from the problem statement")
    void testSnapshot() {
        loadExample();
        assertEquals(Map.of("a", "1", "b", "2"), store.snapshot(2));
        assertEquals(Map.of("a", "3"), store.snapshot(4));
        assertEquals(Map.of(), store.snapshot(0));
    }

    @Test
    @DisplayName("Snapshot: sorted by key")
    void testSnapshotSorted() {
        store.put("c", "3");
        store.put("a", "1");
        store.put("b", "2");
        assertEquals(java.util.List.of("a", "b", "c"), java.util.List.copyOf(store.snapshot(3).keySet()));
    }

    // ---------------- follow-up 2: compactBefore ----------------

    @Test
    @DisplayName("Compaction: reads at or after the compaction version don't change")
    void testCompactionKeepsReads() {
        loadExample();
        store.put("c", "5");   // 5
        store.put("a", "6");   // 6
        store.compactBefore(4);
        assertEquals("3", store.get("a", 4));
        assertEquals("3", store.get("a", 5));
        assertEquals("6", store.get("a"));
        assertNull(store.get("b", 4));
        assertNull(store.get("c", 4));
        assertEquals("5", store.get("c", 5));
        assertEquals(Map.of("a", "3"), store.snapshot(4));
        assertEquals(Map.of("a", "3", "c", "5"), store.snapshot(5));
    }

    @Test
    @DisplayName("Compaction: reads older than the compaction version throw")
    void testCompactionRejectsOldReads() {
        loadExample();
        store.compactBefore(3);
        assertThrows(IllegalArgumentException.class, () -> store.get("a", 2));
        assertThrows(IllegalArgumentException.class, () -> store.snapshot(2));
        assertEquals("3", store.get("a", 3));
    }

    @Test
    @DisplayName("Compaction: frees old writes and fully deleted keys")
    void testCompactionFreesMemory() {
        for (int i = 0; i < 10; i++) {
            store.put("a", "a" + i);
        }
        store.put("b", "b");
        store.delete("b");
        assertEquals(12, store.storedWrites());
        store.compactBefore(store.currentVersion());
        // Only a's latest write is still needed; b's delete reads the same as no b at all
        assertEquals(1, store.storedWrites());
        assertEquals("a9", store.get("a"));
        assertNull(store.get("b"));
    }

    @Test
    @DisplayName("Compaction: keeps writes newer than the compaction version")
    void testCompactionKeepsNewerWrites() {
        store.put("a", "1");   // 1
        store.put("a", "2");   // 2
        store.put("a", "3");   // 3
        store.compactBefore(2);
        assertEquals(2, store.storedWrites());
        assertEquals("2", store.get("a", 2));
        assertEquals("3", store.get("a", 3));
    }

    @Test
    @DisplayName("Compaction: past the current version throws, an older version does nothing")
    void testCompactionBounds() {
        loadExample();
        assertThrows(IllegalArgumentException.class, () -> store.compactBefore(5));
        store.compactBefore(3);
        store.compactBefore(1);
        assertThrows(IllegalArgumentException.class, () -> store.get("a", 2));
    }

    @Test
    @DisplayName("Compaction: writes after compaction work normally")
    void testWritesAfterCompaction() {
        loadExample();
        store.compactBefore(4);
        assertEquals(5, store.put("b", "new"));
        assertNull(store.get("b", 4));
        assertEquals("new", store.get("b"));
    }
}
