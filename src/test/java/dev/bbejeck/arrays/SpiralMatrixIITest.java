package dev.bbejeck.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpiralMatrixIITest {

    private SpiralMatrixII spiralMatrix;

    @BeforeEach
    void setUp() {
        spiralMatrix = new SpiralMatrixII();
    }

    @Test
    void testMinimumInput() {
        // Test n = 1 (edge case - minimum allowed value)
        int[][] result = spiralMatrix.generateMatrix(1);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(1, result[0].length);
        assertEquals(1, result[0][0]);
    }

    @Test
    void testTypicalCase() {
        // Test n = 3 (typical case)
        int[][] expected = {
                {1, 2, 3},
                {8, 9, 4},
                {7, 6, 5}
        };
        int[][] result = spiralMatrix.generateMatrix(3);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEvenSizedMatrix() {
        // Test n = 2 (even-sized matrix)
        int[][] expected = {
                {1, 2},
                {4, 3}
        };
        int[][] result = spiralMatrix.generateMatrix(2);
        assertArrayEquals(expected, result);
    }

    @Test
    void testLargeInput() {
        // Test n = 20 (edge case - maximum allowed value)
        int[][] result = spiralMatrix.generateMatrix(20);
        assertNotNull(result);
        assertEquals(20, result.length);
        assertEquals(20, result[0].length);

        // Check first and last values
        assertEquals(1, result[0][0]);
        assertEquals(400, result[10][9]); // Some middle value

        // Verify consecutive numbers
        boolean[] seen = new boolean[400];
        for (int[] row : result) {
            for (int value : row) {
                assertTrue(value > 0 && value <= 400);
                assertFalse(seen[value - 1], "Number " + value + " appears more than once");
                seen[value - 1] = true;
            }
        }

        // Verify all numbers were used
        for (boolean used : seen) {
            assertTrue(used);
        }
    }

    @Test
    void testMatrixProperties() {
        // Test basic properties for n = 4
        int n = 4;
        int[][] result = spiralMatrix.generateMatrix(n);

        // Test matrix dimensions
        assertEquals(n, result.length);
        assertEquals(n, result[0].length);

        // Verify the resulting 4x4 spiral matrix
        int[][] expected = {
                {1, 2, 3, 4},
                {12, 13, 14, 5},
                {11, 16, 15, 6},
                {10, 9, 8, 7}
        };
        assertArrayEquals(expected, result);
    }
}