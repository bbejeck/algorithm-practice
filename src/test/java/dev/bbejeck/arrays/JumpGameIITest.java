package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the JumpGameII class.
 */
public class JumpGameIITest {

    private JumpGameII jumpGame;

    @BeforeEach
    public void setUp() {
        jumpGame = new JumpGameII();
    }

    @Test
    public void testSingleElement() {
        // When there's only one element, no jumps needed
        int[] nums = {0};
        assertEquals(0, jumpGame.jump(nums));
    }

    @Test
    public void testLeetCodeExample1() {
        // LeetCode example: [2,3,1,1,4]
        int[] nums = {2, 3, 1, 1, 4};
        assertEquals(2, jumpGame.jump(nums));
        // Jump 1: Jump from index 0 to 1
        // Jump 2: Jump from index 1 to the end (index 4)
    }

    @Test
    public void testLeetCodeExample2() {
        // Another LeetCode example: [2,3,0,1,4]
        int[] nums = {2, 3, 0, 1, 4};
        assertEquals(2, jumpGame.jump(nums));
        // Jump 1: Jump from index 0 to 1
        // Jump 2: Jump from index 1 to the end (index 4)
    }

    @Test
    public void testJumpExactlyToEnd() {
        // When we can jump exactly to the end
        int[] nums = {1, 2, 3};
        assertEquals(2, jumpGame.jump(nums));
        // Jump 1: From index 0 to 1
        // Jump 2: From index 1 to 2
    }

    @Test
    public void testMaxJumpsEachTime() {
        // When the optimal solution is to make the max possible jump each time
        int[] nums = {5, 9, 3, 2, 1, 0, 2, 3, 3, 1, 0, 0};
        assertEquals(3, jumpGame.jump(nums));
        // Jump 1: From index 0 to index 5
        // Jump 2: From index 5 to index 7
        // Jump 3: From index 7 to the end (index 11)
    }

    @Test
    public void testAllOnes() {
        // When all elements are 1, we need n-1 jumps
        int[] nums = {1, 1, 1, 1, 1};
        assertEquals(4, jumpGame.jump(nums));
        // Need to make a jump at each position
    }

    @Test
    public void testLargeJumpAtStart() {
        // When we can jump to the end in one go
        int[] nums = {10, 1, 1, 1, 1};
        assertEquals(1, jumpGame.jump(nums));
        // Jump 1: From index 0 directly to the end
    }

    @Test
    public void testComplex() {
        // A more complex example
        int[] nums = {3, 4, 3, 2, 5, 4, 3};
        assertEquals(3, jumpGame.jump(nums));
        // Jump 1: From index 0 to index 1
        // Jump 2: From index 1 to index 4
        // Jump 3: From index 4 to the end (index 6)
    }
}