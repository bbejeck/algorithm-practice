package dev.bbejeck.backtracking;

import java.util.ArrayList;
import java.util.List;

public class Permutations {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> permutations = permute(nums);
        for (List<Integer> perm : permutations) {
            System.out.println(perm);
        }
    }

    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        for (int num : nums) {
            current.add(num);
        }
        backtrack(result, current, 0);
        return result;
    }

    private static void backtrack(List<List<Integer>> result, List<Integer> nums, int start) {
        if (start == nums.size()) {
            // Add a copy of the current permutation to the result
            result.add(new ArrayList<>(nums));
            return;
        }

        for (int i = start; i < nums.size(); i++) {
            // Swap current element with the start element
            swap(nums, start, i);

            // Explore further with the next element
            backtrack(result, nums, start + 1);

            // Backtrack: undo the swap
            swap(nums, start, i);
        }
    }

    private static void swap(List<Integer> nums, int i, int j) {
        int temp = nums.get(i);
        nums.set(i, nums.get(j));
        nums.set(j, temp);
    }
}