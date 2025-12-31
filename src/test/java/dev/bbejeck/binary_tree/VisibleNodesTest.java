package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VisibleNodesTest {

    private VisibleNodes visibleNodes;

    @BeforeEach
    void setUp() {
        visibleNodes = new VisibleNodes();
    }

    @Test
    @DisplayName("Test null root - should return 0")
    void testNullRoot() {
        assertEquals(0, visibleNodes.visibleNodes(null));
    }

    @Test
    @DisplayName("Test single node tree - should return 1")
    void testSingleNode() {
        TreeNode<Integer> root = new TreeNode<Integer>(5);
        assertEquals(1, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test all increasing values from root - all visible")
    void testAllIncreasing() {
        //       1
        //      / \
        //     2   3
        //    /     \
        //   4       5
        TreeNode<Integer> root = new TreeNode<Integer>(1);
        root.left = new TreeNode<Integer>(2);
        root.right = new TreeNode<Integer>(3);
        root.left.left = new TreeNode<Integer>(4);
        root.right.right = new TreeNode<Integer>(5);

        // All nodes are visible: 1->2->4 (all increasing), 1->3->5 (all increasing)
        assertEquals(5, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test all decreasing values from root - only root visible")
    void testAllDecreasing() {
        //       5
        //      / \
        //     3   4
        //    /     \
        //   1       2
        TreeNode<Integer> root = new TreeNode<Integer>(5);
        root.left = new TreeNode<Integer>(3);
        root.right = new TreeNode<Integer>(4);
        root.left.left = new TreeNode<Integer>(1);
        root.right.right = new TreeNode<Integer>(2);

        // Only root is visible since all children are smaller
        assertEquals(1, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test mixed values - partial visibility")
    void testMixedValues() {
        //       5
        //      / \
        //     3   10
        //    /     \
        //   8       6
        TreeNode<Integer> root = new TreeNode<Integer>(5);
        root.left = new TreeNode<Integer>(3);
        root.right = new TreeNode<Integer>(10);
        root.left.left = new TreeNode<Integer>(8);
        root.right.right = new TreeNode<Integer>(6);

        // Visible: 5 (root), 8 (5->3->8, 8 > 5), 10 (5->10, 10 > 5)
        // Not visible: 3 (3 < 5), 6 (6 < 10)
        assertEquals(3, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test left-skewed tree with increasing values")
    void testLeftSkewedIncreasing() {
        //     1
        //    /
        //   2
        //  /
        // 3
        TreeNode<Integer> root = new TreeNode<Integer>(1);
        root.left = new TreeNode<Integer>(2);
        root.left.left = new TreeNode<Integer>(3);

        assertEquals(3, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test right-skewed tree with decreasing values")
    void testRightSkewedDecreasing() {
        // 5
        //  \
        //   4
        //    \
        //     3
        TreeNode<Integer> root = new TreeNode<Integer>(5);
        root.right = new TreeNode<Integer>(4);
        root.right.right = new TreeNode<Integer>(3);

        // Only root is visible
        assertEquals(1, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test with equal values - all should be visible")
    void testEqualValues() {
        //       5
        //      / \
        //     5   5
        //    /     \
        //   5       5
        TreeNode<Integer> root = new TreeNode<Integer>(5);
        root.left = new TreeNode<Integer>(5);
        root.right = new TreeNode<Integer>(5);
        root.left.left = new TreeNode<Integer>(5);
        root.right.right = new TreeNode<Integer>(5);

        // All nodes visible because >= is used (equal values count as visible)
        assertEquals(5, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test with negative values")
    void testNegativeValues() {
        //       -5
        //      /  \
        //    -3    -10
        //    /       \
        //  -1        -8
        TreeNode<Integer> root = new TreeNode<Integer>(-5);
        root.left = new TreeNode<Integer>(-3);
        root.right = new TreeNode<Integer>(-10);
        root.left.left = new TreeNode<Integer>(-1);
        root.right.right = new TreeNode<Integer>(-8);

        // Visible: -5 (root), -3 (-3 > -5), -1 (-1 > -3)
        // Not visible: -10 (-10 < -5), -8 (-8 < -5)
        assertEquals(3, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test complex tree")
    void testComplexTree() {
        //          8
        //        /   \
        //       3     10
        //      / \      \
        //     1   6      14
        //        / \     /
        //       4   7   13
        TreeNode<Integer> root = new TreeNode<Integer>(8);
        root.left = new TreeNode<Integer>(3);
        root.right = new TreeNode<Integer>(10);
        root.left.left = new TreeNode<Integer>(1);
        root.left.right = new TreeNode<Integer>(6);
        root.right.right = new TreeNode<Integer>(14);
        root.left.right.left = new TreeNode<Integer>(4);
        root.left.right.right = new TreeNode<Integer>(7);
        root.right.right.left = new TreeNode<Integer>(13);

        // Visible: 8 (root), 10 (10 > 8), 14 (14 > 10), 13 (13 > 10)
        // Not visible: 3 (3 < 8), 1 (1 < 8), 6 (6 < 8), 4 (4 < 8), 7 (7 < 8)
        assertEquals(3, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test two level tree - both children visible")
    void testTwoLevelBothVisible() {
        //     1
        //    / \
        //   5   3
        TreeNode<Integer> root = new TreeNode<Integer>(1);
        root.left = new TreeNode<Integer>(5);
        root.right = new TreeNode<Integer>(3);

        // All visible: 1 (root), 5 > 1, 3 > 1
        assertEquals(3, visibleNodes.visibleNodes(root));
    }

    @Test
    @DisplayName("Test Integer.MIN_VALUE at root")
    void testMinValueAtRoot() {
        TreeNode<Integer> root = new TreeNode<Integer>(Integer.MIN_VALUE);
        root.left = new TreeNode<Integer>(0);
        root.right = new TreeNode<Integer>(-100);

        // All are visible since root is MIN_VALUE
        assertEquals(3, visibleNodes.visibleNodes(root));
    }
}