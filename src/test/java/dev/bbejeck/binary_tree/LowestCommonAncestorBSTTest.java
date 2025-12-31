package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the LowestCommonAncestorBST class.
 */
class LowestCommonAncestorBSTTest {

    private LowestCommonAncestorBST lcaBST;
    private InsertBst insertBst;

    @BeforeEach
    void setUp() {
        lcaBST = new LowestCommonAncestorBST();
        insertBst = new InsertBst();
    }

    @Test
    @DisplayName("LCA is the root when nodes are on opposite sides")
    void testLcaIsRoot() {
        //        6
        //       / \
        //      2   8
        TreeNode root = buildTree(6, 2, 8);

        Assertions.assertNotNull(root);
        assertEquals(6, lcaBST.lowestCommonAncestor(root, 2, 8));
    }

    @Test
    @DisplayName("LCA when both nodes are in left subtree")
    void testBothNodesInLeftSubtree() {
        //        6
        //       / \
        //      2   8
        //     / \
        //    0   4
        TreeNode root = buildTree(6, 2, 8, 0, 4);

        Assertions.assertNotNull(root);
        assertEquals(2, lcaBST.lowestCommonAncestor(root, 0, 4));
    }

    @Test
    @DisplayName("LCA when both nodes are in right subtree")
    void testBothNodesInRightSubtree() {
        //        6
        //       / \
        //      2   8
        //         / \
        //        7   9
        TreeNode root = buildTree(6, 2, 8, 7, 9);

        assertEquals(8, lcaBST.lowestCommonAncestor(root, 7, 9));
    }

    @Test
    @DisplayName("LCA when one node is ancestor of the other")
    void testOneNodeIsAncestor() {
        //        6
        //       / \
        //      2   8
        //     / \
        //    0   4
        //       / \
        //      3   5
        TreeNode root = buildTree(6, 2, 8, 0, 4, 3, 5);

        assertEquals(2, lcaBST.lowestCommonAncestor(root, 2, 4));
    }

    @Test
    @DisplayName("LCA when searching for root and a descendant")
    void testRootAndDescendant() {
        //        6
        //       / \
        //      2   8
        TreeNode root = buildTree(6, 2, 8);

        assertEquals(6, lcaBST.lowestCommonAncestor(root, 6, 8));
        assertEquals(6, lcaBST.lowestCommonAncestor(root, 6, 2));
    }

    @Test
    @DisplayName("LCA in a larger tree - LeetCode style example")
    void testLeetCodeExample() {
        //           6
        //         /   \
        //        2     8
        //       / \   / \
        //      0   4 7   9
        //         / \
        //        3   5
        TreeNode root = buildTree(6, 2, 8, 0, 4, 7, 9, 3, 5);

        assertEquals(6, lcaBST.lowestCommonAncestor(root, 2, 8));
        assertEquals(2, lcaBST.lowestCommonAncestor(root, 2, 4));
        assertEquals(4, lcaBST.lowestCommonAncestor(root, 3, 5));
        assertEquals(2, lcaBST.lowestCommonAncestor(root, 0, 5));
    }

    @Test
    @DisplayName("LCA with nodes at different depths")
    void testNodesAtDifferentDepths() {
        //           6
        //         /   \
        //        2     8
        //       / \   / \
        //      0   4 7   9
        //         / \
        //        3   5
        TreeNode root = buildTree(6, 2, 8, 0, 4, 7, 9, 3, 5);

        // 0 is at depth 2, 5 is at depth 3
        assertEquals(2, lcaBST.lowestCommonAncestor(root, 0, 5));
        // 3 is at depth 3, 8 is at depth 1
        assertEquals(6, lcaBST.lowestCommonAncestor(root, 3, 8));
    }

    @Test
    @DisplayName("LCA in a left-skewed tree")
    void testLeftSkewedTree() {
        //    5
        //   /
        //  4
        // /
        // 3
        TreeNode root = buildTree(5, 4, 3);

        assertEquals(4, lcaBST.lowestCommonAncestor(root, 3, 4));
        assertEquals(5, lcaBST.lowestCommonAncestor(root, 3, 5));
    }

    @Test
    @DisplayName("LCA in a right-skewed tree")
    void testRightSkewedTree() {
        // 5
        //  \
        //   6
        //    \
        //     7
        TreeNode root = buildTree(5, 6, 7);

        assertEquals(6, lcaBST.lowestCommonAncestor(root, 6, 7));
        assertEquals(5, lcaBST.lowestCommonAncestor(root, 5, 7));
    }

    @Test
    @DisplayName("LCA with reversed parameter order gives same result")
    void testParameterOrderDoesNotMatter() {
        //        6
        //       / \
        //      2   8
        //     / \
        //    0   4
        TreeNode root = buildTree(6, 2, 8, 0, 4);

        assertEquals(lcaBST.lowestCommonAncestor(root, 0, 4),
                lcaBST.lowestCommonAncestor(root, 4, 0));
        assertEquals(lcaBST.lowestCommonAncestor(root, 2, 8),
                lcaBST.lowestCommonAncestor(root, 8, 2));
    }

    @Test
    @DisplayName("LCA when both parameters are the same node")
    void testSameNode() {
        //        6
        //       / \
        //      2   8
        TreeNode root = buildTree(6, 2, 8);

        assertEquals(6, lcaBST.lowestCommonAncestor(root, 6, 6));
        assertEquals(2, lcaBST.lowestCommonAncestor(root, 2, 2));
    }

    // Helper method to build a BST from values
    private TreeNode buildTree(int... values) {
        if (values.length == 0) {
            return null;
        }
        TreeNode root = insertBst.insert(null, values[0]);
        for (int i = 1; i < values.length; i++) {
            insertBst.insert(root, values[i]);
        }
        return root;
    }
}