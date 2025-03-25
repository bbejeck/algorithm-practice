package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JumpGameTest {

    private JumpGame jumpGame;

    @BeforeEach
    void setUp() {
        jumpGame = new JumpGame();
    }

    @Test
    @DisplayName("Test positive example case")
    void testPositiveExample() {
        int[] nums = {2, 3, 1, 1, 4};
        assertTrue(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test negative example case")
    void testNegativeExample() {
        int[] nums = {3, 2, 1, 0, 4};
        assertFalse(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test single element array")
    void testSingleElement() {
        int[] nums = {0};
        assertTrue(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test minimum jumps required")
    void testMinimumJumpsRequired() {
        int[] nums = {1, 1, 1, 1, 1};
        assertTrue(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test exactly reachable")
    void testExactlyReachable() {
        int[] nums = {5, 0, 0, 0, 0, 0};
        assertTrue(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test early zero making end unreachable")
    void testEarlyZero() {
        int[] nums = {0, 1, 2, 3};
        assertFalse(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test zero in the middle that can be jumped over")
    void testZeroInMiddleJumpable() {
        int[] nums = {2, 0, 1, 0, 4};
        assertFalse(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test zero at the end")
    void testZeroAtEnd() {
        int[] nums = {2, 3, 1, 1, 0};
        assertTrue(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test largest possible jump")
    void testLargeJump() {
        int[] nums = new int[25001];
        nums[0] = 25000;
        for (int i = 1; i < 25001; i++) {
            nums[i] = 0;
        }
        assertTrue(jumpGame.canJump(nums));
    }

    @Test
    @DisplayName("Test multiple paths to reach end")
    void testMultiplePaths() {
        int[] nums = {1, 2, 0, 1, 4};
        assertTrue(jumpGame.canJump(nums));
    }
}