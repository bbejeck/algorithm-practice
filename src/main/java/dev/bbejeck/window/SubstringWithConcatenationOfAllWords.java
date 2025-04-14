package dev.bbejeck.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 4/10/25
 * Time: 8:51 PM
 */
public class SubstringWithConcatenationOfAllWords {
    public List<Integer> findSubstring(String s, String[] words) {
        int wordLength = words[0].length();
        int allWordLength = words.length * wordLength;

        if (allWordLength > s.length()) {
            return Collections.emptyList();
        }

        if (wordLength == 1) {
            char firstChar = words[0].charAt(0);
            boolean allSame = true;
            for (String word : words) {
                if (word.length() != 1 || word.charAt(0) != firstChar) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                List<Integer> result = new ArrayList<>();
                int count = 0;

                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == firstChar) {
                        count++;
                    } else {
                        count = 0;
                    }

                    // If we've seen enough characters and position is valid
                    if (count >= words.length && i - words.length + 1 >= 0) {
                        result.add(i - words.length + 1);
                    }
                }

                return result;
            }
        }


        Map<String, Integer> wordMap = new HashMap<>();
        for (String word : words) {
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }
        
        List<Integer> si = new ArrayList<>();
        for (int i = 0; i <= s.length() - allWordLength; i++) {
            Map<String, Integer> seen = new HashMap<>();
            int j = 0;
            while ( j < words.length) {
                String word = s.substring(i + j * wordLength, i + (j + 1) * wordLength);
                if (wordMap.containsKey(word)) {
                       seen.put(word, seen.getOrDefault(word, 0) + 1);
                       if (seen.get(word) > wordMap.getOrDefault(word, 0)) {
                            break;
                       }
                } else {
                   break;
                }
                j++;
            }
            if (j == words.length) {
                si.add(i);
            }
        }
        return si;
    }
}
