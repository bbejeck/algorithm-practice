package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 3/24/25
 * Time: 11:50 PM
 */
public class JumpGame {

    public boolean canJump(int[] nums) {
        if (nums.length == 1) {
            return true;
        }
        int maxJump = 0;
        int lastIndex = nums.length - 1;
        for (int i = 0; i < nums.length; i ++) {
            if (i > maxJump) {
                return false;
            }
            maxJump = Math.max(maxJump, i + nums[i]);
            if (maxJump >= lastIndex) {
                return true;
            }
        }
        return false;
    }
}
