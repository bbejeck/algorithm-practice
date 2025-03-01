package dev.bbejeck.linkedlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlattenDoublyLinkedListTest {

    private FlattenDoublyLinkedList flattener;

    @BeforeEach
    void setUp() {
        flattener = new FlattenDoublyLinkedList();
    }

    @Test
    void testFlattenNullList() {
        assertNull(flattener.flatten(null));
    }

    @Test
    void testFlattenSingleLevelList() {
        // Create a simple list: 1 <-> 2 <-> 3
        Node head = createNode(1);
        Node second = createNode(2);
        Node third = createNode(3);

        connectNodes(head, second);
        connectNodes(second, third);

        Node result = flattener.flatten(head);

        assertNotNull(result);
        assertEquals(1, result.val);
        assertEquals(2, result.next.val);
        assertEquals(3, result.next.next.val);
        assertNull(result.next.next.next);
    }

    @Test
    void testFlattenTwoLevelList() {
        // Create list: 1 <-> 2 <-> 3
        //                   |
        //                   4 <-> 5
        Node head = createNode(1);
        Node second = createNode(2);
        Node third = createNode(3);
        Node child1 = createNode(4);
        Node child2 = createNode(5);

        connectNodes(head, second);
        connectNodes(second, third);
        connectNodes(child1, child2);
        second.child = child1;

        Node result = flattener.flatten(head);

        // Expected: 1 <-> 2 <-> 4 <-> 5 <-> 3
        assertNotNull(result);
        assertEquals(1, result.val);
        assertEquals(2, result.next.val);
        assertEquals(4, result.next.next.val);
        assertEquals(5, result.next.next.next.val);
        assertEquals(3, result.next.next.next.next.val);
        assertNull(result.next.next.next.next.next);
        assertNull(second.child);
    }

    @Test
    void testFlattenMultiLevelList() {
        // Create list: 1 <-> 2 <-> 3
        //                   |
        //                   4 <-> 5
        //                   |
        //                   6 <-> 7
        Node head = createNode(1);
        Node second = createNode(2);
        Node third = createNode(3);
        Node child1 = createNode(4);
        Node child2 = createNode(5);
        Node grandChild1 = createNode(6);
        Node grandChild2 = createNode(7);

        connectNodes(head, second);
        connectNodes(second, third);
        connectNodes(child1, child2);
        connectNodes(grandChild1, grandChild2);
        second.child = child1;
        child1.child = grandChild1;

        Node result = flattener.flatten(head);

        // Expected: 1 <-> 2 <-> 4 <-> 6 <-> 7 <-> 5 <-> 3
        assertNotNull(result);
        assertEquals(1, result.val);
        assertEquals(2, result.next.val);
        assertEquals(4, result.next.next.val);
        assertEquals(6, result.next.next.next.val);
        assertEquals(7, result.next.next.next.next.val);
        assertEquals(5, result.next.next.next.next.next.val);
        assertEquals(3, result.next.next.next.next.next.next.val);
        assertNull(result.next.next.next.next.next.next.next);
        assertNull(second.child);
        assertNull(child1.child);
    }

    @Test
    void testFlattenDeeplyNestedList() {
        // Create a deeply nested list structure:
        // 1 -> 2 -> 3 -> 4
        //      |
        //      5 -> 6
        //      |    |
        //      7    8 -> 9
        //      |         |
        //      10        11 -> 12

        Node head = createNode(1);
        Node n2 = createNode(2);
        Node n3 = createNode(3);
        Node n4 = createNode(4);
        Node n5 = createNode(5);
        Node n6 = createNode(6);
        Node n7 = createNode(7);
        Node n8 = createNode(8);
        Node n9 = createNode(9);
        Node n10 = createNode(10);
        Node n11 = createNode(11);
        Node n12 = createNode(12);

        // Connect main level
        connectNodes(head, n2);
        connectNodes(n2, n3);
        connectNodes(n3, n4);

        // Connect first child level
        connectNodes(n5, n6);
        n2.child = n5;

        // Connect second child levels
        connectNodes(n8, n9);
        n6.child = n8;
        n5.child = n7;

        // Connect third child levels
        n7.child = n10;
        n9.child = n11;
        connectNodes(n11, n12);

        Node result = flattener.flatten(head);

        // Expected: 1 -> 2 -> 5 -> 7 -> 10 -> 6 -> 8 -> 9 -> 11 -> 12 -> 3 -> 4
        assertNotNull(result);

        // Verify the flattened sequence
        int[] expectedValues = {1, 2, 5, 7, 10, 6, 8, 9, 11, 12, 3, 4};
        Node current = result;
        for (int expectedValue : expectedValues) {
            assertNotNull(current, "Node should not be null for value: " + expectedValue);
            assertEquals(expectedValue, current.val);
            if (current.next != null) {
                assertEquals(current, current.next.prev, "Prev pointer should point to previous node");
            }
            current = current.next;
        }
        assertNull(current, "Last node's next should be null");

        // Verify all child pointers are null
        current = result;
        while (current != null) {
            assertNull(current.child, "Child pointer should be null after flattening");
            current = current.next;
        }
    }

    @Test
    void testFlattenZigZagNestedList() {
        // Create a zig-zag nested structure:
        // 1 -> 2 -> 3
        //      |
        //      4 -> 5
        //           |
        //           6 -> 7
        //                |
        //                8 -> 9

        Node n1 = createNode(1);
        Node n2 = createNode(2);
        Node n3 = createNode(3);
        Node n4 = createNode(4);
        Node n5 = createNode(5);
        Node n6 = createNode(6);
        Node n7 = createNode(7);
        Node n8 = createNode(8);
        Node n9 = createNode(9);

        // Connect main level
        connectNodes(n1, n2);
        connectNodes(n2, n3);

        // Connect nested levels
        connectNodes(n4, n5);
        connectNodes(n6, n7);
        connectNodes(n8, n9);

        n2.child = n4;
        n5.child = n6;
        n7.child = n8;

        Node result = flattener.flatten(n1);

        // Expected: 1 -> 2 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9 -> 3
        int[] expectedValues = {1, 2, 4, 5, 6, 7, 8, 9, 3};
        Node current = result;

        // Verify the sequence and connections
        for (int expectedValue : expectedValues) {
            assertNotNull(current, "Node should not be null for value: " + expectedValue);
            assertEquals(expectedValue, current.val);

            // Verify doubly linked list properties
            if (current.next != null) {
                assertEquals(current, current.next.prev, "Prev pointer should point to previous node");
            }
            assertNull(current.child, "Child pointer should be null after flattening");
            current = current.next;
        }
        assertNull(current, "Last node's next should be null");
    }


    private Node createNode(int val) {
        return new Node(val);
    }

    private void connectNodes(Node first, Node second) {
        first.next = second;
        second.prev = first;
    }
}