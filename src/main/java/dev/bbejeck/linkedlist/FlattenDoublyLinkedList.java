package dev.bbejeck.linkedlist;

/**
 * User: Bill Bejeck
 * Date: 2/27/25
 * Time: 8:40 PM
 */
public class FlattenDoublyLinkedList {

    public Node flatten(Node head) {
           Node flatten = new Node();
           flattenHelper(flatten, head);
           return flatten.next;
    }

    public void flattenHelper(Node flatten, Node head) {
        if (head == null) {
            System.out.println("Reached a null head, returning.");
            return;
        }
        Node current = head;
        while (current != null) {
            System.out.printf("Visiting node: %s%n", head);
            if (current.child != null) {
                System.out.printf("Node %s has a child %s, calling flattenHelper recursively.%n", head, head.child);
                flattenHelper(flatten, current.child);
                current.child = null;
                System.out.printf("Returning from recursive call for child of node: %s%n", head);
            }
            System.out.printf("Start of block right after recursive call %s%n", head);
            flatten.next = current;
            System.out.printf("Linked node: %s to flatten list.%n", head);
            flatten = flatten.next;
            current = current.next;
            if (current != null) {
                System.out.printf("Moving to next node: %s%n", head);
            }
        }
    }
}
