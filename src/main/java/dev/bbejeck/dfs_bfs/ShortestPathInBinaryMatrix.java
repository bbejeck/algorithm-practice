package dev.bbejeck.dfs_bfs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * User: Bill Bejeck
 * Date: 8/8/25
 * Time: 1:03 PM
 */
public class ShortestPathInBinaryMatrix {

    public int shortestPathBinaryMatrix(int[][] grid) {

        int n = grid.length;
        if (grid[0][0] != 0 || grid[n-1][n-1] != 0) {
            return -1;
        }
        boolean[][] visited = new boolean[n][n];
        visited[0][0] = true;
        Queue<int[]> queue  = new LinkedList<>();
        int[][] directions = new int[][] {{-1, 0}, {1, 0}, {0, -1},{0, 1}, {-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
        queue.offer(new int[]{0, 0, 1});
        while(!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int dist = current[2];
            if (row == n-1 && col == n-1) {
                return dist;
            }
            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];
                if (newRow >= 0 &&
                        newRow < n &&
                        newCol >= 0 &&
                        newCol < n &&
                        grid[newRow][newCol]==0 &&
                        !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol, dist + 1});
                }
            }
        }
        return -1;

    }
}
