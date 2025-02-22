package dev.bbejeck.dynamic_programming;

/**
 * User: Bill Bejeck
 * Date: 2/12/25
 * Time: 8:03 PM
 */
public class UniquePathsII {

    public int uniquePathsWithObstacles(int[][] grid) {
        if (grid[0][0] == 1 || grid[grid.length - 1][grid[0].length - 1] == 1) {
            return 0;
        }
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] dp = new int[rows][cols];

        dp[0][0] = grid[0][0] == 0 ? 1 : 0;
        for (int col =1; col < cols; col++ ) {
            if (grid[0][col] == 0) {
                dp[0][col] = dp[0][col - 1];
            }
        }
        for (int row = 1; row < rows; row++) {
            if (grid[row][0] == 0) {
                dp[row][0] = dp[row - 1][0];
            }
        }
        for (int row = 1; row < rows; row++) {
            for (int col = 1; col < cols; col++) {
                if (grid[row][col] == 1) {
                    dp[row][col] = 0;
                } else {
                    dp[row][col] = dp[row - 1][col] + dp[row][col - 1];
                }
            }
        }

        return dp[rows - 1][cols - 1];
    }
    

    public static void main(String[] args) {
        //int[][] grid = {{0,1},{0,0}};
        //int[][] grid = {{0,0,0},{0,1,0},{0,0,0}};
        int[][] grid = {{0,1}};
        UniquePathsII uniquePathsII = new UniquePathsII();
        System.out.println(uniquePathsII.uniquePathsWithObstacles(grid));
    }
}
