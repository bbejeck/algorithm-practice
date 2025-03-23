package dev.bbejeck.dynamic_programming;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CandyTest {

    private final Candy candy = new Candy();

    @Test
    public void testEmptyArray() {
        int[] ratings = {};
        assertEquals(0, candy.candy(ratings), "Empty array should return 0 candies");
    }

    @Test
    public void testSingleChild() {
        int[] ratings = {5};
        assertEquals(1, candy.candy(ratings), "Single child should get exactly 1 candy");
    }

    @Test
    public void testAllSameRatings() {
        int[] ratings = {3, 3, 3, 3, 3};
        assertEquals(5, candy.candy(ratings), "All children with same rating should get 1 candy each");
    }

    @Test
    public void testIncreasingRatings() {
        int[] ratings = {1, 2, 3, 4, 5};
        assertEquals(15, candy.candy(ratings), "Candy distribution for strictly increasing ratings failed");
    }

    @Test
    public void testDecreasingRatings() {
        int[] ratings = {5, 4, 3, 2, 1};
        assertEquals(15, candy.candy(ratings), "Candy distribution for strictly decreasing ratings failed");
    }

    @Test
    public void testPeakInMiddle() {
        int[] ratings = {1, 3, 5, 3, 1};
        assertEquals(9, candy.candy(ratings), "Failed to handle peak in the middle");
    }

    @Test
    public void testValleyInMiddle() {
        int[] ratings = {5, 3, 1, 3, 5};
        assertEquals(11, candy.candy(ratings), "Failed to handle valley in the middle");
    }

    @Test
    public void testLeetcodeExample1() {
        int[] ratings = {1, 0, 2};
        assertEquals(5, candy.candy(ratings), "Failed LeetCode example 1");
    }

    @Test
    public void testLeetcodeExample2() {
        int[] ratings = {1, 2, 2};
        assertEquals(4, candy.candy(ratings), "Failed LeetCode example 2");
    }

    @Test
    public void testComplexCase() {
        int[] ratings = {1, 3, 4, 5, 2, 1, 6, 7, 3};
        assertEquals(19, candy.candy(ratings), "Failed on complex case with multiple peaks and valleys");
    }

    @Test
    public void testEqualNeighbors() {
        int[] ratings = {1, 2, 2, 3, 4, 4, 3};
        assertEquals(12, candy.candy(ratings), "Failed on cases with equal adjacent ratings");
    }
}