package dev.bbejeck.counting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FirstUniqueNumber class
 */
public class FirstUniqueNumberTest {

    @Test
    @DisplayName("Test with example 1: adds eventually eliminate all uniques")
    void testExample1() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{2, 3, 5});
        assertEquals(2, firstUnique.showFirstUnique());
        firstUnique.add(5);
        assertEquals(2, firstUnique.showFirstUnique());
        firstUnique.add(2);
        assertEquals(3, firstUnique.showFirstUnique());
        firstUnique.add(3);
        assertEquals(-1, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test with example 2: unique added at the end becomes first unique")
    void testExample2() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{7, 7, 7, 7, 7, 7});
        assertEquals(-1, firstUnique.showFirstUnique());
        firstUnique.add(7);
        firstUnique.add(3);
        firstUnique.add(3);
        firstUnique.add(7);
        firstUnique.add(17);
        assertEquals(17, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test with example 3: single element removed by duplicate add")
    void testExample3() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{809});
        assertEquals(809, firstUnique.showFirstUnique());
        firstUnique.add(809);
        assertEquals(-1, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test with empty initial array")
    void testEmptyArray() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{});
        assertEquals(-1, firstUnique.showFirstUnique());
        firstUnique.add(4);
        assertEquals(4, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test that duplicates in the initial array are skipped")
    void testDuplicatesInInitialArray() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{1, 2, 1, 3, 2, 4});
        assertEquals(3, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test that insertion order of uniques is preserved")
    void testInsertionOrderPreserved() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{9, 8, 7});
        firstUnique.add(6);
        firstUnique.add(9);
        assertEquals(8, firstUnique.showFirstUnique());
        firstUnique.add(8);
        assertEquals(7, firstUnique.showFirstUnique());
        firstUnique.add(7);
        assertEquals(6, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test that a number never becomes unique again after being duplicated")
    void testNoResurrectionAfterDuplicate() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{5, 5});
        firstUnique.add(5);
        firstUnique.add(5);
        assertEquals(-1, firstUnique.showFirstUnique());
        firstUnique.add(1);
        firstUnique.add(1);
        firstUnique.add(1);
        assertEquals(-1, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test that showFirstUnique does not consume the value")
    void testShowIsIdempotent() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{10, 20});
        assertEquals(10, firstUnique.showFirstUnique());
        assertEquals(10, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test with negative numbers and zero")
    void testNegativeAndZero() {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{0, -3, 0, -2});
        assertEquals(-3, firstUnique.showFirstUnique());
        firstUnique.add(-3);
        assertEquals(-2, firstUnique.showFirstUnique());
    }

    @Test
    @DisplayName("Test with a large number of elements")
    void testLargeInput() {
        int n = 100_000;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i;
        }
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(nums);
        for (int i = 0; i < n - 1; i++) {
            firstUnique.add(i);
        }
        assertEquals(n - 1, firstUnique.showFirstUnique());
    }
}
