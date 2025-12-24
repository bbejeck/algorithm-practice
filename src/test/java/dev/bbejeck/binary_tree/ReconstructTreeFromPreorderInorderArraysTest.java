package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for ReconstructTreeFromPreorderInorderArrays class
 */
class ReconstructTreeFromPreorderInorderArraysTest {

    private ReconstructTreeFromPreorderInorderArrays reconstructor;

    @BeforeEach
    void setUp() {
        reconstructor = new ReconstructTreeFromPreorderInorderArrays();
    }


    @Test
    @DisplayName("Should return null for empty arrays")
    void testEmptyArrays() {
        int[] preorder = {};
        int[] inorder = {};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNull(result);
    }

    @Test
    @DisplayName("Should build single node tree")
    void testSingleNode() {
        int[] preorder = {1};
        int[] inorder = {1};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(1, result.val);
        assertNull(result.left);
        assertNull(result.right);
    }

    @Test
    @DisplayName("Should build tree with two nodes - left child only")
    void testTwoNodesLeftChild() {
        //   2
        //  /
        // 1
        int[] preorder = {2, 1};
        int[] inorder = {1, 2};
        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(2, result.val);
        assertNotNull(result.left);
        assertEquals(1, result.left.val);
        assertNull(result.right);
    }

    @Test
    @DisplayName("Should build tree with two nodes - right child only")
    void testTwoNodesRightChild() {
        //   1
        //    \
        //     2
        int[] preorder = {1, 2};
        int[] inorder = {1, 2};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(1, result.val);
        assertNull(result.left);
        assertNotNull(result.right);
        assertEquals(2, result.right.val);
    }

    @Test
    @DisplayName("Should build balanced tree with three nodes")
    void testBalancedThreeNodes() {
        //     2
        //    / \
        //   1   3
        int[] preorder = {2, 1, 3};
        int[] inorder = {1, 2, 3};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(2, result.val);
        assertNotNull(result.left);
        assertEquals(1, result.left.val);
        assertNotNull(result.right);
        assertEquals(3, result.right.val);
    }

    @Test
    @DisplayName("Should build LeetCode example tree")
    void testLeetCodeExample() {
        //       3
        //      / \
        //     9  20
        //       /  \
        //      15   7
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(3, result.val);

        // Left subtree
        assertNotNull(result.left);
        assertEquals(9, result.left.val);
        assertNull(result.left.left);
        assertNull(result.left.right);

        // Right subtree
        assertNotNull(result.right);
        assertEquals(20, result.right.val);
        assertNotNull(result.right.left);
        assertEquals(15, result.right.left.val);
        assertNotNull(result.right.right);
        assertEquals(7, result.right.right.val);
    }

    @Test
    @DisplayName("Should build left-skewed tree")
    void testLeftSkewedTree() {
        //       4
        //      /
        //     3
        //    /
        //   2
        //  /
        // 1
        int[] preorder = {4, 3, 2, 1};
        int[] inorder = {1, 2, 3, 4};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(4, result.val);
        assertNull(result.right);

        assertEquals(3, result.left.val);
        assertNull(result.left.right);

        assertEquals(2, result.left.left.val);
        assertNull(result.left.left.right);

        assertEquals(1, result.left.left.left.val);
    }

    @Test
    @DisplayName("Should build right-skewed tree")
    void testRightSkewedTree() {
        // 1
        //  \
        //   2
        //    \
        //     3
        //      \
        //       4
        int[] preorder = {1, 2, 3, 4};
        int[] inorder = {1, 2, 3, 4};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(1, result.val);
        assertNull(result.left);

        assertEquals(2, result.right.val);
        assertNull(result.right.left);

        assertEquals(3, result.right.right.val);
        assertNull(result.right.right.left);

        assertEquals(4, result.right.right.right.val);
    }

    @Test
    @DisplayName("Should build complete binary tree")
    void testCompleteBinaryTree() {
        //        1
        //       / \
        //      2   3
        //     / \ / \
        //    4  5 6  7
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder = {4, 2, 5, 1, 6, 3, 7};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(1, result.val);

        // Level 2
        assertEquals(2, result.left.val);
        assertEquals(3, result.right.val);

        // Level 3
        assertEquals(4, result.left.left.val);
        assertEquals(5, result.left.right.val);
        assertEquals(6, result.right.left.val);
        assertEquals(7, result.right.right.val);
    }

    @Test
    @DisplayName("Should build asymmetric tree")
    void testAsymmetricTree() {
        //      1
        //     / \
        //    2   3
        //   /     \
        //  4       5
        int[] preorder = {1, 2, 4, 3, 5};
        int[] inorder = {4, 2, 1, 3, 5};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(1, result.val);

        assertEquals(2, result.left.val);
        assertEquals(4, result.left.left.val);
        assertNull(result.left.right);

        assertEquals(3, result.right.val);
        assertNull(result.right.left);
        assertEquals(5, result.right.right.val);
    }

    @Test
    @DisplayName("Should handle negative values")
    void testNegativeValues() {
        //     -1
        //     / \
        //   -2  -3
        int[] preorder = {-1, -2, -3};
        int[] inorder = {-2, -1, -3};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(-1, result.val);
        assertEquals(-2, result.left.val);
        assertEquals(-3, result.right.val);
    }

    @Test
    @DisplayName("Should handle tree with zero value")
    void testZeroValue() {
        //     0
        //    / \
        //   1   2
        int[] preorder = {0, 1, 2};
        int[] inorder = {1, 0, 2};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(0, result.val);
        assertEquals(1, result.left.val);
        assertEquals(2, result.right.val);
    }

    @Test
    @DisplayName("Should build larger tree correctly")
    void testLargerTree() {
        //          5
        //        /   \
        //       3     8
        //      / \   / \
        //     2   4 7   9
        //    /     /
        //   1     6
        int[] preorder = {5, 3, 2, 1, 4, 8, 7, 6, 9};
        int[] inorder = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        TreeNode result = reconstructor.constructBinaryTree(preorder, inorder);

        assertNotNull(result);
        assertEquals(5, result.val);

        // Left subtree
        assertEquals(3, result.left.val);
        assertEquals(2, result.left.left.val);
        assertEquals(1, result.left.left.left.val);
        assertEquals(4, result.left.right.val);

        // Right subtree
        assertEquals(8, result.right.val);
        assertEquals(7, result.right.left.val);
        assertEquals(6, result.right.left.left.val);
        assertEquals(9, result.right.right.val);
    }
}