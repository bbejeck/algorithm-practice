package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrappingRainwaterTest {

    private TrappingRainwater rainwaterSolver;

    @BeforeEach
    void setUp() {
        rainwaterSolver = new TrappingRainwater();
    }

    @Test
    @DisplayName("Test example case with multiple traps")
    void testStandardExample() {
        int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        assertEquals(6, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with no trapped water")
    void testNoTrappedWater() {
        int[] height = {1, 2, 3, 4, 5};
        assertEquals(0, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with descending heights")
    void testDescendingHeights() {
        int[] height = {5, 4, 3, 2, 1};
        assertEquals(0, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with single deep trap")
    void testSingleDeepTrap() {
        int[] height = {5, 0, 5};
        assertEquals(5, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with multiple valleys")
    void testMultipleValleys() {
        int[] height = {3, 0, 2, 0, 4};
        assertEquals(7, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with flat middle section")
    void testFlatMiddleSection() {
        int[] height = {4, 2, 2, 2, 4};
        assertEquals(6, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with empty array")
    void testEmptyArray() {
        int[] height = {};
        assertEquals(0, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with single element")
    void testSingleElement() {
        int[] height = {5};
        assertEquals(0, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with two elements")
    void testTwoElements() {
        int[] height = {5, 3};
        assertEquals(0, rainwaterSolver.trap(height));
    }

    @Test
    @DisplayName("Test case with LeetCode example")
    void testLeetCodeExample() {
        int[] height = {4, 2, 0, 3, 2, 5};
        assertEquals(9, rainwaterSolver.trap(height));
    }
}