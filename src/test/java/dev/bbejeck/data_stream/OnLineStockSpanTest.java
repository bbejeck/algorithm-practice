package dev.bbejeck.data_stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OnLineStockSpan (LeetCode Problem 901)
 */
public class OnLineStockSpanTest {

    private OnLineStockSpan stockSpanner;

    @BeforeEach
    void setUp() {
        stockSpanner = new OnLineStockSpan();
    }

    @Test
    void testExample1FromLeetCode() {
        // Test case from LeetCode: [100, 80, 60, 70, 60, 75, 85]
        // Expected spans:           [1,   1,  1,  2,  1,  4,  6]

        assertEquals(1, stockSpanner.next(100)); // span = 1
        assertEquals(1, stockSpanner.next(80));  // span = 1
        assertEquals(1, stockSpanner.next(60));  // span = 1
        assertEquals(2, stockSpanner.next(70));  // span = 2 (70, 60)
        assertEquals(1, stockSpanner.next(60));  // span = 1
        assertEquals(4, stockSpanner.next(75));  // span = 4 (75, 60, 70, 60)
        assertEquals(6, stockSpanner.next(85));  // span = 6 (85, 75, 60, 70, 60, 80)
    }

    @Test
    void testSinglePrice() {
        assertEquals(1, stockSpanner.next(50));
    }

    @Test
    void testIncreasingPrices() {
        // All increasing prices - each should have span of all previous + current
        assertEquals(1, stockSpanner.next(10)); // span = 1
        assertEquals(2, stockSpanner.next(20)); // span = 2 (20, 10)
        assertEquals(3, stockSpanner.next(30)); // span = 3 (30, 20, 10)
        assertEquals(4, stockSpanner.next(40)); // span = 4 (40, 30, 20, 10)
    }

    @Test
    void testDecreasingPrices() {
        // All decreasing prices - each should have span of 1
        assertEquals(1, stockSpanner.next(40)); // span = 1
        assertEquals(1, stockSpanner.next(30)); // span = 1
        assertEquals(1, stockSpanner.next(20)); // span = 1
        assertEquals(1, stockSpanner.next(10)); // span = 1
    }

    @Test
    void testEqualPrices() {
        // Equal prices should accumulate spans
        assertEquals(1, stockSpanner.next(50)); // span = 1
        assertEquals(2, stockSpanner.next(50)); // span = 2 (both 50s)
        assertEquals(3, stockSpanner.next(50)); // span = 3 (all three 50s)
        assertEquals(1, stockSpanner.next(40)); // span = 1 (40 < 50)
        assertEquals(5, stockSpanner.next(50)); // span = 5 (50, 40, 50, 50, 50)
    }

    @Test
    void testMixedPattern() {
        // More complex pattern
        assertEquals(1, stockSpanner.next(31)); // span = 1
        assertEquals(2, stockSpanner.next(41)); // span = 1
        assertEquals(3, stockSpanner.next(48)); // span = 1
        assertEquals(4, stockSpanner.next(59)); // span = 1
        assertEquals(5, stockSpanner.next(79)); // span = 2 (79, 59)
        assertEquals(1, stockSpanner.next(75)); // span = 1
        assertEquals(1, stockSpanner.next(71)); // span = 1
        assertEquals(3, stockSpanner.next(76)); // span = 3 (76, 71, 75)
        assertEquals(1, stockSpanner.next(73)); // span = 8 (73, 76, 71, 75, 79, 59, 48, 41)
    }

    @Test
    void testLargeNumbers() {
        // Test with larger numbers to ensure no overflow issues
        assertEquals(1, stockSpanner.next(1000000));
        assertEquals(1, stockSpanner.next(999999));
        assertEquals(3, stockSpanner.next(1000001));
    }

    @Test
    void testMinimumValues() {
        // Test with minimum constraints (price >= 1)
        assertEquals(1, stockSpanner.next(1));
        assertEquals(2, stockSpanner.next(1));
        assertEquals(3, stockSpanner.next(2));
    }
}