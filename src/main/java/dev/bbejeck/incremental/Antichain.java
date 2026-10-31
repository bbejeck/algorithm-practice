package dev.bbejeck.incremental;

import java.util.Set;

/**
 * Antichains and Frontiers over Partially Ordered Time
 *
 * Background: Materialize doc/developer/platform/formalism.md. Times form a lattice, and a frontier
 * is an antichain of times used as a lower or upper bound.
 *
 * A Time is an (epoch, iteration) pair, ordered by the product order:
 *   a <= b  iff  a.epoch <= b.epoch AND a.iteration <= b.iteration
 * Some pairs are incomparable, for example (1, 3) and (2, 1).
 *
 * Part 1, Time and the Antichain basics:
 *   Time.lessEqual / Time.lessThan implement the order above (lessThan means <= and not equal).
 *   An Antichain is a set of times where no two are comparable. It holds only the minimal times
 *   inserted into it.
 *   insert(t) returns false and changes nothing if some element is already <= t. Otherwise it removes
 *   every element >= t, adds t, and returns true.
 *   lessEqual(t) is true if some element f has f <= t ("t is at or beyond the frontier").
 *   lessThan(t)  is true if some element f has f <  t.
 *   The empty antichain is the latest possible frontier: no time is at or beyond it.
 *
 * Part 2, lattice operations:
 *   Time.join is the least upper bound (max of each coordinate), Time.meet is the greatest lower
 *   bound (min of each coordinate).
 *   Antichain.meet(a, b) is the frontier of times at or beyond a OR b (minimal elements of the union).
 *   This is how you combine the progress of two inputs into one operator.
 *   Antichain.join(a, b) is the frontier of times at or beyond a AND b (minimal elements of all
 *   pairwise Time.joins). Be ready to explain why join with the empty antichain is empty.
 *
 * Example:
 *   insert (2,2) -> true      {(2,2)}
 *   insert (3,3) -> false     {(2,2)}            (3,3) is beyond (2,2)
 *   insert (1,5) -> true      {(2,2), (1,5)}     incomparable with (2,2)
 *   lessEqual (2,5) -> true,  lessEqual (3,1) -> false
 *   insert (1,1) -> true      {(1,1)}            removes both
 *
 * Constraints: coordinates are non-negative longs. Up to 10^4 inserts.
 */
public class Antichain {

    public record Time(long epoch, long iteration) {

        public boolean lessEqual(Time other) {
            throw new UnsupportedOperationException("TODO");
        }

        public boolean lessThan(Time other) {
            throw new UnsupportedOperationException("TODO");
        }

        public Time join(Time other) {
            throw new UnsupportedOperationException("TODO");
        }

        public Time meet(Time other) {
            throw new UnsupportedOperationException("TODO");
        }
    }

    public boolean insert(Time t) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean lessEqual(Time t) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean lessThan(Time t) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("TODO");
    }

    public Set<Time> elements() {
        throw new UnsupportedOperationException("TODO");
    }

    public static Antichain meet(Antichain a, Antichain b) {
        throw new UnsupportedOperationException("TODO");
    }

    public static Antichain join(Antichain a, Antichain b) {
        throw new UnsupportedOperationException("TODO");
    }
}
