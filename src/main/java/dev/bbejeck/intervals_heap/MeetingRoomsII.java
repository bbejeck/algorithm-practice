package dev.bbejeck.intervals_heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * User: Bill Bejeck
 * Date: 8/8/25
 * Time: 11:21 AM
 */
public class MeetingRoomsII {
    static final int START_TIME = 0;
    static final int END_TIME = 1;
    
    public int minMeetingRooms(int[][] intervals) {
        if (intervals.length == 0) {
            return 0;
        }

        // Sort intervals by start time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[START_TIME]));
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int[] currentInterval : intervals) {
            if (!heap.isEmpty() && heap.peek() <= currentInterval[START_TIME]) {
                heap.poll();
            }
            heap.offer(currentInterval[END_TIME]);
        }
        return heap.size();
    }
}
