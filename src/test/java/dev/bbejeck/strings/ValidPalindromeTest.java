package dev.bbejeck.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidPalindromeTest {

    private final ValidPalindrome validator = new ValidPalindrome();

    @Test
    @DisplayName("Single character is always a palindrome")
    void testSingleCharacter() {
        assertTrue(validator.isPalindrome("a"));
        assertTrue(validator.isPalindrome("5"));
        assertTrue(validator.isPalindrome("!"));
    }

    @ParameterizedTest
    @DisplayName("True for valid palindromes with mixed case and non-alphanumeric characters")
    @ValueSource(strings = {
            "A man, a plan, a canal: Panama",
            "No 'x' in Nixon",
            "Was it a car or a cat I saw?",
            "Madam, in Eden, I'm Adam",
            "A Santa at NASA",
            "Never odd or even",
            "Step on no pets",
            "1a2b3c3b2a1"
    })
    void testValidPalindromes(String input) {
        assertTrue(validator.isPalindrome(input));
    }

    @ParameterizedTest
    @DisplayName("False for non-palindromes")
    @ValueSource(strings = {
            "hello",
            "palindrome",
            "Race a car",
            "abc123",
            "A b C d",
            "Java is fun"
    })
    void testNonPalindromes(String input) {
        assertFalse(validator.isPalindrome(input));
    }

    @Test
    @DisplayName("Test empty string and whitespace")
    void testEmptyAndWhitespace() {
        // Empty string would be a valid palindrome
        assertTrue(validator.isPalindrome(""));
        // String with only non-alphanumeric characters (effectively empty after filtering)
        assertTrue(validator.isPalindrome(" .,;:!?"));
    }

    @Test
    @DisplayName("Test alphanumeric palindromes")
    void testAlphanumericPalindromes() {
        assertTrue(validator.isPalindrome("12321"));
        assertTrue(validator.isPalindrome("A1b2C3c2b1a"));
        assertTrue(validator.isPalindrome("123 321"));
        assertTrue(validator.isPalindrome("1221"));
    }

    @Test
    @DisplayName("Test case insensitivity")
    void testCaseInsensitive() {
        assertTrue(validator.isPalindrome("AbBa"));
        assertTrue(validator.isPalindrome("aAbBaA"));
        assertTrue(validator.isPalindrome("RaCeCaR"));
    }

    @Test
    @DisplayName("Test extreme length within constraints")
    void testLongPalindrome() {
        // Create a palindrome at the upper boundary of the constraints
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append('a');
        }
        sb.append('x'); // Middle character
        for (int i = 0; i < 100000; i++) {
            sb.append('a');
        }
        assertTrue(validator.isPalindrome(sb.toString()));

        // Modify to make it not a palindrome
        sb.setCharAt(1000, 'b');
        assertFalse(validator.isPalindrome(sb.toString()));
    }

    @Test
    @DisplayName("Test edge case handling")
    void testEdgeCases() {
        // String with alphanumeric and non-alphanumeric characters
        assertTrue(validator.isPalindrome("A man, a plan, a canal: Panama"));
        // String with numbers and special characters
        assertTrue(validator.isPalindrome("$12,321$"));
        // String with spaces and special characters
        assertTrue(validator.isPalindrome("Doc, note: I dissent. A fast never prevents a fatness. I diet on cod."));
    }
}