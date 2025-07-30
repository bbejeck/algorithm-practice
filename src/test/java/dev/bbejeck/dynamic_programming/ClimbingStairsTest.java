package dev.bbejeck.dynamic_programming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ClimbingStairs class
 * Tests the dynamic programming solution for LeetCode problem #70
 */
public class ClimbingStairsTest {

    private ClimbingStairs climbingStairs;

    @BeforeEach
    void setUp() {
        climbingStairs = new ClimbingStairs();
    }

    @Test
    void testBaseCases() {
        // Test n = 1: only one way (1 step)
        assertEquals(1, climbingStairs.climbStairs(1));

        // Test n = 2: two ways (1+1 or 2)
        assertEquals(2, climbingStairs.climbStairs(2));
    }

    @Test
    void testSmallValues() {
        // Test n = 3: three ways (1+1+1, 1+2, 2+1)
        assertEquals(3, climbingStairs.climbStairs(3));

        // Test n = 4: five ways
        assertEquals(5, climbingStairs.climbStairs(4));

        // Test n = 5: eight ways
        assertEquals(8, climbingStairs.climbStairs(5));
    }

    @Test
    void testFibonacciSequence() {
        // The climbing stairs follows Fibonacci sequence
        // f(1)=1, f(2)=2, f(3)=3, f(4)=5, f(5)=8, f(6)=13, f(7)=21
        assertEquals(13, climbingStairs.climbStairs(6));
        assertEquals(21, climbingStairs.climbStairs(7));
        assertEquals(34, climbingStairs.climbStairs(8));
    }

    @Test
    void testLargerValues() {
        // Test some larger values to ensure algorithm works efficiently
        assertEquals(89, climbingStairs.climbStairs(10));
        assertEquals(987, climbingStairs.climbStairs(15));
        assertEquals(10946, climbingStairs.climbStairs(20));
    }

    @Test
    void testEdgeCaseMinimum() {
        // Test the minimum valid input
        assertEquals(1, climbingStairs.climbStairs(1));
    }

    @Test
    void testSequentialValues() {
        // Test that each value follows the pattern f(n) = f(n-1) + f(n-2)
        int prev2 = climbingStairs.climbStairs(1); // 1
        int prev1 = climbingStairs.climbStairs(2); // 2

        for (int i = 3; i <= 10; i++) {
            int current = climbingStairs.climbStairs(i);
            int expected = prev1 + prev2;
            assertEquals(expected, current,
                    "For n=" + i + ", expected f(" + (i-1) + ") + f(" + (i-2) + ") = " + expected);
            prev2 = prev1;
            prev1 = current;
        }
    }

    @Test
    void testPerformance() {
        // Test that larger inputs complete in reasonable time
        assertDoesNotThrow(() -> {
            int result = climbingStairs.climbStairs(45);
            assertTrue(result > 0, "Result should be positive for valid input");
        });
    }
}