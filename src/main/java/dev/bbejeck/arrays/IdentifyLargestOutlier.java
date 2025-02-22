package dev.bbejeck.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 2/16/25
 * Time: 12:40 PM
 */
public class IdentifyLargestOutlier {

    public int getLargestOutlier(int[] nums) {
        Map<Integer, Integer> numMap = new HashMap<>();
        for(int num: nums) {
            numMap.put(num,null);
        }
        int temp = 0;
        for(int i = 1; i < nums.length; i++) {
            temp = nums[i-1] + nums[i] + temp;
            numMap.remove(temp);
        }
        int largest = Integer.MIN_VALUE;
        for(Integer intKey: numMap.keySet()){
            if (intKey > largest) {
                largest = intKey;
            }
        }
        return largest;
    }
}
