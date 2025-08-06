package dev.bbejeck.dfs_bfs;

/**
 * User: Bill Bejeck
 * Date: 8/6/25
 * Time: 10:18 AM
 */
public class MaxAreaOfIslandDFS {
    public int maxAreaOfIsland(int[][] grid) {
        int maxArea = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                 maxArea = Math.max(maxArea, dfs(grid, i, j, 0));
            }
        }
        return maxArea;
    }

    public int dfs(int[][] grid, int row, int col, int count) {
            if (row < 0 ||
                    row >= grid.length ||
                    col < 0 ||
                    col >= grid[0].length ||
                    grid[row][col] == 0) {
                return count;
            }
            grid[row][col] = 0;
            count++;
            count = dfs(grid, row + 1, col, count);
            count = dfs(grid, row - 1, col, count);
            count = dfs(grid, row, col + 1, count);
            count = dfs(grid, row, col - 1, count);
            return count;
    }
}
