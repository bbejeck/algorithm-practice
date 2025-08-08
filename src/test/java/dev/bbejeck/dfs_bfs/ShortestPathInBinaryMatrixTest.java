package dev.bbejeck.dfs_bfs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShortestPathInBinaryMatrixTest {

    @Test
    void testPathExistsInSmallGrid() {
        int[][] grid = {
                {0, 1},
                {1, 0}
        };
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(2, solution.shortestPathBinaryMatrix(grid));
    }

    @Test
    void testPathBlockedAtStart() {
        int[][] grid = {
                {1, 0},
                {0, 0}
        };
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(-1, solution.shortestPathBinaryMatrix(grid));
    }

    @Test
    void testPathBlockedAtEnd() {
        int[][] grid = {
                {0, 0},
                {0, 1}
        };
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(-1, solution.shortestPathBinaryMatrix(grid));
    }

    @Test
    void testFullGridPathExists() {
        int[][] grid = {
                {0, 0, 0},
                {1, 1, 0},
                {1, 1, 0}
        };
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(4, solution.shortestPathBinaryMatrix(grid));
    }

    @Test
    void testFullGridNoPathExists() {
        int[][] grid = {
                {0, 0, 0},
                {1, 1, 1},
                {1, 1, 0}
        };
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(-1, solution.shortestPathBinaryMatrix(grid));
    }

    @Test
    void testSingleCellGrid() {
        int[][] grid = {{0}};
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(1, solution.shortestPathBinaryMatrix(grid));
    }

    @Test
    void testSingleCellBlockedGrid() {
        int[][] grid = {{1}};
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(-1, solution.shortestPathBinaryMatrix(grid));
    }

    @Test
    void testLargeGridPathExists() {
        int[][] grid = {
                {0, 0, 0, 0},
                {1, 1, 0, 1},
                {1, 0, 0, 1},
                {1, 1, 0, 0}
        };
        ShortestPathInBinaryMatrix solution = new ShortestPathInBinaryMatrix();
        assertEquals(5, solution.shortestPathBinaryMatrix(grid));
    }
}