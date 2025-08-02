package dev.bbejeck.dfs_bfs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * User: Bill Bejeck
 * Date: 8/1/25
 * Time: 11:07 PM
 */
public class NumberOfIslands {
    public int numIslands(char[][] grid) {
        int[][] directions = new int[][]{{0,1}, {1,0}, {-1,0}, {0,-1}};
        int numIslands = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    numIslands++;
                    bfs(grid, i, j, directions);
                }
            }
        }
        return numIslands;
    }

    void bfs(char[][] grid, int startRow, int startCol, int[][] directions) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol});
        grid[startRow][startCol] = '0';
        while(!queue.isEmpty()){
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            for (int[] direction : directions){
                int newRow = row + direction[0];
                int newCol = col + direction[1];
                if (newRow >= 0
                        && newRow < grid.length
                        && newCol >= 0
                        && newCol < grid[0].length
                        && grid[newRow][newCol] == '1') {
                    grid[newRow][newCol] = '0';
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
    }
}
