package dev.bbejeck.dfs_bfs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MaxAreaOfIslandDFS
 */
public class MaxAreaOfIslandDFSTest {

    private MaxAreaOfIslandDFS solution;

    @BeforeEach
    void setUp() {
        solution = new MaxAreaOfIslandDFS();
    }

    @Test
    @DisplayName("Test LeetCode example 1: should return max area 6")
    void testLeetCodeExample1() {
        int[][] grid = {
                {0,0,1,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,1,1,0,1,0,0,0,0,0,0,0,0},
                {0,1,0,0,1,1,0,0,1,0,1,0,0},
                {0,1,0,0,1,1,0,0,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,0,0,0,0,0,0,1,1,0,0,0,0}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(6, result, "Should return maximum island area of 6");
    }

    @Test
    @DisplayName("Test LeetCode example 2: all zeros should return 0")
    void testAllZeros() {
        int[][] grid = {{0,0,0,0,0,0,0,0}};

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(0, result, "Grid with no islands should return 0");
    }

    @Test
    @DisplayName("Test single cell island")
    void testSingleCellIsland() {
        int[][] grid = {{1}};

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(1, result, "Single cell island should have area 1");
    }

    @Test
    @DisplayName("Test single cell water")
    void testSingleCellWater() {
        int[][] grid = {{0}};

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(0, result, "Single cell of water should return 0");
    }

    @Test
    @DisplayName("Test rectangular island")
    void testRectangularIsland() {
        int[][] grid = {
                {1,1,1},
                {1,1,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(6, result, "2x3 rectangular island should have area 6");
    }

    @Test
    @DisplayName("Test multiple islands of different sizes")
    void testMultipleIslands() {
        int[][] grid = {
                {1,1,0,0,0},
                {1,0,0,1,1},
                {0,0,0,1,1},
                {0,0,0,0,0},
                {1,0,0,1,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(4, result, "Should return area of largest island (4)");
    }

    @Test
    @DisplayName("Test L-shaped island")
    void testLShapedIsland() {
        int[][] grid = {
                {1,0,0},
                {1,0,0},
                {1,1,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(5, result, "L-shaped island should have area 5");
    }

    @Test
    @DisplayName("Test scattered single islands")
    void testScatteredSingleIslands() {
        int[][] grid = {
                {1,0,1},
                {0,0,0},
                {1,0,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(1, result, "All islands have area 1, should return 1");
    }

    @Test
    @DisplayName("Test snake-shaped island")
    void testSnakeShapedIsland() {
        int[][] grid = {
                {1,1,0,0,0},
                {0,1,0,0,0},
                {0,1,1,1,0},
                {0,0,0,1,0},
                {0,0,0,1,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(9, result, "Snake-shaped connected island should have area 9");
    }

    @Test
    @DisplayName("Test grid with all land")
    void testAllLand() {
        int[][] grid = {
                {1,1,1},
                {1,1,1},
                {1,1,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(9, result, "Grid with all land should return total area 9");
    }

    @Test
    @DisplayName("Test diagonal separation - should not connect")
    void testDiagonalSeparation() {
        int[][] grid = {
                {1,0,1},
                {0,1,0},
                {1,0,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(1, result, "Diagonally positioned islands should not connect");
    }

    @Test
    @DisplayName("Test linear horizontal island")
    void testLinearHorizontalIsland() {
        int[][] grid = {{1,1,1,1,1}};

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(5, result, "Horizontal line island should have area 5");
    }

    @Test
    @DisplayName("Test linear vertical island")
    void testLinearVerticalIsland() {
        int[][] grid = {
                {1},
                {1},
                {1},
                {1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(4, result, "Vertical line island should have area 4");
    }

    @Test
    @DisplayName("Test complex shaped island with hole")
    void testComplexIslandWithHole() {
        int[][] grid = {
                {1,1,1,1,1},
                {1,0,0,0,1},
                {1,0,1,0,1},
                {1,0,0,0,1},
                {1,1,1,1,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(16, result, "Should count outer ring (16) as largest island");
    }

    @Test
    @DisplayName("Test grid modification - original grid should be modified")
    void testGridModification() {
        int[][] grid = {
                {1,1,0},
                {1,0,1},
                {0,0,1}
        };

        // Create expected grid state after processing (all 1s become 0s)
        int[][] expectedAfter = {
                {0,0,0},
                {0,0,0},
                {0,0,0}
        };

        int result = solution.maxAreaOfIsland(grid);

        assertEquals(3, result, "Should find max area of 3");
        assertArrayEquals(expectedAfter, grid, "Original grid should be modified (all 1s become 0s)");
    }

    @Test
    @DisplayName("Test cross-shaped island")
    void testCrossShapedIsland() {
        int[][] grid = {
                {0,1,0},
                {1,1,1},
                {0,1,0}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(5, result, "Cross-shaped island should have area 5");
    }

    @Test
    @DisplayName("Test T-shaped island")
    void testTShapedIsland() {
        int[][] grid = {
                {1,1,1},
                {0,1,0},
                {0,1,0}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(5, result, "T-shaped island should have area 5");
    }

    @Test
    @DisplayName("Test multiple rectangular islands")
    void testMultipleRectangularIslands() {
        int[][] grid = {
                {1,1,0,1,1},
                {1,1,0,1,1},
                {0,0,0,0,0},
                {1,0,0,0,1},
                {1,0,0,0,1}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(4, result, "Should find largest rectangular island with area 4");
    }

    @Test
    @DisplayName("Test alternating rows pattern")
    void testAlternatingRowsPattern() {
        int[][] grid = {
                {1,1,1,1},
                {0,0,0,0},
                {1,1,1,1},
                {0,0,0,0}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(4, result, "Should find island of area 4 in alternating pattern");
    }

    @Test
    @DisplayName("Test alternating columns pattern")
    void testAlternatingColumnsPattern() {
        int[][] grid = {
                {1,0,1,0},
                {1,0,1,0},
                {1,0,1,0}
        };

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(3, result, "Should find largest column island with area 3");
    }

    @Test
    @DisplayName("Test DFS behavior with deep recursion")
    void testDeepRecursion() {
        // Create a long vertical island to test DFS depth
        int[][] grid = new int[20][1];
        for (int i = 0; i < 20; i++) {
            grid[i][0] = 1;
        }

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(20, result, "Long vertical island should have area 20");
    }

    @Test
    @DisplayName("Test large grid with scattered islands")
    void testLargeGridWithScatteredIslands() {
        int[][] grid = new int[10][10]; // All zeros initially

        // Add some scattered islands
        grid[0][0] = 1; grid[0][1] = 1; // Island of size 2
        grid[5][5] = 1; grid[5][6] = 1; grid[6][5] = 1; grid[6][6] = 1; // Island of size 4
        grid[9][9] = 1; // Island of size 1

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(4, result, "Should find largest island of area 4");
    }

    @Test
    @DisplayName("Test checkerboard pattern")
    void testCheckerboardPattern() {
        int[][] grid = new int[4][4];

        // Create checkerboard pattern (1s isolated by 0s)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = (i + j) % 2;
            }
        }

        int result = solution.maxAreaOfIsland(grid);
        assertEquals(1, result, "Checkerboard pattern should have max island area 1");
    }

    @Test
    @DisplayName("Test DFS method directly")
    void testDFSMethodDirectly() {
        int[][] grid = {
                {1,1,0},
                {1,0,0},
                {0,0,1}
        };

        // Test the DFS method directly on the connected component
        int area = solution.dfs(grid, 0, 0, 0);
        assertEquals(3, area, "DFS should return area 3 for connected component");

        // Verify that the visited cells are marked as 0
        assertEquals(0, grid[0][0], "Visited cell should be marked as 0");
        assertEquals(0, grid[0][1], "Visited cell should be marked as 0");
        assertEquals(0, grid[1][0], "Visited cell should be marked as 0");
    }

    @Test
    @DisplayName("Test DFS on single cell")
    void testDFSOnSingleCell() {
        int[][] grid = {{1}};

        int area = solution.dfs(grid, 0, 0, 0);
        assertEquals(1, area, "DFS on single cell should return area 1");
        assertEquals(0, grid[0][0], "Single cell should be marked as visited");
    }

    @Test
    @DisplayName("Test DFS on water cell")
    void testDFSOnWaterCell() {
        int[][] grid = {{0}};

        int area = solution.dfs(grid, 0, 0, 0);
        assertEquals(0, area, "DFS on water cell should return area 0");
    }
}