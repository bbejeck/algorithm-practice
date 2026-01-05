package dev.bbejeck.dfs_bfs;

import dev.bbejeck.binary_tree.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TreePath class.
 */
class TreePathTest {

    private TreePath treePath;

    @BeforeEach
    void setUp() {
        treePath = new TreePath();
    }

    // Helper method to create a TreeNode with initialized children list
    private TreeNode<Integer> createNode(int val) {
        return new TreeNode<>(val, null, null, new ArrayList<>());
    }

    @Test
    @DisplayName("Single node tree returns single path")
    void testSingleNode() {
        TreeNode<Integer> root = createNode(1);

        List<String> result = treePath.allPaths(root);

        assertEquals(1, result.size());
        assertEquals("1", result.get(0));
    }

    @Test
    @DisplayName("Tree with one child returns single path")
    void testOneChild() {
        //   1
        //   |
        //   2
        TreeNode<Integer> root = createNode(1);
        TreeNode<Integer> child = createNode(2);
        root.addChild(child);

        List<String> result = treePath.allPaths(root);

        assertEquals(1, result.size());
        assertEquals("1->2", result.get(0));
    }

    @Test
    @DisplayName("Tree with two children returns two paths")
    void testTwoChildren() {
        //     1
        //    / \
        //   2   3
        TreeNode<Integer> root = createNode(1);
        TreeNode<Integer> child1 = createNode(2);
        TreeNode<Integer> child2 = createNode(3);
        root.addChild(child1);
        root.addChild(child2);

        List<String> result = treePath.allPaths(root);

        assertEquals(2, result.size());
        assertTrue(result.contains("1->2"));
        assertTrue(result.contains("1->3"));
    }

    @Test
    @DisplayName("Three-level tree returns correct paths")
    void testThreeLevelTree() {
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode<Integer> root = createNode(1);
        TreeNode<Integer> child1 = createNode(2);
        TreeNode<Integer> child2 = createNode(3);
        TreeNode<Integer> grandChild1 = createNode(4);
        TreeNode<Integer> grandChild2 = createNode(5);

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandChild1);
        child1.addChild(grandChild2);

        List<String> result = treePath.allPaths(root);

        assertEquals(3, result.size());
        assertTrue(result.contains("1->2->4"));
        assertTrue(result.contains("1->2->5"));
        assertTrue(result.contains("1->3"));
    }

    @Test
    @DisplayName("Deep linear tree (chain) returns single long path")
    void testDeepLinearTree() {
        //   1
        //   |
        //   2
        //   |
        //   3
        //   |
        //   4
        TreeNode<Integer> node1 = createNode(1);
        TreeNode<Integer> node2 = createNode(2);
        TreeNode<Integer> node3 = createNode(3);
        TreeNode<Integer> node4 = createNode(4);

        node1.addChild(node2);
        node2.addChild(node3);
        node3.addChild(node4);

        List<String> result = treePath.allPaths(node1);

        assertEquals(1, result.size());
        assertEquals("1->2->3->4", result.get(0));
    }

    @Test
    @DisplayName("Wide tree with many children")
    void testWideTree() {
        //       1
        //    /|\ \
        //   2 3 4 5
        TreeNode<Integer> root = createNode(1);
        for (int i = 2; i <= 5; i++) {
            root.addChild(createNode(i));
        }

        List<String> result = treePath.allPaths(root);

        assertEquals(4, result.size());
        assertTrue(result.contains("1->2"));
        assertTrue(result.contains("1->3"));
        assertTrue(result.contains("1->4"));
        assertTrue(result.contains("1->5"));
    }

    @Test
    @DisplayName("Complex tree with varying depths")
    void testComplexTree() {
        //         1
        //       / | \
        //      2  3  4
        //     /      |
        //    5       6
        //   /
        //  7
        TreeNode<Integer> root = createNode(1);
        TreeNode<Integer> node2 = createNode(2);
        TreeNode<Integer> node3 = createNode(3);
        TreeNode<Integer> node4 = createNode(4);
        TreeNode<Integer> node5 = createNode(5);
        TreeNode<Integer> node6 = createNode(6);
        TreeNode<Integer> node7 = createNode(7);

        root.addChild(node2);
        root.addChild(node3);
        root.addChild(node4);
        node2.addChild(node5);
        node4.addChild(node6);
        node5.addChild(node7);

        List<String> result = treePath.allPaths(root);

        assertEquals(3, result.size());
        assertTrue(result.contains("1->2->5->7"));
        assertTrue(result.contains("1->3"));
        assertTrue(result.contains("1->4->6"));
    }

    @Test
    @DisplayName("Tree with negative values")
    void testNegativeValues() {
        //     -1
        //    /  \
        //  -2   -3
        TreeNode<Integer> root = createNode(-1);
        root.addChild(createNode(-2));
        root.addChild(createNode(-3));

        List<String> result = treePath.allPaths(root);

        assertEquals(2, result.size());
        assertTrue(result.contains("-1->-2"));
        assertTrue(result.contains("-1->-3"));
    }

    @Test
    @DisplayName("Tree with zero value")
    void testZeroValue() {
        //     0
        //    / \
        //   1   2
        TreeNode<Integer> root = createNode(0);
        root.addChild(createNode(1));
        root.addChild(createNode(2));

        List<String> result = treePath.allPaths(root);

        assertEquals(2, result.size());
        assertTrue(result.contains("0->1"));
        assertTrue(result.contains("0->2"));
    }

    @Test
    @DisplayName("Tree with large values")
    void testLargeValues() {
        TreeNode<Integer> root = createNode(Integer.MAX_VALUE);
        root.addChild(createNode(Integer.MIN_VALUE));
        root.addChild(createNode(0));

        List<String> result = treePath.allPaths(root);

        assertEquals(2, result.size());
        assertTrue(result.contains(Integer.MAX_VALUE + "->" + Integer.MIN_VALUE));
        assertTrue(result.contains(Integer.MAX_VALUE + "->0"));
    }

    @Test
    @DisplayName("Balanced ternary tree")
    void testBalancedTernaryTree() {
        //           1
        //        /  |  \
        //       2   3   4
        //      /|  /|  /|
        //     5 6 7 8 9 10
        TreeNode<Integer> root = createNode(1);
        TreeNode<Integer> node2 = createNode(2);
        TreeNode<Integer> node3 = createNode(3);
        TreeNode<Integer> node4 = createNode(4);

        root.addChild(node2);
        root.addChild(node3);
        root.addChild(node4);

        node2.addChild(createNode(5));
        node2.addChild(createNode(6));
        node3.addChild(createNode(7));
        node3.addChild(createNode(8));
        node4.addChild(createNode(9));
        node4.addChild(createNode(10));

        List<String> result = treePath.allPaths(root);

        assertEquals(6, result.size());
        assertTrue(result.contains("1->2->5"));
        assertTrue(result.contains("1->2->6"));
        assertTrue(result.contains("1->3->7"));
        assertTrue(result.contains("1->3->8"));
        assertTrue(result.contains("1->4->9"));
        assertTrue(result.contains("1->4->10"));
    }

    @Test
    @DisplayName("Path order matches DFS traversal order")
    void testPathOrderMatchesDfs() {
        //     1
        //    / \
        //   2   3
        TreeNode<Integer> root = createNode(1);
        root.addChild(createNode(2));
        root.addChild(createNode(3));

        List<String> result = treePath.allPaths(root);

        // DFS should visit left child first
        assertEquals("1->2", result.get(0));
        assertEquals("1->3", result.get(1));
    }
}