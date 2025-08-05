package dev.bbejeck.lru;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LRUCache implementation
 */
public class LRUCacheTest {

    private LRUCache cache;

    @BeforeEach
    void setUp() {
        cache = new LRUCache(2);
    }

    @Test
    void testGetFromEmptyCache() {
        assertEquals(-1, cache.get(1));
    }

    @Test
    void testPutAndGetSingleItem() {
        cache.put(1, 100);
        assertEquals(100, cache.get(1));
    }

    @Test
    void testGetNonExistentKey() {
        cache.put(1, 100);
        assertEquals(-1, cache.get(2));
    }

    @Test
    void testUpdateExistingKey() {
        cache.put(1, 100);
        cache.put(1, 200);
        assertEquals(200, cache.get(1));
    }

    @Test
    void testCapacityLimit() {
        // Fill to capacity
        cache.put(1, 100);
        cache.put(2, 200);
        assertEquals(100, cache.get(1));
        assertEquals(200, cache.get(2));

        // Add third item - should evict least recently used
        cache.put(3, 300);
        assertEquals(-1, cache.get(1)); // 2 was evicted
        assertEquals(200, cache.get(2));
        assertEquals(300, cache.get(3));
    }

    @Test
    void testLRUEvictionOrder() {
        cache.put(1, 100);
        cache.put(2, 200);

        // Access key 1 to make it most recently used
        cache.get(1);

        // Add new item - should evict key 2 (least recently used)
        cache.put(3, 300);

        assertEquals(100, cache.get(1)); // Still present
        assertEquals(-1, cache.get(2));  // Evicted
        assertEquals(300, cache.get(3)); // New item
    }

    @Test
    void testGetUpdatesLRUOrder() {
        cache.put(1, 100);
        cache.put(2, 200);

        // Get key 1 to make it most recently used
        assertEquals(100, cache.get(1));

        // Add new item - key 2 should be evicted
        cache.put(3, 300);

        assertEquals(100, cache.get(1)); // Still there
        assertEquals(-1, cache.get(2));  // Evicted
    }

    @Test
    void testPutUpdatesLRUOrder() {
        cache.put(1, 100);
        cache.put(2, 200);

        // Update key 1 to make it most recently used
        cache.put(1, 150);

        // Add new item - key 2 should be evicted
        cache.put(3, 300);

        assertEquals(150, cache.get(1)); // Updated and still there
        assertEquals(-1, cache.get(2));  // Evicted
        assertEquals(300, cache.get(3));
    }

    @Test
    void testCapacityOne() {
        LRUCache singleCache = new LRUCache(1);

        singleCache.put(1, 100);
        assertEquals(100, singleCache.get(1));

        // Add second item - first should be evicted
        singleCache.put(2, 200);
        assertEquals(-1, singleCache.get(1));
        assertEquals(200, singleCache.get(2));
    }

    @Test
    void testLargerCapacity() {
        LRUCache largeCache = new LRUCache(3);

        largeCache.put(1, 100);
        largeCache.put(2, 200);
        largeCache.put(3, 300);

        // All should be present
        assertEquals(100, largeCache.get(1));
        assertEquals(200, largeCache.get(2));
        assertEquals(300, largeCache.get(3));

        // Add fourth item - oldest (key 1) should be evicted
        largeCache.put(4, 400);
        assertEquals(-1, largeCache.get(1)); // Evicted
        assertEquals(200, largeCache.get(2));
        assertEquals(300, largeCache.get(3));
        assertEquals(400, largeCache.get(4));
    }

    @Test
    void testSequentialOperations() {
        // Simulate LeetCode example: ["LRUCache","put","put","get","put","get","put","get","get","get"]
        // [[2],[1,1],[2,2],[1],[3,3],[2],[4,4],[1],[3],[4]]

        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));     // returns 1
        cache.put(3, 3);                   // evicts key 2
        assertEquals(-1, cache.get(2));    // returns -1 (not found)
        cache.put(4, 4);                   // evicts key 1
        assertEquals(-1, cache.get(1));    // returns -1 (not found)
        assertEquals(3, cache.get(3));     // returns 3
        assertEquals(4, cache.get(4));     // returns 4
    }

    @Test
    void testRepeatedAccess() {
        cache.put(1, 100);
        cache.put(2, 200);

        // Access key 1 multiple times
        assertEquals(100, cache.get(1));
        assertEquals(100, cache.get(1));
        assertEquals(100, cache.get(1));

        // Add new item - key 2 should still be evicted
        cache.put(3, 300);
        assertEquals(100, cache.get(1));
        assertEquals(-1, cache.get(2));
        assertEquals(300, cache.get(3));
    }

    @Test
    void testZeroValues() {
        cache.put(1, 0);
        cache.put(2, -5);

        assertEquals(0, cache.get(1));
        assertEquals(-5, cache.get(2));
    }

    @Test
    void testSameKeyDifferentValues() {
        cache.put(1, 100);
        cache.put(1, 200);
        cache.put(1, 300);

        assertEquals(300, cache.get(1));

        // Add second key
        cache.put(2, 400);
        assertEquals(300, cache.get(1));
        assertEquals(400, cache.get(2));
    }
}