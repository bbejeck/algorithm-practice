package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaxDepthOfTreeTest {

    private MaxDepthOfTree maxDepthCalculator;

    @BeforeEach
    void setUp() {
        maxDepthCalculator = new MaxDepthOfTree();
    }

    @Test
    @DisplayName("Test null root - should return 0")
    void testNullRoot() {
        assertEquals(0, maxDepthCalculator.maxDepth(null));
    }

    @Test
    @DisplayName("Test single node tree - should return 1")
    void testSingleNode() {
        TreeNode root = new TreeNode(1);
        assertEquals(1, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test two level tree - left child only")
    void testTwoLevelLeftOnly() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        assertEquals(2, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test two level tree - right child only")
    void testTwoLevelRightOnly() {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        assertEquals(2, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test two level tree - both children")
    void testTwoLevelBothChildren() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        assertEquals(2, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test three level balanced tree")
    void testThreeLevelBalanced() {
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        assertEquals(3, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test left-skewed tree")
    void testLeftSkewedTree() {
        //     1
        //    /
        //   2
        //  /
        // 3
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(3);
        assertEquals(3, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test right-skewed tree")
    void testRightSkewedTree() {
        // 1
        //  \
        //   2
        //    \
        //     3
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.right = new TreeNode(3);
        assertEquals(3, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test unbalanced tree - deeper left subtree")
    void testUnbalancedDeeperLeft() {
        //       1
        //      / \
        //     2   3
        //    /
        //   4
        //  /
        // 5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.left.left = new TreeNode(5);
        assertEquals(4, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test unbalanced tree - deeper right subtree")
    void testUnbalancedDeeperRight() {
        //     1
        //    / \
        //   2   3
        //        \
        //         4
        //          \
        //           5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.right = new TreeNode(4);
        root.right.right.right = new TreeNode(5);
        assertEquals(4, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test complex unbalanced tree")
    void testComplexUnbalanced() {
        //         1
        //       /   \
        //      2     3
        //     / \   /
        //    4   5 6
        //   /     /
        //  7     8
        //       /
        //      9
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.left.left.left = new TreeNode(7);
        root.right.left.left = new TreeNode(8);
        root.right.left.left.left = new TreeNode(9);
        assertEquals(5, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test LeetCode example 1: [3,9,20,null,null,15,7]")
    void testLeetCodeExample1() {
        //     3
        //    / \
        //   9  20
        //     /  \
        //    15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        assertEquals(3, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test LeetCode example 2: [1,null,2]")
    void testLeetCodeExample2() {
        // 1
        //  \
        //   2
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        assertEquals(2, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test deep left chain")
    void testDeepLeftChain() {
        TreeNode root = new TreeNode(1);
        TreeNode current = root;

        // Create a chain of 10 nodes going left
        for (int i = 2; i <= 10; i++) {
            current.left = new TreeNode(i);
            current = current.left;
        }

        assertEquals(10, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test deep right chain")
    void testDeepRightChain() {
        TreeNode root = new TreeNode(1);
        TreeNode current = root;

        // Create a chain of 15 nodes going right
        for (int i = 2; i <= 15; i++) {
            current.right = new TreeNode(i);
            current = current.right;
        }

        assertEquals(15, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test perfect binary tree depth 4")
    void testPerfectBinaryTreeDepth4() {
        //           1
        //       /       \
        //      2         3
        //    /   \     /   \
        //   4     5   6     7
        //  / \   / \ / \   / \
        // 8   9 10 11 12 13 14 15
        TreeNode root = new TreeNode(1);

        // Level 2
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);

        // Level 3
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        // Level 4
        root.left.left.left = new TreeNode(8);
        root.left.left.right = new TreeNode(9);
        root.left.right.left = new TreeNode(10);
        root.left.right.right = new TreeNode(11);
        root.right.left.left = new TreeNode(12);
        root.right.left.right = new TreeNode(13);
        root.right.right.left = new TreeNode(14);
        root.right.right.right = new TreeNode(15);

        assertEquals(4, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test tree with negative values")
    void testTreeWithNegativeValues() {
        TreeNode root = new TreeNode(-1);
        root.left = new TreeNode(-2);
        root.right = new TreeNode(-3);
        root.left.left = new TreeNode(-4);
        assertEquals(3, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test tree with zero values")
    void testTreeWithZeroValues() {
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(0);
        root.right = new TreeNode(0);
        assertEquals(2, maxDepthCalculator.maxDepth(root));
    }

    @Test
    @DisplayName("Test recursiveCount method directly")
    void testRecursiveCountMethodDirectly() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);

        // Test that recursiveCount works correctly when called directly
        assertEquals(2, maxDepthCalculator.recursiveCount(root, 0));
        assertEquals(0, maxDepthCalculator.recursiveCount(null, 0));
        assertEquals(5, maxDepthCalculator.recursiveCount(root, 3)); // Starting with depth 3
    }
}