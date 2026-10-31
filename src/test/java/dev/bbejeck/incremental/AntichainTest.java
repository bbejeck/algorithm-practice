package dev.bbejeck.incremental;

import dev.bbejeck.incremental.Antichain.Time;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Antichain
 */
public class AntichainTest {

    private static Time t(long epoch, long iteration) {
        return new Time(epoch, iteration);
    }

    private static Antichain of(Time... times) {
        Antichain a = new Antichain();
        for (Time time : times) {
            a.insert(time);
        }
        return a;
    }

    // Part 1: Time order

    @Test
    @DisplayName("Product order: comparable and incomparable pairs")
    void testTimeLessEqual() {
        assertTrue(t(1, 2).lessEqual(t(1, 3)));
        assertTrue(t(1, 2).lessEqual(t(1, 2)));
        assertFalse(t(1, 3).lessEqual(t(2, 1)));
        assertFalse(t(2, 1).lessEqual(t(1, 3)));
        assertFalse(t(2, 2).lessEqual(t(1, 1)));
    }

    @Test
    @DisplayName("lessThan is strict")
    void testTimeLessThan() {
        assertTrue(t(1, 2).lessThan(t(1, 3)));
        assertFalse(t(1, 2).lessThan(t(1, 2)));
        assertFalse(t(1, 3).lessThan(t(2, 1)));
    }

    // Part 1: Antichain

    @Test
    @DisplayName("Insert keeps only minimal elements")
    void testInsert() {
        Antichain a = new Antichain();
        assertTrue(a.insert(t(2, 2)));
        assertFalse(a.insert(t(3, 3)));
        assertEquals(Set.of(t(2, 2)), a.elements());

        assertTrue(a.insert(t(1, 5)));
        assertEquals(Set.of(t(2, 2), t(1, 5)), a.elements());

        assertTrue(a.insert(t(1, 1)));
        assertEquals(Set.of(t(1, 1)), a.elements());
    }

    @Test
    @DisplayName("Inserting a duplicate returns false")
    void testInsertDuplicate() {
        Antichain a = of(t(4, 4));
        assertFalse(a.insert(t(4, 4)));
        assertEquals(Set.of(t(4, 4)), a.elements());
    }

    @Test
    @DisplayName("lessEqual against a frontier with incomparable elements")
    void testFrontierLessEqual() {
        Antichain a = of(t(1, 5), t(2, 2));
        assertTrue(a.lessEqual(t(2, 5)));
        assertTrue(a.lessEqual(t(2, 2)));
        assertTrue(a.lessEqual(t(1, 5)));
        assertFalse(a.lessEqual(t(1, 1)));
        assertFalse(a.lessEqual(t(3, 1)));
        assertFalse(a.lessEqual(t(1, 4)));
    }

    @Test
    @DisplayName("lessThan against a frontier excludes the elements themselves")
    void testFrontierLessThan() {
        Antichain a = of(t(1, 5), t(2, 2));
        assertFalse(a.lessThan(t(2, 2)));
        assertFalse(a.lessThan(t(1, 5)));
        assertTrue(a.lessThan(t(2, 6)));
        assertTrue(a.lessThan(t(3, 2)));
    }

    @Test
    @DisplayName("Empty antichain: nothing is at or beyond it")
    void testEmpty() {
        Antichain a = new Antichain();
        assertTrue(a.isEmpty());
        assertTrue(a.elements().isEmpty());
        assertFalse(a.lessEqual(t(0, 0)));
        assertFalse(a.lessEqual(t(Long.MAX_VALUE, Long.MAX_VALUE)));
        assertFalse(a.lessThan(t(5, 5)));
        a.insert(t(0, 0));
        assertFalse(a.isEmpty());
    }

    @Test
    @DisplayName("Random inserts: frontier agrees with brute force and stays an antichain")
    void testRandomAgainstBruteForce() {
        Random random = new Random(7);
        for (int round = 0; round < 50; round++) {
            Antichain a = new Antichain();
            List<Time> inserted = new ArrayList<>();
            for (int i = 0; i < 40; i++) {
                Time time = t(random.nextInt(10), random.nextInt(10));
                boolean expected = inserted.stream().noneMatch(s -> s.lessEqual(time));
                assertEquals(expected, a.insert(time), "insert " + time);
                inserted.add(time);
            }
            List<Time> elements = new ArrayList<>(a.elements());
            for (Time x : elements) {
                for (Time y : elements) {
                    if (!x.equals(y)) {
                        assertFalse(x.lessEqual(y), x + " and " + y + " are comparable");
                    }
                }
            }
            for (int e = 0; e < 10; e++) {
                for (int i = 0; i < 10; i++) {
                    Time probe = t(e, i);
                    boolean expectedLe = inserted.stream().anyMatch(s -> s.lessEqual(probe));
                    boolean expectedLt = inserted.stream().anyMatch(s -> s.lessThan(probe));
                    assertEquals(expectedLe, a.lessEqual(probe), "lessEqual " + probe);
                    assertEquals(expectedLt, a.lessThan(probe), "lessThan " + probe);
                }
            }
        }
    }

    // Part 2: lattice operations

    @Test
    @DisplayName("Time join and meet")
    void testTimeJoinMeet() {
        assertEquals(t(2, 3), t(1, 3).join(t(2, 1)));
        assertEquals(t(1, 1), t(1, 3).meet(t(2, 1)));
        assertEquals(t(4, 4), t(4, 4).join(t(4, 4)));
        assertEquals(t(1, 2), t(1, 2).meet(t(3, 4)));
    }

    @Test
    @DisplayName("Frontier meet is the minimal elements of the union")
    void testFrontierMeet() {
        assertEquals(Set.of(t(1, 5), t(2, 2)), Antichain.meet(of(t(1, 5)), of(t(2, 2))).elements());
        assertEquals(Set.of(t(1, 1)), Antichain.meet(of(t(1, 1)), of(t(2, 2))).elements());
        assertEquals(Set.of(t(3, 3)), Antichain.meet(new Antichain(), of(t(3, 3))).elements());
        assertTrue(Antichain.meet(new Antichain(), new Antichain()).isEmpty());
    }

    @Test
    @DisplayName("Frontier join is the minimal elements of pairwise joins")
    void testFrontierJoin() {
        assertEquals(Set.of(t(3, 2)), Antichain.join(of(t(1, 5), t(2, 2)), of(t(3, 1))).elements());
        assertEquals(Set.of(t(2, 2)), Antichain.join(of(t(1, 1)), of(t(2, 2))).elements());
        assertEquals(Set.of(t(1, 5), t(5, 1)), Antichain.join(of(t(1, 5), t(5, 1)), of(t(0, 0))).elements());
        assertTrue(Antichain.join(new Antichain(), of(t(3, 3))).isEmpty());
    }

    @Test
    @DisplayName("Random meet/join agree with their definitions")
    void testRandomMeetJoin() {
        Random random = new Random(11);
        for (int round = 0; round < 50; round++) {
            List<Time> xs = new ArrayList<>();
            List<Time> ys = new ArrayList<>();
            Antichain a = new Antichain();
            Antichain b = new Antichain();
            for (int i = 0; i < 6; i++) {
                Time x = t(random.nextInt(8), random.nextInt(8));
                Time y = t(random.nextInt(8), random.nextInt(8));
                xs.add(x);
                ys.add(y);
                a.insert(x);
                b.insert(y);
            }
            Antichain meet = Antichain.meet(a, b);
            Antichain join = Antichain.join(a, b);
            for (int e = 0; e < 8; e++) {
                for (int i = 0; i < 8; i++) {
                    Time probe = t(e, i);
                    boolean beyondA = xs.stream().anyMatch(s -> s.lessEqual(probe));
                    boolean beyondB = ys.stream().anyMatch(s -> s.lessEqual(probe));
                    assertEquals(beyondA || beyondB, meet.lessEqual(probe), "meet " + probe);
                    assertEquals(beyondA && beyondB, join.lessEqual(probe), "join " + probe);
                }
            }
        }
    }
}
