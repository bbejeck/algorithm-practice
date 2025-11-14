package dev.bbejeck.arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ArrayPartition class
 */
public class ArrayPartitionTest {

    private final ArrayPartition arrayPartition = new ArrayPartition();

    @Test
    @DisplayName("Test with example 1: [1,4,3,2]")
    void testExample1() {
        int[] nums = {1, 4, 3, 2};
        int expected = 4; // min(1,2) + min(3,4) = 1 + 3 = 4
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with example 2: [6,2,6,5,1,2]")
    void testExample2() {
        int[] nums = {6, 2, 6, 5, 1, 2};
        int expected = 9; // min(1,2) + min(2,5) + min(6,6) = 1 + 2 + 6 = 9
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with minimum array size (2 elements)")
    void testMinimumSize() {
        int[] nums = {1, 2};
        int expected = 1; // min(1,2) = 1
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with identical pairs")
    void testIdenticalPairs() {
        int[] nums = {1, 1, 2, 2, 3, 3};
        int expected = 6; // min(1,1) + min(2,2) + min(3,3) = 1 + 2 + 3 = 6
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with all same values")
    void testAllSameValues() {
        int[] nums = {5, 5, 5, 5};
        int expected = 10; // min(5,5) + min(5,5) = 5 + 5 = 10
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with negative numbers")
    void testNegativeNumbers() {
        int[] nums = {-1, -2, -3, -4};
        int expected = -6; // After sort: [-4,-3,-2,-1], min(-4,-3) + min(-2,-1) = -4 + -2 = -6
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with mixed positive and negative numbers")
    void testMixedNumbers() {
        int[] nums = {-1, 0, 1, 2};
        int expected = 0; // After sort: [-1,0,1,2], min(-1,0) + min(1,2) = -1 + 1 = 0
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with unsorted array in descending order")
    void testDescendingOrder() {
        int[] nums = {10, 9, 8, 7, 6, 5};
        int expected = 18; // After sort: [5,6,7,8,9,10], min(5,6) + min(7,8) + min(9,10) = 5 + 7 + 9 = 21
        assertEquals(21, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with already sorted array")
    void testAlreadySorted() {
        int[] nums = {1, 2, 3, 4, 5, 6};
        int expected = 9; // min(1,2) + min(3,4) + min(5,6) = 1 + 3 + 5 = 9
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with large numbers")
    void testLargeNumbers() {
        int[] nums = {10000, 9999, 5000, 4999};
        int expected = 9999; // After sort: [4999,5000,9999,10000], min(4999,5000) + min(9999,10000) = 4999 + 9999 = 14998
        assertEquals(14998, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with zeros")
    void testWithZeros() {
        int[] nums = {0, 0, 1, 1};
        int expected = 1; // min(0,0) + min(1,1) = 0 + 1 = 1
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with alternating small and large values")
    void testAlternatingValues() {
        int[] nums = {1, 100, 2, 200};
        int expected = 3; // After sort: [1,2,100,200], min(1,2) + min(100,200) = 1 + 100 = 101
        assertEquals(101, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with larger array (10 elements)")
    void testLargerArray() {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int expected = 25; // min(1,2) + min(3,4) + min(5,6) + min(7,8) + min(9,10) = 1+3+5+7+9 = 25
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with duplicates spread across array")
    void testDuplicatesSpread() {
        int[] nums = {3, 1, 3, 1};
        int expected = 2; // After sort: [1,1,3,3], min(1,1) + min(3,3) = 1 + 3 = 4
        assertEquals(4, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with maximum constraint values")
    void testMaxConstraintValues() {
        int[] nums = {-10000, 10000, -10000, 10000};
        int expected = 0; // After sort: [-10000,-10000,10000,10000], min(-10000,-10000) + min(10000,10000) = -10000 + 10000 = 0
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test optimal pairing strategy")
    void testOptimalPairing() {
        // The strategy is to sort and pair adjacent elements
        // This maximizes the sum by pairing the smallest with the next smallest
        int[] nums = {7, 3, 1, 5};
        int expected = 4; // After sort: [1,3,5,7], min(1,3) + min(5,7) = 1 + 5 = 6
        assertEquals(6, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with sequential numbers")
    void testSequentialNumbers() {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8};
        int expected = 16; // 1+3+5+7 = 16
        assertEquals(expected, arrayPartition.arrayPairSum(nums));
    }

    @Test
    @DisplayName("Test with random unordered array")
    void testRandomUnordered() {
        int[] nums = {8, 2, 6, 4};
        int expected = 6; // After sort: [2,4,6,8], min(2,4) + min(6,8) = 2 + 6 = 8
        assertEquals(8, arrayPartition.arrayPairSum(nums));
    }
}