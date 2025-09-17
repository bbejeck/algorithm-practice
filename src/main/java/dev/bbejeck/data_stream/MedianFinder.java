package dev.bbejeck.data_stream;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * User: Bill Bejeck
 * Date: 9/15/25
 * Time: 9:08 PM
 */
public class MedianFinder {
    private final PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.naturalOrder());
    private final PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

    public void addNum(int num) {
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.add(num);
        } else {
            minHeap.add(num);
        }
        if (maxHeap.size() + 1 < minHeap.size()){
            maxHeap.add(minHeap.poll());
        } else if (minHeap.size() + 1 < maxHeap.size()) {
            minHeap.add(maxHeap.poll());
        }
    }

    public double median() {
       if (maxHeap.size() > minHeap.size()) {
           return maxHeap.peek();
       } else if (minHeap.size() > maxHeap.size()) {
           return minHeap.peek();
       } else {
           return (double) (minHeap.peek() + maxHeap.peek()) / 2;
       }
    }
}
