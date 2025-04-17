package dev.bbejeck.strings;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 4/16/25
 * Time: 8:54 PM
 */
public class ZigZagConversation {

    public String convert(String s, int numRows) {
        if (numRows == s.length() || numRows == 1) {
            return s;
        }
        int current = 1;
        boolean ascending = true;

        Map<Integer, StringBuilder> zagMap = new HashMap<>();
        for (int i = 1; i <= numRows; i++) {
            zagMap.put(i, new StringBuilder());
        }
        char[] chars = s.toCharArray();
        for (char c : chars) {
            zagMap.get(current).append(c);
            if (ascending) {
                current++;
                if (current > numRows) {
                    current -= 2;
                    ascending = false;
                }
            } else {
                current--;
                if (current < 1) {
                    current = 2;
                    ascending = true;
                }
            }
        }
        StringBuilder allStrings = new StringBuilder();
        for (int i = 1; i <= numRows; i++) {
            allStrings.append(zagMap.get(i));
        }
        return allStrings.toString();
    }
}
