package dev.bbejeck.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CloneGraph
 */
public class CloneGraphTest {

    private CloneGraph cloneGraph;

    @BeforeEach
    void setUp() {
        cloneGraph = new CloneGraph();
    }

    @Test
    void testCloneGraph_NullInput() {
        Node result = cloneGraph.cloneGraph(null);
        assertNull(result);
    }

    @Test
    void testCloneGraph_SingleNode() {
        // Create single node with no neighbors
        Node node = new Node(1);

        Node cloned = cloneGraph.cloneGraph(node);

        assertNotNull(cloned);
        assertEquals(1, cloned.val);
        assertTrue(cloned.neighbors.isEmpty());
        assertNotSame(node, cloned); // Different object reference
    }

    @Test
    void testCloneGraph_TwoConnectedNodes() {
        // Create: 1 -- 2
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        node1.neighbors.add(node2);
        node2.neighbors.add(node1);

        Node cloned = cloneGraph.cloneGraph(node1);

        assertNotNull(cloned);
        assertEquals(1, cloned.val);
        assertEquals(1, cloned.neighbors.size());

        Node clonedNode2 = cloned.neighbors.get(0);
        assertEquals(2, clonedNode2.val);
        assertEquals(1, clonedNode2.neighbors.size());
        assertEquals(1, clonedNode2.neighbors.get(0).val);

        // Verify different object references
        assertNotSame(node1, cloned);
        assertNotSame(node2, clonedNode2);

        // Verify the cloned graph maintains proper connectivity
        assertSame(cloned, clonedNode2.neighbors.get(0));
    }

    @Test
    void testCloneGraph_SquareGraph() {
        // Create the classic 4-node square: adjList = [[2,4],[1,3],[2,4],[1,3]]
        //   1 ---- 2
        //   |      |
        //   4 ---- 3
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        node1.neighbors.addAll(Arrays.asList(node2, node4));
        node2.neighbors.addAll(Arrays.asList(node1, node3));
        node3.neighbors.addAll(Arrays.asList(node2, node4));
        node4.neighbors.addAll(Arrays.asList(node1, node3));

        Node cloned = cloneGraph.cloneGraph(node1);

        // Verify structure and connectivity
        Map<Integer, Node> nodeMap = mapNodesByValue(cloned);

        assertEquals(4, nodeMap.size());
        assertTrue(nodeMap.containsKey(1));
        assertTrue(nodeMap.containsKey(2));
        assertTrue(nodeMap.containsKey(3));
        assertTrue(nodeMap.containsKey(4));

        // Verify connections match original adjacency list
        Node clonedNode1 = nodeMap.get(1);
        Node clonedNode2 = nodeMap.get(2);
        Node clonedNode3 = nodeMap.get(3);
        Node clonedNode4 = nodeMap.get(4);

        // Node 1 connects to [2,4]
        assertEquals(2, clonedNode1.neighbors.size());
        assertTrue(containsNodeWithValue(clonedNode1.neighbors, 2));
        assertTrue(containsNodeWithValue(clonedNode1.neighbors, 4));

        // Node 2 connects to [1,3]
        assertEquals(2, clonedNode2.neighbors.size());
        assertTrue(containsNodeWithValue(clonedNode2.neighbors, 1));
        assertTrue(containsNodeWithValue(clonedNode2.neighbors, 3));

        // Node 3 connects to [2,4]
        assertEquals(2, clonedNode3.neighbors.size());
        assertTrue(containsNodeWithValue(clonedNode3.neighbors, 2));
        assertTrue(containsNodeWithValue(clonedNode3.neighbors, 4));

        // Node 4 connects to [1,3]
        assertEquals(2, clonedNode4.neighbors.size());
        assertTrue(containsNodeWithValue(clonedNode4.neighbors, 1));
        assertTrue(containsNodeWithValue(clonedNode4.neighbors, 3));

        // Verify all nodes are different object references
        assertNotSame(node1, clonedNode1);
        assertNotSame(node2, clonedNode2);
        assertNotSame(node3, clonedNode3);
        assertNotSame(node4, clonedNode4);
    }

    @Test
    void testCloneGraph_Triangle() {
        // Create triangle: 1-2-3-1
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);

        node1.neighbors.addAll(Arrays.asList(node2, node3));
        node2.neighbors.addAll(Arrays.asList(node1, node3));
        node3.neighbors.addAll(Arrays.asList(node1, node2));

        Node cloned = cloneGraph.cloneGraph(node1);

        Map<Integer, Node> nodeMap = mapNodesByValue(cloned);
        assertEquals(3, nodeMap.size());

        // Each node should have 2 neighbors in a triangle
        for (Node node : nodeMap.values()) {
            assertEquals(2, node.neighbors.size());
        }

        // Verify triangle connectivity
        Node clonedNode1 = nodeMap.get(1);
        assertTrue(containsNodeWithValue(clonedNode1.neighbors, 2));
        assertTrue(containsNodeWithValue(clonedNode1.neighbors, 3));
    }

    @Test
    void testCloneGraph_ComplexGraph() {
        // Create a more complex graph with 5 nodes
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);

        node1.neighbors.addAll(Arrays.asList(node2, node3));
        node2.neighbors.addAll(Arrays.asList(node1, node4));
        node3.neighbors.addAll(Arrays.asList(node1, node4, node5));
        node4.neighbors.addAll(Arrays.asList(node2, node3, node5));
        node5.neighbors.addAll(Arrays.asList(node3, node4));

        Node cloned = cloneGraph.cloneGraph(node1);

        Map<Integer, Node> nodeMap = mapNodesByValue(cloned);
        assertEquals(5, nodeMap.size());

        // Verify specific connections
        assertTrue(containsNodeWithValue(nodeMap.get(1).neighbors, 2));
        assertTrue(containsNodeWithValue(nodeMap.get(1).neighbors, 3));
        assertEquals(2, nodeMap.get(1).neighbors.size());

        assertTrue(containsNodeWithValue(nodeMap.get(3).neighbors, 1));
        assertTrue(containsNodeWithValue(nodeMap.get(3).neighbors, 4));
        assertTrue(containsNodeWithValue(nodeMap.get(3).neighbors, 5));
        assertEquals(3, nodeMap.get(3).neighbors.size());
    }

    // Helper method to create a map of nodes by their values using BFS
    private Map<Integer, Node> mapNodesByValue(Node startNode) {
        Map<Integer, Node> nodeMap = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        queue.offer(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodeMap.put(current.val, current);

            for (Node neighbor : current.neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return nodeMap;
    }

    // Helper method to check if a list contains a node with specific value
    private boolean containsNodeWithValue(List<Node> nodes, int value) {
        return nodes.stream().anyMatch(node -> node.val == value);
    }
}