package dev.bbejeck.arrays;

import java.util.HashMap;

/**
 * User: Bill Bejeck
 * Date: 1/14/25
 * Time: 9:41 PM
 */
public class RomanToInteger {
    private static final HashMap<Character, Integer> romanNumerals = new HashMap<>();

    static {
        romanNumerals.put('I', 1);
        romanNumerals.put('V', 5);
        romanNumerals.put('X', 10);
        romanNumerals.put('L', 50);
        romanNumerals.put('C', 100);
        romanNumerals.put('D', 500);
        romanNumerals.put('M', 1000);
    }
    public int romanToInt(String s) {
        char[] chars = s.toCharArray();
        int romanSum = 0;
        int previous = Integer.MAX_VALUE;

        for (int i = 0; i < chars.length; i++) {
            int current = romanNumerals.get(chars[i]);
            if (current > previous) {
                int temp = current - previous;
                romanSum -= previous;
                romanSum = romanSum + temp;
            } else {
                romanSum+= current;
            }
            previous = current;
        }
        return romanSum;

    }

    public static void main(String[] args) {
        RomanToInteger romanToInteger = new RomanToInteger();
        System.out.println(romanToInteger.romanToInt("III"));
        System.out.println(romanToInteger.romanToInt("LVIII"));
        System.out.println(romanToInteger.romanToInt("MCMXCIV"));

    }
}
