package dev.bbejeck.strings;

import dev.bbejeck.Utils;

/**
 * User: Bill Bejeck
 * Date: 3/15/25
 * Time: 2:13 PM
 */
public class ShortestPalindromeKMP {

    public String shortestPalindrome(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        String rev = new StringBuilder(s).reverse().toString();
        String combined = s + "#" + rev;
        int[] lps = Utils.computeLPS(combined);
        int lonPalindromeLen = lps[combined.length() -1];
        String suffix = s.substring(lonPalindromeLen);
        String revSuffix = new StringBuilder(suffix).reverse().toString();
        return revSuffix + s;
    }
}
