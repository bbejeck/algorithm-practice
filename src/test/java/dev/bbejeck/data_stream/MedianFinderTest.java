package dev.bbejeck.data_stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MedianFinder class
 */
public class MedianFinderTest {

    private MedianFinder medianFinder;

    @BeforeEach
    void setUp() {
        medianFinder = new MedianFinder();
    }

    @Test
    void testSingleElement() {
        medianFinder.addNum(5);
        assertEquals(5.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testTwoElements() {
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        assertEquals(1.5, medianFinder.median(), 0.0001);
    }

    @Test
    void testThreeElementsOddCount() {
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        medianFinder.addNum(3);
        assertEquals(2.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testFourElementsEvenCount() {
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        medianFinder.addNum(3);
        medianFinder.addNum(4);
        assertEquals(2.5, medianFinder.median(), 0.0001);
    }

    @Test
    void testUnsortedSequence() {
        medianFinder.addNum(6);
        medianFinder.addNum(10);
        medianFinder.addNum(2);
        medianFinder.addNum(6);
        medianFinder.addNum(5);
        medianFinder.addNum(0);
        medianFinder.addNum(6);
        medianFinder.addNum(3);
        medianFinder.addNum(1);
        medianFinder.addNum(0);
        medianFinder.addNum(0);
        // Sorted: [0,0,0,1,2,3,5,6,6,6,10]
        // Median should be 3 (middle element)
        assertEquals(3.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testNegativeNumbers() {
        medianFinder.addNum(-1);
        medianFinder.addNum(-2);
        medianFinder.addNum(-3);
        assertEquals(-2.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testMixedPositiveNegative() {
        medianFinder.addNum(-5);
        medianFinder.addNum(-1);
        medianFinder.addNum(3);
        medianFinder.addNum(7);
        // Sorted: [-5, -1, 3, 7]
        // Median should be (-1 + 3) / 2 = 1.0
        assertEquals(1.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testDuplicateNumbers() {
        medianFinder.addNum(1);
        medianFinder.addNum(1);
        medianFinder.addNum(1);
        assertEquals(1.0, medianFinder.median(), 0.0001);

        medianFinder.addNum(2);
        assertEquals(1.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testLargeNumbers() {
        medianFinder.addNum(Integer.MAX_VALUE);
        medianFinder.addNum(Integer.MIN_VALUE);
        // Median should be (MAX_VALUE + MIN_VALUE) / 2.0 = -0.5
        assertEquals(-0.5, medianFinder.median(), 0.0001);
    }

    @Test
    void testSequentialMedianCalculations() {
        // Test that median is correct after each addition
        medianFinder.addNum(1);
        assertEquals(1.0, medianFinder.median(), 0.0001);

        medianFinder.addNum(2);
        assertEquals(1.5, medianFinder.median(), 0.0001);

        medianFinder.addNum(3);
        assertEquals(2.0, medianFinder.median(), 0.0001);

        medianFinder.addNum(4);
        assertEquals(2.5, medianFinder.median(), 0.0001);

        medianFinder.addNum(5);
        assertEquals(3.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testReverseOrderInsertion() {
        medianFinder.addNum(5);
        medianFinder.addNum(4);
        medianFinder.addNum(3);
        medianFinder.addNum(2);
        medianFinder.addNum(1);
        // Sorted: [1,2,3,4,5]
        // Median should be 3
        assertEquals(3.0, medianFinder.median(), 0.0001);
    }

    @Test
    void testZeroValues() {
        medianFinder.addNum(0);
        assertEquals(0.0, medianFinder.median(), 0.0001);

        medianFinder.addNum(0);
        assertEquals(0.0, medianFinder.median(), 0.0001);

        medianFinder.addNum(1);
        assertEquals(0.0, medianFinder.median(), 0.0001);
    }
}