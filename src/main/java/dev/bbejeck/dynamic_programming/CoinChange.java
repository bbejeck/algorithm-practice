package dev.bbejeck.dynamic_programming;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 3/12/25
 * Time: 10:16 PM
 */
public class CoinChange {
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        int[] options = new int[amount + 1];
        Arrays.fill(options, amount + 1);
        options[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    options[i] = Math.min(options[i], options[i - coin] + 1);
                }
            }
        }
        return options[amount] > amount ? -1 : options[amount];
    }
}
