package dev.bbejeck.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * In-Memory Pub-Sub Broker
 *
 * Build a thread-safe broker where publishers send messages to named topics and every current
 * subscriber of that topic gets its own copy. Any java.util.concurrent class is allowed. The point
 * is the design: who holds which lock, what a slow subscriber costs everyone else, and shutdown.
 *
 * Part 1, publish / subscribe:
 *   new PubSubBroker(bufferSize): each subscription buffers at most bufferSize undelivered messages.
 *     Throw IllegalArgumentException if bufferSize < 1.
 *   subscribe(topic) returns a Subscription that receives every message published to that topic after
 *     subscribe returns. Messages published earlier are not replayed.
 *   publish(topic, message) delivers to every current subscriber of the topic and returns how many
 *     subscribers it was delivered to (0 if none). Publishing never blocks on a slow subscriber.
 *   Subscription.poll(timeout, unit) returns the next message, waiting up to timeout. Returns null if
 *     it timed out.
 *   Subscription.close() unsubscribes. Later publishes don't count it, and poll returns null right away.
 *   Messages from one publishing thread to one topic arrive at each subscriber in publish order.
 *
 * Part 2, backpressure and shutdown:
 *   When a subscription's buffer is full, publish drops that subscription's OLDEST buffered message
 *   to make room, and increments its dropped() count. Other subscribers are not affected.
 *   close() on the broker: later publish and subscribe calls throw IllegalStateException. Existing
 *   subscriptions can still poll whatever is already buffered, then poll returns null without waiting.
 *   A poll that is waiting when the broker closes must return null promptly.
 *   Subscribing and unsubscribing while other threads publish must be safe.
 *   Be ready to explain why publishing shouldn't hold one global lock while delivering, and what
 *   other overflow policies you could choose (block the publisher, drop the newest, disconnect).
 *
 * Example (bufferSize 2):
 *   s1 = subscribe("t"), s2 = subscribe("t")
 *   publish("t", "m1") -> 2;  s1.poll -> "m1", s2.poll -> "m1"
 *   publish("t", "a"), publish("t", "b"), publish("t", "c")  (nobody polling)
 *   s1.poll -> "b", s1.poll -> "c", s1.dropped() == 1
 *
 * Constraints: many publisher and subscriber threads, many topics.
 */
public class PubSubBroker implements AutoCloseable {

    public interface Subscription extends AutoCloseable {

        String poll(long timeout, TimeUnit unit) throws InterruptedException;

        long dropped();

        @Override
        void close();
    }

    public PubSubBroker(int bufferSize) {
        throw new UnsupportedOperationException("TODO");
    }

    public Subscription subscribe(String topic) {
        throw new UnsupportedOperationException("TODO");
    }

    public int publish(String topic, String message) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("TODO");
    }
}
