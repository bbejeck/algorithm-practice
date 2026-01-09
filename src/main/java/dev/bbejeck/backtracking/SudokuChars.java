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
        Set<Character>[] rows = new HashSet[9];
        Set<Character>[] cols = new HashSet[9];
        Set<Character>[] boxes = new HashSet[9];

        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                char c = board[row][col];
                if (c != '.') {
                    int boxIndex = (row / 3) * 3 + col / 3;

                    if (!rows[row].add(c) ||
                            !cols[col].add(c) ||
                            !boxes[boxIndex].add(c)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
