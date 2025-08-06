package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the ContainerWithMostWater class.
 * Constraints:
 * - n == height.length
 * - 2 <= n <= 10^5
 * - 0 <= height[i] <= 10^4
 */
public class ContainerWithMostWaterTest {

    private ContainerWithMostWater container;

    @BeforeEach
    void setUp() {
        container = new ContainerWithMostWater();
    }

    @Test
    @DisplayName("Should return correct area for minimum valid array size (n=2)")
    void testMinimumArraySize() {
        int[] heights = {3, 7};
        assertEquals(3, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should return correct area when heights include 0")
    void testWithZeroHeight() {
        int[] heights = {0, 5, 0, 8, 0};
        assertEquals(10, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should return correct area for all zero heights")
    void testAllZeroHeights() {
        int[] heights = {0, 0, 0, 0};
        assertEquals(0, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should return correct area for example case")
    void testExampleCase() {
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        assertEquals(49, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should handle max height values (10^4)")
    void testMaxHeightValues() {
        int[] heights = {10000, 10000};
        assertEquals(10000, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should handle declining heights")
    void testDecliningHeights() {
        int[] heights = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        assertEquals(20, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should handle increasing heights")
    void testIncreasingHeights() {
        int[] heights = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals(20, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should handle case when tallest walls are at the ends")
    void testTallestWallsAtEnds() {
        int[] heights = {8, 1, 2, 3, 4, 5, 6, 7, 9};
        assertEquals(64, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should handle case when tallest walls are in the middle")
    void testTallestWallsInMiddle() {
        int[] heights = {1, 2, 9, 4, 5, 10, 3, 2, 1};
        assertEquals(27, container.maxArea(heights));
    }

    @Test
    @DisplayName("Should correctly calculate area for large array")
    void testLargeArray() {
        // Create a larger array (still within constraints)
        int[] heights = new int[1000];
        for (int i = 0; i < heights.length; i++) {
            heights[i] = i % 100; // Values between 0 and 99
        }

        // Expected: The maximum area should be calculated by the algorithm
        int result = container.maxArea(heights);

        // We can verify the result is reasonable (not negative, not zero for this data)
        assertTrue(result > 0);

        // For a more precise test, we can calculate the expected value manually
        // With this pattern, max area would be between indices 99 and 999
        // min(99, 99) * (999-99) = 99 * 900 = 89100
        assertEquals(89100, result);
    }
}