package dev.bbejeck.incremental;

import dev.bbejeck.incremental.TimestampSelector.Frontier;
import dev.bbejeck.incremental.TimestampSelector.Reclocker;
import dev.bbejeck.incremental.TimestampSelector.Selection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static dev.bbejeck.incremental.TimestampSelector.Isolation.SERIALIZABLE;
import static dev.bbejeck.incremental.TimestampSelector.Isolation.STRICT_SERIALIZABLE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TimestampSelector
 */
public class TimestampSelectorTest {

    private static Frontier f(long since, long upper) {
        return new Frontier(since, upper);
    }

    // Part 1: select

    @Test
    @DisplayName("Serializable picks the latest timestamp ready for all inputs")
    void testSerializableReady() {
        TimestampSelector selector = new TimestampSelector();
        assertEquals(new Selection(9, true), selector.select(List.of(f(2, 10), f(5, 12)), SERIALIZABLE, 100));
        assertEquals(new Selection(4, true), selector.select(List.of(f(0, 5)), SERIALIZABLE, 0));
    }

    @Test
    @DisplayName("Serializable falls back to the smallest valid timestamp when nothing is ready")
    void testSerializableNotReady() {
        TimestampSelector selector = new TimestampSelector();
        // input 1 is compacted up to 10, input 2 is only complete up to 6
        assertEquals(new Selection(10, false), selector.select(List.of(f(10, 15), f(3, 7)), SERIALIZABLE, 0));
        // since == upper: nothing readable without waiting
        assertEquals(new Selection(4, false), selector.select(List.of(f(4, 4)), SERIALIZABLE, 0));
    }

    @Test
    @DisplayName("Strict serializable is at least now and may wait")
    void testStrictUsesNow() {
        TimestampSelector selector = new TimestampSelector();
        assertEquals(new Selection(20, true), selector.select(List.of(f(0, 50)), STRICT_SERIALIZABLE, 20));
        assertEquals(new Selection(60, false), selector.select(List.of(f(0, 50)), STRICT_SERIALIZABLE, 60));
    }

    @Test
    @DisplayName("Strict serializable is at least every since")
    void testStrictRespectsSince() {
        TimestampSelector selector = new TimestampSelector();
        assertEquals(new Selection(30, true), selector.select(List.of(f(30, 50), f(0, 40)), STRICT_SERIALIZABLE, 5));
    }

    @Test
    @DisplayName("Strict timestamps never go backwards, even if the clock does")
    void testStrictMonotonic() {
        TimestampSelector selector = new TimestampSelector();
        assertEquals(40, selector.select(List.of(f(0, 100)), STRICT_SERIALIZABLE, 40).timestamp());
        assertEquals(40, selector.select(List.of(f(0, 100)), STRICT_SERIALIZABLE, 35).timestamp());
        assertEquals(41, selector.select(List.of(f(0, 100)), STRICT_SERIALIZABLE, 41).timestamp());
    }

    @Test
    @DisplayName("Strict reads see recorded writes")
    void testStrictAfterWrite() {
        TimestampSelector selector = new TimestampSelector();
        selector.recordWrite(70);
        assertEquals(new Selection(70, false), selector.select(List.of(f(0, 65)), STRICT_SERIALIZABLE, 10));
        selector.recordWrite(50);
        assertEquals(70, selector.select(List.of(f(0, 100)), STRICT_SERIALIZABLE, 10).timestamp());
    }

    @Test
    @DisplayName("Serializable reads do not move the strict oracle")
    void testSerializableDoesNotAffectStrict() {
        TimestampSelector selector = new TimestampSelector();
        assertEquals(99, selector.select(List.of(f(0, 100)), SERIALIZABLE, 0).timestamp());
        assertEquals(10, selector.select(List.of(f(0, 100)), STRICT_SERIALIZABLE, 10).timestamp());
    }

    @Test
    @DisplayName("Serializable ignores recorded writes and the clock")
    void testSerializableIgnoresOracle() {
        TimestampSelector selector = new TimestampSelector();
        selector.recordWrite(500);
        selector.select(List.of(f(0, 1000)), STRICT_SERIALIZABLE, 600);
        assertEquals(new Selection(19, true), selector.select(List.of(f(0, 20)), SERIALIZABLE, 900));
    }

    @Test
    @DisplayName("Invalid inputs")
    void testInvalidInputs() {
        TimestampSelector selector = new TimestampSelector();
        assertThrows(IllegalArgumentException.class, () -> selector.select(List.of(), SERIALIZABLE, 0));
        assertThrows(IllegalArgumentException.class, () -> selector.select(List.of(f(5, 3)), STRICT_SERIALIZABLE, 0));
    }

    @Test
    @DisplayName("Random: selections are always valid, strict ones monotonic")
    void testRandomInvariants() {
        Random random = new Random(9);
        TimestampSelector selector = new TimestampSelector();
        long lastStrict = Long.MIN_VALUE;
        long lastWrite = Long.MIN_VALUE;
        for (int i = 0; i < 2000; i++) {
            if (random.nextInt(5) == 0) {
                long w = random.nextInt(1000);
                selector.recordWrite(w);
                lastWrite = Math.max(lastWrite, w);
                continue;
            }
            int n = 1 + random.nextInt(3);
            Frontier[] inputs = new Frontier[n];
            long maxSince = Long.MIN_VALUE;
            long minUpper = Long.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                long since = random.nextInt(1000);
                long upper = since + random.nextInt(200);
                inputs[j] = f(since, upper);
                maxSince = Math.max(maxSince, since);
                minUpper = Math.min(minUpper, upper);
            }
            long now = random.nextInt(1000);
            boolean strict = random.nextBoolean();
            Selection s = selector.select(List.of(inputs), strict ? STRICT_SERIALIZABLE : SERIALIZABLE, now);
            assertTrue(s.timestamp() >= maxSince, "timestamp below a since");
            assertEquals(s.timestamp() < minUpper, s.ready(), "ready flag");
            if (strict) {
                long expected = Math.max(maxSince, Math.max(now, Math.max(lastStrict, lastWrite)));
                assertEquals(expected, s.timestamp());
                lastStrict = s.timestamp();
            } else {
                assertEquals(Math.max(maxSince, minUpper - 1), s.timestamp());
            }
        }
    }

    // Part 2: Reclocker

    @Test
    @DisplayName("Offsets map to the first binding that covers them")
    void testReclock() {
        Reclocker r = new Reclocker();
        r.mint(10, 3);
        r.mint(20, 7);
        assertEquals(10, r.reclock(0));
        assertEquals(10, r.reclock(2));
        assertEquals(20, r.reclock(3));
        assertEquals(20, r.reclock(6));
        assertThrows(IllegalStateException.class, () -> r.reclock(7));
        assertThrows(IllegalArgumentException.class, () -> r.reclock(-1));
    }

    @Test
    @DisplayName("Nothing is reclocked before the first binding")
    void testEmpty() {
        Reclocker r = new Reclocker();
        assertThrows(IllegalStateException.class, () -> r.reclock(0));
        r.mint(5, 0);
        assertThrows(IllegalStateException.class, () -> r.reclock(0));
    }

    @Test
    @DisplayName("Bindings with no new data advance time only")
    void testEmptyBindings() {
        Reclocker r = new Reclocker();
        r.mint(10, 3);
        r.mint(15, 3);
        r.mint(20, 5);
        assertEquals(10, r.reclock(2));
        assertEquals(20, r.reclock(3));
        assertEquals(20, r.reclock(4));
    }

    @Test
    @DisplayName("Bindings must move forward")
    void testMintMonotonic() {
        Reclocker r = new Reclocker();
        r.mint(10, 5);
        assertThrows(IllegalArgumentException.class, () -> r.mint(10, 8));
        assertThrows(IllegalArgumentException.class, () -> r.mint(9, 8));
        assertThrows(IllegalArgumentException.class, () -> r.mint(11, 4));
        r.mint(11, 8);
        assertEquals(11, r.reclock(7));
    }

    @Test
    @DisplayName("A transaction always gets one timestamp")
    void testTransactions() {
        Reclocker r = new Reclocker();
        r.mint(20, 7);
        r.registerTransaction(8, 11);
        assertThrows(IllegalArgumentException.class, () -> r.mint(30, 9));
        assertThrows(IllegalArgumentException.class, () -> r.mint(30, 11));
        r.mint(30, 8);
        r.mint(40, 12);
        for (long offset = 8; offset <= 11; offset++) {
            assertEquals(40, r.reclock(offset));
        }
        assertEquals(30, r.reclock(7));
    }

    @Test
    @DisplayName("Invalid transaction registrations")
    void testInvalidTransactions() {
        Reclocker r = new Reclocker();
        r.mint(10, 5);
        assertThrows(IllegalArgumentException.class, () -> r.registerTransaction(7, 6));
        assertThrows(IllegalArgumentException.class, () -> r.registerTransaction(4, 6));
        r.registerTransaction(5, 6);
    }

    @Test
    @DisplayName("Random: reclocking is monotonic and matches the bindings")
    void testRandomReclock() {
        Random random = new Random(13);
        Reclocker r = new Reclocker();
        long ts = 0;
        long upper = 0;
        long[] expected = new long[200_000];
        while (upper < expected.length - 10) {
            ts += 1 + random.nextInt(3);
            long next = upper + random.nextInt(6);
            r.mint(ts, next);
            for (long o = upper; o < next; o++) {
                expected[(int) o] = ts;
            }
            upper = next;
        }
        long finalUpper = upper;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            long previous = Long.MIN_VALUE;
            for (int o = 0; o < finalUpper; o++) {
                long got = r.reclock(o);
                assertEquals(expected[o], got);
                assertTrue(got >= previous);
                previous = got;
            }
        });
    }
}
