package dev.bbejeck.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 7/31/25
 * Time: 3:29 PM
 */
public class Node {
    public int val;
    public List<Node> neighbors;
    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }
    public Node(int val) {
        this.val = val;
        neighbors = new ArrayList<Node>();
    }
    public Node(int val, ArrayList<Node> neighbors) {
        this.val = val;
        this.neighbors = neighbors;
    }
    
}
