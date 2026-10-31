package dev.bbejeck.window;

import dev.bbejeck.window.SlidingWindowSumsByTag.Datapoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SlidingWindowSumsByTag
 */
public class SlidingWindowSumsByTagTest {

    private final SlidingWindowSumsByTag sums = new SlidingWindowSumsByTag();

    private static Datapoint dp(long ts, long value, String... tags) {
        return new Datapoint(List.of(tags), ts, value);
    }

    // ---------------- count-based windows ----------------

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        List<Datapoint> points = List.of(
                dp(5, -1, "a"),
                dp(1, 100, "b"),
                dp(3, 5, "a", "b"),
                dp(2, 4, "a"));
        assertEquals(List.of(9L, 4L), sums.slidingWindowSumsByTag(points, "a", 2));
    }

    @Test
    @DisplayName("Empty input returns an empty list")
    void testEmpty() {
        assertEquals(List.of(), sums.slidingWindowSumsByTag(List.of(), "a", 1));
    }

    @Test
    @DisplayName("k <= 0 returns an empty list")
    void testInvalidK() {
        List<Datapoint> points = List.of(dp(1, 1, "a"));
        assertEquals(List.of(), sums.slidingWindowSumsByTag(points, "a", 0));
        assertEquals(List.of(), sums.slidingWindowSumsByTag(points, "a", -2));
    }

    @Test
    @DisplayName("Fewer than k matching points returns an empty list")
    void testFewerThanK() {
        List<Datapoint> points = List.of(dp(1, 1, "a"), dp(2, 2, "a"), dp(3, 3, "b"));
        assertEquals(List.of(), sums.slidingWindowSumsByTag(points, "a", 3));
    }

    @Test
    @DisplayName("k equal to the number of matches gives one window")
    void testKEqualsMatches() {
        List<Datapoint> points = List.of(dp(1, 1, "a"), dp(2, 2, "a"), dp(3, 3, "a"));
        assertEquals(List.of(6L), sums.slidingWindowSumsByTag(points, "a", 3));
    }

    @Test
    @DisplayName("k = 1 returns each matching value in ts order")
    void testKOne() {
        List<Datapoint> points = List.of(dp(3, 30, "a"), dp(1, 10, "a"), dp(2, 20, "a"));
        assertEquals(List.of(10L, 20L, 30L), sums.slidingWindowSumsByTag(points, "a", 1));
    }

    @Test
    @DisplayName("Tag must match exactly, not by prefix or case")
    void testExactTagMatch() {
        List<Datapoint> points = List.of(
                dp(1, 1, "env:prod"),
                dp(2, 2, "env:production"),
                dp(3, 4, "ENV:PROD"),
                dp(4, 8, "env:prod"));
        assertEquals(List.of(9L), sums.slidingWindowSumsByTag(points, "env:prod", 2));
    }

    @Test
    @DisplayName("Points with no tags never match")
    void testNoTags() {
        List<Datapoint> points = List.of(dp(1, 5), dp(2, 7, "a"));
        assertEquals(List.of(7L), sums.slidingWindowSumsByTag(points, "a", 1));
    }

    @Test
    @DisplayName("Equal timestamps keep their input order")
    void testStableOrderForEqualTimestamps() {
        // ts order: (1,1) then the three ts=2 points in input order: 10, 100, 1000
        List<Datapoint> points = List.of(
                dp(2, 10, "a"),
                dp(2, 100, "a"),
                dp(1, 1, "a"),
                dp(2, 1000, "a"));
        assertEquals(List.of(11L, 110L, 1100L), sums.slidingWindowSumsByTag(points, "a", 2));
    }

    @Test
    @DisplayName("Negative values")
    void testNegativeValues() {
        List<Datapoint> points = List.of(dp(1, -5, "a"), dp(2, 3, "a"), dp(3, -10, "a"));
        assertEquals(List.of(-2L, -7L), sums.slidingWindowSumsByTag(points, "a", 2));
    }

    @Test
    @DisplayName("Sums beyond the int range don't overflow")
    void testLargeSums() {
        List<Datapoint> points = List.of(
                dp(1, 1_000_000_000L, "a"),
                dp(2, 1_000_000_000L, "a"),
                dp(3, 1_000_000_000L, "a"));
        assertEquals(List.of(3_000_000_000L), sums.slidingWindowSumsByTag(points, "a", 3));
    }

    @Test
    @DisplayName("200,000 datapoints")
    void testLargeInput() {
        int n = 200_000;
        List<Datapoint> points = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            points.add(dp(i, 1, i % 2 == 0 ? "a" : "b"));
        }
        List<Long> result = sums.slidingWindowSumsByTag(points, "a", 1_000);
        assertEquals(n / 2 - 1_000 + 1, result.size());
        assertTrue(result.stream().allMatch(s -> s == 1_000L));
    }

    // ---------------- follow-up: time-based windows ----------------

    @Test
    @DisplayName("Time window: each sum covers (ts - windowSize, ts]")
    void testTimeWindow() {
        List<Datapoint> points = List.of(
                dp(10, 3, "a"),
                dp(1, 1, "a"),
                dp(2, 2, "a"),
                dp(12, 4, "a"));
        // ts=1 -> {1}; ts=2 -> {1,2}; ts=10 -> {3} (1 and 2 are >= 5 older); ts=12 -> {3,4}
        assertEquals(List.of(1L, 3L, 3L, 7L), sums.timeWindowSumsByTag(points, "a", 5));
    }

    @Test
    @DisplayName("Time window: a point exactly windowSize older is excluded")
    void testTimeWindowBoundary() {
        List<Datapoint> points = List.of(dp(0, 1, "a"), dp(5, 2, "a"), dp(4, 4, "a"));
        // ts=0 -> {1}; ts=4 -> {1,4}; ts=5 -> {4,2} because ts=0 is exactly 5 older
        assertEquals(List.of(1L, 5L, 6L), sums.timeWindowSumsByTag(points, "a", 5));
    }

    @Test
    @DisplayName("Time window: equal timestamps are all inside each other's window")
    void testTimeWindowEqualTimestamps() {
        List<Datapoint> points = List.of(dp(7, 1, "a"), dp(7, 2, "a"), dp(7, 4, "a"));
        assertEquals(List.of(1L, 3L, 7L), sums.timeWindowSumsByTag(points, "a", 1));
    }

    @Test
    @DisplayName("Time window: filters by tag and ignores other tags")
    void testTimeWindowFiltersTag() {
        List<Datapoint> points = List.of(dp(1, 100, "b"), dp(2, 1, "a"), dp(3, 2, "a"));
        assertEquals(List.of(1L, 3L), sums.timeWindowSumsByTag(points, "a", 10));
    }

    @Test
    @DisplayName("Time window: windowSize <= 0 returns an empty list")
    void testTimeWindowInvalidSize() {
        assertEquals(List.of(), sums.timeWindowSumsByTag(List.of(dp(1, 1, "a")), "a", 0));
    }
}
