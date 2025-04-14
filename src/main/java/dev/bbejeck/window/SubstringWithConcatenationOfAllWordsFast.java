package dev.bbejeck.window;

import java.util.AbstractList;
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
public class SubstringWithConcatenationOfAllWordsFast {
    public List<Integer> findSubstring(String s, String[] words) {
        return new AbstractList<Integer>() {
            //from given Condition Substring should be of lenght
            //wordLen * no of words in the list
            //Since they havent specified that all word are unique
            //We can mantain a HahsMap With the words and they occurance count
            //Start idx can be in range (0,)
            //Now ,from the Start Index
            //till thr right+wordlen  outofbound
            //we will take substring of length word and check if it is presnet in Map .
            //if presennt we will add it to the current  Window HaspMap (Where we will store the occurances of thewords )
            //then we will move on to the next word
            // when wordcout Map equals to matched count
            // then we will add left pointer to result


            int wordLen = words[0].length();
            int winLen = words.length * wordLen;
            List<Integer> result = null;

            private void init() {
                result = new ArrayList<>();
                Map<String, Integer> wordCount = new HashMap<>();

                // Count occurrences of each word
                for (String word : words) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }

                // Iterate through the string in steps of word length
                for (int i = 0; i < wordLen; i++) {
                    slidingWindow(s, i, wordCount, wordLen, winLen);
                }
            }

            private void slidingWindow(String s, int start, Map<String, Integer> wordCount, int wordLen, int winLen) {
                Map<String, Integer> windowCount = new HashMap<>();
                int left = start, right = start, matched = 0;

                while (right + wordLen <= s.length()) {
                    String word = s.substring(right, right + wordLen);
                    right += wordLen;

                    if (wordCount.containsKey(word)) {
                        windowCount.put(word, windowCount.getOrDefault(word, 0) + 1);

                        if (windowCount.get(word).equals(wordCount.get(word))) {
                            matched++;
                        }
                    }

                    while (matched == wordCount.size()) {
                        if (right - left == winLen) {
                            result.add(left);
                        }

                        String leftWord = s.substring(left, left + wordLen);
                        left += wordLen;

                        if (wordCount.containsKey(leftWord)) {
                            if (windowCount.get(leftWord).equals(wordCount.get(leftWord))) {
                                matched--;
                            }
                            windowCount.put(leftWord, windowCount.get(leftWord) - 1);
                        }
                    }
                }
            }

            @Override
            public int size() {
                if (result == null) {
                    init();
                }
                return result.size();
            }

            @Override
            public Integer get(int idx) {
                if (result == null) {
                    init();
                }
                return result.get(idx);
            }
        };
    }
}
