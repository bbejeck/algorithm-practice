package dev.bbejeck.strings;

import dev.bbejeck.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.bbejeck.Utils.countMap;
import static dev.bbejeck.Utils.decrement;
import static dev.bbejeck.Utils.deleteIfZero;
import static dev.bbejeck.Utils.increment;


public class AnagramStringIndex {
    public static void main(String[] args) {
        String word = "ab";
        String str = "abxaba";
        List<Integer> indices = new ArrayList<>();

        Map<Character, Integer> wordCountMap = countMap(word);
        for (int i = 0; i < word.length(); i++) {
            char c = str.charAt(i);
            decrement(wordCountMap, c);
            deleteIfZero(wordCountMap, c);
        }

        if (wordCountMap.isEmpty()) {
            indices.add(0);
        }

        for (int i = word.length(); i < str.length(); i++) {
            char startChar = str.charAt(i - word.length());
            char endChar = str.charAt(i);
            System.out.printf("start pos=%d start char=%s %n", i - word.length(), startChar);
            increment(wordCountMap, startChar);
            System.out.printf("Freq map after increment %s%n", wordCountMap);
            deleteIfZero(wordCountMap, startChar);
            System.out.printf("Freq map after incr deleteIfZero %s%n", wordCountMap);

            System.out.printf("end pos=%d end char=%s%n", i, endChar);
            decrement(wordCountMap, endChar);
            System.out.printf("Freq map after decrement %s%n", wordCountMap);
            deleteIfZero(wordCountMap, endChar);
            System.out.printf("Freq map after decr deleteIfZero %s%n", wordCountMap);
            
            if (wordCountMap.isEmpty()) {
                System.out.printf("Found an anagram at pos=%d%n", i - word.length() + 1);
                indices.add(i - word.length() + 1);
            }

        }

        System.out.println(indices);
        
    }
}
