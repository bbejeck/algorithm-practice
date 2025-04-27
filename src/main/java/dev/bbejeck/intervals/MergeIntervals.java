package dev.bbejeck.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 4/27/25
 * Time: 12:25 PM
 */
public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) {
            return intervals;
        }
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        int[] mi = intervals[0];
        for(int i = 1; i < intervals.length; i++) {
            int[] curr = intervals[i];
            if (mi[1] >= curr[0]){
                mi[1] = Math.max(mi[1], curr[1]);
            } else {
                merged.add(mi);
                mi = curr;
            }
        }
        merged.add(mi);
        return merged.toArray(new int[0][]);
    }
}
