package dev.bbejeck.window;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * User: Bill Bejeck
 * Date: 11/13/25
 * Time: 7:38 PM
 */
public class RecentCounter {

    private final Deque<Integer> counter;

    public RecentCounter() {
        counter = new ArrayDeque<>(10000);
    }

    public int ping(int t) {
        counter.addLast(t);
        while(counter.peek() < t - 3000) {
            counter.pollFirst();
        }
        return counter.size();
    }
}
