package dev.bbejeck.concurrency;

import dev.bbejeck.concurrency.BufferedFileWriter.FileWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BufferedFileWriter
 */
public class BufferedFileWriterTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    // Part 1

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        FileWriter file = new FileWriter();
        BufferedFileWriter w = new BufferedFileWriter(file, 5);
        w.write("ab");
        w.write("cd");
        assertEquals(List.of(), file.writes());
        w.write("e");
        assertEquals(List.of("abcde"), file.writes());
        w.write("fghijk");
        assertEquals(List.of("abcde", "fghijk"), file.writes());
        assertEquals(0, file.flushCalls());
        w.flush();
        assertEquals(List.of("abcde", "fghijk"), file.writes());
        assertEquals(1, file.flushCalls());
    }

    @Test
    @DisplayName("flush forwards what's left, then flushes")
    void testFlush() {
        FileWriter file = new FileWriter();
        BufferedFileWriter w = new BufferedFileWriter(file, 10);
        w.write("abc");
        w.flush();
        assertEquals(List.of("abc"), file.writes());
        assertEquals(1, file.flushCalls());
        w.flush();
        w.flush();
        assertEquals(List.of("abc"), file.writes(), "empty flush must not forward");
        assertEquals(3, file.flushCalls());
    }

    @Test
    @DisplayName("Capacity 1 forwards every non-empty write")
    void testCapacityOne() {
        FileWriter file = new FileWriter();
        BufferedFileWriter w = new BufferedFileWriter(file, 1);
        w.write("a");
        w.write("bc");
        assertEquals(List.of("a", "bc"), file.writes());
        assertThrows(IllegalArgumentException.class, () -> new BufferedFileWriter(new FileWriter(), 0));
    }

    // Part 2

    @Test
    @DisplayName("Concurrent writes: contiguous, nothing lost, per-thread order, no overlapping calls")
    void testConcurrentWrites() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            int threads = 8;
            int perThread = 3000;
            FileWriter file = new FileWriter();
            BufferedFileWriter w = new BufferedFileWriter(file, 64);
            CyclicBarrier start = new CyclicBarrier(threads);
            AtomicReference<Throwable> failure = new AtomicReference<>();
            List<Thread> workers = new ArrayList<>();
            for (int t = 0; t < threads; t++) {
                int id = t;
                workers.add(new Thread(() -> {
                    try {
                        start.await();
                        for (int i = 0; i < perThread; i++) {
                            w.write("<" + id + ":" + i + ":" + "x".repeat(i % 7) + ">");
                            if (i % 500 == 0) {
                                w.flush();
                            }
                        }
                    } catch (Throwable e) {
                        failure.compareAndSet(null, e);
                    }
                }));
            }
            workers.forEach(Thread::start);
            for (Thread t : workers) {
                t.join();
            }
            w.flush();
            assertNull(failure.get());
            assertEquals(0, file.concurrentCalls(), "underlying FileWriter was called concurrently");

            String contents = file.contents();
            assertTrue(contents.startsWith("<") && contents.endsWith(">"));
            String[] tokens = contents.substring(1, contents.length() - 1).split("><", -1);
            assertEquals(threads * perThread, tokens.length);
            int[] next = new int[threads];
            for (String token : tokens) {
                String[] parts = token.split(":", -1);
                assertEquals(3, parts.length, "write was split or interleaved: " + token);
                int id = Integer.parseInt(parts[0]);
                int seq = Integer.parseInt(parts[1]);
                assertEquals(next[id], seq, "thread " + id + " out of order");
                assertEquals("x".repeat(seq % 7), parts[2]);
                next[id]++;
            }
        });
    }

    @Test
    @DisplayName("A thread's flush makes its earlier writes visible")
    void testFlushVisibility() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            FileWriter file = new FileWriter();
            BufferedFileWriter w = new BufferedFileWriter(file, 1_000_000);
            AtomicReference<String> failure = new AtomicReference<>();
            List<Thread> workers = new ArrayList<>();
            for (int t = 0; t < 6; t++) {
                int id = t;
                workers.add(new Thread(() -> {
                    for (int i = 0; i < 200; i++) {
                        String token = "[" + id + "-" + i + "]";
                        w.write(token);
                        w.flush();
                        if (!file.contents().contains(token)) {
                            failure.compareAndSet(null, token + " missing after flush");
                        }
                    }
                }));
            }
            workers.forEach(Thread::start);
            for (Thread t : workers) {
                t.join();
            }
            assertNull(failure.get(), failure::get);
            assertEquals(0, file.concurrentCalls());
        });
    }

    // Part 3

    @Test
    @DisplayName("Background flushing pushes small writes without an explicit flush")
    void testAutoFlush() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            FileWriter file = new FileWriter();
            BufferedFileWriter w = new BufferedFileWriter(file, 1000);
            w.startAutoFlush(20, TimeUnit.MILLISECONDS);
            w.write("hello");
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            while (!file.contents().equals("hello") && System.nanoTime() < deadline) {
                Thread.sleep(10);
            }
            assertEquals("hello", file.contents());
            assertTrue(file.flushCalls() > 0);
            w.close();
            assertEquals(0, file.concurrentCalls());
        });
    }

    @Test
    @DisplayName("close stops the background thread, flushes, and rejects later calls")
    void testClose() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            FileWriter file = new FileWriter();
            BufferedFileWriter w = new BufferedFileWriter(file, 1000);
            w.startAutoFlush(10, TimeUnit.MILLISECONDS);
            w.write("abc");
            w.close();
            assertEquals("abc", file.contents());
            int flushes = file.flushCalls();
            Thread.sleep(150);
            assertEquals(flushes, file.flushCalls(), "background flushes continued after close");
            assertThrows(IllegalStateException.class, () -> w.write("x"));
            assertThrows(IllegalStateException.class, w::flush);
            w.close();
            assertEquals(flushes, file.flushCalls());
        });
    }

    @Test
    @DisplayName("close without auto flush still flushes")
    void testCloseWithoutAutoFlush() {
        FileWriter file = new FileWriter();
        BufferedFileWriter w = new BufferedFileWriter(file, 1000);
        w.write("xyz");
        w.close();
        assertEquals("xyz", file.contents());
        assertEquals(1, file.flushCalls());
    }
}
