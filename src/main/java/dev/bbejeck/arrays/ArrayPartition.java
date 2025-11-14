package dev.bbejeck.arrays;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 11/13/25
 * Time: 7:59 PM
 */
public class ArrayPartition {

    public int arrayPairSum(int[] nums) {
        int sum = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i += 2) {
            sum += nums[i];
        }
        return sum;
    }
}
