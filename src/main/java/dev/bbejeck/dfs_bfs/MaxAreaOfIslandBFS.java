package dev.bbejeck.dfs_bfs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * User: Bill Bejeck
 * Date: 8/6/25
 * Time: 9:53 AM
 */
public class MaxAreaOfIslandBFS {
    
    public int maxAreaOfIsland(int[][] grid) {
        int maxArea = 0;
        Queue<int[]> queue = new LinkedList<>();
        int[][] directions = new int[][]{{0,1}, {1,0}, {-1,0}, {0,-1}};
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    int tempArea = 1;
                    grid[i][j] = 0;
                    queue.offer(new int[]{i, j});
                    while(!queue.isEmpty()) {
                        int[] current = queue.poll();
                        for (int[] direction : directions) {
                            int newRow = current[0] + direction[0];
                            int newCol = current[1] + direction[1];
                            if (newRow >= 0 &&
                                    newRow < grid.length &&
                                    newCol >= 0 &&
                                    newCol < grid[0].length &&
                                    grid[newRow][newCol] == 1) {
                                tempArea++;
                                queue.offer(new int[]{newRow, newCol});
                                grid[newRow][newCol] = 0;
                            }
                        }
                    }
                    maxArea = Math.max(maxArea, tempArea);
                }
            }
        }
        return maxArea;
    }
}
