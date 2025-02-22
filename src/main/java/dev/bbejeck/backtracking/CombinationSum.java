package dev.bbejeck.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 1/20/25
 * Time: 4:44 PM
 */
public class CombinationSum {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {

        List<List<Integer>> results = new ArrayList<>();
        backtrack(results, candidates, new ArrayList<>(),0, target);
        return results;
    }

    public void backtrack(List<List<Integer>> results,
                          int[] candidates,
                          List<Integer> current,
                          int start,
                          int target) {
        
        if (target < 0) {
            return;
        } else if (target == 0) {
            results.add(new ArrayList<>(current));
        } else {

            for (int i = start; i < candidates.length; i++) {
                int val = candidates[i];
                current.add(val);
                backtrack(results, candidates, current, i , target - val);
                current.removeLast();
            }
        }
    }

    public static void main(String[] args) {
        CombinationSum combinationSum = new CombinationSum();
        int[] candidates = {2, 3, 5};
        List<List<Integer>> result = combinationSum.combinationSum(candidates, 8);
        System.out.println(result);
    }
}
