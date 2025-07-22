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
        int[] solution = new int[2];
        if (nums.length == 2) {
            solution[1] = 1;
        } else {
            for (int i = 0; i < nums.length; i++) {
                int x = target - nums[i];
                if (numMap.containsKey(x)) {
                    solution[0] = i;
                    solution[1] = numMap.get(x);
                    break;
                } else {
                    numMap.put(nums[i], i);
                }                                               
            }
        }
        return solution;
    }
}
