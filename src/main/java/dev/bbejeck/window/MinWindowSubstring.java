package dev.bbejeck.window;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 4/19/25
 * Time: 12:10 PM
 */
public class MinWindowSubstring {
    public String minWindow(String s, String t) {
        // Handle edge cases
        if (t.length() > s.length()) {
            return "";
        }
        if (s.equals(t)) {
            return s;
        }
        Map<Character, Integer> targetFreq = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetFreq.put(c, targetFreq.getOrDefault(c, 0) + 1);
        }
        int left = 0;
        int right = 0;
        int requiredChars = targetFreq.size();
        int formedChars = 0;
        Map<Character, Integer> windowFreq = new HashMap<>();

        // Track minimum window
        int minLen = Integer.MAX_VALUE;
        int minLeft = 0;
        int minRight = 0;

        while (right < s.length()) {
            char rightChar = s.charAt(right);
            windowFreq.put(rightChar, windowFreq.getOrDefault(rightChar, 0) + 1);

            if (targetFreq.containsKey(rightChar) &&
                    windowFreq.get(rightChar).intValue() == targetFreq.get(rightChar).intValue()) {
                formedChars++;
            }
            while (left <= right && formedChars == requiredChars) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minLeft = left;
                    minRight = right;
                }
                char leftChar = s.charAt(left);
                windowFreq.put(leftChar, windowFreq.get(leftChar) - 1);

                if (targetFreq.containsKey(leftChar) &&
                        windowFreq.get(leftChar) < targetFreq.get(leftChar)) {
                    formedChars--;
                }
                left++;
            }
            right++;
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minRight + 1);
    }
}
