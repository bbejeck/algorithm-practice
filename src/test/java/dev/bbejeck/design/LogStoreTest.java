package dev.bbejeck.design;

import dev.bbejeck.design.LogStore.Filter;
import dev.bbejeck.design.LogStore.Log;
import dev.bbejeck.design.LogStore.Severity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static dev.bbejeck.design.LogStore.Severity.ERROR;
import static dev.bbejeck.design.LogStore.Severity.INFO;
import static dev.bbejeck.design.LogStore.Severity.WARN;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LogStore
 */
public class LogStoreTest {

    private static final Log L1 = new Log(100, INFO, "api", "start");
    private static final Log L2 = new Log(200, ERROR, "db", "timeout");
    private static final Log L3 = new Log(200, WARN, "api", "slow");
    private static final Log L4 = new Log(300, ERROR, "api", "500");

    private static LogStore example() {
        LogStore store = new LogStore();
        store.append(L1);
        store.append(L2);
        store.append(L3);
        store.append(L4);
        return store;
    }

    // Part 1

    @Test
    @DisplayName("count with inclusive time ranges")
    void testCountRange() {
        LogStore store = example();
        assertEquals(3, store.count(100, 250, Filter.ANY));
        assertEquals(4, store.count(100, 300, Filter.ANY));
        assertEquals(2, store.count(200, 200, Filter.ANY));
        assertEquals(0, store.count(201, 299, Filter.ANY));
        assertEquals(0, store.count(400, 500, Filter.ANY));
        assertEquals(0, store.count(0, 99, Filter.ANY));
    }

    @Test
    @DisplayName("count with filters")
    void testCountFilters() {
        LogStore store = example();
        assertEquals(2, store.count(0, 1000, new Filter(ERROR, null)));
        assertEquals(3, store.count(0, 1000, new Filter(null, "api")));
        assertEquals(1, store.count(0, 1000, new Filter(ERROR, "api")));
        assertEquals(0, store.count(0, 1000, new Filter(INFO, "db")));
        assertEquals(0, store.count(0, 1000, new Filter(null, "missing")));
    }

    @Test
    @DisplayName("get ascending and descending, ties in append order")
    void testGetOrder() {
        LogStore store = example();
        assertEquals(List.of(L1, L2, L3, L4), store.get(0, 1000, Filter.ANY, true, 10, 0));
        assertEquals(List.of(L4, L3, L2, L1), store.get(0, 1000, Filter.ANY, false, 10, 0));
        assertEquals(List.of(L4, L3), store.get(0, 1000, new Filter(null, "api"), false, 2, 0));
    }

    @Test
    @DisplayName("get with limit and offset")
    void testGetLimitOffset() {
        LogStore store = example();
        assertEquals(List.of(L2, L3), store.get(0, 1000, Filter.ANY, true, 2, 1));
        assertEquals(List.of(L4), store.get(0, 1000, Filter.ANY, true, 5, 3));
        assertEquals(List.of(), store.get(0, 1000, Filter.ANY, true, 5, 4));
        assertEquals(List.of(), store.get(0, 1000, Filter.ANY, true, 0, 0));
        assertEquals(List.of(L1), store.get(0, 1000, new Filter(null, "api"), false, 1, 2));
    }

    @Test
    @DisplayName("Empty store and backwards timestamps")
    void testEmptyAndInvalid() {
        LogStore store = new LogStore();
        assertEquals(0, store.count(0, Long.MAX_VALUE, Filter.ANY));
        assertEquals(List.of(), store.get(0, Long.MAX_VALUE, Filter.ANY, true, 10, 0));
        store.append(new Log(50, INFO, "a", "x"));
        store.append(new Log(50, INFO, "a", "y"));
        assertThrows(IllegalArgumentException.class, () -> store.append(new Log(49, INFO, "a", "z")));
        assertEquals(2, store.count(0, 100, Filter.ANY));
    }

    @Test
    @DisplayName("Random operations agree with brute force")
    void testRandom() {
        Random random = new Random(5);
        String[] services = {"api", "db", "web"};
        LogStore store = new LogStore();
        List<Log> all = new ArrayList<>();
        long ts = 0;
        for (int i = 0; i < 2000; i++) {
            ts += random.nextInt(3);
            Log log = new Log(ts, Severity.values()[random.nextInt(3)], services[random.nextInt(3)], "m" + i);
            store.append(log);
            all.add(log);
            if (i % 10 == 0) {
                long start = random.nextInt((int) ts + 1);
                long end = start + random.nextInt(200);
                Filter filter = new Filter(random.nextBoolean() ? null : Severity.values()[random.nextInt(3)],
                        random.nextBoolean() ? null : services[random.nextInt(3)]);
                List<Log> matches = new ArrayList<>();
                for (Log l : all) {
                    if (l.timestamp() >= start && l.timestamp() <= end
                            && (filter.severity() == null || filter.severity() == l.severity())
                            && (filter.serviceId() == null || filter.serviceId().equals(l.serviceId()))) {
                        matches.add(l);
                    }
                }
                assertEquals(matches.size(), store.count(start, end, filter));
                boolean asc = random.nextBoolean();
                int limit = random.nextInt(10);
                int offset = random.nextInt(10);
                List<Log> ordered = new ArrayList<>(matches);
                if (!asc) {
                    Collections.reverse(ordered);
                }
                List<Log> expected = ordered.subList(Math.min(offset, ordered.size()), Math.min(offset + limit, ordered.size()));
                assertEquals(expected, store.get(start, end, filter, asc, limit, offset));
            }
        }
    }

    @Test
    @DisplayName("get with a small limit doesn't scan the whole window")
    void testGetPerformance() {
        LogStore store = bigStore();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int i = 0; i < 100_000; i++) {
                assertEquals(5, store.get(0, Long.MAX_VALUE, Filter.ANY, i % 2 == 0, 5, 10).size());
            }
        });
    }

    // Part 2

    @Test
    @DisplayName("count is O(log n) with no filter, severity, service, or both")
    void testCountPerformance() {
        LogStore store = bigStore();
        Filter[] filters = {Filter.ANY, new Filter(ERROR, null), new Filter(null, "svc3"), new Filter(WARN, "svc7")};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            long total = 0;
            for (int i = 0; i < 200_000; i++) {
                total += store.count(i, 150_000 + i, filters[i % 4]);
            }
            assertTrue(total > 0);
        });
    }

    private static LogStore bigStore() {
        LogStore store = new LogStore();
        Severity[] severities = Severity.values();
        for (int i = 0; i < 200_000; i++) {
            store.append(new Log(i, severities[i % 3], "svc" + (i % 10), "m"));
        }
        return store;
    }
}
