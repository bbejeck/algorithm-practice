package dev.bbejeck.linkedlist;

import jdk.jshell.execution.Util;

/**
 * User: Bill Bejeck
 * Date: 2/2/25
 * Time: 1:14 PM
 */
public class DeleteFromList {

    public ListNode removeElements(ListNode head, int val) {
        if (head == null) {
            return head;
        }
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode current = dummy;

        while(current.next != null){
            if (current.next.val == val) {
                ListNode temp = current.next;
                current.next = temp.next;
                temp.next = null;
            } else {
                current = current.next;
            }
        }
        return dummy.next;
    }
    public static void main(String[] args) {
        DeleteFromList deleteFromList = new DeleteFromList();
        ListNode head = Utils.createLinkedList(new int[]{7,7,7,7});
        ListNode updated = deleteFromList.removeElements(head, 7);
        Utils.printLinkedList(updated);
        head = Utils.createLinkedList(new int[]{1,2,3,6,4,5,6});
        updated = deleteFromList.removeElements(head, 6);
        Utils.printLinkedList(updated);
    }
}
