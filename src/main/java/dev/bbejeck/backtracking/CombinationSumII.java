package dev.bbejeck.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Bill Bejeck
 * Date: 1/25/25
 * Time: 4:03 PM
 */
public class CombinationSumII {

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> results = new ArrayList<>();
        Arrays.sort(candidates);
        Set<List<Integer>>  haveSeen = new HashSet<>();
        backtrack(candidates, target, 0, results, new ArrayList<>(), haveSeen);
        return results;
    }


    public void backtrack(int[] candidates, int target, int start, List<List<Integer>> results, List<Integer> current, Set<List<Integer>> haveSeen) {
         int sum = current.stream().reduce(0, Integer::sum);
         if (sum == target && !haveSeen.contains(current)) {
             results.add(new ArrayList<>(current));
             haveSeen.add(current);
         }

        for (int i = start; i < candidates.length; i++) {
             int val = candidates[i];
             current.add(val);
             backtrack(candidates, target, i + 1, results, current, haveSeen);
             current.removeLast();
        }
    }

    public static void main(String[] args) {
        CombinationSumII combinationSumII = new CombinationSumII();
        System.out.println(combinationSumII.combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));
    }
}
