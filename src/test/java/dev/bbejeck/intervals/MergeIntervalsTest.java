package dev.bbejeck.intervals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MergeIntervalsTest {

    private final MergeIntervals mergeIntervals = new MergeIntervals();

    @Test
    public void testSingleInterval() {
        int[][] input = {{1, 3}};
        int[][] expected = {{1, 3}};

        int[][] result = mergeIntervals.merge(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testNonOverlappingIntervals() {
        int[][] input = {{1, 3}, {5, 7}, {9, 11}};
        int[][] expected = {{1, 3}, {5, 7}, {9, 11}};

        int[][] result = mergeIntervals.merge(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testOverlappingIntervals() {
        int[][] input = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected = {{1, 6}, {8, 10}, {15, 18}};

        int[][] result = mergeIntervals.merge(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testCompletelyOverlappingIntervals() {
        int[][] input = {{1, 10}, {2, 5}, {3, 7}};
        int[][] expected = {{1, 10}};

        int[][] result = mergeIntervals.merge(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testUnsortedIntervals() {
        int[][] input = {{3, 7}, {1, 4}, {8, 10}, {2, 6}};
        int[][] expected = {{1, 7}, {8, 10}};

        int[][] result = mergeIntervals.merge(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testAdjacentIntervals() {
        int[][] input = {{1, 3}, {3, 6}, {6, 9}};
        int[][] expected = {{1, 9}};

        int[][] result = mergeIntervals.merge(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testEmptyArray() {
        int[][] input = {};
        int[][] expected = {};

        int[][] result = mergeIntervals.merge(input);

        assertArrayEquals(expected, result);
    }

    private void assertArrayEquals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length, "Arrays have different lengths");

        for (int i = 0; i < expected.length; i++) {
            Assertions.assertArrayEquals(expected[i], actual[i],
                    "Difference at interval index " + i);
        }
    }
}