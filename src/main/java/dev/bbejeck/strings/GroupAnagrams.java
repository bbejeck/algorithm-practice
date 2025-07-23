package dev.bbejeck.strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 7/22/25
 * Time: 9:44 PM
 */
public class GroupAnagrams {
        public List<List<String>> groupAnagrams(String[] strs) {
            Map<String, List<String>> anagramGroups = new HashMap<>();

            for (String str : strs) {
                char[] chars = str.toCharArray();
                Arrays.sort(chars);
                String key = new String(chars);
                anagramGroups.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
            }

            return new ArrayList<>(anagramGroups.values());
        }
}
