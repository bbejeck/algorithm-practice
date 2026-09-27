package dev.bbejeck.counting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 9/24/26
 * Time: 9:00 PM
 */
public class SubdomainVisits {

    public List<String> subdomainVisits(String[] cpdomains) {
        Map<String, Integer> domainCounts = new HashMap<>();
        for (String cpdomain : cpdomains){
            int splitIdx = cpdomain.indexOf(" ");
            int count = Integer.parseInt(cpdomain.substring(0,splitIdx));
            String domains = cpdomain.substring(splitIdx + 1);
            domainCounts.merge(domains, count, Integer::sum);
            int nextIndex = domains.indexOf('.');
            while (nextIndex > -1) {
                domainCounts.merge(domains.substring(nextIndex + 1),
                        count,
                        Integer::sum);
                nextIndex = domains.indexOf('.', nextIndex + 1);
            }

        }
        List<String> results = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : domainCounts.entrySet()) {
            results.add(entry.getValue() + " " + entry.getKey());
        }
        return results;
    }
}
