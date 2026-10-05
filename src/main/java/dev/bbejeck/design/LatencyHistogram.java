package dev.bbejeck.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Latency Histogram
 * <p>
 * histogram(latencies, bucketCount, bucketWidth) returns an int[bucketCount] where
 * element i is the number of latencies that fall in bucket i.
 * <p>
 * - Bucket i covers [i * bucketWidth, (i + 1) * bucketWidth).
 * The first bucket starts at 0. Lower bound inclusive, upper bound exclusive.
 * - Any latency past the end of the last bucket is counted in the last bucket.
 * - Throw IllegalArgumentException if bucketCount <= 0, bucketWidth <= 0,
 * or any latency is negative.
 * <p>
 * Example: latencies [0, 5, 9, 10, 25, 1000], bucketCount 3, bucketWidth 10
 * bucket 0  [0, 10)             : 0, 5, 9   -> 3
 * bucket 1  [10, 20)            : 10        -> 1
 * bucket 2  [20, 30) + overflow : 25, 1000  -> 2
 * result: [3, 1, 2]
 */
public class LatencyHistogram {

    public int[] histogram(int[] latencies, int bucketCount, int bucketWidth) {
        if (bucketCount <= 0 || bucketWidth <= 0) {
            throw new IllegalArgumentException("bucketCount or bucketWidth must be positive");
        }
        int[] result = new int[bucketCount];
        for (int latency : latencies) {
            if (latency < 0) {
                throw new IllegalArgumentException("latency must be non-negative");
            }
            int bucket = Math.min(latency / bucketWidth, bucketCount - 1);
            result[bucket]++;
        }
        return result;
    }
}
