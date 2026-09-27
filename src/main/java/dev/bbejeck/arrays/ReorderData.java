package dev.bbejeck.arrays;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 9/23/26
 * Time: 9:36 PM
 */
public class ReorderData {

    public String[] reorderLogFiles(String[] logs) {
        List<String[]> strArr = new ArrayList<>();
        List<String[]> numArr = new ArrayList<>();
        for (String log : logs){
            String[] parts = log.split(" ", 2);
            if(parts[1].matches("[0-9 ]+")) {
                numArr.add(parts);
            }else{
                strArr.add(parts);
            }
        }
        strArr.sort(Comparator.<String[], String>comparing(s -> s[1]).
                thenComparing(s -> s[0]));
        String[] results = new String[strArr.size() + numArr.size()];
        int counter = 0;
        for(String[] s : strArr){
            results[counter++] = String.join(" ", s);
        }
        for(String[] n : numArr) {
            results[counter++] = String.join(" ", n);
        }
        return results;
    }
}
