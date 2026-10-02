package dev.bbejeck.counting;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: Bill Bejeck
 * Date: 9/29/26
 * Time: 8:58 PM
 */
public class FirstUniqueNumber {
    Map<Integer, Integer> numberTracker = new HashMap<>();
    Set<Integer> uniques = new LinkedHashSet<>();

    public FirstUniqueNumber(int[] nums) {
        for (int num : nums) {
            add(num);
        }
    }

    public int showFirstUnique() {
        if (uniques.isEmpty()) {
            return -1;
        } else {
           return uniques.iterator().next();
        }
    }

    public void add (int num) {
        if (numberTracker.merge(num, 1, Integer::sum) == 1) {
            uniques.add(num);
        } else {
            uniques.remove(num);
        }
    }
}
