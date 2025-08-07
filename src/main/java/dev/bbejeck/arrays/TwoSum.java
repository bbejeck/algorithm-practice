package dev.bbejeck.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 7/21/25
 * Time: 11:30 PM
 */
public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (numMap.containsKey(complement)) {
                return new int[]{numMap.get(complement), i};
            }
            numMap.put(nums[i], i);
        }
        return new int[0]; // No solution found
    }
}
