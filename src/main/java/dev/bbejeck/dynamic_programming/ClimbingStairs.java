package dev.bbejeck.dynamic_programming;

/**
 * User: Bill Bejeck
 * Date: 7/29/25
 * Time: 11:02 PM
 */
public class ClimbingStairs {
        public int climbStairs(int n) {
            if (n <= 2) {
                return n;
            }
            int[] numWays = new int[n + 1];
            numWays[1] = 1;
            numWays[2] = 2;

            for (int i = 3; i <= n; i++) {
                numWays[i] = numWays[i - 1] + numWays[i - 2];
            }
            return numWays[n];
        }
}
