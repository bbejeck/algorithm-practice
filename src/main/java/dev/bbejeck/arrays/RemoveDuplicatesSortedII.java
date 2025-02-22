package dev.bbejeck.arrays;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 2/5/25
 * Time: 9:10 PM
 */
public class RemoveDuplicatesSortedII {

    public int removeDuplicates(int[] nums) {
        int previous = Integer.MIN_VALUE;
        int currentIdx = 0;
        int k = 0;
        int seenCounter = 0;

        for (int i = 0; i < nums.length; i++) {
             if(nums[i] == previous) {
                 if (seenCounter < 1) {
                     previous = nums[i];
                     nums[currentIdx] = nums[i];
                     seenCounter+=1;
                     k++;
                     currentIdx+=1;
                     continue;
                 }
             }
             if (nums[i] != previous) {
                previous = nums[i];
                nums[currentIdx] = nums[i];
                currentIdx +=1;
                seenCounter = 0;
                k++;
            }
        }
        return k;
    }

    public static void main(String[] args) {
        int[] nums = {0,0,1,1,1,1,2,3,3};
        System.out.printf("%s%n", Arrays.toString(nums));
        RemoveDuplicatesSortedII removeDuplicatesSorted = new RemoveDuplicatesSortedII();
        int newLength = removeDuplicatesSorted.removeDuplicates(nums);
        System.out.printf("%s%n", Arrays.toString(nums));
        System.out.printf("New length %d%n", newLength);
    }

}
