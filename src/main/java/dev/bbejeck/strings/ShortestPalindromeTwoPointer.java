package dev.bbejeck.strings;

/**
 * User: Bill Bejeck
 * Date: 3/14/25
 * Time: 12:57 PM
 */
public class ShortestPalindromeTwoPointer {

    public String shortestPalindrome(String s) {
        StringBuilder builder = new StringBuilder(s);
        String reversed = builder.reverse().toString();
        int n = s.length();
        for (int i = 0; i < n; i++ ) {
            if (s.substring(0, n-i).equals(reversed.substring(i))){
                return reversed.substring(0, i) + s;
            }
        }

        return reversed + s;
    }
}
