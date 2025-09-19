package dev.bbejeck.data_stream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KthLargest class
 */
public class KthLargestTest {

    @Test
    void testBasicFunctionality() {
        // Test the example from LeetCode problem 703
        int[] nums = {4, 5, 8, 2};
        KthLargest kthLargest = new KthLargest(3, nums);

        assertEquals(4, kthLargest.add(3));  // [2,3,4,5,8] -> 3rd largest is 4
        assertEquals(5, kthLargest.add(5));  // [2,3,4,5,5,8] -> 3rd largest is 5
        assertEquals(5, kthLargest.add(10)); // [2,3,4,5,5,8,10] -> 3rd largest is 5
        assertEquals(8, kthLargest.add(9));  // [2,3,4,5,5,8,9,10] -> 3rd largest is 8
        assertEquals(8, kthLargest.add(4));  // [2,3,4,4,5,5,8,9,10] -> 3rd largest is 8
    }

    @Test
    void testEmptyInitialArray() {
        int[] nums = {};
        KthLargest kthLargest = new KthLargest(1, nums);

        assertEquals(5, kthLargest.add(5));
        assertEquals(5, kthLargest.add(2));  // 1st largest remains 5
        assertEquals(7, kthLargest.add(7));  // 1st largest is now 7
    }

    @Test
    void testSingleElement() {
        int[] nums = {5};
        KthLargest kthLargest = new KthLargest(1, nums);

        assertEquals(5, kthLargest.add(1));  // 1st largest is still 5
        assertEquals(7, kthLargest.add(7));  // 1st largest is now 7
        assertEquals(7, kthLargest.add(3));  // 1st largest remains 7
    }

    @Test
    void testKEqualsArraySize() {
        int[] nums = {3, 1, 4, 2};
        KthLargest kthLargest = new KthLargest(4, nums);

        assertEquals(1, kthLargest.add(0));  // [0,1,2,3,4] -> 4th largest is 1
        assertEquals(1, kthLargest.add(1));  // [0,1,1,2,3,4] -> 4th largest is 1
        assertEquals(2, kthLargest.add(5));  // [0,1,1,2,3,4,5] -> 4th largest is 2
    }

    @Test
    void testKGreaterThanArraySize() {
        int[] nums = {1, 2};
        KthLargest kthLargest = new KthLargest(3, nums);

        assertEquals(1, kthLargest.add(3));  // [1,2,3] -> 3rd largest is 1
        assertEquals(2, kthLargest.add(4));  // [1,2,3,4] -> 3rd largest is 2
        assertEquals(2, kthLargest.add(1));  // [1,1,2,3,4] -> 3rd largest is 2
    }

    @Test
    void testDuplicateValues() {
        int[] nums = {2, 2, 2};
        KthLargest kthLargest = new KthLargest(2, nums);

        assertEquals(2, kthLargest.add(2));  // All 2s, 2nd largest is 2
        assertEquals(2, kthLargest.add(1));  // [1,2,2,2,2] -> 2nd largest is 2
        assertEquals(2, kthLargest.add(3));  // [1,2,2,2,2,3] -> 2nd largest is 2
    }

    @Test
    void testNegativeNumbers() {
        int[] nums = {-1, -3, -2};
        KthLargest kthLargest = new KthLargest(2, nums);

        assertEquals(-2, kthLargest.add(-4)); // [-4,-3,-2,-1] -> 2nd largest is -2
        assertEquals(-1, kthLargest.add(0));  // [-4,-3,-2,-1,0] -> 2nd largest is -1
        assertEquals(-1, kthLargest.add(-5)); // [-5,-4,-3,-2,-1,0] -> 2nd largest is -1
    }

    @Test
    void testLargeK() {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        KthLargest kthLargest = new KthLargest(5, nums);

        assertEquals(7, kthLargest.add(11)); // [1,2,3,4,5,6,7,8,9,10,11] -> 5th largest is 7... wait
        // Actually: we keep only 5 largest, so heap contains [7,8,9,10,11], 5th largest is 7
        // But our implementation keeps 5 elements total, so after adding 11:
        // heap should contain [7,8,9,10,11] and return 7
        assertEquals(8, kthLargest.add(12)); // heap: [8,9,10,11,12], return 8
    }

    @Test
    void testExtremeValues() {
        int[] nums = {Integer.MAX_VALUE, Integer.MIN_VALUE};
        KthLargest kthLargest = new KthLargest(1, nums);

        assertEquals(Integer.MAX_VALUE, kthLargest.add(0));
        assertEquals(Integer.MAX_VALUE, kthLargest.add(Integer.MAX_VALUE - 1));
    }

    @Test
    void testConstructorValidation() {
        int[] nums = {1, 2, 3};

        // Test invalid k values
        assertThrows(IllegalArgumentException.class, () -> {
            new KthLargest(0, nums);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new KthLargest(-1, nums);
        });
    }

    @Test
    void testSequentialOperations() {
        int[] nums = {7, 10, 4, 3, 20, 15};
        KthLargest kthLargest = new KthLargest(3, nums);

        // Initial array sorted: [3,4,7,10,15,20]
        // We keep 3 largest: [10,15,20], so 3rd largest is 10
        assertEquals(10, kthLargest.add(5));  // Add 5: still [10,15,20]
        assertEquals(10, kthLargest.add(8));  // Add 8: still [10,15,20]
        assertEquals(13, kthLargest.add(13)); // Add 13: now [13,15,20]
    }

    @Test
    void testSingleElementHeap() {
        int[] nums = {};
        KthLargest kthLargest = new KthLargest(1, nums);

        assertEquals(1, kthLargest.add(1));
        assertEquals(2, kthLargest.add(2));
        assertEquals(3, kthLargest.add(3));
        assertEquals(4, kthLargest.add(4));
    }

    @Test
    void testMonotonicIncreasing() {
        int[] nums = {};
        KthLargest kthLargest = new KthLargest(3, nums);

        assertEquals(1, kthLargest.add(1)); // [1] -> 1st element
        assertEquals(1, kthLargest.add(2)); // [1,2] -> smaller of 2
        assertEquals(1, kthLargest.add(3)); // [1,2,3] -> 3rd largest is 1
        assertEquals(2, kthLargest.add(4)); // [2,3,4] -> 3rd largest is 2
        assertEquals(3, kthLargest.add(5)); // [3,4,5] -> 3rd largest is 3
    }

    @Test
    void testMonotonicDecreasing() {
        int[] nums = {};
        KthLargest kthLargest = new KthLargest(2, nums);

        assertEquals(5, kthLargest.add(5)); // [5]
        assertEquals(4, kthLargest.add(4)); // [4,5] -> 2nd largest is 4
        assertEquals(4, kthLargest.add(3)); // [4,5] -> 2nd largest is 4
        assertEquals(4, kthLargest.add(3)); // [4,5] -> 2nd largest is 4
    }
}