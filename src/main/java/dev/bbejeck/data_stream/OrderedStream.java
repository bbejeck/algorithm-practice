package dev.bbejeck.data_stream;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 9/20/25
 * Time: 7:45 PM
 */
public class OrderedStream {
    private final String[] vals;
    private int pointer = 1;

    public OrderedStream(int n) {
         vals = new String[n + 1];
    }

    public List<String> insert(int idKey, String value) {
            List<String> result = new ArrayList<>();
            vals[idKey] = value;
            while(pointer < vals.length && vals[pointer] != null) {
                    result.add(vals[pointer]);
                    pointer++;
            }
            return result;
    }
}
