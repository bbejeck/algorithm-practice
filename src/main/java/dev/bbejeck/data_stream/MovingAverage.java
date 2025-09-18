package dev.bbejeck.data_stream;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 9/17/25
 * Time: 8:23 PM
 */
public class MovingAverage {
    private int sum;
    private int count;
    private final int[] window;

    public MovingAverage(int size) {
        window = new int[size];
    }

    public double next(int val) {
        int index = count % window.length;
        sum += val - window[index];
        window[index] = val;
        count++;
        return (double) sum / Math.min(count, window.length);
    }
}
