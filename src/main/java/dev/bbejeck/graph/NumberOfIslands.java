package dev.bbejeck.graph;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User: Bill Bejeck
 * Date: 2/19/25
 * Time: 8:29 PM
 */
public class NumberOfIslands {

    public int numIslands(char[][] grid) {
        UnionFind<String> nodes = new UnionFind<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '1') {
                    nodes.set(i + "," + j);
                }
            }
        }
       int rows = grid.length;
        int cols = grid[0].length;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '1') {
                   if (row > 0 && grid[row-1][col] == '1')  {
                         nodes.union(row + "," + col, (row-1) + "," + col);
                   }
                   if (col < cols-1  && grid[row][col+1] == '1') {
                       nodes.union(row + "," + col, row + "," + (col+1));
                   }
                }
            }
        }

        Map<String,String> results = nodes.unionFindMap();

       Set<String> distinctSet = results.keySet()
               .stream()
               .map(nodes::find)
               .collect(Collectors.toSet());
        System.out.printf("Keys of distinct sets %s%n", distinctSet);;
        return distinctSet.size();
    }

    public static void main(String[] args) {
        char[][] grid = {
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };
        
        char[][] gridII = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };

        char[][] gridIII = {
                           {'1'},
                           {'1'}
        };

        char[][]gridIV = { {'1','1'}};

        char[][] edgeCaseGrid = {
                {'1', '0', '1'},  // isolated islands
                {'0', '1', '0'},  // diagonal 1's
                {'1', '0', '1'}   // corners touching
        };


        System.out.printf("GridIII length=%d GridIII[0].length=%d%n", gridIII.length, gridIII[0].length);
        NumberOfIslands numberOfIslands = new NumberOfIslands();
        System.out.println(numberOfIslands.numIslands(grid));
        System.out.println(numberOfIslands.numIslands(gridII));
        System.out.println(numberOfIslands.numIslands(gridIII));
        System.out.println(numberOfIslands.numIslands(edgeCaseGrid));
        System.out.println(numberOfIslands.numIslands(gridIV));
    }
}
