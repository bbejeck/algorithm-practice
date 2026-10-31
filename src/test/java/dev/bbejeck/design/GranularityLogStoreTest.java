package dev.bbejeck.design;

import dev.bbejeck.design.GranularityLogStore.Granularity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static dev.bbejeck.design.GranularityLogStore.Granularity.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GranularityLogStore
 */
public class GranularityLogStoreTest {

    @Test
    @DisplayName("Example 1")
    void testExample1() {
        GranularityLogStore store = new GranularityLogStore();
        store.add(1, "2017-01-01 23:59:59");
        store.add(2, "2017-01-02 00:00:00");
        store.add(3, "2018-03-01 12:00:00");
        assertEquals(List.of(1, 2), store.query("2017-01-01 00:00:00", "2017-12-31 23:59:59", YEAR));
        assertEquals(List.of(1), store.query("2017-01-01 23:00:00", "2017-01-01 23:59:00", HOUR));
    }

    @Test
    @DisplayName("Example 2: chronological order, ties in add order")
    void testExample2() {
        GranularityLogStore store = new GranularityLogStore();
        store.add(10, "2020-05-20 10:20:30");
        store.add(11, "2020-05-01 00:00:00");
        store.add(12, "2020-05-20 10:20:30");
        assertEquals(List.of(11, 10, 12), store.query("2020-05-01 00:00:00", "2020-05-31 23:59:59", MONTH));
        assertEquals(List.of(10, 12), store.query("2020-05-20 00:00:00", "2020-05-20 23:59:59", DAY));
    }

    @Test
    @DisplayName("Bounds are cut to the granularity, not just the stored timestamps")
    void testBoundsTruncated() {
        GranularityLogStore store = new GranularityLogStore();
        store.add(1, "2020-05-01 00:00:00");
        store.add(2, "2020-05-31 23:59:59");
        store.add(3, "2020-06-01 00:00:00");
        assertEquals(List.of(1, 2), store.query("2020-05-31 12:00:00", "2020-05-01 06:00:00", MONTH));
        assertEquals(List.of(2), store.query("2020-05-31 23:59:00", "2020-05-31 23:59:00", MINUTE));
        assertEquals(List.of(1, 2, 3), store.query("2020-12-31 23:59:59", "2020-01-01 00:00:00", YEAR));
    }

    @Test
    @DisplayName("SECOND granularity is exact; empty results")
    void testSecond() {
        GranularityLogStore store = new GranularityLogStore();
        store.add(1, "2021-03-04 05:06:07");
        store.add(2, "2021-03-04 05:06:08");
        assertEquals(List.of(1), store.query("2021-03-04 05:06:07", "2021-03-04 05:06:07", SECOND));
        assertEquals(List.of(1, 2), store.query("2021-03-04 05:06:07", "2021-03-04 05:06:08", SECOND));
        assertEquals(List.of(), store.query("2021-03-04 05:06:09", "2021-03-04 06:00:00", SECOND));
        assertEquals(List.of(), new GranularityLogStore().query("2000-01-01 00:00:00", "2099-01-01 00:00:00", YEAR));
    }

    @Test
    @DisplayName("Random adds and queries agree with brute force")
    void testRandom() {
        Random random = new Random(29);
        GranularityLogStore store = new GranularityLogStore();
        List<String[]> logs = new ArrayList<>();
        int[] cut = {4, 7, 10, 13, 16, 19};
        for (int i = 0; i < 1500; i++) {
            String ts = randomTimestamp(random);
            store.add(i, ts);
            logs.add(new String[]{ts, String.valueOf(i)});
            if (i % 5 == 0) {
                String a = randomTimestamp(random);
                String b = randomTimestamp(random);
                String start = a.compareTo(b) <= 0 ? a : b;
                String end = a.compareTo(b) <= 0 ? b : a;
                Granularity g = Granularity.values()[random.nextInt(6)];
                int n = cut[g.ordinal()];
                List<String[]> expected = new ArrayList<>();
                for (String[] log : logs) {
                    String t = log[0].substring(0, n);
                    if (t.compareTo(start.substring(0, n)) >= 0 && t.compareTo(end.substring(0, n)) <= 0) {
                        expected.add(log);
                    }
                }
                expected.sort(Comparator.comparing(l -> l[0]));
                List<Integer> ids = expected.stream().map(l -> Integer.parseInt(l[1])).toList();
                assertEquals(ids, store.query(start, end, g), g + " " + start + " .. " + end);
            }
        }
    }

    @Test
    @DisplayName("Narrow queries over many logs are fast")
    void testPerformance() {
        GranularityLogStore store = new GranularityLogStore();
        Random random = new Random(1);
        for (int i = 0; i < 100_000; i++) {
            store.add(i, randomTimestamp(random));
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 100_000; i++) {
                String ts = randomTimestamp(random);
                store.query(ts, ts, SECOND);
            }
        });
    }

    private static String randomTimestamp(Random random) {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d",
                2018 + random.nextInt(3), 1 + random.nextInt(12), 1 + random.nextInt(28),
                random.nextInt(24), random.nextInt(60), random.nextInt(60));
    }
}
