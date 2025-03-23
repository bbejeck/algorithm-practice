package dev.bbejeck.dynamic_programming;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 3/22/25
 * Time: 9:38 PM
 */
public class Candy {
    public int candy(int[] ratings) {
        int[] numCandy = new int[ratings.length];
        Arrays.fill(numCandy, 1);
        for(int i = 1; i < ratings.length; i++) {
            if(ratings[i - 1] < ratings[i]) {
                numCandy[i] = numCandy[i-1] + 1;
            }
        }
        for (int i = ratings.length - 1; i > 0; i--) {
            if (ratings[i - 1] > ratings[i]){
                numCandy[i - 1] = Math.max(numCandy[i - 1], numCandy[i] + 1);
            }
        }
        return Arrays.stream(numCandy).sum();
    }
}
