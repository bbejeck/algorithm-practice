package dev.bbejeck;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 12/12/24
 * Time: 8:44 PM
 */
public class Utils {

    private Utils() {
    }

    public static int from2D(int row, int col, int numColumns) {
        return row * numColumns + col;
    }
    public static int[] to2D(int index, int numColumns) {
        return new int[] {index / numColumns, index % numColumns};
    }

    public static boolean allZero(Map<Character, Integer> map) {
        return map.values().stream().reduce(0, Integer::sum) == 0;
    }

    public static void decrement(Map<Character, Integer> map, char character) {
        map.put(character, map.getOrDefault(character, 0) - 1);
    }

    public static void deleteIfZero(Map<Character, Integer> map, char character) {
        if (map.get(character) == 0) {
            map.remove(character);
        }
    }

    public static void increment(Map<Character, Integer> map, char character) {
        map.put(character, map.getOrDefault(character, 0) + 1);
    }

    public static Map<Character, Integer> countMap(String str) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : str.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        return map;
    }
}
