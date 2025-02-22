package dev.bbejeck.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 1/26/25
 * Time: 1:06 PM
 */
public class CombinationSumIII {

    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();

        backtrack(result, new ArrayList<>(), 1, k, n);
        return result;

    }

    public void backtrack(List<List<Integer>> result, List<Integer> current, int start, int maxCombo, int target) {
        if (target < 0) {
            return;
        }
        else if (target == 0 && current.size() == maxCombo ) {
            result.add(new ArrayList<>(current));
        } else {
             for (int i = start; i <= 9; i++) {
                 current.add(i);
                 backtrack(result, current, i + 1, maxCombo, target - i);
                 current.removeLast();
             }
        }
    }

    public static void main(String[] args) {
        CombinationSumIII combinationSumII = new CombinationSumIII();
        System.out.println(combinationSumII.combinationSum3(3, 7));
        System.out.println(combinationSumII.combinationSum3(3, 9));
    }
}
