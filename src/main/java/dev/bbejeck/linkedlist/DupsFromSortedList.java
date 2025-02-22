package dev.bbejeck.linkedlist;

import static dev.bbejeck.linkedlist.Utils.createLinkedList;
import static dev.bbejeck.linkedlist.Utils.printLinkedList;

/**
 * User: Bill Bejeck
 * Date: 1/30/25
 * Time: 9:21 PM
 */
public class DupsFromSortedList {

    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(Integer.MIN_VALUE);
        ListNode previous = dummy;
        ListNode current;

        if (head == null || head.next == null) {
            return head;
        }

        dummy.next = head;
        for (current = head; head != null; head = head.next) {
            if (current.next != null && current.val == current.next.val) {
                current = current.next;
                previous.next = null;
            } else if (previous.next != current) {
                previous.next = current.next;
                current = current.next;
            } else {
                previous = current;
                current = current.next;
            }
        }
        if (previous == dummy) {
            return null;
        }
        return dummy.next;
    }


    public static void main(String[] args) {
        DupsFromSortedList dups = new DupsFromSortedList();
        //int[] values = {1, 2, 3, 3, 3, 4, 4, 5};
        int[] values = {1, 1, 2};
        ListNode head = createLinkedList(values);
        ListNode modifiedHead = dups.deleteDuplicates(head);
        printLinkedList(modifiedHead);

    }
}
