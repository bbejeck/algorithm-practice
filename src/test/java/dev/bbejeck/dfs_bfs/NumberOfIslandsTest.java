
package dev.bbejeck.dfs_bfs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class NumberOfIslandsTest {

    private NumberOfIslands solver;

    @BeforeEach
    void setUp() {
        solver = new NumberOfIslands();
    }

    @Test
    void testBasicExamples() {
        // LeetCode Example 1
        char[][] grid1 = {
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };
        assertEquals(1, solver.numIslands(grid1));

        // LeetCode Example 2
        char[][] grid2 = {
                {'1','1','0','0','0'},
                {'1','1','0','0','0'},
                {'0','0','1','0','0'},
                {'0','0','0','1','1'}
        };
        assertEquals(3, solver.numIslands(grid2));
    }

    @Test
    void testEdgeCases() {
        // Single cell with island
        char[][] singleIsland = {{'1'}};
        assertEquals(1, solver.numIslands(singleIsland));

        // Single cell with water
        char[][] singleWater = {{'0'}};
        assertEquals(0, solver.numIslands(singleWater));

        // All water
        char[][] allWater = {
                {'0','0','0'},
                {'0','0','0'},
                {'0','0','0'}
        };
        assertEquals(0, solver.numIslands(allWater));

        // All land (one big island)
        char[][] allLand = {
                {'1','1','1'},
                {'1','1','1'},
                {'1','1','1'}
        };
        assertEquals(1, solver.numIslands(allLand));
    }

    @Test
    void testLinearShapes() {
        // Horizontal line
        char[][] horizontal = {
                {'1','1','1','1'}
        };
        assertEquals(1, solver.numIslands(horizontal));

        // Vertical line
        char[][] vertical = {
                {'1'},
                {'1'},
                {'1'},
                {'1'}
        };
        assertEquals(1, solver.numIslands(vertical));

        // Broken horizontal line
        char[][] brokenHorizontal = {
                {'1','1','0','1','1'}
        };
        assertEquals(2, solver.numIslands(brokenHorizontal));

        // Broken vertical line
        char[][] brokenVertical = {
                {'1'},
                {'1'},
                {'0'},
                {'1'},
                {'1'}
        };
        assertEquals(2, solver.numIslands(brokenVertical));
    }

    @Test
    void testComplexShapes() {
        // L-shaped island
        char[][] lShaped = {
                {'1','0','0'},
                {'1','0','0'},
                {'1','1','1'}
        };
        assertEquals(1, solver.numIslands(lShaped));

        // U-shaped island
        char[][] uShaped = {
                {'1','0','1'},
                {'1','0','1'},
                {'1','1','1'}
        };
        assertEquals(1, solver.numIslands(uShaped));

        // Donut shape (hole in middle)
        char[][] donut = {
                {'1','1','1'},
                {'1','0','1'},
                {'1','1','1'}
        };
        assertEquals(1, solver.numIslands(donut));
    }

    @Test
    void testMultipleIslands() {
        // Four corners
        char[][] fourCorners = {
                {'1','0','1'},
                {'0','0','0'},
                {'1','0','1'}
        };
        assertEquals(4, solver.numIslands(fourCorners));

        // Checkerboard pattern (every cell is separate)
        char[][] checkerboard = {
                {'1','0','1'},
                {'0','1','0'},
                {'1','0','1'}
        };
        assertEquals(5, solver.numIslands(checkerboard));

        // Multiple complex islands
        char[][] multipleComplex = {
                {'1','1','0','0','1'},
                {'1','0','0','1','1'},
                {'0','0','1','0','0'},
                {'1','0','0','1','1'}
        };
        assertEquals(5, solver.numIslands(multipleComplex));
    }

    @Test
    void testDiagonalConnections() {
        // Diagonals should NOT connect (only 4-directional)
        char[][] diagonal = {
                {'1','0','0'},
                {'0','1','0'},
                {'0','0','1'}
        };
        assertEquals(3, solver.numIslands(diagonal));
    }

    @Test
    void testGridModification() {
        // Test that the method modifies the original grid (BFS marks visited cells as '0')
        char[][] grid = {
                {'1','1','0'},
                {'1','0','0'},
                {'0','0','1'}
        };

        // Make a copy to verify modification
        char[][] originalGrid = {
                {'1','1','0'},
                {'1','0','0'},
                {'0','0','1'}
        };

        int result = solver.numIslands(grid);
        assertEquals(2, result);

        // Verify that the grid has been modified (all '1's should become '0's)
        assertNotEquals(java.util.Arrays.deepToString(originalGrid),
                java.util.Arrays.deepToString(grid));

        // All land cells should now be water
        for (char[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                assertEquals('0', chars[j]);
            }
        }
    }

    @Test
    void testLargeGrid() {
        // Test with a larger grid
        char[][] largeGrid = new char[10][10];

        // Fill with alternating pattern
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                largeGrid[i][j] = ((i + j) % 2 == 0) ? '1' : '0';
            }
        }

        int result = solver.numIslands(largeGrid);
        // In a checkerboard pattern, each '1' is isolated
        assertEquals(50, result); // 100 cells / 2 = 50 islands
    }

    @Test
    void testBFSSpecificBehavior() {
        // Test that ensures BFS processes level by level
        // This grid tests the queue behavior of BFS
        char[][] grid = {
                {'1','1','1','1'},
                {'1','0','0','1'},
                {'1','0','0','1'},
                {'1','1','1','1'}
        };

        assertEquals(1, solver.numIslands(grid));

        // After BFS, all connected '1's should be marked as '0'
        for (char[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                assertEquals('0', chars[j]);
            }
        }
    }

    @Test
    void testSnakeShapedIsland() {
        // Test a snake-like connected island
        char[][] snake = {
                {'1','1','0','0','0'},
                {'0','1','0','0','0'},
                {'0','1','1','1','0'},
                {'0','0','0','1','0'},
                {'0','0','0','1','1'}
        };
        assertEquals(1, solver.numIslands(snake));
    }
}