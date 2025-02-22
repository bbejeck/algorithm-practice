package dev.bbejeck.backtracking;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 1/12/25
 * Time: 1:24 PM
 */
public class NQueens {

    int[] rows;
    int numSolutions;


    public NQueens(int n) {
        rows = new int[n];
        reset();
    }

    public void reset() {
        Arrays.fill(rows, -1);
    }

    public static void main(String[] args) {
        NQueens nQueens = new NQueens(8);
        nQueens.solve();
        System.out.printf("Number of solutions %d%n", nQueens.numSolutions);
    }

    public void solve() {
        backtrack(0);
    }

    boolean canPlaceQueen(int row, int col) {
        for (int prev = 0; prev < row; prev++) {
            if (rows[prev] == col || Math.abs(rows[prev] - col) == Math.abs(prev - row)) {
                return false;
            }
        }
        return true;
    }

    public void backtrack(int row) {
        if (row == rows.length) {
            numSolutions++;
            System.out.printf("Found solution %s%n", Arrays.toString(rows));
            return;
        }
        for (int col = 0; col < rows.length; col++) {
            if (canPlaceQueen(row, col)) {
                rows[row] = col;
                backtrack(row + 1);
                rows[row] = -1;
            }
        }
    }   
}
