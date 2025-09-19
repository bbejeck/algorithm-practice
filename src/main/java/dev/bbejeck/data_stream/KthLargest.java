package dev.bbejeck.data_stream;

import java.util.PriorityQueue;

/**
 * User: Bill Bejeck
 * Date: 9/18/25
 * Time: 9:11 PM
 */
public class KthLargest {
    private final int maxSize;
    private final PriorityQueue<Integer> minHeap;

    public KthLargest(int k, int[] nums) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be greater than 0");
        }
        this.maxSize = k;
        minHeap = new PriorityQueue<>(k);
        for (int num : nums) {
            add(num);
        }
    }

    public int add(int val) {
        minHeap.offer(val);
        if (minHeap.size() > maxSize){
            minHeap.poll();
        }
        return minHeap.peek();
    }
}
