package dev.bbejeck.linkedlist;

import static dev.bbejeck.linkedlist.Utils.createLinkedList;
import static dev.bbejeck.linkedlist.Utils.printLinkedList;

/**
 * User: Bill Bejeck
 * Date: 2/1/25
 * Time: 4:59 PM
 */
public class SortLinkList {

    public ListNode sortList(ListNode head) {
        return partition(head);
    }

    public ListNode merge (ListNode left, ListNode right) {
        if (left == null) {
            return right;
        }
        if (right == null ) {
            return left;
        }

        if (left.val <= right.val) {
            left.next = merge(left.next, right);
            return left;
        } else {
            right.next = merge(left, right.next);
            return right;
        }

    }

    public ListNode partition(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }

        System.out.println("Start of partition");
        printLinkedList(head);
        ListNode fast = head;
        ListNode slow = head;
        ListNode prev = null;

        while(fast != null && fast.next !=null ) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        if (prev != null) {
            prev.next = null;
        }
        System.out.println("End of partition");
        printLinkedList(slow);
        printLinkedList(head);

        ListNode left = partition(head);
        ListNode right = partition(slow);
        return merge(left, right);
    }

    public static void main(String[] args) {
        SortLinkList sort = new SortLinkList();
        ListNode head = createLinkedList(new int[]{4, 2, 1, 3});
        ListNode modifiedHead = sort.sortList(head);
        Utils.printLinkedList(modifiedHead);
        

    }

}
