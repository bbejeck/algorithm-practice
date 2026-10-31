package dev.bbejeck.design;

import dev.bbejeck.design.QueryClient.BatchQuery;
import dev.bbejeck.design.QueryClient.ErrorCode;
import dev.bbejeck.design.QueryClient.Plan;
import dev.bbejeck.design.QueryClient.QueryException;
import dev.bbejeck.design.QueryClient.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dev.bbejeck.design.QueryClient.State.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QueryClient
 */
public class QueryClientTest {

    private static final Map<String, String> CONFIG = Map.of("account", "acme", "token", "t0k3n");

    private static final Map<String, Plan> WAREHOUSE = Map.of(
            "SELECT 1", new Plan(List.of(QUEUED, RUNNING, SUCCEEDED), List.of(1)),
            "SELECT bad", new Plan(List.of(QUEUED, FAILED), List.of()),
            "SELECT slow", new Plan(List.of(QUEUED, RUNNING, RUNNING, RUNNING, SUCCEEDED), List.of("done")),
            "SELECT flaky", new Plan(List.of(QUEUED, SUCCEEDED), List.of(42), Set.of(1, 3)),
            "SELECT stuck", new Plan(List.of(RUNNING), List.of()));

    private static QueryClient client() {
        return new QueryClient(CONFIG, WAREHOUSE, 100);
    }

    private static void assertError(ErrorCode code, Executable call) {
        QueryException e = assertThrows(QueryException.class, call);
        assertEquals(code, e.code());
    }

    private static Map<State, Integer> counts(int queued, int running, int succeeded, int failed, int canceled) {
        Map<State, Integer> m = new EnumMap<>(State.class);
        m.put(QUEUED, queued);
        m.put(RUNNING, running);
        m.put(SUCCEEDED, succeeded);
        m.put(FAILED, failed);
        m.put(CANCELED, canceled);
        return m;
    }

    // Part 1

    @Test
    @DisplayName("Example: start, poll to success, fetch")
    void testHappyPath() {
        QueryClient c = client();
        String id = c.startQuery("SELECT 1");
        assertEquals("q1", id);
        assertEquals(QUEUED, c.getStatus(id));
        assertEquals(RUNNING, c.getStatus(id));
        assertEquals(SUCCEEDED, c.getStatus(id));
        assertEquals(List.of(1), c.fetchResults(id));
        assertEquals(SUCCEEDED, c.getStatus(id), "terminal states repeat");
        assertEquals(List.of(1), c.fetchResults(id));
    }

    @Test
    @DisplayName("Ids are sequential; invalid SQL doesn't use one up")
    void testIds() {
        QueryClient c = client();
        assertEquals("q1", c.startQuery("SELECT 1"));
        assertError(ErrorCode.INVALID_SQL, () -> c.startQuery("DROP TABLE x"));
        assertEquals("q2", c.startQuery("SELECT 1"));
        assertEquals("q3", c.startQuery("SELECT bad"));
    }

    @Test
    @DisplayName("Missing or empty credentials fail every call")
    void testMissingCredentials() {
        Map<String, String> noToken = Map.of("account", "acme");
        Map<String, String> emptyToken = new HashMap<>(Map.of("account", "acme", "token", ""));
        Map<String, String> emptyAccount = Map.of("account", "", "token", "x");
        for (Map<String, String> config : List.of(noToken, emptyToken, emptyAccount, Map.<String, String>of())) {
            QueryClient c = new QueryClient(config, WAREHOUSE, 100);
            assertError(ErrorCode.MISSING_CREDENTIALS, () -> c.startQuery("SELECT 1"));
            assertError(ErrorCode.MISSING_CREDENTIALS, () -> c.getStatus("q1"));
            assertError(ErrorCode.MISSING_CREDENTIALS, () -> c.fetchResults("q1"));
            assertError(ErrorCode.MISSING_CREDENTIALS, () -> c.cancel("q1"));
            assertError(ErrorCode.MISSING_CREDENTIALS, () -> c.submitBatch(List.of(), 1));
            assertError(ErrorCode.MISSING_CREDENTIALS, () -> c.pollBatch("b1"));
        }
    }

    @Test
    @DisplayName("Unknown query ids")
    void testUnknownQuery() {
        QueryClient c = client();
        assertError(ErrorCode.UNKNOWN_QUERY, () -> c.getStatus("q1"));
        assertError(ErrorCode.UNKNOWN_QUERY, () -> c.fetchResults("q9"));
        assertError(ErrorCode.UNKNOWN_QUERY, () -> c.cancel("nope"));
    }

    @Test
    @DisplayName("fetchResults before success, and after failure")
    void testFetchStates() {
        QueryClient c = client();
        String ok = c.startQuery("SELECT 1");
        assertError(ErrorCode.NOT_READY, () -> c.fetchResults(ok));
        c.getStatus(ok);
        c.getStatus(ok);
        assertError(ErrorCode.NOT_READY, () -> c.fetchResults(ok));

        String bad = c.startQuery("SELECT bad");
        c.getStatus(bad);
        assertEquals(FAILED, c.getStatus(bad));
        assertError(ErrorCode.QUERY_FAILED, () -> c.fetchResults(bad));
    }

    @Test
    @DisplayName("Last timeline state repeats")
    void testTimelineEnd() {
        QueryClient c = client();
        String id = c.startQuery("SELECT stuck");
        for (int i = 0; i < 10; i++) {
            assertEquals(RUNNING, c.getStatus(id));
        }
    }

    @Test
    @DisplayName("Cancel moves a running query to CANCELED; terminal queries are untouched")
    void testCancel() {
        QueryClient c = client();
        String id = c.startQuery("SELECT slow");
        c.getStatus(id);
        c.cancel(id);
        assertEquals(CANCELED, c.getStatus(id));
        assertError(ErrorCode.QUERY_FAILED, () -> c.fetchResults(id));

        String done = c.startQuery("SELECT 1");
        c.getStatus(done);
        c.getStatus(done);
        c.getStatus(done);
        c.cancel(done);
        assertEquals(SUCCEEDED, c.getStatus(done));

        String unpolled = c.startQuery("SELECT 1");
        c.cancel(unpolled);
        assertEquals(CANCELED, c.getStatus(unpolled));
    }

    @Test
    @DisplayName("Timeout cancels a query after timeoutPolls successful polls")
    void testTimeout() {
        QueryClient c = new QueryClient(CONFIG, WAREHOUSE, 2);
        String id = c.startQuery("SELECT slow");
        assertEquals(QUEUED, c.getStatus(id));
        assertEquals(RUNNING, c.getStatus(id));
        assertError(ErrorCode.TIMEOUT, () -> c.getStatus(id));
        assertEquals(CANCELED, c.getStatus(id));
        assertError(ErrorCode.QUERY_FAILED, () -> c.fetchResults(id));
    }

    @Test
    @DisplayName("Reaching a terminal state on the last allowed poll is not a timeout")
    void testNoTimeoutAtLimit() {
        QueryClient c = new QueryClient(CONFIG, WAREHOUSE, 3);
        String id = c.startQuery("SELECT 1");
        c.getStatus(id);
        c.getStatus(id);
        assertEquals(SUCCEEDED, c.getStatus(id));
        assertEquals(SUCCEEDED, c.getStatus(id));
    }

    @Test
    @DisplayName("Network failures don't advance the timeline or count toward the timeout")
    void testNetworkFailures() {
        QueryClient c = new QueryClient(CONFIG, WAREHOUSE, 2);
        String id = c.startQuery("SELECT flaky");
        assertError(ErrorCode.NETWORK, () -> c.getStatus(id));      // attempt 1
        assertEquals(QUEUED, c.getStatus(id));                       // attempt 2
        assertError(ErrorCode.NETWORK, () -> c.fetchResults(id));   // attempt 3
        assertEquals(SUCCEEDED, c.getStatus(id));                    // attempt 4, poll 2 of 2
        assertEquals(List.of(42), c.fetchResults(id));               // attempt 5
    }

    @Test
    @DisplayName("Network failure is checked after the unknown id check")
    void testCheckOrder() {
        QueryClient c = client();
        assertError(ErrorCode.UNKNOWN_QUERY, () -> c.getStatus("q1"));
        String id = c.startQuery("SELECT flaky");
        assertError(ErrorCode.NETWORK, () -> c.fetchResults(id));
        assertError(ErrorCode.NOT_READY, () -> c.fetchResults(id));
    }

    // Part 2

    @Test
    @DisplayName("Batch example with maxConcurrent 1")
    void testBatchExample() {
        QueryClient c = client();
        String b = c.submitBatch(List.of(new BatchQuery(0, FAILED), new BatchQuery(1, SUCCEEDED), new BatchQuery(0, SUCCEEDED)), 1);
        assertEquals("b1", b);
        assertEquals(counts(1, 1, 0, 1, 0), c.pollBatch(b));
        assertEquals(counts(0, 0, 2, 1, 0), c.pollBatch(b));
        assertEquals(counts(0, 0, 2, 1, 0), c.pollBatch(b));
    }

    @Test
    @DisplayName("Batches progress independently")
    void testBatchesIndependent() {
        QueryClient c = client();
        String b1 = c.submitBatch(List.of(new BatchQuery(2, SUCCEEDED), new BatchQuery(1, CANCELED), new BatchQuery(1, FAILED)), 2);
        String b2 = c.submitBatch(List.of(new BatchQuery(1, SUCCEEDED)), 2);
        assertEquals("b2", b2);
        assertEquals(counts(1, 2, 0, 0, 0), c.pollBatch(b1));
        assertEquals(counts(0, 1, 0, 0, 0), c.pollBatch(b2));
        // b1: q1 has 1 step left, q2 finishes CANCELED, q3 starts
        assertEquals(counts(0, 2, 0, 0, 1), c.pollBatch(b1));
        assertEquals(counts(0, 0, 1, 0, 0), c.pollBatch(b2));
        assertEquals(counts(0, 0, 1, 1, 1), c.pollBatch(b1));
    }

    @Test
    @DisplayName("Many zero-step queries all finish in one poll")
    void testBatchZeroSteps() {
        QueryClient c = client();
        List<BatchQuery> queries = List.of(new BatchQuery(0, SUCCEEDED), new BatchQuery(0, SUCCEEDED),
                new BatchQuery(0, FAILED), new BatchQuery(3, SUCCEEDED), new BatchQuery(0, SUCCEEDED));
        String b = c.submitBatch(queries, 1);
        assertEquals(counts(1, 1, 2, 1, 0), c.pollBatch(b));
        assertEquals(counts(1, 1, 2, 1, 0), c.pollBatch(b));
        assertEquals(counts(1, 1, 2, 1, 0), c.pollBatch(b));
        assertEquals(counts(0, 0, 4, 1, 0), c.pollBatch(b));
    }

    @Test
    @DisplayName("Empty batches, unknown batches, invalid arguments")
    void testBatchEdges() {
        QueryClient c = client();
        String b = c.submitBatch(List.of(), 3);
        assertEquals(counts(0, 0, 0, 0, 0), c.pollBatch(b));
        assertError(ErrorCode.UNKNOWN_BATCH, () -> c.pollBatch("b99"));
        assertThrows(IllegalArgumentException.class, () -> c.submitBatch(List.of(new BatchQuery(1, SUCCEEDED)), 0));
        assertThrows(IllegalArgumentException.class, () -> c.submitBatch(List.of(new BatchQuery(1, RUNNING)), 1));
        assertEquals("b2", c.submitBatch(List.of(new BatchQuery(1, SUCCEEDED)), 1));
    }
}
