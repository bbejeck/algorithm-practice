package dev.bbejeck.backtracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    private Sudoku sudoku;

    @BeforeEach
    void setUp() {
        sudoku = new Sudoku();
    }

    @Nested
    @DisplayName("isValidSudoku tests")
    class IsValidSudokuTests {

        @Test
        @DisplayName("Valid partial board returns true")
        void testValidPartialBoard() {
            int[][] board = {
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
            assertTrue(sudoku.isValidSudoku(board));
        }

        @Test
        @DisplayName("Empty board returns true")
        void testEmptyBoard() {
            int[][] board = new int[9][9];
            assertTrue(sudoku.isValidSudoku(board));
        }

        @Test
        @DisplayName("Board with duplicate in row returns false")
        void testDuplicateInRow() {
            int[][] board = {
                    {5, 3, 0, 0, 7, 0, 0, 5, 0},  // Duplicate 5 in row
                    {6, 0, 0, 1, 9, 5, 0, 0, 0},
                    {0, 9, 8, 0, 0, 0, 0, 6, 0},
                    {8, 0, 0, 0, 6, 0, 0, 0, 3},
                    {4, 0, 0, 8, 0, 3, 0, 0, 1},
                    {7, 0, 0, 0, 2, 0, 0, 0, 6},
                    {0, 6, 0, 0, 0, 0, 2, 8, 0},
                    {0, 0, 0, 4, 1, 9, 0, 0, 5},
                    {0, 0, 0, 0, 8, 0, 0, 7, 9}
            };
            assertFalse(sudoku.isValidSudoku(board));
        }

        @Test
        @DisplayName("Board with duplicate in column returns false")
        void testDuplicateInColumn() {
            int[][] board = {
                    {5, 3, 0, 0, 7, 0, 0, 0, 0},
                    {6, 0, 0, 1, 9, 5, 0, 0, 0},
                    {5, 9, 8, 0, 0, 0, 0, 6, 0},  // Duplicate 5 in column 0
                    {8, 0, 0, 0, 6, 0, 0, 0, 3},
                    {4, 0, 0, 8, 0, 3, 0, 0, 1},
                    {7, 0, 0, 0, 2, 0, 0, 0, 6},
                    {0, 6, 0, 0, 0, 0, 2, 8, 0},
                    {0, 0, 0, 4, 1, 9, 0, 0, 5},
                    {0, 0, 0, 0, 8, 0, 0, 7, 9}
            };
            assertFalse(sudoku.isValidSudoku(board));
        }

        @Test
        @DisplayName("Board with duplicate in 3x3 box returns false")
        void testDuplicateInBox() {
            int[][] board = {
                    {5, 3, 0, 0, 7, 0, 0, 0, 0},
                    {6, 5, 0, 1, 9, 5, 0, 0, 0},  // Duplicate 5 in top-left box
                    {0, 9, 8, 0, 0, 0, 0, 6, 0},
                    {8, 0, 0, 0, 6, 0, 0, 0, 3},
                    {4, 0, 0, 8, 0, 3, 0, 0, 1},
                    {7, 0, 0, 0, 2, 0, 0, 0, 6},
                    {0, 6, 0, 0, 0, 0, 2, 8, 0},
                    {0, 0, 0, 4, 1, 9, 0, 0, 5},
                    {0, 0, 0, 0, 8, 0, 0, 7, 9}
            };
            assertFalse(sudoku.isValidSudoku(board));
        }

        @Test
        @DisplayName("Completely solved valid board returns true")
        void testSolvedBoard() {
            int[][] board = {
                    {5, 3, 4, 6, 7, 8, 9, 1, 2},
                    {6, 7, 2, 1, 9, 5, 3, 4, 8},
                    {1, 9, 8, 3, 4, 2, 5, 6, 7},
                    {8, 5, 9, 7, 6, 1, 4, 2, 3},
                    {4, 2, 6, 8, 5, 3, 7, 9, 1},
                    {7, 1, 3, 9, 2, 4, 8, 5, 6},
                    {9, 6, 1, 5, 3, 7, 2, 8, 4},
                    {2, 8, 7, 4, 1, 9, 6, 3, 5},
                    {3, 4, 5, 2, 8, 6, 1, 7, 9}
            };
            assertTrue(sudoku.isValidSudoku(board));
        }
    }

    @Nested
    @DisplayName("canPlace tests")
    class CanPlaceTests {

        private int[][] board;

        @BeforeEach
        void setUpBoard() {
            board = new int[][] {
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
        }

        @Test
        @DisplayName("Can place number in valid empty cell")
        void testCanPlaceValid() {
            assertTrue(sudoku.canPlace(board, 3, 3, 5));
        }

        @Test
        @DisplayName("Cannot place number already in row")
        void testCannotPlaceDuplicateInRow() {
            assertFalse(sudoku.canPlace(board, 0, 2, 5));  // 5 already in row 0
        }

        @Test
        @DisplayName("Cannot place number already in column")
        void testCannotPlaceDuplicateInColumn() {
            assertFalse(sudoku.canPlace(board, 3, 1, 9));  // 9 already in column 1
        }

        @Test
        @DisplayName("Cannot place number already in 3x3 box")
        void testCannotPlaceDuplicateInBox() {
            assertFalse(sudoku.canPlace(board, 1, 1, 8));  // 8 already in top-left box
        }

        @Test
        @DisplayName("Can place number at corner of box")
        void testCanPlaceAtBoxCorner() {
            assertTrue(sudoku.canPlace(board, 7, 3, 2));
        }

        @Test
        @DisplayName("Cannot place when conflicts with row, column, and box")
        void testMultipleConflicts() {
            assertFalse(sudoku.canPlace(board, 5, 4, 6));  // 6 in row, column, and box
        }
    }

    @Nested
    @DisplayName("solveSudoku tests")
    class SolveSudokuTests {

        @Test
        @DisplayName("Solves standard puzzle correctly")
        void testSolveStandardPuzzle() {
            int[][] board = {
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

            int[][] solved = sudoku.solveSudoku(board);

            assertTrue(sudoku.isValidSudoku(solved));
            assertNoEmptyCells(solved);
        }

        @Test
        @DisplayName("Solves empty board")
        void testSolveEmptyBoard() {
            int[][] board = new int[9][9];

            int[][] solved = sudoku.solveSudoku(board);

            assertTrue(sudoku.isValidSudoku(solved));
            assertNoEmptyCells(solved);
        }

        @Test
        @DisplayName("Preserves pre-filled values")
        void testPreservesPrefilledValues() {
            int[][] board = {
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

            int[][] solved = sudoku.solveSudoku(board);

            // Check that original non-zero values are preserved
            assertEquals(5, solved[0][0]);
            assertEquals(3, solved[0][1]);
            assertEquals(7, solved[0][4]);
            assertEquals(9, solved[8][8]);
        }

        @Test
        @DisplayName("Solves hard puzzle")
        void testSolveHardPuzzle() {
            int[][] board = {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 3, 0, 8, 5},
                    {0, 0, 1, 0, 2, 0, 0, 0, 0},
                    {0, 0, 0, 5, 0, 7, 0, 0, 0},
                    {0, 0, 4, 0, 0, 0, 1, 0, 0},
                    {0, 9, 0, 0, 0, 0, 0, 0, 0},
                    {5, 0, 0, 0, 0, 0, 0, 7, 3},
                    {0, 0, 2, 0, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 4, 0, 0, 0, 9}
            };

            int[][] solved = sudoku.solveSudoku(board);

            assertTrue(sudoku.isValidSudoku(solved));
            assertNoEmptyCells(solved);
        }

        private void assertNoEmptyCells(int[][] board) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    assertNotEquals(0, board[row][col],
                            String.format("Cell [%d][%d] is still empty", row, col));
                }
            }
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Solve and validate round trip")
        void testSolveAndValidateRoundTrip() {
            int[][] board = {
                    {0, 0, 0, 2, 6, 0, 7, 0, 1},
                    {6, 8, 0, 0, 7, 0, 0, 9, 0},
                    {1, 9, 0, 0, 0, 4, 5, 0, 0},
                    {8, 2, 0, 1, 0, 0, 0, 4, 0},
                    {0, 0, 4, 6, 0, 2, 9, 0, 0},
                    {0, 5, 0, 0, 0, 3, 0, 2, 8},
                    {0, 0, 9, 3, 0, 0, 0, 7, 4},
                    {0, 4, 0, 0, 5, 0, 0, 3, 6},
                    {7, 0, 3, 0, 1, 8, 0, 0, 0}
            };

            // Verify input is valid but incomplete
            assertTrue(sudoku.isValidSudoku(board));

            // Solve
            int[][] solved = sudoku.solveSudoku(board);

            // Verify solution is valid and complete
            assertTrue(sudoku.isValidSudoku(solved));

            // Verify all cells are filled
            for (int[] row : solved) {
                for (int cell : row) {
                    assertTrue(cell >= 1 && cell <= 9);
                }
            }
        }
    }
}