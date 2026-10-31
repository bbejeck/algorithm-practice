package dev.bbejeck.intervals_heap;

import java.util.List;

/**
 * K Nearest Vectors (brute-force vector search)
 *
 * Each vector has an int id and a float[] of values.
 *
 * kNearest(vectors, query, k, metric):
 *   Return the k vectors closest to query, closest first, as (id, distance) pairs.
 *   Ties on distance go to the smaller id.
 *   L2:     squared Euclidean distance, sum of (a[i] - b[i])^2
 *   COSINE: 1 - (a . b) / (|a| * |b|). A zero vector has no direction, so it's skipped
 *           (and a zero query matches nothing).
 *
 * Returns an empty list if k <= 0, and every vector if there are fewer than k.
 * Throws IllegalArgumentException if a vector's dimension differs from the query's.
 *
 * Example: query [0, 0], k = 2, L2
 *   id 1 [3, 4] -> 25, id 2 [1, 1] -> 2, id 3 [-1, 1] -> 2
 *   result: [(2, 2.0), (3, 2.0)]
 *
 * Follow-up, mergeTopK(partitionResults, k):
 *   The vectors are split across partitions (files, shards). Each partition already returned
 *   its own top k, closest first. Merge them into the global top k. Ids are unique across partitions.
 *
 * Constraints: n <= 100,000 vectors, dimension <= 1,024. Aim for better than sorting all n.
 */
public class KNearestVectors {

    public record Vector(int id, float[] values) {
    }

    public record Neighbor(int id, double distance) {
    }

    public enum Metric {L2, COSINE}

    public List<Neighbor> kNearest(List<Vector> vectors, float[] query, int k, Metric metric) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Neighbor> mergeTopK(List<List<Neighbor>> partitionResults, int k) {
        throw new UnsupportedOperationException("TODO");
    }
}
