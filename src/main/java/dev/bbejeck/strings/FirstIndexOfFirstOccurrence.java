package dev.bbejeck.strings;

/**
 * User: Bill Bejeck
 * Date: 3/11/25
 * Time: 9:16 PM
 */
public class FirstIndexOfFirstOccurrence {

    public int strStr(String haystack, String needle) {
        if (needle.length() > haystack.length()) {
            return -1;
        }

        if (needle.length() == haystack.length() && needle.equals(haystack)) {
            return 0;
        }

        char[] haystackChars = haystack.toCharArray();
        char[] needleChars = needle.toCharArray();

        for (int i = 0; i <= haystackChars.length - needleChars.length; i++){
            int j = i;
            int k = 0;
            boolean keepLooking = true;
            while (k < needleChars.length && keepLooking) {
                if (haystackChars[j] == needleChars[k]) {
                    j++;
                    k++;
                } else {
                    keepLooking = false;
                }
            }
            if (k == needleChars.length) {
                return i;
            }
        }

        return -1;

    }
}
