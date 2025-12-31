package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LowestCommonAncestor class
 */
public class LowestCommonAncestorTest {

    private LowestCommonAncestor lca;

    @BeforeEach
    void setUp() {
        lca = new LowestCommonAncestor();
    }

    /**
     * Test with the classic LeetCode example:
     *        3
     *       / \
     *      5   1
     *     / \ / \
     *    6  2 0  8
     *      / \
     *     7   4
     */
    @Test
    void testClassicExample_5And1() {
        // Build the tree
        TreeNode<Integer> root = new TreeNode<>(3);
        TreeNode<Integer> node5 = new TreeNode<>(5);
        TreeNode<Integer> node1 = new TreeNode<>(1);
        TreeNode<Integer> node6 = new TreeNode<>(6);
        TreeNode<Integer> node2 = new TreeNode<>(2);
        TreeNode<Integer> node0 = new TreeNode<>(0);
        TreeNode<Integer> node8 = new TreeNode<>(8);
        TreeNode<Integer> node7 = new TreeNode<>(7);
        TreeNode<Integer> node4 = new TreeNode<>(4);

        root.left = node5;
        root.right = node1;
        node5.left = node6;
        node5.right = node2;
        node1.left = node0;
        node1.right = node8;
        node2.left = node7;
        node2.right = node4;

        // Test: LCA of 5 and 1 should be 3
        TreeNode<Integer> result = lca.lowestCommonAncestor(root, node5, node1);
        assertEquals(3, result.val);
    }

    @Test
    void testClassicExample_5And4() {
        // Build the same tree
        TreeNode<Integer> root = new TreeNode<>(3);
        TreeNode<Integer> node5 = new TreeNode<>(5);
        TreeNode<Integer> node1 = new TreeNode<>(1);
        TreeNode<Integer> node6 = new TreeNode<>(6);
        TreeNode<Integer> node2 = new TreeNode<>(2);
        TreeNode<Integer> node0 = new TreeNode<>(0);
        TreeNode<Integer> node8 = new TreeNode<>(8);
        TreeNode<Integer> node7 = new TreeNode<>(7);
        TreeNode<Integer> node4 = new TreeNode<>(4);

        root.left = node5;
        root.right = node1;
        node5.left = node6;
        node5.right = node2;
        node1.left = node0;
        node1.right = node8;
        node2.left = node7;
        node2.right = node4;

        // Test: LCA of 5 and 4 should be 5 (node is ancestor of itself)
        TreeNode<Integer> result = lca.lowestCommonAncestor(root, node5, node4);
        assertEquals(5, result.val);
    }

    @Test
    void testClassicExample_6And4() {
        // Build the same tree
        TreeNode<Integer> root = new TreeNode<>(3);
        TreeNode<Integer> node5 = new TreeNode<>(5);
        TreeNode<Integer> node1 = new TreeNode<>(1);
        TreeNode<Integer> node6 = new TreeNode<>(6);
        TreeNode<Integer> node2 = new TreeNode<>(2);
        TreeNode<Integer> node0 = new TreeNode<>(0);
        TreeNode<Integer> node8 = new TreeNode<>(8);
        TreeNode<Integer> node7 = new TreeNode<>(7);
        TreeNode<Integer> node4 = new TreeNode<>(4);

        root.left = node5;
        root.right = node1;
        node5.left = node6;
        node5.right = node2;
        node1.left = node0;
        node1.right = node8;
        node2.left = node7;
        node2.right = node4;

        // Test: LCA of 6 and 4 should be 5
        TreeNode<Integer> result = lca.lowestCommonAncestor(root, node6, node4);
        assertEquals(5, result.val);
    }

    @Test
    void testSingleNodeTree() {
        TreeNode<Integer> root = new TreeNode<>(1);

        // Both nodes are the same (root)
        TreeNode<Integer> result = lca.lowestCommonAncestor(root, root, root);
        assertEquals(1, result.val);
    }

    @Test
    void testTwoNodeTree() {
        TreeNode<Integer> root = new TreeNode<>(1);
        TreeNode<Integer> left = new TreeNode<>(2);
        root.left = left;

        // LCA of root and its child should be root
        TreeNode<Integer> result = lca.lowestCommonAncestor(root, root, left);
        assertEquals(1, result.val);

        // LCA of child and root should be root
        TreeNode<Integer> result2 = lca.lowestCommonAncestor(root, left, root);
        assertEquals(1, result2.val);
    }

    @Test
    void testLinearTree() {
        // Create a linear tree: 1 -> 2 -> 3 -> 4
        TreeNode<Integer> node1 = new TreeNode<>(1);
        TreeNode<Integer> node2 = new TreeNode<>(2);
        TreeNode<Integer> node3 = new TreeNode<>(3);
        TreeNode<Integer> node4 = new TreeNode<>(4);

        node1.left = node2;
        node2.left = node3;
        node3.left = node4;

        // LCA of 1 and 4 should be 1
        TreeNode<Integer> result = lca.lowestCommonAncestor(node1, node1, node4);
        assertEquals(1, result.val);

        // LCA of 2 and 4 should be 2
        TreeNode<Integer> result2 = lca.lowestCommonAncestor(node1, node2, node4);
        assertEquals(2, result2.val);

        // LCA of 3 and 4 should be 3
        TreeNode<Integer> result3 = lca.lowestCommonAncestor(node1, node3, node4);
        assertEquals(3, result3.val);
    }

    @Test
    void testSameNode() {
        TreeNode<Integer> root = new TreeNode<>(3);
        TreeNode<Integer> left = new TreeNode<>(5);
        TreeNode<Integer> right = new TreeNode<>(1);
        root.left = left;
        root.right = right;

        // LCA of a node with itself should be the node itself
        TreeNode<Integer> result = lca.lowestCommonAncestor(root, left, left);
        assertEquals(5, result.val);
    }

    @Test
    void testComplexTree() {
        // Create a more complex tree for edge case testing
        //       1
        //      / \
        //     2   3
        //    /   / \
        //   4   5   6
        //  /     \
        // 7       8

        TreeNode<Integer> node1 = new TreeNode<>(1);
        TreeNode<Integer> node2 = new TreeNode<>(2);
        TreeNode<Integer> node3 = new TreeNode<>(3);
        TreeNode<Integer> node4 = new TreeNode<>(4);
        TreeNode<Integer> node5 = new TreeNode<>(5);
        TreeNode<Integer> node6 = new TreeNode<>(6);
        TreeNode<Integer> node7 = new TreeNode<>(7);
        TreeNode<Integer> node8 = new TreeNode<>(8);

        node1.left = node2;
        node1.right = node3;
        node2.left = node4;
        node3.left = node5;
        node3.right = node6;
        node4.left = node7;
        node5.right = node8;

        // Test various combinations
        assertEquals(1, lca.lowestCommonAncestor(node1, node7, node8).val); // Across different subtrees
        assertEquals(2, lca.lowestCommonAncestor(node1, node2, node7).val); // Parent-descendant
        assertEquals(3, lca.lowestCommonAncestor(node1, node5, node6).val); // Siblings
        assertEquals(1, lca.lowestCommonAncestor(node1, node4, node3).val); // Cross subtrees
    }
}