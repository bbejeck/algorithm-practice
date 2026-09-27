package dev.bbejeck.window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HitCounterRingBuffer class
 */
public class HitCounterRingBufferTest {

    private HitCounterRingBuffer hitCounter;

    @BeforeEach
    void setUp() {
        hitCounter = new HitCounterRingBuffer();
    }

    @Test
    @DisplayName("Test with example 1: hits at 1, 2, 3, 300")
    void testExample1() {
        hitCounter.hit(1);
        hitCounter.hit(2);
        hitCounter.hit(3);
        assertEquals(3, hitCounter.getHits(4));
        hitCounter.hit(300);
        assertEquals(4, hitCounter.getHits(300));
        assertEquals(3, hitCounter.getHits(301)); // hit at 1 has expired
    }

    @Test
    @DisplayName("Test no hits returns zero")
    void testNoHits() {
        assertEquals(0, hitCounter.getHits(1));
        assertEquals(0, hitCounter.getHits(500));
    }

    @Test
    @DisplayName("Test multiple hits in the same second are all counted")
    void testMultipleHitsSameSecond() {
        hitCounter.hit(10);
        hitCounter.hit(10);
        hitCounter.hit(10);
        assertEquals(3, hitCounter.getHits(10));
    }

    @Test
    @DisplayName("Test getHits sums hits across different seconds in the window")
    void testSumsAcrossWindow() {
        hitCounter.hit(1);
        hitCounter.hit(50);
        hitCounter.hit(50);
        hitCounter.hit(299);
        assertEquals(4, hitCounter.getHits(299));
    }

    @Test
    @DisplayName("Test getHits at a second with no hits still counts earlier hits in the window")
    void testQuerySecondWithNoHits() {
        hitCounter.hit(5);
        hitCounter.hit(6);
        assertEquals(2, hitCounter.getHits(100));
    }

    @Test
    @DisplayName("Test window boundary: hit 299 seconds ago counts, 300 seconds ago does not")
    void testWindowBoundary() {
        hitCounter.hit(100);
        assertEquals(1, hitCounter.getHits(399)); // 399 - 299 = 100, still inside
        assertEquals(0, hitCounter.getHits(400)); // 100 is now expired
    }

    @Test
    @DisplayName("Test all hits expire after the window passes")
    void testAllHitsExpire() {
        hitCounter.hit(1);
        hitCounter.hit(2);
        hitCounter.hit(3);
        assertEquals(0, hitCounter.getHits(1000));
    }

    @Test
    @DisplayName("Test bucket reused after wrap-around resets its count")
    void testBucketWrapAround() {
        hitCounter.hit(5);
        hitCounter.hit(5);
        hitCounter.hit(305); // same bucket as 5, stale count must be replaced
        assertEquals(1, hitCounter.getHits(305));
    }

    @Test
    @DisplayName("Test stale bucket at the query index is not counted")
    void testStaleBucketAtQueryIndex() {
        hitCounter.hit(7);
        hitCounter.hit(7);
        // 607 maps to the same bucket as 7, but the hits at 7 are long expired
        assertEquals(0, hitCounter.getHits(607));
    }
}
