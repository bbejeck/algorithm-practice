package dev.bbejeck.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Buffered File Writer with Concurrency Support (reported Datadog technical screen question, via PracHub)
 *
 * FileWriter (given below) writes straight to disk, so every call is slow. It is NOT thread-safe:
 * it counts any call that overlaps another call as a violation. You may only call its write and
 * flush methods.
 *
 * Part 1, single-threaded:
 *   BufferedFileWriter(underlying, capacity): capacity in characters. Throw IllegalArgumentException
 *     if capacity < 1.
 *   write(data) appends to an internal buffer. If the buffer then holds >= capacity characters,
 *     forward the whole buffer with ONE underlying.write call and clear it. Large writes are not split.
 *   flush() forwards the buffer if it is non-empty, then always calls underlying.flush().
 *   Forwarding bytes (batching) and underlying.flush() (durability) are different things: a full
 *   buffer only forwards.
 *
 * Part 2, thread safety. Many threads call write and flush at once. Guarantee that:
 *   - each write's characters reach the file contiguously, with nothing lost or duplicated
 *   - writes from one thread reach the file in the order that thread made them
 *   - every write that returned before flush() was called is in the file when flush() returns
 *   - the underlying FileWriter is never called by two threads at once
 *   Be ready to explain the "swap the buffer under the lock, write outside it" optimization and the
 *   ordering bug it introduces.
 *
 * Part 3, background flushing:
 *   startAutoFlush(interval, unit) starts one background thread that calls flush() every interval.
 *   close() stops the background thread (waiting for it to exit), then flushes. After close, write
 *   and flush throw IllegalStateException, and no more background flushes happen. A second close
 *   does nothing.
 *
 * Example (capacity 5):
 *   write "ab", write "cd"   -> nothing forwarded
 *   write "e"                -> underlying.write("abcde")
 *   write "fghijk"           -> underlying.write("fghijk")
 *   flush                    -> nothing to forward; underlying.flush()
 */
public class BufferedFileWriter {

    // Given: the slow, non-thread-safe writer. Don't change it.
    public static class FileWriter {
        private final StringBuilder contents = new StringBuilder();
        private final List<String> writes = new ArrayList<>();
        private final AtomicInteger inFlight = new AtomicInteger();
        private final AtomicInteger violations = new AtomicInteger();
        private int flushCalls;

        public void write(String data) {
            enter();
            try {
                synchronized (this) {
                    contents.append(data);
                    writes.add(data);
                }
            } finally {
                inFlight.decrementAndGet();
            }
        }

        public void flush() {
            enter();
            try {
                synchronized (this) {
                    flushCalls++;
                }
            } finally {
                inFlight.decrementAndGet();
            }
        }

        private void enter() {
            if (inFlight.incrementAndGet() > 1) {
                violations.incrementAndGet();
            }
            Thread.yield();
        }

        public synchronized String contents() {
            return contents.toString();
        }

        public synchronized List<String> writes() {
            return List.copyOf(writes);
        }

        public synchronized int flushCalls() {
            return flushCalls;
        }

        public int concurrentCalls() {
            return violations.get();
        }
    }

    public BufferedFileWriter(FileWriter underlying, int capacity) {
        throw new UnsupportedOperationException("TODO");
    }

    public void write(String data) {
        throw new UnsupportedOperationException("TODO");
    }

    public void flush() {
        throw new UnsupportedOperationException("TODO");
    }

    public void startAutoFlush(long interval, TimeUnit unit) {
        throw new UnsupportedOperationException("TODO");
    }

    public void close() {
        throw new UnsupportedOperationException("TODO");
    }
}
