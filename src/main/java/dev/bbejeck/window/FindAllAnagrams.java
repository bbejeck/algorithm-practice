package dev.bbejeck.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 12/7/25
 * Time: 8:48 PM
 */
public class FindAllAnagrams {

    public List<Integer> findAnagrams(String original, String check) {
        List<Integer> found = new ArrayList<>();
        int checkLen = check.length();
        int originalLen = original.length();
        if (checkLen > originalLen) {
            return found;
        }
        int[] checkChars = new int[26];
        int[] window = new int[26];
        for (int i = 0; i < checkLen; i++) {
            checkChars[check.charAt(i) - 'a']++;
            window[original.charAt(i) - 'a']++;
        }
        if (Arrays.equals(checkChars, window)) {
            found.add(0);
        }
        for (int i = checkLen; i < originalLen; i++) {
            window[original.charAt(i - checkLen) -'a']--;
            window[original.charAt(i) - 'a']++;
            if (Arrays.equals(checkChars, window)) {
                found.add(i - checkLen + 1);
            }
        }
        return found;
    }
}
