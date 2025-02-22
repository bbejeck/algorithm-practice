package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 2/22/25
 * Time: 1:11 PM
 */
public class SpiralMatrixII {

    public int[][] generateMatrix(int n) {

        int[][]grid = new int[n][n];
        int startValue = 1;

        int rows = grid.length;
        int cols = grid[0].length;
        int left = 0;
        int right = cols - 1;
        int top = 0;
        int bottom = rows - 1;

        while (left <= right && top <= bottom) {
            for (int col = left; col <= right; col++) {
                grid[top][col] = startValue++;
            }
            top++;

            for (int row = top; row <= bottom; row++) {
                grid[row][right] = startValue++;
            }
            right--;
            
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    grid[bottom][col] = startValue++;
                }
                bottom--;
            }

            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    grid[row][left] = startValue++;
                }
                left++;
            }
        }
        return grid;
    }


}
