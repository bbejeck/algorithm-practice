package dev.bbejeck.arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 12/2/24
 * Time: 8:58 PM
 */
public class SubsetSum {
    public static void main(String[] args) {
        int[] nums_1 = {3, 7, 5, -50, 6, 10};
        int[] nums_2 = {34, -50, 42, 14, -5, 86};
        List<int[]> narrays = List.of(nums_2, nums_1);
        for (int[] nums : narrays) {
            /*
            int maxEndingHere = 0;
            int maxSoFar = 0;
            for (int num : nums) {
                 maxEndingHere = Math.max(maxEndingHere + num, num);
                 maxSoFar = Math.max(maxSoFar, maxEndingHere);
            }
           System.out.printf("Max subset sum %d%n", maxSoFar);
        */
            int currentSum = 0;
            int prevSum = 0;
            for (int num : nums) {
                if (num + currentSum >= currentSum || num + currentSum > prevSum) {
                    currentSum = num + currentSum;
                } else {
                    prevSum = currentSum;
                    currentSum = 0;
                }
            }
            System.out.printf("Max subset sum %d%n", Math.max(prevSum, currentSum));
        }
    }

    /*
      int currentSum = 0;
      int prevSum = 0;
      for (int num : nums) {
         if (num + currentSum >= currentSum || num + currentSum > prevSum) {
             currentSum = num + currentSum;
         } else {
            prevSum = currentSum;
            currentSum = 0;
         }
      }
      System.out.printf("Max subset sum %d%n", Math.max(prevSum, currentSum));
     */
}
