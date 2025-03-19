package dev.bbejeck.dynamic_programming;

/**
 * User: Bill Bejeck
 * Date: 3/18/25
 * Time: 6:09 PM
 */
public class LongestPalindromicSubstring {

    public String longestPalindrome(String s) {
         if (s == null || s.isEmpty()) {
             return "";
         }
         int start = 0,  end = 0;
         for (int i = 0; i < s.length(); i++) {
             int len1 = findPalindromeLength(s, i, i);
             int len2 = findPalindromeLength(s, i, i+1);
             int len = Math.max(len1, len2);
             if (len > end - start) {
                 start = i - (len - 1) / 2;
                 end = i + len / 2;
             }
         }  
         return s.substring(start, end + 1);
    }

    private int findPalindromeLength(String s, int left, int right) {
        while (right < s.length() && left >=0 && s.charAt(right) == s.charAt(left)){
            right++;
            left--;
        }
        return right - left -1;
    }
}
