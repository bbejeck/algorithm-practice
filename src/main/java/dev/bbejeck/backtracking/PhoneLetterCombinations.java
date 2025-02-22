package dev.bbejeck.backtracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 1/12/25
 * Time: 5:04 PM
 */
public class PhoneLetterCombinations {
        static Map<Character, String> phoneMap = Map.of('2', "abc",
                '3', "def", 
                '4', "ghi", 
                '5', "jkl", 
                '6', "mno", 
                '7', "pqrs", 
                '8', "tuv", 
                '9', "wxyz");

    public static void main(String[] args) {
        String digits = "23";
        List<String> result =letterCombinations(digits);
        System.out.printf("%s%n", result);

    }

    public static List<String> letterCombinations(String phoneDigits) {
        List<String> result = new ArrayList<>();
        if (phoneDigits == null || phoneDigits.isEmpty()) {
            return result;
        }
        backtrack(result, "", phoneDigits, 0);
        return result;
    }

    private static void backtrack(List<String> result, String combination, String digits, int index) {
        if (index == digits.length()) {
            result.add(combination);
            return;
        }

        char digit = digits.charAt(index);
        String letters = phoneMap.get(digit);
        for (char letter : letters.toCharArray()) {
            backtrack(result, combination + letter, digits, index + 1);
        }
    }
}
