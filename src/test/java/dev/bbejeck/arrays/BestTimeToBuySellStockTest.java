package dev.bbejeck.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BestTimeToBuySellStockTest {

    private final BestTimeToBuySellStock solution = new BestTimeToBuySellStock();

    @Test
    public void testBasicCases() {
        // Regular case from the example
        assertEquals(5, solution.maxProfit(new int[]{7,1,5,3,6,4}));

        // No profit possible (decreasing sequence)
        assertEquals(0, solution.maxProfit(new int[]{7,6,4,3,1}));
    }

    @Test
    public void testEdgeCases() {
        // Single element array
        assertEquals(0, solution.maxProfit(new int[]{1}));

        // Two elements - profit possible
        assertEquals(1, solution.maxProfit(new int[]{1,2}));

        // Two elements - no profit possible
        assertEquals(0, solution.maxProfit(new int[]{2,1}));

        // Array with all same values
        assertEquals(0, solution.maxProfit(new int[]{1,1,1,1}));
    }

    @Test
    public void testSpecialCases() {
        // Maximum possible difference
        assertEquals(Integer.MAX_VALUE - 1,
                solution.maxProfit(new int[]{1, Integer.MAX_VALUE}));

        // Very large array with profit at the end
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = 10000 - i;
        }
        largeArray[9999] = 20000; // profit possibility at the very end
       assertEquals(19998, solution.maxProfit(largeArray));

        // V-shaped array
        assertEquals(4, solution.maxProfit(new int[]{5,4,3,2,1,5}));

        // Mountain-shaped array
        assertEquals(4, solution.maxProfit(new int[]{1,2,5,3,2,1}));
    }

    @Test
    public void testBoundaryValues() {
        // Array with maximum possible values
        assertEquals(0, solution.maxProfit(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}));

        // Array with minimum possible values
        assertEquals(0, solution.maxProfit(new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE}));

        // Array with both extremes
        assertEquals(Integer.MAX_VALUE - Integer.MIN_VALUE - 1,
                solution.maxProfit(new int[]{Integer.MIN_VALUE + 1, Integer.MAX_VALUE}));
    }

    @Test
    public void testPatternCases() {
        // Saw-tooth pattern
        assertEquals(2, solution.maxProfit(new int[]{1,3,1,3,1,3}));

        // Plateau pattern
        assertEquals(2, solution.maxProfit(new int[]{1,1,1,3,3,3}));

        // Steps pattern
        assertEquals(4, solution.maxProfit(new int[]{1,2,2,3,3,4,4,5}));
    }

    @Test
    public void testMinimalProfitCases() {
        // Profit of 1
        assertEquals(1, solution.maxProfit(new int[]{2,3}));

        // Multiple small opportunities
        assertEquals(1, solution.maxProfit(new int[]{1,2,1,2,1,2}));
    }
}