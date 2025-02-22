package dev.bbejeck.linkedlist;

/**
 * User: Bill Bejeck
 * Date: 2/1/25
 * Time: 5:06 PM
 */
public class Utils {

        public static ListNode createLinkedList(int[] values) {
            if (values.length == 0) return null;
            ListNode head = new ListNode(values[0]);
            ListNode current = head;
            for (int i = 1; i < values.length; i++) {
                current.next = new ListNode(values[i]);
                current = current.next;
            }
            return head;
        }

        public static void printLinkedList(ListNode head) {
            while (head != null) {
                System.out.print(head.val + " ");
                head = head.next;
            }
            System.out.println();
        }
}
