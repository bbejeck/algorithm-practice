package dev.bbejeck.data_stream;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * User: Bill Bejeck
 * Date: 9/20/25
 * Time: 7:07 PM
 */
public class OnLineStockSpan {
    private final Deque<int[]> stack = new ArrayDeque<>();

    public int next(int price) {
         int[] newSpan = {price, 1};
         while(!stack.isEmpty() && stack.peek()[0] <= price) {
                int[] prevSpan = stack.pop();
                newSpan[1] += prevSpan[1];
         }
         stack.push(newSpan);
         return newSpan[1];
    }
}
