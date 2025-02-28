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

    private Node createNode(int val) {
        return new Node(val);
    }

    private void connectNodes(Node first, Node second) {
        first.next = second;
        second.prev = first;
    }
}