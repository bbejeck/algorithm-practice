package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BalancedTree class
 */
class BalancedTreeTest {

    private BalancedTree balancedTree;

    @BeforeEach
    void setUp() {
        balancedTree = new BalancedTree();
    }

    @Test
    @DisplayName("Should return true for null tree")
    void testNullTree() {
        assertTrue(balancedTree.isBalanced(null));
    }

    @Test
    @DisplayName("Should return true for single node tree")
    void testSingleNode() {
        TreeNode<Integer> root = new TreeNode<>(1);
        assertTrue(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return true for balanced tree with two levels")
    void testBalancedTwoLevels() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2),
                new TreeNode<>(3));
        assertTrue(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return true for balanced tree with three levels")
    void testBalancedThreeLevels() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2,
                        new TreeNode<>(4),
                        new TreeNode<>(5)),
                new TreeNode<>(3,
                        new TreeNode<>(6),
                        new TreeNode<>(7)));
        assertTrue(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return false for unbalanced tree - left heavy")
    void testUnbalancedLeftHeavy() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2,
                        new TreeNode<>(3,
                                new TreeNode<>(4),
                                null),
                        null),
                null);
        assertFalse(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return false for unbalanced tree - right heavy")
    void testUnbalancedRightHeavy() {
        TreeNode<Integer> root = new TreeNode<>(1,
                null,
                new TreeNode<>(2,
                        null,
                        new TreeNode<>(3,
                                null,
                                new TreeNode<>(4))));
        assertFalse(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return true for tree with height difference of 1")
    void testHeightDifferenceOfOne() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2,
                        new TreeNode<>(4),
                        null),
                new TreeNode<>(3));
        assertTrue(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return false for tree with height difference of 2")
    void testHeightDifferenceOfTwo() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2,
                        new TreeNode<>(3,
                                new TreeNode<>(4),
                                null),
                        null),
                new TreeNode<>(5));
        assertFalse(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return true for tree with only left child at depth 1")
    void testOnlyLeftChild() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2),
                null);
        assertTrue(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return true for tree with only right child at depth 1")
    void testOnlyRightChild() {
        TreeNode<Integer> root = new TreeNode<>(1,
                null,
                new TreeNode<>(2));
        assertTrue(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return false for unbalanced subtree")
    void testUnbalancedSubtree() {
        // Root looks balanced but left subtree is unbalanced
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2,
                        new TreeNode<>(4,
                                new TreeNode<>(6),
                                null),
                        null),
                new TreeNode<>(3,
                        new TreeNode<>(5),
                        null));
        assertFalse(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return true for perfect binary tree")
    void testPerfectBinaryTree() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2,
                        new TreeNode<>(4),
                        new TreeNode<>(5)),
                new TreeNode<>(3,
                        new TreeNode<>(6),
                        new TreeNode<>(7)));
        assertTrue(balancedTree.isBalanced(root));
    }

    @Test
    @DisplayName("Should return true for complete binary tree")
    void testCompleteBinaryTree() {
        TreeNode<Integer> root = new TreeNode<>(1,
                new TreeNode<>(2,
                        new TreeNode<>(4),
                        new TreeNode<>(5)),
                new TreeNode<>(3,
                        new TreeNode<>(6),
                        null));
        assertTrue(balancedTree.isBalanced(root));
    }
}