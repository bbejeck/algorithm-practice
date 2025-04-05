package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 4/3/25
 * Time: 9:56 PM
 */
public class JumpGameII {
    public int jump(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        int farthest = 0;
        int numJumps = 0;
        int currentEnd = 0;
        for (int i = 0; i < nums.length; i++) {
            farthest = Math.max(farthest, i + nums[i]);

            if (farthest >= nums.length - 1) {
                numJumps++;
                break;
            }

            if (i == currentEnd) {
                numJumps++;
                currentEnd = farthest;
            }
        }
        return numJumps;

    }
}
