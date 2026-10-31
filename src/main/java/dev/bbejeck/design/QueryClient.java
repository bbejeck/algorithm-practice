package dev.bbejeck.design;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Snowflake-style Query Client (reported Datadog onsite question, via PracHub)
 *
 * PracHub only showed a summary of this problem, so the exact rules below are my own definitions,
 * written to cover the behavior that summary describes.
 *
 * The "warehouse" is a mock: a map from SQL text to a Plan. A plan has the timeline of states its
 * status polls return, the result rows, and which attempts fail with a network error.
 *
 * Part 1, async queries. QueryClient(config, warehouse, timeoutPolls):
 *   Every public method first checks credentials: config must have non-empty "account" and "token"
 *   values, otherwise throw QueryException(MISSING_CREDENTIALS).
 *   startQuery(sql): SQL that isn't in the warehouse throws INVALID_SQL. Otherwise return the next
 *     id: "q1", "q2", ... (failed starts don't use up an id).
 *   getStatus(id): unknown ids throw UNKNOWN_QUERY. Each successful poll returns the next state in
 *     the timeline. Past the end of the timeline, the last state repeats. Once a query is terminal
 *     (SUCCEEDED, FAILED, CANCELED) it stays terminal.
 *     Timeout: a query still not terminal after timeoutPolls successful polls is CANCELED by the next
 *     poll, which throws TIMEOUT. Later polls return CANCELED.
 *   fetchResults(id): returns the rows if the last state returned by getStatus was SUCCEEDED.
 *     Throws QUERY_FAILED for FAILED or CANCELED, NOT_READY otherwise (including before any poll).
 *   cancel(id): moves a query that isn't terminal to CANCELED. Does nothing to a terminal query.
 *   Network failures: getStatus and fetchResults calls on one query are attempts 1, 2, 3, ...
 *     If the attempt number is in the plan's networkFailOn, throw NETWORK and change nothing (the
 *     timeline doesn't advance and the poll doesn't count toward the timeout).
 *   Check in this order: credentials, unknown id, network failure, then the operation itself.
 *
 * Part 2, batches:
 *   submitBatch(queries, maxConcurrent) returns "b1", "b2", ... Every query starts QUEUED. Throw
 *     IllegalArgumentException if maxConcurrent < 1 or a final state isn't terminal.
 *   pollBatch(id) runs one cycle of that batch only (unknown ids throw UNKNOWN_BATCH):
 *     1. Each RUNNING query does one step. A query that has done all its steps ends in its finalState.
 *     2. While fewer than maxConcurrent are RUNNING, start the next QUEUED query in submit order.
 *        A query with 0 steps ends in its finalState as soon as it starts, freeing its slot.
 *     Return the number of queries in each of the five states (every state present, zeros included).
 *   Keep counts up to date as you go instead of recounting the whole batch each poll.
 *
 * Example:
 *   plan for "SELECT 1": timeline [QUEUED, RUNNING, SUCCEEDED], rows [1]
 *   startQuery("SELECT 1") -> "q1"; getStatus x3 -> QUEUED, RUNNING, SUCCEEDED; fetchResults -> [1]
 *
 *   submitBatch([(0, FAILED), (1, SUCCEEDED), (0, SUCCEEDED)], maxConcurrent 1) -> "b1"
 *   poll 1 -> QUEUED 1, RUNNING 1, FAILED 1
 *   poll 2 -> SUCCEEDED 2, FAILED 1
 *   poll 3 -> unchanged
 *
 * Follow-up to discuss: how would you keep secrets like the token out of logs and config files?
 */
public class QueryClient {

    public enum State {
        QUEUED, RUNNING, SUCCEEDED, FAILED, CANCELED;

        public boolean isTerminal() {
            return this == SUCCEEDED || this == FAILED || this == CANCELED;
        }
    }

    public enum ErrorCode {
        MISSING_CREDENTIALS, INVALID_SQL, NETWORK, TIMEOUT, UNKNOWN_QUERY, NOT_READY, QUERY_FAILED, UNKNOWN_BATCH
    }

    public static class QueryException extends RuntimeException {
        private final ErrorCode code;

        public QueryException(ErrorCode code) {
            super(code.name());
            this.code = code;
        }

        public ErrorCode code() {
            return code;
        }
    }

    public record Plan(List<State> timeline, List<Object> rows, Set<Integer> networkFailOn) {
        public Plan(List<State> timeline, List<Object> rows) {
            this(timeline, rows, Set.of());
        }
    }

    public record BatchQuery(int steps, State finalState) { }

    public QueryClient(Map<String, String> config, Map<String, Plan> warehouse, int timeoutPolls) {
        throw new UnsupportedOperationException("TODO");
    }

    public String startQuery(String sql) {
        throw new UnsupportedOperationException("TODO");
    }

    public State getStatus(String queryId) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Object> fetchResults(String queryId) {
        throw new UnsupportedOperationException("TODO");
    }

    public void cancel(String queryId) {
        throw new UnsupportedOperationException("TODO");
    }

    public String submitBatch(List<BatchQuery> queries, int maxConcurrent) {
        throw new UnsupportedOperationException("TODO");
    }

    public Map<State, Integer> pollBatch(String batchId) {
        throw new UnsupportedOperationException("TODO");
    }
}
