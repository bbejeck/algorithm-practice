package dev.bbejeck.concurrency;

import dev.bbejeck.concurrency.PubSubBroker.Subscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PubSubBroker
 */
public class PubSubBrokerTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private static String poll(Subscription s) throws InterruptedException {
        return s.poll(1, TimeUnit.SECONDS);
    }

    // Part 1

    @Test
    @DisplayName("Every subscriber gets its own copy")
    void testFanOut() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(10);
            Subscription s1 = broker.subscribe("t");
            Subscription s2 = broker.subscribe("t");
            assertEquals(2, broker.publish("t", "m1"));
            assertEquals("m1", poll(s1));
            assertEquals("m1", poll(s2));
        });
    }

    @Test
    @DisplayName("Topics are isolated")
    void testTopics() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(10);
            Subscription a = broker.subscribe("a");
            Subscription b = broker.subscribe("b");
            assertEquals(1, broker.publish("a", "for-a"));
            assertEquals(0, broker.publish("c", "nobody"));
            assertEquals("for-a", poll(a));
            assertNull(b.poll(100, TimeUnit.MILLISECONDS));
        });
    }

    @Test
    @DisplayName("No replay of messages published before subscribing")
    void testNoReplay() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(10);
            broker.publish("t", "early");
            Subscription s = broker.subscribe("t");
            broker.publish("t", "late");
            assertEquals("late", poll(s));
            assertNull(s.poll(50, TimeUnit.MILLISECONDS));
        });
    }

    @Test
    @DisplayName("Messages arrive in publish order")
    void testOrder() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(100);
            Subscription s = broker.subscribe("t");
            for (int i = 0; i < 50; i++) {
                broker.publish("t", "m" + i);
            }
            for (int i = 0; i < 50; i++) {
                assertEquals("m" + i, poll(s));
            }
        });
    }

    @Test
    @DisplayName("poll waits for a message from another thread, or times out")
    void testPollWaits() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(10);
            Subscription s = broker.subscribe("t");
            long start = System.nanoTime();
            assertNull(s.poll(150, TimeUnit.MILLISECONDS));
            assertTrue(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) >= 140);

            Thread publisher = new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                broker.publish("t", "hello");
            });
            publisher.start();
            assertEquals("hello", s.poll(5, TimeUnit.SECONDS));
            publisher.join();
        });
    }

    @Test
    @DisplayName("Unsubscribed subscriptions stop receiving")
    void testUnsubscribe() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(10);
            Subscription s1 = broker.subscribe("t");
            Subscription s2 = broker.subscribe("t");
            s1.close();
            assertEquals(1, broker.publish("t", "m"));
            long start = System.nanoTime();
            assertNull(s1.poll(5, TimeUnit.SECONDS));
            assertTrue(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < 1000, "closed poll should not wait");
            assertEquals("m", poll(s2));
        });
    }

    @Test
    @DisplayName("Invalid buffer size")
    void testInvalidBuffer() {
        assertThrows(IllegalArgumentException.class, () -> new PubSubBroker(0));
    }

    @Test
    @DisplayName("Concurrent publishers: nothing lost, per-publisher order kept")
    void testConcurrentPublishers() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            int publishers = 4;
            int perPublisher = 5_000;
            PubSubBroker broker = new PubSubBroker(publishers * perPublisher);
            Subscription s = broker.subscribe("t");
            List<Thread> threads = new ArrayList<>();
            for (int p = 0; p < publishers; p++) {
                int id = p;
                threads.add(new Thread(() -> {
                    for (int i = 0; i < perPublisher; i++) {
                        broker.publish("t", id + ":" + i);
                    }
                }));
            }
            threads.forEach(Thread::start);
            int[] last = new int[publishers];
            Arrays.fill(last, -1);
            for (int n = 0; n < publishers * perPublisher; n++) {
                String m = s.poll(5, TimeUnit.SECONDS);
                assertNotNull(m, "missing messages after " + n);
                String[] parts = m.split(":");
                int id = Integer.parseInt(parts[0]);
                int seq = Integer.parseInt(parts[1]);
                assertEquals(last[id] + 1, seq, "publisher " + id + " out of order");
                last[id] = seq;
            }
            for (Thread t : threads) {
                t.join();
            }
            assertEquals(0, s.dropped());
        });
    }

    // Part 2

    @Test
    @DisplayName("Full buffer drops the oldest message without blocking the publisher")
    void testDropOldest() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            PubSubBroker broker = new PubSubBroker(2);
            Subscription slow = broker.subscribe("t");
            Subscription fast = broker.subscribe("t");
            List<String> fastGot = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                assertEquals(2, broker.publish("t", "m" + i));
                fastGot.add(poll(fast));
            }
            assertEquals(List.of("m0", "m1", "m2", "m3", "m4"), fastGot);
            assertEquals(0, fast.dropped());
            assertEquals("m3", poll(slow));
            assertEquals("m4", poll(slow));
            assertEquals(3, slow.dropped());
        });
    }

    @Test
    @DisplayName("A subscriber that never polls doesn't slow publishing")
    void testSlowSubscriberDoesNotBlock() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            PubSubBroker broker = new PubSubBroker(1);
            Subscription stuck = broker.subscribe("t");
            for (int i = 0; i < 100_000; i++) {
                broker.publish("t", "m" + i);
            }
            assertEquals(99_999, stuck.dropped());
            assertEquals("m99999", poll(stuck));
        });
    }

    @Test
    @DisplayName("Closing the broker: no more publish/subscribe, buffered messages still drain")
    void testClose() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(10);
            Subscription s = broker.subscribe("t");
            broker.publish("t", "a");
            broker.publish("t", "b");
            broker.close();
            assertThrows(IllegalStateException.class, () -> broker.publish("t", "c"));
            assertThrows(IllegalStateException.class, () -> broker.subscribe("t"));
            assertEquals("a", poll(s));
            assertEquals("b", poll(s));
            long start = System.nanoTime();
            assertNull(s.poll(5, TimeUnit.SECONDS));
            assertTrue(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < 1000, "poll after close should not wait");
        });
    }

    @Test
    @DisplayName("A poll waiting when the broker closes returns null promptly")
    void testCloseWakesPoller() {
        assertTimeoutPreemptively(TIMEOUT, () -> {
            PubSubBroker broker = new PubSubBroker(10);
            Subscription s = broker.subscribe("t");
            AtomicReference<String> got = new AtomicReference<>("not set");
            CountDownLatch done = new CountDownLatch(1);
            Thread poller = new Thread(() -> {
                try {
                    got.set(s.poll(30, TimeUnit.SECONDS));
                } catch (InterruptedException ignored) {
                }
                done.countDown();
            });
            poller.start();
            Thread.sleep(100);
            broker.close();
            assertTrue(done.await(2, TimeUnit.SECONDS), "poller still waiting after close");
            assertNull(got.get());
        });
    }

    @Test
    @DisplayName("Subscribe and unsubscribe while publishing")
    void testChurn() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            PubSubBroker broker = new PubSubBroker(16);
            AtomicBoolean running = new AtomicBoolean(true);
            AtomicReference<Throwable> failure = new AtomicReference<>();
            List<Thread> threads = new ArrayList<>();
            for (int p = 0; p < 3; p++) {
                threads.add(new Thread(() -> {
                    try {
                        while (running.get()) {
                            broker.publish("t", "x");
                        }
                    } catch (Throwable t) {
                        failure.compareAndSet(null, t);
                    }
                }));
            }
            for (int c = 0; c < 3; c++) {
                threads.add(new Thread(() -> {
                    try {
                        for (int i = 0; i < 2_000; i++) {
                            Subscription s = broker.subscribe("t");
                            s.poll(1, TimeUnit.MILLISECONDS);
                            s.close();
                        }
                    } catch (Throwable t) {
                        failure.compareAndSet(null, t);
                    }
                }));
            }
            threads.forEach(Thread::start);
            for (int i = 3; i < threads.size(); i++) {
                threads.get(i).join();
            }
            running.set(false);
            for (Thread t : threads) {
                t.join();
            }
            assertNull(failure.get(), () -> "thread failed: " + failure.get());
            assertEquals(0, broker.publish("t", "after"));
        });
    }
}
