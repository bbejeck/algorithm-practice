package dev.bbejeck.strings;

import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 7/30/25
 * Time: 11:42 PM
 */
public class WordBreak {

    public boolean wordBreak(String s, List<String> wordDict) {
        int n = s.length();
        boolean[] canSplit = new boolean[n + 1];
        canSplit[0] = true;
        for (int i = 1; i <= n; i++) {
            for (String word:  wordDict) {
                int start = i - word.length();
                if (start >= 0 &&
                        canSplit[start] &&
                        s.substring(start, start + word.length()).equals(word)
                   ) {
                    canSplit[i] = true;
                    break;
                }
            }
        }
        return canSplit[n];
    }
}
