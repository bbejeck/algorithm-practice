package dev.bbejeck.linkedlist;

/**
 * User: Bill Bejeck
 * Date: 7/24/25
 * Time: 10:17 PM
 */
public class ReverseLinkedList {

    public ListNode reverse(ListNode head) {
        ListNode current = head;
        ListNode previous = null;
        while (current != null) {
            ListNode tempNext = current.next;
            current.next = previous;
            previous = current;
            current = tempNext;
        }
        return previous;
    }
}
