package dev.bbejeck.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 1/16/25
 * Time: 2:52 PM
 */
public class PowerSet {

    public List<List<Integer>> powerSet(List<Integer> numbers) {
        List<List<Integer>> results = new ArrayList<>();

        backtrack(results, numbers, new ArrayList<>(), 0);

        return results;
    }

    public void backtrack(List<List<Integer>> results,
                          List<Integer> source,
                          List<Integer> current,
                          int start) {

        results.add(new ArrayList<>(current));

        for(int i = start; i < source.size(); i++) {
            current.add(source.get(i));
            backtrack(results, source, current, i + 1);
            current.removeLast();
        }
    }

    public static void main(String[] args) {
        PowerSet powerSet = new PowerSet();
        List<Integer> numbers = List.of(1, 2, 3);
        List<List<Integer>> result = powerSet.powerSet(numbers);
        System.out.println(result);
    }
}
