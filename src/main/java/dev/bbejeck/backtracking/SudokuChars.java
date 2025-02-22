package dev.bbejeck.backtracking;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Bill Bejeck
 * Date: 1/18/25
 * Time: 6:20 PM
 */
public class SudokuChars {

    public char[][] solveSudoku(char[][] board) {
        backtrack(board, 0, 0);
        return board;
    }

    public boolean backtrack(char[][] board, int row, int col) {
        if (row == board.length) {
            return true;
        }
        if (col == board[row].length) {
            return backtrack(board, row + 1, 0);
        }
        if (board[row][col] != '.') {
            return backtrack(board, row, col + 1);
        }
        for (char num = '1'; num <= '9'; num++) {
            if (canPlace(board, row, col, num)) {
                board[row][col] = num;

                if (backtrack(board, row, col + 1)) {
                    return true;
                }

                board[row][col] = '.';
            }
        }

        return false;
    }

    public boolean canPlace(char[][] board, int row, int column, char candidate) {
        // Check the current row and column
        for (int j = 0; j < board.length; j++) {
            if (board[row][j] == candidate || board[j][column] == candidate) {
                return false;
            }
        }

        // Check the 3x3 grid
        int rowStart = (row / 3) * 3;
        int colStart = (column / 3) * 3;

        for (int i = rowStart; i < rowStart + 3; i++) {
            for (int j = colStart; j < colStart + 3; j++) {
                if (board[i][j] == candidate) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValidSudoku(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            Set<Character> rowNums = new HashSet<>();
            Set<Character> colNums = new HashSet<>();

            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != '.' && !rowNums.add(board[i][j])) {
                    return false;
                }
                if (board[j][i] != '.' && !colNums.add(board[j][i])) {
                    return false;
                }
            }
        }
        return true;
    }

}
