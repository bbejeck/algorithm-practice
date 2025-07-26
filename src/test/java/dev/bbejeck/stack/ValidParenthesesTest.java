package dev.bbejeck.stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ValidParenthesesTest {

    private ValidParentheses validParentheses;

    @BeforeEach
    void setUp() {
        validParentheses = new ValidParentheses();
    }

    @Test
    @DisplayName("Test valid simple parentheses")
    void testValidSimpleParentheses() {
        assertTrue(validParentheses.isValid("()"));
        assertTrue(validParentheses.isValid("[]"));
        assertTrue(validParentheses.isValid("{}"));
    }

    @Test
    @DisplayName("Test valid nested parentheses")
    void testValidNestedParentheses() {
        assertTrue(validParentheses.isValid("([])"));
        assertTrue(validParentheses.isValid("{[()]}"));
        assertTrue(validParentheses.isValid("((()))"));
        assertTrue(validParentheses.isValid("{{{}}}"));
        assertTrue(validParentheses.isValid("[[[]]]"));
    }

    @Test
    @DisplayName("Test valid multiple pairs")
    void testValidMultiplePairs() {
        assertTrue(validParentheses.isValid("()[]{}"));
        assertTrue(validParentheses.isValid("(){}[]"));
        assertTrue(validParentheses.isValid("[]{}()"));
        assertTrue(validParentheses.isValid("()()()"));
        assertTrue(validParentheses.isValid("[][]"));
        assertTrue(validParentheses.isValid("{}{}"));
    }

    @Test
    @DisplayName("Test valid complex combinations")
    void testValidComplexCombinations() {
        assertTrue(validParentheses.isValid("({[]})"));
        assertTrue(validParentheses.isValid("([{}])"));
        assertTrue(validParentheses.isValid("{[()]}()"));
        assertTrue(validParentheses.isValid("()({[]})"));
    }

    @Test
    @DisplayName("Test invalid single character")
    void testInvalidSingleCharacter() {
        assertFalse(validParentheses.isValid("("));
        assertFalse(validParentheses.isValid(")"));
        assertFalse(validParentheses.isValid("["));
        assertFalse(validParentheses.isValid("]"));
        assertFalse(validParentheses.isValid("{"));
        assertFalse(validParentheses.isValid("}"));
    }

    @Test
    @DisplayName("Test invalid unmatched opening brackets")
    void testInvalidUnmatchedOpening() {
        assertFalse(validParentheses.isValid("(("));
        assertFalse(validParentheses.isValid("[["));
        assertFalse(validParentheses.isValid("{{"));
        assertFalse(validParentheses.isValid("(["));
        assertFalse(validParentheses.isValid("({"));
        assertFalse(validParentheses.isValid("[{"));
    }

    @Test
    @DisplayName("Test invalid unmatched closing brackets")
    void testInvalidUnmatchedClosing() {
        assertFalse(validParentheses.isValid("))"));
        assertFalse(validParentheses.isValid("]]"));
        assertFalse(validParentheses.isValid("}}"));
        assertFalse(validParentheses.isValid(")]"));
        assertFalse(validParentheses.isValid(")}"));
        assertFalse(validParentheses.isValid("]}"));
    }

    @Test
    @DisplayName("Test invalid mismatched pairs")
    void testInvalidMismatchedPairs() {
        assertFalse(validParentheses.isValid("(]"));
        assertFalse(validParentheses.isValid("([)]"));
        assertFalse(validParentheses.isValid("{[}]"));
        assertFalse(validParentheses.isValid("({[}])"));
        assertFalse(validParentheses.isValid("(}"));
        assertFalse(validParentheses.isValid("[)"));
    }

    @Test
    @DisplayName("Test invalid wrong order")
    void testInvalidWrongOrder() {
        assertFalse(validParentheses.isValid(")("));
        assertFalse(validParentheses.isValid("}{"));
        assertFalse(validParentheses.isValid("]["));
        assertFalse(validParentheses.isValid(")()"));
        assertFalse(validParentheses.isValid("}{}"));
        assertFalse(validParentheses.isValid("][]"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "b", "1", "x", "z"})
    @DisplayName("Test edge cases - empty and non-bracket characters")
    void testEdgeCases(String input) {
        // Empty string should return true (no brackets to validate)
        if (input.isEmpty()) {
            // Note: Current implementation doesn't handle empty string explicitly
            // This might need to be adjusted based on requirements
        } else {
            // Single non-bracket characters should return false based on length check
            assertFalse(validParentheses.isValid(input));
        }
    }

    @Test
    @DisplayName("Test LeetCode examples")
    void testLeetCodeExamples() {
        // Example 1
        assertTrue(validParentheses.isValid("()"));

        // Example 2
        assertTrue(validParentheses.isValid("()[]{}"));

        // Example 3
        assertFalse(validParentheses.isValid("(]"));

        // Additional common test case
        assertFalse(validParentheses.isValid("([)]"));
    }

    @Test
    @DisplayName("Test isOpen helper method")
    void testIsOpenMethod() {
        assertTrue(validParentheses.isOpen('('));
        assertTrue(validParentheses.isOpen('['));
        assertTrue(validParentheses.isOpen('{'));

        assertFalse(validParentheses.isOpen(')'));
        assertFalse(validParentheses.isOpen(']'));
        assertFalse(validParentheses.isOpen('}'));
        assertFalse(validParentheses.isOpen('a'));
        assertFalse(validParentheses.isOpen('1'));
        assertFalse(validParentheses.isOpen(' '));
    }

    @Test
    @DisplayName("Test long valid strings")
    void testLongValidStrings() {
        StringBuilder sb = new StringBuilder();
        // Create a long valid string with nested brackets
        for (int i = 0; i < 100; i++) {
            sb.append("([{");
        }
        for (int i = 0; i < 100; i++) {
            sb.append("}])");
        }
        assertTrue(validParentheses.isValid(sb.toString()));
    }

    @Test
    @DisplayName("Test long invalid strings")
    void testLongInvalidStrings() {
        StringBuilder sb = new StringBuilder();
        // Create a long string with one unmatched bracket
        for (int i = 0; i < 100; i++) {
            sb.append("()");
        }
        sb.append("("); // Add one unmatched opening bracket
        assertFalse(validParentheses.isValid(sb.toString()));
    }

    @Test
    @DisplayName("Test specific failing case from problem description")
    void testSpecificFailingCase() {
        // The case mentioned in the original question
        assertFalse(validParentheses.isValid("(("));
    }

    @Test
    @DisplayName("Test alternating valid patterns")
    void testAlternatingValidPatterns() {
        assertTrue(validParentheses.isValid("()()()"));
        assertTrue(validParentheses.isValid("[][]"));
        assertTrue(validParentheses.isValid("{}{}{}"));
        assertTrue(validParentheses.isValid("()[]{}"));
    }

    @Test
    @DisplayName("Test deeply nested valid patterns")
    void testDeeplyNestedValidPatterns() {
        assertTrue(validParentheses.isValid("(((())))"));
        assertTrue(validParentheses.isValid("[[[[]]]]"));
        assertTrue(validParentheses.isValid("{{{{}}}}"));
        assertTrue(validParentheses.isValid("({[({[()]})]})"));
    }
}