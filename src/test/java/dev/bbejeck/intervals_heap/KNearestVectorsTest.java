package dev.bbejeck.intervals_heap;

import dev.bbejeck.intervals_heap.KNearestVectors.Metric;
import dev.bbejeck.intervals_heap.KNearestVectors.Neighbor;
import dev.bbejeck.intervals_heap.KNearestVectors.Vector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KNearestVectors
 */
public class KNearestVectorsTest {

    private final KNearestVectors knn = new KNearestVectors();

    private static Vector v(int id, float... values) {
        return new Vector(id, values);
    }

    private static List<Integer> ids(List<Neighbor> neighbors) {
        return neighbors.stream().map(Neighbor::id).toList();
    }

    // ---------------- kNearest ----------------

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        List<Vector> vectors = List.of(v(1, 3, 4), v(2, 1, 1), v(3, -1, 1));
        List<Neighbor> result = knn.kNearest(vectors, new float[]{0, 0}, 2, Metric.L2);
        assertEquals(List.of(new Neighbor(2, 2.0), new Neighbor(3, 2.0)), result);
    }

    @Test
    @DisplayName("Results come back closest first")
    void testSortedClosestFirst() {
        List<Vector> vectors = List.of(v(1, 10), v(2, 1), v(3, 5), v(4, 3));
        assertEquals(List.of(2, 4, 3), ids(knn.kNearest(vectors, new float[]{0}, 3, Metric.L2)));
    }

    @Test
    @DisplayName("Ties on distance go to the smaller id, even if it comes later in the input")
    void testTieBreakById() {
        List<Vector> vectors = List.of(v(9, 1, 0), v(5, 0, 1), v(7, -1, 0), v(1, 0, -1));
        assertEquals(List.of(1, 5), ids(knn.kNearest(vectors, new float[]{0, 0}, 2, Metric.L2)));
    }

    @Test
    @DisplayName("k <= 0 returns an empty list")
    void testInvalidK() {
        List<Vector> vectors = List.of(v(1, 1));
        assertEquals(List.of(), knn.kNearest(vectors, new float[]{0}, 0, Metric.L2));
        assertEquals(List.of(), knn.kNearest(vectors, new float[]{0}, -1, Metric.L2));
    }

    @Test
    @DisplayName("Fewer than k vectors returns all of them")
    void testFewerThanK() {
        List<Vector> vectors = List.of(v(1, 2), v(2, 1));
        assertEquals(List.of(2, 1), ids(knn.kNearest(vectors, new float[]{0}, 10, Metric.L2)));
    }

    @Test
    @DisplayName("Empty input returns an empty list")
    void testEmpty() {
        assertEquals(List.of(), knn.kNearest(List.of(), new float[]{0}, 3, Metric.L2));
    }

    @Test
    @DisplayName("L2 distance is squared and handles negative values")
    void testL2Squared() {
        List<Neighbor> result = knn.kNearest(List.of(v(1, -2, 3)), new float[]{1, -1}, 1, Metric.L2);
        assertEquals(9 + 16, result.get(0).distance(), 1e-9);
    }

    @Test
    @DisplayName("Cosine ignores magnitude and only compares direction")
    void testCosineIgnoresMagnitude() {
        List<Vector> vectors = List.of(v(1, 0, 1), v(2, 100, 0), v(3, 1, 1), v(4, -1, 0));
        List<Neighbor> result = knn.kNearest(vectors, new float[]{1, 0}, 4, Metric.COSINE);
        assertEquals(List.of(2, 3, 1, 4), ids(result));
        assertEquals(0.0, result.get(0).distance(), 1e-9);
        assertEquals(1 - 1 / Math.sqrt(2), result.get(1).distance(), 1e-9);
        assertEquals(1.0, result.get(2).distance(), 1e-9);
        assertEquals(2.0, result.get(3).distance(), 1e-9);
    }

    @Test
    @DisplayName("Cosine skips zero vectors")
    void testCosineSkipsZeroVectors() {
        List<Vector> vectors = List.of(v(1, 0, 0), v(2, 1, 1));
        assertEquals(List.of(2), ids(knn.kNearest(vectors, new float[]{1, 0}, 2, Metric.COSINE)));
    }

    @Test
    @DisplayName("Cosine with a zero query matches nothing")
    void testCosineZeroQuery() {
        List<Vector> vectors = List.of(v(1, 1, 0), v(2, 1, 1));
        assertEquals(List.of(), knn.kNearest(vectors, new float[]{0, 0}, 2, Metric.COSINE));
    }

    @Test
    @DisplayName("A dimension mismatch throws")
    void testDimensionMismatch() {
        List<Vector> vectors = List.of(v(1, 1, 2), v(2, 1, 2, 3));
        assertThrows(IllegalArgumentException.class,
                () -> knn.kNearest(vectors, new float[]{0, 0}, 1, Metric.L2));
        assertThrows(IllegalArgumentException.class,
                () -> knn.kNearest(vectors, new float[]{0, 0}, 1, Metric.COSINE));
    }

    @Test
    @DisplayName("Matches sorting every vector, on 20,000 random 64-dimension vectors")
    void testMatchesFullSort() {
        Random random = new Random(42);
        List<Vector> vectors = randomVectors(random, 20_000, 64, 0);
        float[] query = randomValues(random, 64);
        for (Metric metric : Metric.values()) {
            List<Neighbor> all = knn.kNearest(vectors, query, vectors.size(), metric);
            assertEquals(all.subList(0, 25), knn.kNearest(vectors, query, 25, metric));
        }
    }

    // ---------------- follow-up: mergeTopK ----------------

    @Test
    @DisplayName("Merge: interleaves partitions into one top k")
    void testMerge() {
        List<List<Neighbor>> partitions = List.of(
                List.of(new Neighbor(1, 0.1), new Neighbor(2, 0.5), new Neighbor(3, 0.9)),
                List.of(new Neighbor(4, 0.2), new Neighbor(5, 0.3)),
                List.of(new Neighbor(6, 0.4)));
        assertEquals(List.of(1, 4, 5, 6), ids(knn.mergeTopK(partitions, 4)));
    }

    @Test
    @DisplayName("Merge: ties go to the smaller id across partitions")
    void testMergeTies() {
        List<List<Neighbor>> partitions = List.of(
                List.of(new Neighbor(8, 1.0)),
                List.of(new Neighbor(3, 1.0)));
        assertEquals(List.of(3, 8), ids(knn.mergeTopK(partitions, 2)));
    }

    @Test
    @DisplayName("Merge: empty partitions and fewer than k results")
    void testMergeEmptyPartitions() {
        List<List<Neighbor>> partitions = List.of(
                List.of(),
                List.of(new Neighbor(1, 0.5)),
                List.of());
        assertEquals(List.of(1), ids(knn.mergeTopK(partitions, 3)));
        assertEquals(List.of(), knn.mergeTopK(List.of(), 3));
        assertEquals(List.of(), knn.mergeTopK(partitions, 0));
    }

    @Test
    @DisplayName("Merge: per-partition top k merged equals top k over all vectors")
    void testMergeMatchesGlobalSearch() {
        Random random = new Random(7);
        int k = 10;
        float[] query = randomValues(random, 16);
        List<Vector> all = new ArrayList<>();
        List<List<Neighbor>> partitionResults = new ArrayList<>();
        for (int p = 0; p < 8; p++) {
            List<Vector> partition = randomVectors(random, 1_000, 16, p * 1_000);
            all.addAll(partition);
            partitionResults.add(knn.kNearest(partition, query, k, Metric.L2));
        }
        assertEquals(knn.kNearest(all, query, k, Metric.L2), knn.mergeTopK(partitionResults, k));
    }

    private static List<Vector> randomVectors(Random random, int n, int dimension, int firstId) {
        List<Vector> vectors = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vectors.add(new Vector(firstId + i, randomValues(random, dimension)));
        }
        return vectors;
    }

    private static float[] randomValues(Random random, int dimension) {
        float[] values = new float[dimension];
        for (int i = 0; i < dimension; i++) {
            values[i] = random.nextFloat() * 2 - 1;
        }
        return values;
    }
}
