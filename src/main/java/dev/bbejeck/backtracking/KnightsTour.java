package dev.bbejeck.backtracking;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 2/8/25
 * Time: 3:10 PM
 */
public class KnightsTour {
    int[][] board;
    int placedCount = 0;
    int[][] possibleMoves = new int[8][2];

    public KnightsTour() {
        board = new int[8][8];
        possibleMoves[0] = new int[]{-2, 1};
        possibleMoves[1] = new int[]{2, 1};
        possibleMoves[2] = new int[]{-2, -1};
        possibleMoves[3] = new int[]{2, -1};
        possibleMoves[4] = new int[]{-1, 2};
        possibleMoves[5] = new int[]{1, 2};
        possibleMoves[6] = new int[]{-1, -2};
        possibleMoves[7] = new int[]{1, -2};
    }

    public boolean canPlace(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return false;
        }
        return board[row][col] == 0;
    }

    public boolean backtrack(int row, int col) {
        if (placedCount == 64) {
            return true;
        }

        for (int[] possibleMove : possibleMoves) {
            int moveRow = row + possibleMove[0];
            int moveCol = col + possibleMove[1];
            if (canPlace(moveRow, moveCol)) {
                board[moveRow][moveCol] = placedCount+=1;
                if (backtrack(moveRow, moveCol)) {
                    return true;
                }
                board[moveRow][moveCol] = 0;
                placedCount-=1;
            }
        }
        return false;
    }

    public void showBoardResults() {
        System.out.println("  -------------------------------");
        for (int[] ints : board) {
            for (int value : ints) {
                if (value == 0) {
                    System.out.printf("%4s", "--");
                } else {
                    System.out.printf("%4d", value);
                }
            }
            System.out.println();
        }
        System.out.println("  -------------------------------");
    }

    public static void main(String[] args) {
        KnightsTour knightsTour = new KnightsTour();
        knightsTour.board[0][0] = 1;
        knightsTour.placedCount = 1;
        knightsTour.backtrack(0, 0);
        System.out.printf("Number of placements %d%n", knightsTour.placedCount);
        knightsTour.showBoardResults();
    }
}
