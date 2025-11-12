package dev.bbejeck.linkedlist;

/**
 * User: Bill Bejeck
 * Date: 7/24/25
 * Time: 10:17 PM
 */
public class ReverseLinkedList {

    public ListNode reverse(ListNode head) {
        ListNode current = head;
        ListNode dummy = new ListNode();
        while (current != null) {
            ListNode nextNode = current.next;
            current.next = dummy.next;
            dummy.next = current;
            current = nextNode;
        }
        return dummy.next;
    }
}
