package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 3/15/25
 * Time: 4:18 PM
 */
public class RotateArray {

    public void rotate(int[] nums, int k) {
        if (nums.length > 1 && k > 0 && k != nums.length) {
            if (k > nums.length) {
                k =  k % nums.length;
            }
            int[]copy = new int[nums.length];
            for (int i = 0; i < (nums.length - k); i++) {
                copy[i+k] = nums[i];
            }

            int startCopy = nums.length - k;
            for (int i = startCopy, j = 0; i < nums.length; i++, j++) {
                copy[j] = nums[i];
            }
            System.arraycopy(copy, 0, nums, 0, copy.length);
        }
    }
}
