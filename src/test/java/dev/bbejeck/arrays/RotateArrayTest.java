package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class RotateArrayTest {

    private RotateArray rotateArray;

    @BeforeEach
    void setUp() {
        rotateArray = new RotateArray();
    }

    @Test
    void testRotateWithEmptyArray() {
        int[] nums = {};
        rotateArray.rotate(nums, 3);
        assertArrayEquals(new int[]{}, nums);
    }

    @Test
    void testRotateWithSingleElement() {
        int[] nums = {5};
        rotateArray.rotate(nums, 3);
        assertArrayEquals(new int[]{5}, nums);
    }

    @Test
    void testRotateWithZeroK() {
        int[] nums = {1, 2, 3, 4, 5};
        rotateArray.rotate(nums, 0);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, nums);
    }

    @Test
    void testRotateWithKEqualToLength() {
        int[] nums = {1, 2, 3, 4, 5};
        rotateArray.rotate(nums, 5);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, nums);
    }

    @Test
    void testRotateWithKGreaterThanLength() {
        int[] nums = {1, 2, 3, 4, 5};
        rotateArray.rotate(nums, 7);
        // 7 % 5 = 2, so rotate by 2
        assertArrayEquals(new int[]{4, 5, 1, 2, 3}, nums);
    }

    @Test
    void testRotateByOne() {
        int[] nums = {1, 2, 3, 4, 5};
        rotateArray.rotate(nums, 1);
        assertArrayEquals(new int[]{5, 1, 2, 3, 4}, nums);
    }

    @Test
    void testRotateByTwo() {
        int[] nums = {1, 2, 3, 4, 5};
        rotateArray.rotate(nums, 2);
        assertArrayEquals(new int[]{4, 5, 1, 2, 3}, nums);
    }

    @Test
    void testRotateWithNegativeNumbers() {
        int[] nums = {-1, -100, 3, 99};
        rotateArray.rotate(nums, 2);
        assertArrayEquals(new int[]{3, 99, -1, -100}, nums);
    }

    @Test
    void testRotateWithLargeArray() {
        int[] nums = new int[1000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i;
        }

        int[] expected = new int[1000];
        for (int i = 0; i < nums.length; i++) {
            expected[(i + 100) % 1000] = i;
        }

        rotateArray.rotate(nums, 100);
        assertArrayEquals(expected, nums);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testRotateWithVariousCases(int[] input, int k, int[] expected) {
        rotateArray.rotate(input, k);
        assertArrayEquals(expected, input,
                "Failed with input " + Arrays.toString(input) + ", k=" + k);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(new int[]{1, 2, 3, 4, 5, 6, 7}, 3, new int[]{5, 6, 7, 1, 2, 3, 4}),
                Arguments.of(new int[]{-1, -2, -3, -4, -5, -6}, 2, new int[]{-5, -6, -1, -2, -3, -4}),
                Arguments.of(new int[]{1, 2}, 1, new int[]{2, 1}),
                Arguments.of(new int[]{1, 2, 3}, 4, new int[]{3, 1, 2}),  // 4 % 3 = 1
                Arguments.of(new int[]{1, 1, 1, 1, 1}, 3, new int[]{1, 1, 1, 1, 1}),
                Arguments.of(new int[]{0, 0, 0, 0, 1}, 1, new int[]{1, 0, 0, 0, 0})
        );
    }

    @Test
    void testEdgeCaseWithVeryLargeK() {
        // Test with k much larger than array length
        int[] nums = {1, 2, 3, 4};
        rotateArray.rotate(nums, 10000000);  // 10000000 % 4 = 0
        assertArrayEquals(new int[]{1, 2, 3, 4}, nums);

        rotateArray.rotate(nums, 10000001);  // 10000001 % 4 = 1
        assertArrayEquals(new int[]{4, 1, 2, 3}, nums);
    }

    @Test
    void testWithMultipleRotations() {
        int[] nums = {1, 2, 3, 4, 5};

        // Rotate by 2
        rotateArray.rotate(nums, 2);
        assertArrayEquals(new int[]{4, 5, 1, 2, 3}, nums);

        // Rotate by 3 more (total rotation: 5 which is full cycle)
        rotateArray.rotate(nums, 3);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, nums);
    }
}