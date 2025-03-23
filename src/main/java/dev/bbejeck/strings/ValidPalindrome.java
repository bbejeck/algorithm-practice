package dev.bbejeck.strings;

/**
 * User: Bill Bejeck
 * Date: 3/23/25
 * Time: 3:33 PM
 */
public class ValidPalindrome {

    public boolean isPalindrome(String s) {
        if (s.length() == 1) {
            return true;
        }
        char[] originalChars = s.toCharArray();
        char[] chars = new char[originalChars.length];
        int counter = 0;
        for (char c : originalChars) {
            if (Character.isLetter(c) || Character.isDigit(c)) {
                chars[counter++] = Character.toLowerCase(c);
            }
        }

        int i = 0, j = counter - 1;
        while(i <= j && chars[i] == chars[j]) {
            i++;
            j--;
        }
        return i >= j;
    }
}
