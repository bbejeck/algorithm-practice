package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 3/1/25
 * Time: 8:23 PM
 */
public class ReverseWordsInAString {
    public String reverseWords(String s) {
        System.out.println("Input string: \"" + s + "\"");
        if (s.length() == 1) {
            return s;
        }
        char[] chars = s.toCharArray();
        char[] rev = new char[chars.length];
        System.out.println("Character array: " + java.util.Arrays.toString(chars));
        boolean wordStarted = false;
        int end=0, start = 0;
        int copyStart = 0;
        for(int i = chars.length - 1; i >=0; i--) {
            if (chars[i] == ' ') {
                if (!wordStarted) {
                    continue;
                } else {
                    start =  i + 1;
                    wordStarted = false;
                    int len = (end - start) + 1;
                    System.out.println("Start: " + start + " end: " + end + " len: " + len + "");
                    System.out.println("Copying word: \"" + new String(chars, start, len) + "\" to position " + copyStart);
                    System.arraycopy(chars, start, rev, copyStart, len);
                    System.out.println("Reverse so far : \"" + new String(rev));
                    rev[copyStart + len] = ' ';
                    copyStart += len + 1;
                    System.out.println("Detected word from index " + start + " to " + end);
                }
            } else {
                if (!wordStarted) {
                    wordStarted = true;
                    end = i;
                }
                if (i == 0) {
                    int len = end - i + 1;
                    System.arraycopy(chars, i, rev, copyStart, len);
                    copyStart += len;
                }

            }

        }
        String result = new String(rev).trim();
        System.out.println("Reversed string: \"" + result + "\"");
        return result;
    }

    public static void main(String[] args) {
        ReverseWordsInAString reverseWordsInAString = new ReverseWordsInAString();
        System.out.println(reverseWordsInAString.reverseWords("the sky is blue"));
    }
}
