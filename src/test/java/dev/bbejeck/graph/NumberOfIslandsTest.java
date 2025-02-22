package dev.bbejeck.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberOfIslandsTest {

    private final NumberOfIslands solver = new NumberOfIslands();

    @Test
    public void testEdgeCases() {
        // Test Case 1: Single cell with island
        char[][] singleCell = {
                {'1'}
        };
        assertEquals(1, solver.numIslands(singleCell));

        // Test Case 2: Single cell without island
        char[][] singleCellNoIsland = {
                {'0'}
        };
        assertEquals(0, solver.numIslands(singleCellNoIsland));

        // Test Case 3: Single column connected
        char[][] singleColumn = {
                {'1'},
                {'1'},
                {'1'}
        };
        assertEquals(1, solver.numIslands(singleColumn));

        // Test Case 4: Single column disconnected
        char[][] singleColumnDisconnected = {
                {'1'},
                {'0'},
                {'1'}
        };
        assertEquals(2, solver.numIslands(singleColumnDisconnected));

        // Test Case 5: Single row connected
        char[][] singleRow = {
                {'1', '1', '1'}
        };
        assertEquals(1, solver.numIslands(singleRow));

        // Test Case 6: Single row disconnected
        char[][] singleRowDisconnected = {
                {'1', '0', '1'}
        };
        assertEquals(2, solver.numIslands(singleRowDisconnected));

        // Test Case 7: Diagonals (should not connect)
        char[][] diagonals = {
                {'1', '0', '1'},
                {'0', '1', '0'},
                {'1', '0', '1'}
        };
        assertEquals(5, solver.numIslands(diagonals));

        // Test Case 8: All zeros
        char[][] allZeros = {
                {'0', '0'},
                {'0', '0'}
        };
        assertEquals(0, solver.numIslands(allZeros));

        // Test Case 9: All ones
        char[][] allOnes = {
                {'1', '1'},
                {'1', '1'}
        };
        assertEquals(1, solver.numIslands(allOnes));

        // Test Case 10: Complex pattern
        char[][] complex = {
                {'1', '1', '0', '0', '1'},
                {'1', '0', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'1', '0', '0', '1', '1'}
        };
        assertEquals(5, solver.numIslands(complex));

        // Test Case 11: U-shaped island
        char[][] uShaped = {
                {'1', '0', '1'},
                {'1', '0', '1'},
                {'1', '1', '1'}
        };
        assertEquals(1, solver.numIslands(uShaped));

        // Test Case 12: Multiple small islands
        char[][] multipleIslands = {
                {'1', '0', '1', '0'},
                {'0', '1', '0', '1'},
                {'1', '0', '1', '0'}
        };
        assertEquals(6, solver.numIslands(multipleIslands));
    }
    
    // Test maximum size constraints (optional, depending on memory constraints)
    @Test
    public void testMaximumSize() {
        char[][] maxGrid = new char[300][300];
        // Fill with alternating 1s and 0s
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                maxGrid[i][j] = (i + j) % 2 == 0 ? '1' : '0';
            }
        }
        int result = solver.numIslands(maxGrid);
        // The exact number will depend on the pattern, but should be > 0
        assertTrue(result > 0);
    }


}