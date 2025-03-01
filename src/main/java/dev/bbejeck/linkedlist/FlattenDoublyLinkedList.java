package dev.bbejeck.linkedlist;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * User: Bill Bejeck
 * Date: 2/27/25
 * Time: 8:40 PM
 */
public class FlattenDoublyLinkedList {
    Deque<Node> stack = new ArrayDeque<>();

    public Node flatten(Node head) {
        if (head == null) {
            return null;
        }
        Node current = head;
        Node previous = null;
        stack.push(current);
        while (!stack.isEmpty()) {
            current = stack.pop();
            Node next = current.next;
            if (next != null) {
                stack.push(next);
            }
            Node child = current.child;
            if (child != null) {
                stack.push(child);
                current.child = null;
                current.next = child;
                child.prev = current;
            } else {
                if (previous != null) {
                    previous.next = current;
                    current.prev = previous;
                }
            }
            previous = current;
        }
        return head;
    }
}
