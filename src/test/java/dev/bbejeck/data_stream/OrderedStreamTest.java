
package dev.bbejeck.data_stream;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for OrderedStream implementation of LeetCode 1656
 */
public class OrderedStreamTest {

    @Test
    public void testBasicFunctionality() {
        OrderedStream os = new OrderedStream(5);

        // Insert at position 3, should return empty list
        List<String> result1 = os.insert(3, "ccccc");
        assertEquals(List.of(), result1);

        // Insert at position 1, should return ["aaaaa"]
        List<String> result2 = os.insert(1, "aaaaa");
        assertEquals(List.of("aaaaa"), result2);

        // Insert at position 2, should return ["bbbbb", "ccccc"]
        List<String> result3 = os.insert(2, "bbbbb");
        assertEquals(List.of("bbbbb", "ccccc"), result3);

        // Insert at position 5, should return empty list
        List<String> result4 = os.insert(5, "eeeee");
        assertEquals(List.of(), result4);

        // Insert at position 4, should return ["ddddd", "eeeee"]
        List<String> result5 = os.insert(4, "ddddd");
        assertEquals(List.of("ddddd", "eeeee"), result5);
    }

    @Test
    public void testSequentialInserts() {
        OrderedStream os = new OrderedStream(3);

        // Insert in order 1, 2, 3
        List<String> result1 = os.insert(1, "first");
        assertEquals(List.of("first"), result1);

        List<String> result2 = os.insert(2, "second");
        assertEquals(List.of("second"), result2);

        List<String> result3 = os.insert(3, "third");
        assertEquals(List.of("third"), result3);
    }

    @Test
    public void testReverseOrderInserts() {
        OrderedStream os = new OrderedStream(3);

        // Insert in reverse order 3, 2, 1
        List<String> result1 = os.insert(3, "third");
        assertEquals(List.of(), result1);

        List<String> result2 = os.insert(2, "second");
        assertEquals(List.of(), result2);

        List<String> result3 = os.insert(1, "first");
        assertEquals(List.of("first", "second", "third"), result3);
    }

    @Test
    public void testSingleElement() {
        OrderedStream os = new OrderedStream(1);

        List<String> result = os.insert(1, "only");
        assertEquals(List.of("only"), result);
    }

    @Test
    public void testMixedOrderInserts() {
        OrderedStream os = new OrderedStream(4);

        // Insert 2, 4, 1, 3
        List<String> result1 = os.insert(2, "b");
        assertEquals(List.of(), result1);

        List<String> result2 = os.insert(4, "d");
        assertEquals(List.of(), result2);

        List<String> result3 = os.insert(1, "a");
        assertEquals(List.of("a", "b"), result3);

        List<String> result4 = os.insert(3, "c");
        assertEquals(List.of("c", "d"), result4);
    }

    @Test
    public void testEmptyStringValues() {
        OrderedStream os = new OrderedStream(2);

        List<String> result1 = os.insert(1, "");
        assertEquals(List.of(""), result1);

        List<String> result2 = os.insert(2, "");
        assertEquals(List.of(""), result2);
    }

    @Test
    public void testLargeStream() {
        OrderedStream os = new OrderedStream(1000);

        // Insert every 10th element first
        for (int i = 10; i <= 1000; i += 10) {
            List<String> result = os.insert(i, "value" + i);
            assertEquals(List.of(), result); // Should be empty until we fill the gaps
        }

        // Now fill in the gaps starting from 1
        List<String> result = os.insert(1, "value1");
        assertEquals(1, result.size());
        assertEquals("value1", result.get(0));

        // Fill positions 2-9, should get back 2-10
        for (int i = 2; i <= 9; i++) {
            List<String> currentResult = os.insert(i, "value" + i);
            if (i < 9) {
                assertEquals(List.of("value" + i), currentResult);
            } else {
                // When we insert at position 9, we should get back positions 9 and 10
                assertEquals(List.of("value9", "value10"), currentResult);
            }
        }
    }
}