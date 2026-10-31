package dev.bbejeck.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Bounded Blocking Queue
 *
 * Build a thread-safe FIFO queue with a fixed capacity. Don't use anything from
 * java.util.concurrent except locks (ReentrantLock, Condition) and TimeUnit. synchronized with
 * wait/notifyAll is also fine. Back it with your own array or linked structure.
 *
 * Part 1, blocking operations:
 *   new BoundedBlockingQueue(capacity) throws IllegalArgumentException if capacity < 1.
 *   put(e) adds to the tail, waiting while the queue is full.
 *   take() removes from the head, waiting while the queue is empty.
 *   size() returns the current number of elements.
 *   Null elements throw NullPointerException.
 *   A thread interrupted while waiting gets InterruptedException, and the queue is unchanged.
 *
 * Part 2, timed operations:
 *   offer(e, timeout, unit) waits at most timeout for space. Returns true if added, false if it
 *   timed out.
 *   poll(timeout, unit) waits at most timeout for an element. Returns it, or null if it timed out.
 *   Spurious wakeups must not cut the wait short: track the time left, don't wait the full timeout
 *   again after every wakeup.
 *   Be ready to explain signal vs signalAll here, and why you check the condition in a while loop.
 *
 * Example (capacity 2):
 *   put(a), put(b)       size 2
 *   put(c)               blocks until another thread takes
 *   take() -> a          the blocked put(c) completes
 *   offer(d, 50ms)       returns false after ~50ms (still full)
 *
 * Constraints: many producer and consumer threads at once.
 */
public class BoundedBlockingQueue<E> {

    public BoundedBlockingQueue(int capacity) {
        throw new UnsupportedOperationException("TODO");
    }

    public void put(E e) throws InterruptedException {
        throw new UnsupportedOperationException("TODO");
    }

    public E take() throws InterruptedException {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("TODO");
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("TODO");
    }

    public int size() {
        throw new UnsupportedOperationException("TODO");
    }
}
