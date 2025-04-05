package dev.bbejeck.window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinimumSizeSubArraySumTest {

    private MinimumSizeSubArraySum subArraySum;

    @BeforeEach
    void setUp() {
        subArraySum = new MinimumSizeSubArraySum();
    }

    @Test
    void testWithSingleElementEqualToTarget() {
        int[] nums = {7};
        int target = 7;

        assertEquals(1, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithSingleElementLessThanTarget() {
        int[] nums = {5};
        int target = 7;

        assertEquals(0, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithSingleElementGreaterThanTarget() {
        int[] nums = {10};
        int target = 7;

        assertEquals(1, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithNoSolutionPossible() {
        int[] nums = {1, 2, 3, 4};
        int target = 15;

        assertEquals(0, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithAllElementsRequired() {
        int[] nums = {1, 2, 3, 4};
        int target = 10;

        assertEquals(4, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithContiguousElementsInMiddle() {
        int[] nums = {2, 3, 1, 2, 4, 3};
        int target = 7;

        assertEquals(2, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithMultiplePossibleSubarrays() {
        int[] nums = {1, 4, 4, 2, 5, 3, 1, 8};
        int target = 8;

        assertEquals(1, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithAllNegativeNumbers() {
        int[] nums = {-1, -2, -3, -4};
        int target = 1;

        assertEquals(0, subArraySum.minSubArrayLen(target, nums));
    }

    @Test
    void testWithLargeNumbers() {
        int[] nums = {10000, 20000, 30000};
        int target = 50000;

        assertEquals(2, subArraySum.minSubArrayLen(target, nums));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testParameterized(int[] nums, int target, int expected) {
        assertEquals(expected, subArraySum.minSubArrayLen(target, nums));
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(new int[]{2, 3, 1, 2, 4, 3}, 7, 2),
                Arguments.of(new int[]{1, 4, 4}, 4, 1),
                Arguments.of(new int[]{1, 1, 1, 1, 1, 1, 1, 1}, 11, 0),
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 15, 5),
                Arguments.of(new int[]{5, 1, 3, 5, 10, 7, 4, 9, 2, 8}, 15, 2),
                Arguments.of(new int[]{}, 5, 0)
        );
    }
}