package dev.bbejeck.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnionFindTest {

    private UnionFind<Integer> unionFind;

    @BeforeEach
    void setUp() {
        unionFind = new UnionFind<>();
    }

    @Test
    @DisplayName("Should set a single item")
    void testSet() {
        unionFind.set(1);
        assertTrue(unionFind.connected(1, 1));
    }

    @Test
    @DisplayName("Should handle setting same item multiple times")
    void testSetDuplicate() {
        unionFind.set(1);
        unionFind.set(1);
        assertTrue(unionFind.connected(1, 1));
    }

    @Test
    @DisplayName("Should throw exception for finding non-existent item")
    void testFindNonExistent() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> unionFind.find(1));
        assertEquals("Item 1 not present", exception.getMessage());
    }

    @Test
    @DisplayName("Should find existing item")
    void testFind() {
        unionFind.set(1);
        assertEquals(1, unionFind.find(1));
    }

    @Test
    @DisplayName("Should perform path compression")
    void testPathCompression() {
        // Create a chain of elements
        unionFind.set(1);
        unionFind.set(2);
        unionFind.set(3);

        // Connect them
        unionFind.union(1, 2);
        unionFind.union(2, 3);

        // Find should compress the path
        Integer root = unionFind.find(1);
        assertTrue(unionFind.connected(1, root));
        assertTrue(unionFind.connected(2, root));
        assertTrue(unionFind.connected(3, root));
    }

    @Test
    @DisplayName("Should union two distinct items")
    void testUnion() {
        unionFind.set(1);
        unionFind.set(2);

        assertFalse(unionFind.connected(1, 2));
        unionFind.union(1, 2);
        assertTrue(unionFind.connected(1, 2));
    }

    @Test
    @DisplayName("Should handle union of already connected items")
    void testUnionOfConnectedItems() {
        unionFind.set(1);
        unionFind.set(2);

        unionFind.union(1, 2);
        unionFind.union(1, 2); // Second union should have no effect
        assertTrue(unionFind.connected(1, 2));
    }

    @Test
    @DisplayName("Should maintain connectivity through multiple unions")
    void testTransitiveUnion() {
        unionFind.set(1);
        unionFind.set(2);
        unionFind.set(3);

        unionFind.union(1, 2);
        unionFind.union(2, 3);

        assertTrue(unionFind.connected(1, 3));
    }

    @Test
    @DisplayName("Should detect connected items")
    void testConnected() {
        unionFind.set(1);
        unionFind.set(2);
        unionFind.set(3);

        assertFalse(unionFind.connected(1, 2));

        unionFind.union(1, 2);
        assertTrue(unionFind.connected(1, 2));
        assertFalse(unionFind.connected(1, 3));
    }

    @Test
    @DisplayName("Should throw exception for non-existent items in connected check")
    void testConnectedWithNonExistentItems() {
        assertThrows(IllegalStateException.class,
                () -> unionFind.connected(1, 2));
    }

    @Test
    @DisplayName("Should handle complex operations sequence")
    void testComplexOperations() {
        // Set up multiple elements
        for (int i = 1; i <= 5; i++) {
            unionFind.set(i);
        }

        // Create two separate groups: (1,2,3) and (4,5)
        unionFind.union(1, 2);
        unionFind.union(2, 3);
        unionFind.union(4, 5);

        // Verify groups are separate
        assertTrue(unionFind.connected(1, 3));
        assertTrue(unionFind.connected(4, 5));
        assertFalse(unionFind.connected(3, 4));

        // Join the groups
        unionFind.union(3, 4);

        // Verify all elements are now connected
        assertTrue(unionFind.connected(1, 5));
    }

    @Test
    @DisplayName("Should handle different sized groups in union")
    void testUnionWithDifferentSizes() {
        // Create groups of different sizes
        unionFind.set(1);
        unionFind.set(2);
        unionFind.set(3);
        unionFind.set(4);

        // Make group (1,2) and single elements 3,4
        unionFind.union(1, 2);

        // Union larger group with single element
        unionFind.union(2, 3);
        assertTrue(unionFind.connected(1, 3));

        // Union with another single element
        unionFind.union(3, 4);
        assertTrue(unionFind.connected(1, 4));
    }
}