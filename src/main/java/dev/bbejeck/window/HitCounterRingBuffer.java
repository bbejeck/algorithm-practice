package dev.bbejeck.window;

/**
 * User: Bill Bejeck
 * Date: 9/27/26
 * Time: 6:59 PM
 */
public class HitCounterRingBuffer {

    int[] timestamps = new int[300];
    int[] counts = new int[300];

    public void hit(int timestamp) {
        int index = timestamp % timestamps.length;
        if (timestamps[index] != timestamp) {
            timestamps[index] = timestamp;
            counts[index] = 1;
        } else {
            counts[index]++;
        }
    }

    public int getHits(int timestamp) {
        int total = 0;
        for (int i = 0; i < timestamps.length; i++) {
            if (timestamp - timestamps[i] < 300) {
                total += counts[i];
            }
        }
        return total;
    }
    
}
