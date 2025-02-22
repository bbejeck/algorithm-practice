package dev.bbejeck.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 12/5/24
 * Time: 9:18 PM
 */
public class SmallerElementsToTheRight {

    public static void main(String[] args) {
        int[] nums = {3, 4, 9, 6, 1};
        List<Integer> sortedNums = new ArrayList<>();
        int[] smaller = new int[nums.length];
        for (int i = nums.length - 1; i >= 0; i--) {
            sortedNums.add(nums[i]);
            Collections.sort(sortedNums);
            smaller[i] = sortedNums.indexOf(nums[i]);
        }
        System.out.printf("Smaller to the right results %s%n", Arrays.toString(smaller));
    }
}
