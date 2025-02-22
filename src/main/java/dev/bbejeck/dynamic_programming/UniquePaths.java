package dev.bbejeck.dynamic_programming;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 2/16/25
 * Time: 10:56 AM
 */
public class UniquePaths {

    public int uniquePaths(int m, int n) {
        if (m == 1 && n == 1) {
            return 1;
        }

        int[][] board = new int[m][n];
        board[0][0] = 1;

        for (int col = 1; col < n; col++) {
            board[0][col] = board[0][col-1];
        }

        for (int row = 1; row < m; row++) {
            board[row][0] = board[row-1][0];
        }

        for (int row = 1; row < m; row++) {
            for (int col = 1; col < n; col++){
                board[row][col] = board[row-1][col] + board[row][col-1];
            }
        }
        return board[m -1][n -1];

    }

    public static void main(String[] args) {
        UniquePaths uniquePaths = new UniquePaths();
        System.out.println(uniquePaths.uniquePaths(3, 7));
    }
}
