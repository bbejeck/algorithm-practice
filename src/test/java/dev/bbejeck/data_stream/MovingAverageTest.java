package dev.bbejeck.data_stream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MovingAverage class
 */
public class MovingAverageTest {

    @Test
    void testSingleElementWindow() {
        MovingAverage ma = new MovingAverage(1);
        assertEquals(1.0, ma.next(1), 0.0001);
        assertEquals(2.0, ma.next(2), 0.0001);
        assertEquals(3.0, ma.next(3), 0.0001);
    }

    @Test
    void testBasicMovingAverageSequence() {
        MovingAverage ma = new MovingAverage(3);

        assertEquals(1.0, ma.next(1), 0.0001);        // [1] -> 1/1 = 1.0
        assertEquals(1.5, ma.next(2), 0.0001);        // [1,2] -> 3/2 = 1.5
        assertEquals(2.0, ma.next(3), 0.0001);        // [1,2,3] -> 6/3 = 2.0
        assertEquals(3.0, ma.next(4), 0.0001);        // [2,3,4] -> 9/3 = 3.0
        assertEquals(4.0, ma.next(5), 0.0001);        // [3,4,5] -> 12/3 = 4.0
    }

    @Test
    void testLeetCodeExample() {
        // From LeetCode problem description
        MovingAverage ma = new MovingAverage(3);

        assertEquals(1.0, ma.next(1), 0.0001);
        assertEquals(5.5, ma.next(10), 0.0001);       // (1+10)/2 = 5.5
        assertEquals(4.66667, ma.next(3), 0.001);     // (1+10+3)/3 = 4.666...
        assertEquals(6.0, ma.next(5), 0.0001);        // (10+3+5)/3 = 6.0
    }

    @Test
    void testWindowSizeLargerThanInputs() {
        MovingAverage ma = new MovingAverage(5);

        assertEquals(1.0, ma.next(1), 0.0001);        // [1] -> 1/1 = 1.0
        assertEquals(1.5, ma.next(2), 0.0001);        // [1,2] -> 3/2 = 1.5
        assertEquals(2.0, ma.next(3), 0.0001);        // [1,2,3] -> 6/3 = 2.0
    }

    @Test
    void testNegativeNumbers() {
        MovingAverage ma = new MovingAverage(2);

        assertEquals(-1.0, ma.next(-1), 0.0001);      // [-1] -> -1/1 = -1.0
        assertEquals(-1.5, ma.next(-2), 0.0001);      // [-1,-2] -> -3/2 = -1.5
        assertEquals(-2.5, ma.next(-3), 0.0001);      // [-2,-3] -> -5/2 = -2.5
    }

    @Test
    void testMixedPositiveNegativeNumbers() {
        MovingAverage ma = new MovingAverage(3);

        assertEquals(5.0, ma.next(5), 0.0001);        // [5] -> 5/1 = 5.0
        assertEquals(2.5, ma.next(0), 0.0001);        // [5,0] -> 5/2 = 2.5
        assertEquals(0.0, ma.next(-5), 0.0001);       // [5,0,-5] -> 0/3 = 0.0
        assertEquals(-3.33333, ma.next(-5), 0.0001);      // [0,-5,-5] -> -10/3 = -3.333...
    }

    @Test
    void testZeroValues() {
        MovingAverage ma = new MovingAverage(2);

        assertEquals(0.0, ma.next(0), 0.0001);        // [0] -> 0/1 = 0.0
        assertEquals(0.0, ma.next(0), 0.0001);        // [0,0] -> 0/2 = 0.0
        assertEquals(2.5, ma.next(5), 0.0001);        // [0,5] -> 5/2 = 2.5
    }

    @Test
    void testLargeWindow() {
        MovingAverage ma = new MovingAverage(1000);

        // Add 100 numbers (1 to 100)
        double sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
            double expected = sum / i;
            assertEquals(expected, ma.next(i), 0.0001);
        }
    }

    @Test
    void testCircularBufferBehavior() {
        MovingAverage ma = new MovingAverage(2);

        // First fill the buffer
        assertEquals(1.0, ma.next(1), 0.0001);        // [1] -> 1.0
        assertEquals(1.5, ma.next(2), 0.0001);        // [1,2] -> 1.5

        // Now test circular behavior
        assertEquals(2.5, ma.next(3), 0.0001);        // [2,3] -> 2.5 (replaced 1)
        assertEquals(3.5, ma.next(4), 0.0001);        // [3,4] -> 3.5 (replaced 2)
        assertEquals(4.5, ma.next(5), 0.0001);        // [4,5] -> 4.5 (replaced 3)
    }

    @Test
    void testRepeatedValues() {
        MovingAverage ma = new MovingAverage(3);

        assertEquals(5.0, ma.next(5), 0.0001);
        assertEquals(5.0, ma.next(5), 0.0001);
        assertEquals(5.0, ma.next(5), 0.0001);
        assertEquals(5.0, ma.next(5), 0.0001);        // All 5s should give 5.0
    }

    @Test
    void testIntegerOverflowScenario() {
        MovingAverage ma = new MovingAverage(3);

        // Test with large values that might cause issues
        assertEquals(1000000.0, ma.next(1000000), 0.0001);
        assertEquals(1500000.0, ma.next(2000000), 0.0001);
        assertEquals(2000000.0, ma.next(3000000), 0.0001);
    }

    @Test
    void testDecimalPrecision() {
        MovingAverage ma = new MovingAverage(3);

        assertEquals(1.0, ma.next(1), 0.0001);
        assertEquals(1.5, ma.next(2), 0.0001);

        // 1 + 2 + 4 = 7, 7/3 = 2.333...
        assertEquals(2.33333, ma.next(4), 0.001);

        // 2 + 4 + 7 = 13, 13/3 = 4.333...
        assertEquals(4.33333, ma.next(7), 0.001);
    }
}