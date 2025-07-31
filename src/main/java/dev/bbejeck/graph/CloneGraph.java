package dev.bbejeck.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * User: Bill Bejeck
 * Date: 7/31/25
 * Time: 3:28 PM
 */
public class CloneGraph {
    public Node cloneGraph(Node node) {
        if (node == null) {
            return null;
        }
        Map<Node, Node> visited = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        visited.put(node, new Node(node.val));
        queue.offer(node);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (Node n: current.neighbors) {
                if (!visited.containsKey(n)) {
                    visited.put(n, new Node(n.val));
                    queue.offer(n);
                }
                visited.get(current).neighbors.add(visited.get(n));
            }
        }
        return visited.get(node);
    }
}
