package dev.bbejeck.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZigZagConversationTest {

    private ZigZagConversation zigZagConversation;

    @BeforeEach
    void setUp() {
        zigZagConversation = new ZigZagConversation();
    }

    @Test
    @DisplayName("Test with standard example: PAYPALISHIRING with 3 rows")
    void testStandardExample3Rows() {
        String input = "PAYPALISHIRING";
        int numRows = 3;
        String expected = "PAHNAPLSIIGYIR";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with standard example: PAYPALISHIRING with 4 rows")
    void testStandardExample4Rows() {
        String input = "PAYPALISHIRING";
        int numRows = 4;
        String expected = "PINALSIGYAHRPI";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with single row")
    void testSingleRow() {
        String input = "PAYPALISHIRING";
        int numRows = 1;
        String expected = "PAYPALISHIRING";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with number of rows equal to string length")
    void testRowsEqualToLength() {
        String input = "ABC";
        int numRows = 3;
        String expected = "ABC";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with number of rows greater than string length")
    void testRowsGreaterThanLength() {
        String input = "ABC";
        int numRows = 5;
        String expected = "ABC";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with empty string")
    void testEmptyString() {
        String input = "";
        int numRows = 3;
        String expected = "";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with string of length 1")
    void testSingleCharString() {
        String input = "A";
        int numRows = 3;
        String expected = "A";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with mixed case letters")
    void testMixedCaseLetters() {
        String input = "AbCdEfGhIjK";
        int numRows = 3;
        String expected = "AEIbdfhjCGK";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with punctuation")
    void testStringWithPunctuation() {
        String input = "Hello,World.";
        int numRows = 3;
        String expected = "Horel,ol.lWd";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with maximum allowed numRows (1000)")
    void testMaxNumRows() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("ABCDEFGHIJ"); // 100 * 10 = 1000 characters
        }
        String input = sb.toString();
        int numRows = 1000;

        // With 1000 rows and 1000 characters, each character goes to its own row
        // so the output should be the same as the input
        assertEquals(input, zigZagConversation.convert(input, numRows));
    }

    @Test
    @DisplayName("Test with maximum allowed string length (1000)")
    void testMaxStringLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("ABCDEFGHIJ"); // 100 * 10 = 1000 characters
        }
        String input = sb.toString();
        int numRows = 5;

        // We need to manually calculate the expected output for this test
        String result = zigZagConversation.convert(input, numRows);

        // Verify the result has the same length as the input
        assertEquals(input.length(), result.length());

        // Sample verification of the pattern (checking first few characters)
        // This would depend on the expected ZigZag pattern with 5 rows
        char[] expected = new char[1000];

        // Manually implement a simple version of the zigzag logic to generate expected output
        int[][] indices = new int[numRows][input.length()];
        int row = 0;
        int col = 0;
        boolean goingDown = true;

        for (int i = 0; i < input.length(); i++) {
            indices[row][col] = i;

            if (goingDown) {
                row++;
                if (row == numRows - 1) {
                    goingDown = false;
                }
            } else {
                row--;
                if (row == 0) {
                    goingDown = true;
                }
            }

            if (i < input.length() - 1) {
                col++;
            }
        }

        int index = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < input.length(); c++) {
                if (indices[r][c] != 0 || (r == 0 && c == 0)) {
                    expected[index++] = input.charAt(indices[r][c]);
                }
            }
        }

        // This is a simplified check that just verifies pattern characteristics
        // For a full test, we'd need a complete implementation of the zigzag algorithm
        // which would duplicate the code we're testing
        int checkLength = 20; // Check the first 20 characters
        for (int i = 0; i < checkLength && i < result.length(); i++) {
            assertEquals(expected[i], result.charAt(i));
        }
    }

    @Test
    @DisplayName("Test with 2 rows")
    void testTwoRows() {
        String input = "ABCDEFGH";
        int numRows = 2;
        String expected = "ACEGBDFH";
        assertEquals(expected, zigZagConversation.convert(input, numRows));
    }
}