package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReconstructBinarySearchTreeFromPreorder class
 */
class ReconstructBinarySearchTreeFromPreorderTest {

    private ReconstructBinarySearchTreeFromPreorder treeBuilder;

    @BeforeEach
    void setUp() {
        treeBuilder = new ReconstructBinarySearchTreeFromPreorder();
    }

    @Test
    @DisplayName("Should return null for empty array")
    void testEmptyArray() {
        int[] preorder = {};
        TreeNode result = treeBuilder.buildTree(preorder);
        assertNull(result);
    }

    @Test
    @DisplayName("Should build single node tree")
    void testSingleNode() {
        int[] preorder = {5};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNull(result.left);
        assertNull(result.right);
    }

    @Test
    @DisplayName("Should build tree with root and left child only")
    void testRootWithLeftChild() {
        //   5
        //  /
        // 3
        int[] preorder = {5, 3};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNotNull(result.left);
        assertEquals(3, result.left.val);
        assertNull(result.right);
    }

    @Test
    @DisplayName("Should build tree with root and right child only")
    void testRootWithRightChild() {
        //   5
        //    \
        //     8
        int[] preorder = {5, 8};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNull(result.left);
        assertNotNull(result.right);
        assertEquals(8, result.right.val);
    }

    @Test
    @DisplayName("Should build balanced tree with three nodes")
    void testBalancedThreeNodes() {
        //     5
        //    / \
        //   3   8
        int[] preorder = {5, 3, 8};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(5, result.val);
        assertEquals(3, result.left.val);
        assertEquals(8, result.right.val);
    }

    @Test
    @DisplayName("Should build LeetCode example tree")
    void testLeetCodeExample() {
        //       8
        //      / \
        //     5   10
        //    / \    \
        //   1   7   12
        int[] preorder = {8, 5, 1, 7, 10, 12};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(8, result.val);

        // Left subtree
        assertEquals(5, result.left.val);
        assertEquals(1, result.left.left.val);
        assertEquals(7, result.left.right.val);

        // Right subtree
        assertEquals(10, result.right.val);
        assertNull(result.right.left);
        assertEquals(12, result.right.right.val);
    }

    @Test
    @DisplayName("Should build left-skewed tree (descending order)")
    void testLeftSkewedTree() {
        //       5
        //      /
        //     4
        //    /
        //   3
        //  /
        // 1
        int[] preorder = {5, 4, 3, 1};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNull(result.right);

        assertEquals(4, result.left.val);
        assertNull(result.left.right);

        assertEquals(3, result.left.left.val);
        assertNull(result.left.left.right);

        assertEquals(1, result.left.left.left.val);
    }

    @Test
    @DisplayName("Should build right-skewed tree (ascending order)")
    void testRightSkewedTree() {
        // 1
        //  \
        //   2
        //    \
        //     3
        //      \
        //       5
        int[] preorder = {1, 2, 3, 5};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(1, result.val);
        assertNull(result.left);

        assertEquals(2, result.right.val);
        assertNull(result.right.left);

        assertEquals(3, result.right.right.val);
        assertNull(result.right.right.left);

        assertEquals(5, result.right.right.right.val);
    }

    @Test
    @DisplayName("Should build complete BST")
    void testCompleteBST() {
        //        10
        //       /  \
        //      5    15
        //     / \   / \
        //    3   7 12  20
        int[] preorder = {10, 5, 3, 7, 15, 12, 20};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(10, result.val);

        // Left subtree
        assertEquals(5, result.left.val);
        assertEquals(3, result.left.left.val);
        assertEquals(7, result.left.right.val);

        // Right subtree
        assertEquals(15, result.right.val);
        assertEquals(12, result.right.left.val);
        assertEquals(20, result.right.right.val);
    }

    @Test
    @DisplayName("Should verify BST property - left < root < right")
    void testBSTProperty() {
        int[] preorder = {8, 5, 1, 7, 10, 12};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertTrue(isValidBST(result, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("Should handle multiple calls correctly")
    void testMultipleCalls() {
        // First call
        int[] preorder1 = {5, 3, 8};
        TreeNode result1 = treeBuilder.buildTree(preorder1);
        assertEquals(5, result1.val);
        assertEquals(3, result1.left.val);
        assertEquals(8, result1.right.val);

        // Second call should work independently
        int[] preorder2 = {10, 6, 15};
        TreeNode result2 = treeBuilder.buildTree(preorder2);
        assertEquals(10, result2.val);
        assertEquals(6, result2.left.val);
        assertEquals(15, result2.right.val);
    }

    @Test
    @DisplayName("Should build asymmetric tree")
    void testAsymmetricTree() {
        //      6
        //     / \
        //    2   8
        //     \
        //      4
        int[] preorder = {6, 2, 4, 8};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(6, result.val);
        assertEquals(2, result.left.val);
        assertNull(result.left.left);
        assertEquals(4, result.left.right.val);
        assertEquals(8, result.right.val);
    }

    @Test
    @DisplayName("Should handle larger tree")
    void testLargerTree() {
        //           50
        //         /    \
        //       30      70
        //      /  \    /  \
        //    20   40  60   80
        //   /
        //  10
        int[] preorder = {50, 30, 20, 10, 40, 70, 60, 80};
        TreeNode result = treeBuilder.buildTree(preorder);

        assertNotNull(result);
        assertEquals(50, result.val);

        assertEquals(30, result.left.val);
        assertEquals(20, result.left.left.val);
        assertEquals(10, result.left.left.left.val);
        assertEquals(40, result.left.right.val);

        assertEquals(70, result.right.val);
        assertEquals(60, result.right.left.val);
        assertEquals(80, result.right.right.val);
    }

    // Helper method to verify BST property
    private boolean isValidBST(TreeNode node, int min, int max) {
        if (node == null) {
            return true;
        }
        if (node.val <= min || node.val >= max) {
            return false;
        }
        return isValidBST(node.left, min, node.val) && isValidBST(node.right, node.val, max);
    }
}