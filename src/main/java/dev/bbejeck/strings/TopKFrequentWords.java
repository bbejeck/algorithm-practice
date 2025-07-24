package dev.bbejeck.strings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * User: Bill Bejeck
 * Date: 7/23/25
 * Time: 8:04 PM
 */
public class TopKFrequentWords {

    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> wordCounts = new HashMap<>();
        Comparator<Map.Entry<String, Integer>> comparator =
                Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey);
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(comparator);

        for (String word : words) {
            wordCounts.merge(word, 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            pq.offer(entry);
        }
        List<String> freq = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            freq.add(pq.poll().getKey());
        }

        return freq;

    }
}
