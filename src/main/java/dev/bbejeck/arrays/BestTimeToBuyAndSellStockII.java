package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 3/3/25
 * Time: 9:13 PM
 */
public class BestTimeToBuyAndSellStockII {

    public int maxProfit(int[] prices) {
        if (prices.length == 1) {
            return prices[0];
        }
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i -1]) {
                profit += prices[i] - prices[i -1];
            }
        }
        return profit;
    }

    public static void main(String[] args) {
        int[] prices = {7,1,5,3,6,4};
        BestTimeToBuyAndSellStockII solution = new BestTimeToBuyAndSellStockII();
        System.out.println(solution.maxProfit(prices));
    }
}
