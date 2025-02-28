package dev.bbejeck.linkedlist;

/**
 * User: Bill Bejeck
 * Date: 2/27/25
 * Time: 8:42 PM
 */
public class Node {
    public int val;
    public Node prev;
    public Node next;
    public Node child;

    public Node(int val) {
        this.val = val;
    }
    public Node() {}


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node(val=").append(val);
        if (child != null) {
            sb.append(", child=[").append(child).append("]");
        }
        if (next != null) {
            sb.append(", next=[").append(next).append("]");
        }
        sb.append(")");
        return sb.toString();
    }
}
