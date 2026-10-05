package dev.bbejeck.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LatencyHistogram
 */
public class LatencyHistogramTest {

    private final LatencyHistogram histogram = new LatencyHistogram();

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        assertArrayEquals(new int[]{3, 1, 2},
                histogram.histogram(new int[]{0, 5, 9, 10, 25, 1000}, 3, 10));
    }

    @Test
    @DisplayName("No latencies gives all-zero buckets of the right length")
    void testEmpty() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, histogram.histogram(new int[]{}, 4, 10));
    }

    @Test
    @DisplayName("Lower bound is inclusive, upper bound is exclusive")
    void testBoundaries() {
        // 9 -> bucket 0, 10 -> bucket 1, 19 -> bucket 1, 20 -> bucket 2
        assertArrayEquals(new int[]{1, 2, 1},
                histogram.histogram(new int[]{9, 10, 19, 20}, 3, 10));
    }

    @Test
    @DisplayName("Zero goes in the first bucket")
    void testZero() {
        assertArrayEquals(new int[]{1, 0}, histogram.histogram(new int[]{0}, 2, 5));
    }

    @Test
    @DisplayName("The last bucket's own range and overflow are both counted there")
    void testOverflowIntoLastBucket() {
        // last bucket range is [20, 30); 29 is in range, 30 and 5000 overflow
        assertArrayEquals(new int[]{0, 0, 3},
                histogram.histogram(new int[]{29, 30, 5000}, 3, 10));
    }

    @Test
    @DisplayName("Duplicate latencies are each counted, including overflow")
    void testDuplicateLatencies() {
        assertArrayEquals(new int[]{2, 0, 3},
                histogram.histogram(new int[]{5, 5, 1000, 1000, 1000}, 3, 10));
    }

    @Test
    @DisplayName("A negative latency throws even with a single bucket")
    void testNegativeLatencySingleBucket() {
        assertThrows(IllegalArgumentException.class, () -> histogram.histogram(new int[]{5, -1}, 1, 10));
    }

    @Test
    @DisplayName("A single bucket holds everything")
    void testSingleBucket() {
        assertArrayEquals(new int[]{4},
                histogram.histogram(new int[]{0, 1, 99, 1_000_000}, 1, 10));
    }

    @Test
    @DisplayName("Bucket width of 1")
    void testWidthOne() {
        assertArrayEquals(new int[]{1, 2, 1, 1},
                histogram.histogram(new int[]{0, 1, 1, 2, 3}, 4, 1));
    }

    @Test
    @DisplayName("Input order doesn't matter")
    void testUnsortedInput() {
        assertArrayEquals(new int[]{2, 1, 2},
                histogram.histogram(new int[]{25, 3, 14, 0, 99}, 3, 10));
    }

    @Test
    @DisplayName("Large values don't overflow int when computing bucket ranges")
    void testNoIntOverflow() {
        // bucketCount * bucketWidth = 5,000,000,000, which overflows int
        int width = 1_000_000_000;
        assertArrayEquals(new int[]{1, 1, 1, 0, 0},
                histogram.histogram(new int[]{999_999_999, 1_000_000_000, Integer.MAX_VALUE}, 5, width));
    }

    @Test
    @DisplayName("bucketCount <= 0 throws IllegalArgumentException")
    void testInvalidBucketCount() {
        assertThrows(IllegalArgumentException.class, () -> histogram.histogram(new int[]{1}, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> histogram.histogram(new int[]{1}, -1, 10));
    }

    @Test
    @DisplayName("bucketWidth <= 0 throws IllegalArgumentException")
    void testInvalidBucketWidth() {
        assertThrows(IllegalArgumentException.class, () -> histogram.histogram(new int[]{1}, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> histogram.histogram(new int[]{1}, 3, -5));
    }

    @Test
    @DisplayName("A negative latency throws IllegalArgumentException")
    void testNegativeLatency() {
        assertThrows(IllegalArgumentException.class, () -> histogram.histogram(new int[]{5, -1}, 3, 10));
    }

    @Test
    @DisplayName("Many latencies are all counted")
    void testManyLatencies() {
        int n = 1_000_000;
        int[] latencies = new int[n];
        for (int i = 0; i < n; i++) {
            latencies[i] = i % 100;
        }
        // 10 buckets of width 10 over values 0..99 -> 100,000 each
        int[] expected = new int[10];
        java.util.Arrays.fill(expected, n / 10);
        assertArrayEquals(expected, histogram.histogram(latencies, 10, 10));
    }
}
