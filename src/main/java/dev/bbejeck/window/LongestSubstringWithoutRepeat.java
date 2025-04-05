package dev.bbejeck.window;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 4/5/25
 * Time: 7:39 PM
 */
public class LongestSubstringWithoutRepeat {

    public int lengthOfLongestSubstring(String s) {
        if (s.isEmpty()) {
            return 0;
        }

        if (s.length() == 1) {
            return 1;
        }

        int maxSubString = Integer.MIN_VALUE;
        int left = 0;
        Map<Character, Integer> charIndexMap = new HashMap<>();
        for (int right = 0; right < s.length(); right++) {
            char current = s.charAt(right);
            if (charIndexMap.containsKey(current) &&
                    charIndexMap.get(current) >= left) {
                left = charIndexMap.get(current) + 1;
            }
            charIndexMap.put(current, right);
            maxSubString = Math.max(maxSubString, right - left + 1);
        }

        return maxSubString;
    }
}
