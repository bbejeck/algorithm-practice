package dev.bbejeck.dynamic_programming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for CoinChange algorithm that follows these requirements:
 * - Given coins of different denominations and a total amount
 * - Return the fewest number of coins needed to make up that amount
 * - Return -1 if the amount cannot be made up by any combination of coins
 * - Assume an infinite number of each kind of coin is available
 */
public class CoinChangeTest {

    private CoinChange coinChange;

    @BeforeEach
    void setUp() {
        coinChange = new CoinChange();
    }

    @Test
    public void testZeroAmount() {
        int[] coins = {1, 2, 5};
        // When amount is 0, we need 0 coins
        assertEquals(0, coinChange.coinChange(coins, 0));
    }

    @Test
    public void testImpossibleChange() {
        // Cannot make amount 3 with coins [2,5]
        int[] coins = {2, 5};
        assertEquals(-1, coinChange.coinChange(coins, 3));

        // Cannot make odd amounts with only even-valued coins
        int[] evenCoins = {2, 4, 6, 8};
        assertEquals(-1, coinChange.coinChange(evenCoins, 3));
        assertEquals(-1, coinChange.coinChange(evenCoins, 7));
    }

    @Test
    public void testExampleCases() {
        // Example 1: coins = [1,2,5], amount = 11
        // Output: 3 (5 + 5 + 1 = 11)
        int[] coins1 = {1, 2, 5};
        assertEquals(3, coinChange.coinChange(coins1, 11));

        // Example 2: coins = [2], amount = 3
        // Output: -1 (cannot make 3 with only coin 2)
        int[] coins2 = {2};
        assertEquals(-1, coinChange.coinChange(coins2, 3));

        // Example 3: coins = [1], amount = 0
        // Output: 0 (no coins needed for amount 0)
        int[] coins3 = {1};
        assertEquals(0, coinChange.coinChange(coins3, 0));
    }

    @ParameterizedTest
    @MethodSource("provideCoinChangeTestCases")
    public void testCoinChangeCombinations(int[] coins, int amount, int expectedResult) {
        assertEquals(expectedResult, coinChange.coinChange(coins, amount));
    }

    private static Stream<Arguments> provideCoinChangeTestCases() {
        return Stream.of(
                // Basic test cases
                Arguments.of(new int[]{1, 2, 5}, 11, 3),      // 5+5+1
                Arguments.of(new int[]{2}, 3, -1),            // Impossible
                Arguments.of(new int[]{1}, 0, 0),             // No coins needed

                // Multiple valid combinations but return minimum
                Arguments.of(new int[]{1, 3, 4}, 6, 2),       // Optimal: 3+3=2 coins (not 4+1+1=3)
                Arguments.of(new int[]{1, 4, 5}, 8, 2),       // Optimal: 4+4=2 coins (not 5+1+1+1=4)

                // Larger denominations
                Arguments.of(new int[]{1, 5, 10, 25}, 30, 3), // 10+10+10 = 3 coins
                Arguments.of(new int[]{1, 5, 10, 25}, 40, 4), // 25+5+5+5 = 4 coins

                // Cases where we must use smaller coins
                Arguments.of(new int[]{1, 3, 4}, 7, 2),       // 3+4=2 coins
                Arguments.of(new int[]{2, 5, 10}, 6, 3),      // 2+2+2=3 coins

                // Larger amounts
                Arguments.of(new int[]{1, 2, 5}, 100, 20)     // 20 coins (5×20)
        );
    }

    @Test
    public void testEmptyCoinsArray() {
        // With no coins available, any positive amount is impossible
        int[] coins = {};
        assertEquals(-1, coinChange.coinChange(coins, 5));
    }

    @Test
    public void testSingleCoinDenomination() {
        // When only one coin denomination is available
        int[] coins = {5};

        // Amounts that are multiples of 5
        assertEquals(1, coinChange.coinChange(coins, 5));
        assertEquals(2, coinChange.coinChange(coins, 10));
        assertEquals(3, coinChange.coinChange(coins, 15));

        // Amounts that are not multiples of 5 (impossible)
        assertEquals(-1, coinChange.coinChange(coins, 3));
        assertEquals(-1, coinChange.coinChange(coins, 7));
        assertEquals(-1, coinChange.coinChange(coins, 11));
    }

    @Test
    public void testNonEfficientCoins() {
        // Test where some coins are never used (inefficient)
        int[] coins = {1, 3, 4}; // 3 is more efficient than 1+1+1
        assertEquals(2, coinChange.coinChange(coins, 6)); // 3+3=2 coins
    }

    @Test
    public void testWithOptions() {
        // Test with coins that can create multiple paths to solution
        int[] coins = {1, 5, 10, 21, 25};
        assertEquals(3, coinChange.coinChange(coins, 63)); // 21+21+21=3 coins
        assertEquals(2, coinChange.coinChange(coins, 50)); // 25+25=2 coins
    }
}