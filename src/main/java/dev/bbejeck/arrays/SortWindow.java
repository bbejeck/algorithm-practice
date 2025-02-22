package dev.bbejeck.arrays;

import java.util.Arrays;
import java.util.Set;


public class SortWindow {

    public static void main(String[] args) {

        int[] nums = {3, 7, 5, 6, 9};
        int[] nums_2 = {10, 12, 20, 30, 25, 40, 32, 31, 35, 50, 60};
        int[] nums_3 = {0, 1, 15, 25, 6, 7, 30, 40, 50};
        int[] nums_4 = {2, 6, 4, 8, 10, 9, 15};
        int[] nums_5 = {1, 6, 7, 8, 10, 12, 15};
        Set<int[]> arrays = Set.of(nums, nums_2, nums_3, nums_4, nums_5);

        for (int[] numArray : arrays) {
            int startIndex = -1;
            int endIndex = -1;
            int minSoFar = Integer.MAX_VALUE;
            int maxSoFar = Integer.MIN_VALUE;

            for (int i = 1; i < numArray.length; i++) {
                  maxSoFar = Math.max(maxSoFar, numArray[i]);
                  if (numArray[i] < maxSoFar) {
                      endIndex = i;
                  }
            }

            if (endIndex == -1) {
                System.out.printf("Array %s is sorted%n", Arrays.toString(numArray));
               continue;
            }

            for (int i = numArray.length-1; i >0; i--) {
                minSoFar = Math.min(minSoFar, numArray[i]);
                if (numArray[i] > minSoFar) {
                    startIndex = i;
                }
            }
            System.out.printf("Array sort window (%d, %d) for %s%n", startIndex, endIndex, Arrays.toString(numArray));
            
        }

    }
}
