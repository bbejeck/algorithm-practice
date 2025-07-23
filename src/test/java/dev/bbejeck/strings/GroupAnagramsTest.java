
package dev.bbejeck.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GroupAnagrams
 * Tests various scenarios including edge cases and typical use cases
 */
class GroupAnagramsTest {

    private GroupAnagrams groupAnagrams;

    @BeforeEach
    void setUp() {
        groupAnagrams = new GroupAnagrams();
    }

    @Test
    @DisplayName("Test basic anagram grouping")
    void testBasicAnagramGrouping() {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        assertEquals(3, result.size());
        
        // Convert to sets for easier comparison (order doesn't matter)
        Set<Set<String>> expected = Set.of(
            Set.of("eat", "tea", "ate"),
            Set.of("tan", "nat"),
            Set.of("bat")
        );
        
        Set<Set<String>> actual = new HashSet<>();
        for (List<String> group : result) {
            actual.add(new HashSet<>(group));
        }
        
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test empty array")
    void testEmptyArray() {
        String[] input = {};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Test single element")
    void testSingleElement() {
        String[] input = {"a"};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals("a", result.get(0).get(0));
    }

    @Test
    @DisplayName("Test no anagrams - all unique")
    void testNoAnagrams() {
        String[] input = {"abc", "def", "ghi"};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        assertEquals(3, result.size());
        
        // Each group should have exactly one element
        for (List<String> group : result) {
            assertEquals(1, group.size());
        }
    }

    @Test
    @DisplayName("Test all strings are anagrams")
    void testAllAnagrams() {
        String[] input = {"abc", "bca", "cab", "acb"};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        assertEquals(1, result.size());
        assertEquals(4, result.get(0).size());
        
        Set<String> expected = Set.of("abc", "bca", "cab", "acb");
        Set<String> actual = new HashSet<>(result.get(0));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test empty strings")
    void testEmptyStrings() {
        String[] input = {"", "", "a"};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        assertEquals(2, result.size());
        
        // Find the group with empty strings
        List<String> emptyGroup = result.stream()
            .filter(group -> group.contains(""))
            .findFirst()
            .orElse(null);
        
        assertNotNull(emptyGroup);
        assertEquals(2, emptyGroup.size());
        assertTrue(emptyGroup.stream().allMatch(String::isEmpty));
    }

    @Test
    @DisplayName("Test case sensitivity")
    void testCaseSensitivity() {
        String[] input = {"Aa", "aA", "aa", "AA"};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        // "Aa" and "aA" are anagrams, but "aa" and "AA" are separate
        assertEquals(3, result.size());
    }

    @ParameterizedTest
    @MethodSource("provideAnagramTestCases")
    @DisplayName("Parameterized test cases")
    void testParameterizedCases(String[] input, int expectedGroups, String description) {
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        assertEquals(expectedGroups, result.size(), description);
    }

    private static Stream<Arguments> provideAnagramTestCases() {
        return Stream.of(
            Arguments.of(new String[]{"a"}, 1, "Single character"),
            Arguments.of(new String[]{"ab", "ba"}, 1, "Two anagrams"),
            Arguments.of(new String[]{"ab", "cd"}, 2, "No anagrams"),
            Arguments.of(new String[]{"abc", "bca", "xyz"}, 2, "Mixed case"),
            Arguments.of(new String[]{""}, 1, "Single empty string"),
            Arguments.of(new String[]{"", ""}, 1, "Multiple empty strings")
        );
    }

    @Test
    @DisplayName("Test result structure integrity")
    void testResultStructureIntegrity() {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = groupAnagrams.groupAnagrams(input);
        
        // Verify no null groups
        assertFalse(result.contains(null), "Result should not contain null groups");
        
        // Verify no empty groups
        assertTrue(result.stream().noneMatch(List::isEmpty), "Result should not contain empty groups");
        
        // Verify total count matches input
        int totalStrings = result.stream().mapToInt(List::size).sum();
        assertEquals(input.length, totalStrings, "Total strings in result should match input length");
        
        // Verify all input strings are present
        Set<String> inputSet = new HashSet<>(Arrays.asList(input));
        Set<String> resultSet = new HashSet<>();
        result.forEach(resultSet::addAll);
        assertEquals(inputSet, resultSet, "All input strings should be present in result");
    }
}
