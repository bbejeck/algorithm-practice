package dev.bbejeck.arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 2/17/25
 * Time: 1:01 PM
 */
public class SpiralMatrix {

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> spiral = new ArrayList<>();
        if (matrix == null || matrix.length == 0) {
            return spiral;
        }
        int rows = matrix.length;
        int cols = matrix[0].length;
        int left = 0;
        int right = cols - 1;
        int top = 0;
        int bottom = rows - 1;

        while (left <= right && top <= bottom) {
            for(int col = left; col <= right; col++) {
                spiral.add(matrix[top][col]);
            }
            top++;

            for(int row = top; row <= bottom; row++) {
                spiral.add(matrix[row][right]);
            }
            right--;

            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    spiral.add(matrix[bottom][col]);
                }
                bottom--;
            }

            if (left <= right) {
                for (int row = bottom; row <= top; row--) {
                    spiral.add(matrix[row][left]);
                }
                left++;
            }
        }


      return spiral;

    }

    public static void main(String[] args) {
        SpiralMatrix spiralMatrix = new SpiralMatrix();
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        System.out.println(spiralMatrix.spiralOrder(matrix));
    }
    
}
