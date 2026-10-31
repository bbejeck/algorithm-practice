package dev.bbejeck.window;

import java.util.List;

/**
 * Sliding Window Sums by Tag (reported Datadog phone screen question)
 *
 * Each datapoint has tags, a timestamp and a value.
 *
 * slidingWindowSumsByTag(datapoints, tag, k):
 *   1. Keep only datapoints whose tags contain exactly tag.
 *   2. Order them by ascending ts. Equal timestamps keep their input order.
 *   3. Return the sum of every consecutive window of k values.
 *
 * Returns an empty list if k <= 0 or fewer than k datapoints match.
 *
 * Example: tag "a", k = 2
 *   matching points by ts: (ts=2, 4), (ts=3, 5), (ts=5, -1)
 *   windows: [4 + 5, 5 + -1] = [9, 4]
 *
 * Follow-up, timeWindowSumsByTag(datapoints, tag, windowSize):
 *   The window is a span of time instead of a count of points. For each matching
 *   datapoint in ts order, return the sum of the matching values with ts in
 *   (ts - windowSize, ts].
 *
 * Constraints: n <= 200,000 and values in [-1e9, 1e9], so sums need a long.
 */
public class SlidingWindowSumsByTag {

    public record Datapoint(List<String> tags, long ts, long value) {
    }

    public List<Long> slidingWindowSumsByTag(List<Datapoint> datapoints, String tag, int k) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Long> timeWindowSumsByTag(List<Datapoint> datapoints, String tag, long windowSize) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}