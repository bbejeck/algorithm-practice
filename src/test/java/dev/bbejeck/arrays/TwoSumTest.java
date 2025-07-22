package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TwoSum class
 */
public class TwoSumTest {

    private TwoSum twoSum;

    @BeforeEach
    void setUp() {
        twoSum = new TwoSum();
    }

    @Test
    @DisplayName("Test with exactly two elements that sum to target")
    void testTwoElementsArray() {
        int[] nums = {2, 7};
        int target = 9;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertTrue((result[0] == 0 && result[1] == 1) || (result[0] == 1 && result[1] == 0),
                "Should return indices 0 and 1 for two-element array");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target");
    }

    @Test
    @DisplayName("Test classic example: [2,7,11,15], target = 9")
    void testClassicExample() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target");
        assertTrue(result[0] != result[1], "Indices should be different");
    }

    @Test
    @DisplayName("Test with duplicate numbers: [3,2,4], target = 6")
    void testWithDuplicateNumbers() {
        int[] nums = {3, 2, 4};
        int target = 6;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target");
        assertTrue(result[0] != result[1], "Indices should be different");
    }

    @Test
    @DisplayName("Test with same number used twice: [3,3], target = 6")
    void testSameNumberTwice() {
        int[] nums = {3, 3};
        int target = 6;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertEquals(0, result[0], "First index should be 0");
        assertEquals(1, result[1], "Second index should be 1");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target");
    }

    @ParameterizedTest
    @MethodSource("provideTwoSumTestCases")
    @DisplayName("Test various two sum scenarios")
    void testTwoSumParameterized(int[] nums, int target, String description) {
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertTrue(result[0] >= 0 && result[0] < nums.length,
                "First index should be valid");
        assertTrue(result[1] >= 0 && result[1] < nums.length,
                "Second index should be valid");
        assertTrue(result[0] != result[1], "Indices should be different");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target: " + description);
    }

    private static Stream<Arguments> provideTwoSumTestCases() {
        return Stream.of(
                Arguments.of(new int[]{2, 7, 11, 15}, 9, "Classic example"),
                Arguments.of(new int[]{3, 2, 4}, 6, "Target found with non-adjacent elements"),
                Arguments.of(new int[]{-1, -2, -3, -4, -5}, -8, "Negative numbers"),
                Arguments.of(new int[]{0, 4, 3, 0}, 0, "Zero sum with duplicate zeros"),
                Arguments.of(new int[]{-3, 4, 3, 90}, 0, "Zero sum with positive and negative"),
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 9, "Target at end of array"),
                Arguments.of(new int[]{5, 4, 3, 2, 1}, 9, "Reverse order array"),
                Arguments.of(new int[]{1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 7, 1, 1, 1, 1, 1}, 11, "Many duplicates with solution at end"),
                Arguments.of(new int[]{230, 863, 916, 585, 981, 404, 316, 785, 88, 12, 70, 435, 384, 778, 887, 755, 740, 337, 86, 92, 325, 422, 815, 650, 920, 125, 277, 336, 221, 847, 168, 23, 677, 61, 400, 136, 874, 363, 394, 199, 863, 997, 794, 587, 124, 321, 212, 957, 764, 173, 314, 422, 927, 783, 930, 282, 306, 506, 44, 926, 691, 568, 68, 730, 933, 737, 531, 180, 414, 751, 28, 546, 60, 371, 493, 370, 527, 387, 43, 541, 13, 457, 328, 227, 652, 365, 430, 803, 59, 858, 538, 427, 583, 368, 375, 173, 809, 896, 370, 789}, 542, "Large array test")
        );
    }

    @Test
    @DisplayName("Test with negative numbers")
    void testNegativeNumbers() {
        int[] nums = {-1, -2, -3, -4, -5};
        int target = -8;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target");
    }

    @Test
    @DisplayName("Test with zero values")
    void testWithZeros() {
        int[] nums = {0, 4, 3, 0};
        int target = 0;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target");
    }

    @Test
    @DisplayName("Test edge case with minimum array size")
    void testMinimumArraySize() {
        int[] nums = {1, 2};
        int target = 3;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertEquals(0, result[0], "First index should be 0");
        assertEquals(1, result[1], "Second index should be 1");
    }

    @Test
    @DisplayName("Test solution uniqueness - should return first valid pair found")
    void testSolutionUniqueness() {
        // Array where multiple pairs could sum to target, but algorithm should return first found
        int[] nums = {3, 3, 2, 4};
        int target = 6;
        int[] result = twoSum.twoSum(nums, target);

        assertEquals(2, result.length, "Result should contain exactly 2 indices");
        assertEquals(target, nums[result[0]] + nums[result[1]],
                "Elements at returned indices should sum to target");

        // The algorithm should find one of the valid pairs
        boolean isValidPair = (nums[result[0]] == 3 && nums[result[1]] == 3) ||
                (nums[result[0]] == 2 && nums[result[1]] == 4) ||
                (nums[result[0]] == 4 && nums[result[1]] == 2);
        assertTrue(isValidPair, "Should return a valid pair that sums to target");
    }
}