package dev.bbejeck.linkedlist;

/**
 * User: Bill Bejeck
 * Date: 2/1/25
 * Time: 4:59 PM
 */
public class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
    }
    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
    ListNode() {}

    @Override
    public String toString() {
        return "ListNode -> val: " + val;
    }
}
