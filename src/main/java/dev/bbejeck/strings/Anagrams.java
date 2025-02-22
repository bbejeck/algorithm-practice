package dev.bbejeck.strings;

import java.util.Map;

import static dev.bbejeck.Utils.allZero;
import static dev.bbejeck.Utils.countMap;
import static dev.bbejeck.Utils.decrement;


public class Anagrams {

    public static void main(String[] args) {
        String word = "abba";
        String candidate = "baba";

        Map<Character, Integer> wordMap = countMap(word);
        for (int i = 0; i < candidate.length(); i++) {
            decrement(wordMap, candidate.charAt(i));
        }
        if (allZero(wordMap)) {
            System.out.printf("%s IS an anagram of %s%n", word, candidate);
        } else {
            System.out.printf("%s is NOT an anagram of %s%n", word, candidate);
        }
    }
}
