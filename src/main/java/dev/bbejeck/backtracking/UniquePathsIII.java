package dev.bbejeck.backtracking;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Bill Bejeck
 * Date: 2/12/25
 * Time: 8:03 PM
 */
public class UniquePathsIII {
    int[][] moves = {{0,1},{0,-1},{1,0},{-1,0}};
    int completedCount = 0;
    int totalAvailableMoves = 0;
    int[] start;
    Set<String>allMoves = new HashSet<>();

    public int uniquePathsIII(int[][] grid) {
         start = findStart(grid);
         totalAvailableMoves = getTotalAvailableMoves(grid);
         backtrack(grid, start[0], start[1], 1);
         return completedCount;
    }

    public boolean canMove(int[][] grid, int row, int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return false;
        }
        return grid[row][col] != -1;
    }

    public void backtrack(int[][] grid, int row, int col, int movesCount) {
         if (grid[row][col] == 2) {
             if (movesCount == totalAvailableMoves) {
                 completedCount++;
                 System.out.println(allMoves);
                 allMoves.clear();
             }
             return;
         }
        int temp = grid[row][col];
        grid[row][col] = -1;
         for (int[] move : moves) {
             int newRow = row + move[0];
             int newCol = col + move[1];
              if(canMove(grid, newRow, newCol)) {
                  allMoves.add(newRow + "," + newCol);
                  backtrack(grid, newRow, newCol, movesCount + 1);
              }
         }
        grid[row][col] = temp;

    }


    public int[] findStart(int[][] grid) {
        int[] start = new int[2];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 1) {
                    start[0] = row;
                    start[1] = col;
                    return start;
                }
            }
        }
        return start;
    }

    public int getTotalAvailableMoves(int[][] grid) {
        int total = 0;
        for (int[] ints : grid) {
            for (int anInt : ints) {
                if (anInt != -1) {
                    total++;
                }
            }
        }
        return total;
    }

    public static void main(String[] args) {
        //int[][] grid = {{1,0,0,0},{0,0,0,0},{0,0,2,-1}};
        int[][] grid = {{1,0,0,0},{0,0,0,0},{0,0,0,2}};
        UniquePathsIII uniquePathsIII = new UniquePathsIII();
        System.out.println(uniquePathsIII.uniquePathsIII(grid));
    }
}
