package dev.bbejeck.window;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RecentCounter class
 */
public class RecentCounterTest {

    @Test
    void testBasicFunctionality() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));     // [1] -> count = 1
        assertEquals(2, counter.ping(100));   // [1, 100] -> count = 2
        assertEquals(3, counter.ping(3001));  // [1, 100, 3001] -> count = 3
        assertEquals(3, counter.ping(3002));  // [100, 3001, 3002] -> count = 3 (1 is removed)
    }

    @Test
    void testSinglePing() {
        RecentCounter counter = new RecentCounter();
        assertEquals(1, counter.ping(1));
    }

    @Test
    void testAllPingsWithinWindow() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));
        assertEquals(2, counter.ping(100));
        assertEquals(3, counter.ping(500));
        assertEquals(4, counter.ping(1000));
        assertEquals(5, counter.ping(2000)); // All within 3000ms of 2000
    }

    @Test
    void testRemovalAtExactBoundary() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));
        assertEquals(2, counter.ping(3001));  // [1, 3001] both within range
        assertEquals(2, counter.ping(6001));  // [3001, 6001] -> 1 is removed (6001 - 3000 = 3001)
    }

    @Test
    void testMultipleRemovalsAtOnce() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));
        assertEquals(2, counter.ping(2));
        assertEquals(3, counter.ping(3));
        assertEquals(4, counter.ping(100));
        assertEquals(5, counter.ping(200));
        assertEquals(1, counter.ping(3500));  // Only 200 and 3500 remain
    }

    @Test
    void testConsecutivePings() {
        RecentCounter counter = new RecentCounter();

        for (int i = 1; i <= 100; i++) {
            assertEquals(i, counter.ping(i));
        }
        // All 100 pings are within 3000ms window
    }

    @Test
    void testLargeTimeGaps() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));
        assertEquals(1, counter.ping(5000));   // Only 5000 remains
        assertEquals(1, counter.ping(10000));  // Only 10000 remains
        assertEquals(1, counter.ping(20000));  // Only 20000 remains
    }

    @Test
    void testPingsAtWindowBoundary() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(100));
        assertEquals(2, counter.ping(3100));   // [100, 3100] -> 100 is still in range
        assertEquals(2, counter.ping(3200));   // [100, 3100, 3200] -> 100 is still in range (3200 - 3000 = 200)
        assertEquals(2, counter.ping(6200));   // [3100, 3200, 6200] -> only 3100 onwards
    }

    @Test
    void testMaximumTimeValue() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1000000000 - 3000)); // Near max value
        assertEquals(2, counter.ping(1000000000 - 1500));
        assertEquals(3, counter.ping(1000000000));        // Max allowed value (10^9)
    }

    @Test
    void testMinimumTimeValue() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));      // Minimum value
        assertEquals(2, counter.ping(2));
        assertEquals(3, counter.ping(3));
    }

    @Test
    void testStrictlyIncreasingValues() {
        RecentCounter counter = new RecentCounter();

        int[] times = {1, 50, 150, 500, 1500, 3000, 3500, 4000, 4500, 6000};
        int[] expected = {1, 2, 3, 4, 5, 6, 4, 4, 5, 5};

        for (int i = 0; i < times.length; i++) {
            assertEquals(expected[i], counter.ping(times[i]),
                    "Failed at index " + i + " with time " + times[i]);
        }
    }

    @Test
    void testThousandPingsInWindow() {
        RecentCounter counter = new RecentCounter();

        // Add 1000 pings all within 3000ms window
        for (int i = 1; i <= 1000; i++) {
            assertEquals(i, counter.ping(i));
        }
    }

    @Test
    void testThousandPingsWithRollingWindow() {
        RecentCounter counter = new RecentCounter();

        // Add pings that will cause rolling window behavior
        for (int i = 1; i <= 100; i++) {
            counter.ping(i * 100);
        }

        assertEquals(32, counter.ping(10000));
    }

    @Test
    void testEveryPingRemovesPrevious() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));
        assertEquals(1, counter.ping(4000));
        assertEquals(1, counter.ping(8000));
        assertEquals(1, counter.ping(12000));
        assertEquals(1, counter.ping(16000));
    }

    @Test
    void testMaxCallsScenario() {
        // Test with up to 10^4 calls
        RecentCounter counter = new RecentCounter();

        for (int i = 1; i <= 10000; i++) {
            int result = counter.ping(i);
            assertTrue(result >= 1 && result <= Math.min(i, 3001),
                    "Result at ping " + i + " was " + result);
        }
    }

    @Test
    void testPingsWithExactly3000MsDifference() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1000));
        assertEquals(2, counter.ping(2000));
        assertEquals(3, counter.ping(3000));
        assertEquals(4, counter.ping(4000));  // [1000, 2000, 3000, 4000] all in range
        assertEquals(4, counter.ping(4001));  // 1000 is removed (4001 - 3000 = 1001)
    }

    @Test
    void testIntervalOfOne() {
        RecentCounter counter = new RecentCounter();

        for (int i = 1; i <= 20; i++) {
            int result = counter.ping(i);
            assertEquals(i, result);
        }
    }

    @Test
    void testSparseTimestamps() {
        RecentCounter counter = new RecentCounter();

        assertEquals(1, counter.ping(1));
        assertEquals(2, counter.ping(1000));
        assertEquals(3, counter.ping(2000));
        assertEquals(2, counter.ping(5000));   // Only 2000 and 5000 remain
        assertEquals(2, counter.ping(8000));   // Only 5000 and 8000 remain
    }
}