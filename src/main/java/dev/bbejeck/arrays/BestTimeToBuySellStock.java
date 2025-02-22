package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 2/20/25
 * Time: 8:56 PM
 */
public class BestTimeToBuySellStock {

    public int maxProfit(int[] prices) {
        if (prices.length == 1) {
            return 0;
        }
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = Integer.MIN_VALUE;

        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else {
                int tempProfit = price - minPrice;
                if (tempProfit > maxProfit) {
                    maxProfit = tempProfit;
                }
            }

        }
        return maxProfit != Integer.MIN_VALUE ? maxProfit : 0;
    }
    public static void main(String[] args) {
        BestTimeToBuySellStock bestTimeToBuySellStock = new BestTimeToBuySellStock();
        int[] prices = {7,1,5,3,6,4};
        System.out.println(bestTimeToBuySellStock.maxProfit(prices));
    }

    
}
