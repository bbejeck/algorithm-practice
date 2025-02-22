package dev.bbejeck.arrays;

import java.util.Arrays;

/**
 * User: Bill Bejeck
 * Date: 1/25/25
 * Time: 5:00 PM
 */
public class TwoSumII {
    public int[] twoSum(int[] numbers, int target) {
        System.out.println(Arrays.toString(numbers));
        int start = 0;
        int end = numbers.length - 1;
        int[] result = new int[2];

        while (start < end) {
            int sum = numbers[start] + numbers[end];
            System.out.printf("sum=%d  start=%d end=%d%n", sum, start, end);
            if (sum == target) {
                result[0] = start + 1;
                result[1] = end + 1;
                break;
            } else if (sum < target) {
                start += 1;
            } else {
                end -= 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        TwoSumII twoSumII = new TwoSumII();
        int[] result = twoSumII.twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(result));
    }
}
