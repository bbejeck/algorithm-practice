package dev.bbejeck.arrays;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 2/5/25
 * Time: 9:10 PM
 */
public class RemoveDuplicatesSorted {

    public int removeDuplicates(int[] nums) {
        int previous = Integer.MIN_VALUE;
        int currentIdx = 0;
        int k = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != previous) {
                previous = nums[i];
                nums[currentIdx] = nums[i];
                currentIdx +=1;
                k++;
            }
        }
        return k;
    }

    public static void main(String[] args) {
        int[] nums = {0,0,1,1,1,2,2,3,3,4};
        RemoveDuplicatesSorted removeDuplicatesSorted = new RemoveDuplicatesSorted();
        int newLength = removeDuplicatesSorted.removeDuplicates(nums);
        System.out.printf("New length %d%n", newLength);
        System.out.printf("%s%n", Arrays.toString(nums));
    }

}
