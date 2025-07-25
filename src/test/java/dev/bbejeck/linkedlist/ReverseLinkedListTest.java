package dev.bbejeck.linkedlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReverseLinkedListTest {

    private ReverseLinkedList reverseLinkedList;

    @BeforeEach
    void setUp() {
        reverseLinkedList = new ReverseLinkedList();
    }

    // Helper method to create a linked list from an array
    private ListNode createLinkedList(int[] values) {
        if (values.length == 0) return null;

        ListNode head = new ListNode(values[0]);
        ListNode current = head;

        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }

        return head;
    }

    // Helper method to convert linked list to array for easy comparison
    private int[] linkedListToArray(ListNode head) {
        if (head == null) return new int[0];

        // First pass: count nodes
        int count = 0;
        ListNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }

        // Second pass: populate array
        int[] result = new int[count];
        current = head;
        for (int i = 0; i < count; i++) {
            result[i] = current.val;
            current = current.next;
        }

        return result;
    }

    @Test
    @DisplayName("Test reverse of null list")
    void testReverseNullList() {
        ListNode result = reverseLinkedList.reverse(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Test reverse of single node list")
    void testReverseSingleNode() {
        ListNode head = new ListNode(42);
        ListNode result = reverseLinkedList.reverse(head);

        assertNotNull(result);
        assertEquals(42, result.val);
        assertNull(result.next);
    }

    @Test
    @DisplayName("Test reverse of two node list")
    void testReverseTwoNodes() {
        int[] original = {1, 2};
        int[] expected = {2, 1};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse of classic example [1,2,3,4,5]")
    void testReverseClassicExample() {
        int[] original = {1, 2, 3, 4, 5};
        int[] expected = {5, 4, 3, 2, 1};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse of three node list")
    void testReverseThreeNodes() {
        int[] original = {1, 2, 3};
        int[] expected = {3, 2, 1};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse with negative numbers")
    void testReverseWithNegativeNumbers() {
        int[] original = {-1, -2, -3, -4};
        int[] expected = {-4, -3, -2, -1};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse with mixed positive and negative numbers")
    void testReverseWithMixedNumbers() {
        int[] original = {-5, 0, 3, -2, 7};
        int[] expected = {7, -2, 3, 0, -5};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse with duplicate values")
    void testReverseWithDuplicates() {
        int[] original = {1, 2, 2, 3, 3, 3};
        int[] expected = {3, 3, 3, 2, 2, 1};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse with all same values")
    void testReverseWithAllSameValues() {
        int[] original = {5, 5, 5, 5};
        int[] expected = {5, 5, 5, 5};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse of longer list")
    void testReverseLongerList() {
        int[] original = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] expected = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test that original list is properly modified")
    void testOriginalListModification() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);

        node1.next = node2;
        node2.next = node3;

        ListNode result = reverseLinkedList.reverse(node1);

        // Result should point to the last node (node3)
        assertSame(node3, result);

        // Check that links are properly reversed
        assertEquals(3, result.val);
        assertEquals(2, result.next.val);
        assertEquals(1, result.next.next.val);
        assertNull(result.next.next.next);

        // Original first node should now be last
        assertNull(node1.next);
    }

    @Test
    @DisplayName("Test reverse twice returns to original")
    void testReverseTwice() {
        int[] original = {1, 2, 3, 4, 5};

        ListNode head = createLinkedList(original);
        ListNode firstReverse = reverseLinkedList.reverse(head);
        ListNode secondReverse = reverseLinkedList.reverse(firstReverse);

        assertArrayEquals(original, linkedListToArray(secondReverse));
    }

    @Test
    @DisplayName("Test reverse with zero value")
    void testReverseWithZero() {
        int[] original = {0, 1, 0, 2, 0};
        int[] expected = {0, 2, 0, 1, 0};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test reverse with maximum and minimum integer values")
    void testReverseWithExtremeValues() {
        int[] original = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE};
        int[] expected = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        assertArrayEquals(expected, linkedListToArray(result));
    }

    @Test
    @DisplayName("Test that reversed list has no cycles")
    void testNoCyclesInReversedList() {
        int[] original = {1, 2, 3, 4, 5};

        ListNode head = createLinkedList(original);
        ListNode result = reverseLinkedList.reverse(head);

        // Use Floyd's cycle detection to ensure no cycles
        ListNode slow = result;
        ListNode fast = result;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            assertNotSame(slow, fast, "Reversed list should not contain cycles");
        }
    }
}