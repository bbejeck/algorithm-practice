package dev.bbejeck.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Bill Bejeck
 * Date: 1/18/25
 * Time: 3:12 PM
 */
public class Sudoku {

    public int[][] solveSudoku(int[][] board) {
        backtrack(board, 0, 0);
        return board;
    }

    public boolean backtrack(int[][] board, int row, int col) {
        if (row == board.length) {
            return true;
        }
        if (col == board[0].length) {
            return backtrack(board, row + 1, 0);
        }
        if (board[row][col] != 0) {
            return backtrack(board, row, col + 1);
        }
        for (int num = 1; num <= 9; num++) {
            if (canPlace(board, row, col, num)) {
                board[row][col] = num;

                if (backtrack(board, row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }

        // If no number works, return false to backtrack
        return false;
    }
    
    public boolean canPlace(int[][] board, int row, int column, int candidate) {
        for(int j = 0; j < board.length; j++) {
            if(board[row][j] == candidate || board[j][column] == candidate) {
                return false;
            }
        }
        int rowStart = (row / 3) * 3;
        int colStart = (column / 3) * 3;

        for(int i = rowStart; i < rowStart + 3; i++) {
            for(int j = colStart; j < colStart + 3; j++) {
                if(board[i][j] == candidate) {
                    return false;
                }
            }
        }
      return true;
    }


    public boolean isValidSudoku(int[][] board) {
        List<Set<Integer>> rowList = new ArrayList<>(9);
        List<Set<Integer>> colList = new ArrayList<>(9);
        List<Set<Integer>> boxList = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            rowList.add(new HashSet<>());
            colList.add(new HashSet<>());
            boxList.add(new HashSet<>());
        }
        for (int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
                if(board[row][col] != 0) {
                    int boxIndex = (row / 3) * 3 + col / 3;
                    int candidate = board[row][col];
                    if (!rowList.get(row).add(candidate) || !colList.get(col).add(candidate) || !boxList.get(boxIndex).add(candidate)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        //runTests(soduku);
        int[][] boardToSolve = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        int[][] solved = sudoku.solveSudoku(boardToSolve);
        boolean valid = sudoku.isValidSudoku(solved);
        if (valid) {
            System.out.printf("Solved the Sudoku!!!%n");
            for (int[] row : solved) {
                System.out.printf("%s%n", Arrays.toString(row));
            }
        } else {
            System.out.printf("Board not valid!!!%n");
        }

    }

    private static void runTests(Sudoku sudoku) {
        int[][] validBoard1 = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        int[][] validBoard2 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        int[][] invalidBoard1 = {
                {5, 3, 0, 0, 7, 0, 0, 5, 0}, // Duplicate '5' in the first row
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        int[][] invalidBoard2 = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {5, 9, 8, 0, 0, 0, 0, 6, 0}, // Duplicate '5' in the first column
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        System.out.println("Should be valid (true) => " + sudoku.isValidSudoku(validBoard1));
        System.out.println("Should be valid (true) => " + sudoku.isValidSudoku(validBoard2));
        System.out.println("Should be invalid (false) => " + sudoku.isValidSudoku(invalidBoard1));
        System.out.println("Should be invalid (false) => " + sudoku.isValidSudoku(invalidBoard2));
        System.out.println("======================");

        System.out.println("Should be able to place 5 in [3][5] => " + sudoku.canPlace(validBoard1, 3, 3, 5)); // true
        System.out.println("Should not be able to place 6 in [5][8] => " + sudoku.canPlace(invalidBoard2, 5, 8, 6));  // false
        System.out.println("Should not be able to place 6 in [5][4] => " + sudoku.canPlace(validBoard1, 5, 4, 6)); // false
        System.out.println("Should not be able to place 9 in [3][1] => " + sudoku.canPlace(validBoard1, 3, 1, 9)); // false
        System.out.println("Should be able to place 2 in [7][3] => " + sudoku.canPlace(validBoard1, 7, 3, 2)); // true
        System.out.println("Should not be able to place 1 in [1][4] => " + sudoku.canPlace(validBoard1, 1, 4, 1)); // false
    }
}

