package dev.bbejeck.concurrency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BoundedBlockingQueue
 */
public class BoundedBlockingQueueTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    // Part 1

    @Test
    @DisplayName("FIFO order and size")
    void testFifo() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<String> q = new BoundedBlockingQueue<>(3);
            assertEquals(0, q.size());
            q.put("a");
            q.put("b");
            q.put("c");
            assertEquals(3, q.size());
            assertEquals("a", q.take());
            q.put("d");
            assertEquals("b", q.take());
            assertEquals("c", q.take());
            assertEquals("d", q.take());
            assertEquals(0, q.size());
        });
    }

    @Test
    @DisplayName("Invalid capacity and null elements")
    void testInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new BoundedBlockingQueue<String>(0));
        assertThrows(IllegalArgumentException.class, () -> new BoundedBlockingQueue<String>(-1));
        BoundedBlockingQueue<String> q = new BoundedBlockingQueue<>(1);
        assertThrows(NullPointerException.class, () -> q.put(null));
        assertThrows(NullPointerException.class, () -> q.offer(null, 1, TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("put blocks while full until a take")
    void testPutBlocks() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<Integer> q = new BoundedBlockingQueue<>(1);
            q.put(1);
            CountDownLatch done = new CountDownLatch(1);
            Thread producer = new Thread(() -> {
                try {
                    q.put(2);
                    done.countDown();
                } catch (InterruptedException ignored) {
                }
            });
            producer.start();
            assertFalse(done.await(200, TimeUnit.MILLISECONDS), "put should block while full");
            assertEquals(1, q.take());
            assertTrue(done.await(2, TimeUnit.SECONDS), "put should complete after take");
            assertEquals(2, q.take());
            producer.join();
        });
    }

    @Test
    @DisplayName("take blocks while empty until a put")
    void testTakeBlocks() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<Integer> q = new BoundedBlockingQueue<>(2);
            AtomicReference<Integer> got = new AtomicReference<>();
            CountDownLatch done = new CountDownLatch(1);
            Thread consumer = new Thread(() -> {
                try {
                    got.set(q.take());
                    done.countDown();
                } catch (InterruptedException ignored) {
                }
            });
            consumer.start();
            assertFalse(done.await(200, TimeUnit.MILLISECONDS), "take should block while empty");
            q.put(42);
            assertTrue(done.await(2, TimeUnit.SECONDS));
            assertEquals(42, got.get());
            consumer.join();
        });
    }

    @Test
    @DisplayName("Interrupting a blocked put throws and leaves the queue unchanged")
    void testInterruptPut() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<Integer> q = new BoundedBlockingQueue<>(1);
            q.put(1);
            AtomicReference<Throwable> thrown = new AtomicReference<>();
            Thread producer = new Thread(() -> {
                try {
                    q.put(2);
                } catch (Throwable t) {
                    thrown.set(t);
                }
            });
            producer.start();
            Thread.sleep(100);
            producer.interrupt();
            producer.join();
            assertInstanceOf(InterruptedException.class, thrown.get());
            assertEquals(1, q.size());
            assertEquals(1, q.take());
        });
    }

    @Test
    @DisplayName("Interrupting a blocked take throws")
    void testInterruptTake() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<Integer> q = new BoundedBlockingQueue<>(1);
            AtomicReference<Throwable> thrown = new AtomicReference<>();
            Thread consumer = new Thread(() -> {
                try {
                    q.take();
                } catch (Throwable t) {
                    thrown.set(t);
                }
            });
            consumer.start();
            Thread.sleep(100);
            consumer.interrupt();
            consumer.join();
            assertInstanceOf(InterruptedException.class, thrown.get());
            assertEquals(0, q.size());
        });
    }

    @Test
    @DisplayName("Many producers and consumers: every item delivered once, per-producer order kept")
    void testManyProducersConsumers() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            int producers = 4;
            int consumers = 4;
            int perProducer = 20_000;
            BoundedBlockingQueue<int[]> q = new BoundedBlockingQueue<>(8);
            AtomicInteger maxSize = new AtomicInteger();
            List<Thread> threads = new ArrayList<>();
            List<List<int[]>> received = new ArrayList<>();

            for (int p = 0; p < producers; p++) {
                int id = p;
                threads.add(new Thread(() -> {
                    try {
                        for (int i = 0; i < perProducer; i++) {
                            q.put(new int[]{id, i});
                            maxSize.accumulateAndGet(q.size(), Math::max);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
            int total = producers * perProducer;
            AtomicInteger remaining = new AtomicInteger(total);
            for (int c = 0; c < consumers; c++) {
                List<int[]> mine = new ArrayList<>();
                received.add(mine);
                threads.add(new Thread(() -> {
                    try {
                        while (remaining.getAndDecrement() > 0) {
                            mine.add(q.take());
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
            threads.forEach(Thread::start);
            for (Thread t : threads) {
                t.join();
            }

            assertTrue(maxSize.get() <= 8, "size exceeded capacity: " + maxSize.get());
            boolean[][] seen = new boolean[producers][perProducer];
            int count = 0;
            for (List<int[]> mine : received) {
                int[] last = new int[producers];
                Arrays.fill(last, -1);
                for (int[] item : mine) {
                    assertFalse(seen[item[0]][item[1]], "duplicate " + item[0] + ":" + item[1]);
                    seen[item[0]][item[1]] = true;
                    assertTrue(item[1] > last[item[0]], "producer order broken");
                    last[item[0]] = item[1];
                    count++;
                }
            }
            assertEquals(total, count);
            assertEquals(0, q.size());
        });
    }

    // Part 2

    @Test
    @DisplayName("offer times out when full and succeeds when there is space")
    void testOffer() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<String> q = new BoundedBlockingQueue<>(1);
            assertTrue(q.offer("a", 10, TimeUnit.MILLISECONDS));
            long start = System.nanoTime();
            assertFalse(q.offer("b", 150, TimeUnit.MILLISECONDS));
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            assertTrue(elapsedMs >= 140, "returned too early: " + elapsedMs + "ms");
            assertTrue(elapsedMs < 2000, "waited too long: " + elapsedMs + "ms");
            assertEquals(1, q.size());
        });
    }

    @Test
    @DisplayName("offer succeeds if space frees up before the timeout")
    void testOfferWakesUp() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<String> q = new BoundedBlockingQueue<>(1);
            q.put("a");
            Thread consumer = new Thread(() -> {
                try {
                    Thread.sleep(100);
                    q.take();
                } catch (InterruptedException ignored) {
                }
            });
            consumer.start();
            assertTrue(q.offer("b", 5, TimeUnit.SECONDS));
            consumer.join();
            assertEquals("b", q.take());
        });
    }

    @Test
    @DisplayName("poll times out when empty and returns an element when one arrives")
    void testPoll() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<String> q = new BoundedBlockingQueue<>(2);
            long start = System.nanoTime();
            assertNull(q.poll(150, TimeUnit.MILLISECONDS));
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            assertTrue(elapsedMs >= 140, "returned too early: " + elapsedMs + "ms");

            Thread producer = new Thread(() -> {
                try {
                    Thread.sleep(100);
                    q.put("x");
                } catch (InterruptedException ignored) {
                }
            });
            producer.start();
            assertEquals("x", q.poll(5, TimeUnit.SECONDS));
            producer.join();
        });
    }

    @Test
    @DisplayName("Timed operations with zero timeout don't wait")
    void testZeroTimeout() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            BoundedBlockingQueue<String> q = new BoundedBlockingQueue<>(1);
            assertNull(q.poll(0, TimeUnit.MILLISECONDS));
            assertTrue(q.offer("a", 0, TimeUnit.MILLISECONDS));
            assertFalse(q.offer("b", 0, TimeUnit.MILLISECONDS));
            assertEquals("a", q.poll(0, TimeUnit.MILLISECONDS));
        });
    }
}
