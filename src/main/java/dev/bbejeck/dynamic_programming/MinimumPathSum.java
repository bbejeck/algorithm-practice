package dev.bbejeck.dynamic_programming;

/**
 * User: Bill Bejeck
 * Date: 2/17/25
 * Time: 11:41 AM
 */
public class MinimumPathSum {

    public int minPathSum(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] dp = new int[rows][cols];

        dp[0][0] = grid[0][0];

        for(int col = 1; col < cols; col++) {
            dp[0][col] = dp[0][col-1] + grid[0][col];
        }
        
        for(int row = 1; row < rows; row++) {
            dp[row][0] = dp[row-1][0] + grid[row][0];
        }

        for (int row = 1; row < rows; row++) {
            for (int col = 1; col < cols; col++) {
                 dp[row][col] = Math.min(dp[row - 1][col], dp[row][col - 1]) + grid[row][col];
            }
        }
       return dp[rows - 1][cols - 1];
    }

    public static void main(String[] args) {
        int[][] grid = {{1,3,1},{1,5,1},{4,2,1}};
        MinimumPathSum minimumPathSum = new MinimumPathSum();
        System.out.println(minimumPathSum.minPathSum(grid));
    }
}
