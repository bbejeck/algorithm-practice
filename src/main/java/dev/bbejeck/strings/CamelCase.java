package dev.bbejeck.strings;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 9/22/26
 * Time: 7:47 PM
 */
public class CamelCase {

    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> results = new ArrayList<>();
        char[] p = pattern.toCharArray();
        for (String query : queries) {
             results.add(isMatch(query.toCharArray(), p));
        }
        return results;
    }

    public boolean isMatch(char[] q, char[] p){
        int j = 0;
        for (int i = 0; i < q.length; i++) {
            if (j < p.length && q[i] == p[j]) {
                j++;
            } else if (q[i] >= 'A' && q[i] <= 'Z') {
                return false;
            }
        }
        return j == p.length;
    }
}
