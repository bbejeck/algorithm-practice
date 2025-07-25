package dev.bbejeck.strings;

/**
 * User: Bill Bejeck
 * Date: 7/24/25
 * Time: 10:09 PM
 */
public class LongestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        int maxCols = Integer.MAX_VALUE;
        for (String word : strs) {
            maxCols = Math.min(maxCols, word.length());
        }

        for (int i = 0; i < maxCols; i++) {
            char current = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (current != strs[j].charAt(i)) {
                    return  strs[0].substring(0, i);
                }
            }
        }

        return strs[0].substring(0, maxCols);
    }
}
