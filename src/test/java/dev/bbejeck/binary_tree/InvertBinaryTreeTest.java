package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InvertBinaryTree class
 */
class InvertBinaryTreeTest {

    private InvertBinaryTree invertBinaryTree;

    @BeforeEach
    void setUp() {
        invertBinaryTree = new InvertBinaryTree();
    }

    @Test
    @DisplayName("Should return null for null tree")
    void testNullTree() {
        assertNull(invertBinaryTree.invertTree(null));
    }

    @Test
    @DisplayName("Should return same node for single node tree")
    void testSingleNode() {
        TreeNode root = new TreeNode(1);
        TreeNode result = invertBinaryTree.invertTree(root);

        assertNotNull(result);
        assertEquals(1, result.val);
        assertNull(result.left);
        assertNull(result.right);
    }

    @Test
    @DisplayName("Should invert tree with two levels")
    void testTwoLevels() {
        //     1           1
        //    / \   =>    / \
        //   2   3       3   2
        TreeNode root = new TreeNode(1,
                new TreeNode(2),
                new TreeNode(3));

        TreeNode result = invertBinaryTree.invertTree(root);

        assertEquals(1, result.val);
        assertEquals(3, result.left.val);
        assertEquals(2, result.right.val);
    }

    @Test
    @DisplayName("Should invert tree with three levels")
    void testThreeLevels() {
        //       4               4
        //      / \             / \
        //     2   7    =>     7   2
        //    / \ / \         / \ / \
        //   1  3 6  9       9  6 3  1
        TreeNode root = new TreeNode(4,
                new TreeNode(2,
                        new TreeNode(1),
                        new TreeNode(3)),
                new TreeNode(7,
                        new TreeNode(6),
                        new TreeNode(9)));

        TreeNode result = invertBinaryTree.invertTree(root);

        assertEquals(4, result.val);
        assertEquals(7, result.left.val);
        assertEquals(2, result.right.val);
        assertEquals(9, result.left.left.val);
        assertEquals(6, result.left.right.val);
        assertEquals(3, result.right.left.val);
        assertEquals(1, result.right.right.val);
    }

    @Test
    @DisplayName("Should invert tree with only left children")
    void testOnlyLeftChildren() {
        //   1           1
        //  /             \
        // 2       =>      2
        //  \             /
        //   3           3
        TreeNode root = new TreeNode(1,
                new TreeNode(2,
                        null,
                        new TreeNode(3)),
                null);

        TreeNode result = invertBinaryTree.invertTree(root);

        assertEquals(1, result.val);
        assertNull(result.left);
        assertNotNull(result.right);
        assertEquals(2, result.right.val);
        assertEquals(3, result.right.left.val);
        assertNull(result.right.right);
    }

    @Test
    @DisplayName("Should invert tree with only right children")
    void testOnlyRightChildren() {
        //   1           1
        //    \         /
        //     2  =>   2
        //      \       \
        //       3       3
        TreeNode root = new TreeNode(1,
                null,
                new TreeNode(2,
                        null,
                        new TreeNode(3)));

        TreeNode result = invertBinaryTree.invertTree(root);

        assertEquals(1, result.val);
        assertNotNull(result.left);
        assertNull(result.right);
        assertEquals(2, result.left.val);
        assertEquals(3, result.left.left.val);
        assertNull(result.left.right);
    }

    @Test
    @DisplayName("Should invert asymmetric tree")
    void testAsymmetricTree() {
        //     1               1
        //    / \             / \
        //   2   3    =>     3   2
        //  /     \         /     \
        // 4       5       5       4
        TreeNode root = new TreeNode(1,
                new TreeNode(2,
                        new TreeNode(4),
                        null),
                new TreeNode(3,
                        null,
                        new TreeNode(5)));

        TreeNode result = invertBinaryTree.invertTree(root);

        assertEquals(1, result.val);
        assertEquals(3, result.left.val);
        assertEquals(2, result.right.val);
        assertEquals(5, result.left.left.val);
        assertNull(result.left.right);
        assertNull(result.right.left);
        assertEquals(4, result.right.right.val);
    }

    @Test
    @DisplayName("Should invert and then invert again to get original tree")
    void testDoubleInversion() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2,
                        new TreeNode(4),
                        new TreeNode(5)),
                new TreeNode(3,
                        new TreeNode(6),
                        new TreeNode(7)));

        TreeNode inverted = invertBinaryTree.invertTree(root);
        TreeNode doubleInverted = invertBinaryTree.invertTree(inverted);

        // After double inversion, should be back to original structure
        assertEquals(1, doubleInverted.val);
        assertEquals(2, doubleInverted.left.val);
        assertEquals(3, doubleInverted.right.val);
        assertEquals(4, doubleInverted.left.left.val);
        assertEquals(5, doubleInverted.left.right.val);
        assertEquals(6, doubleInverted.right.left.val);
        assertEquals(7, doubleInverted.right.right.val);
    }

    @Test
    @DisplayName("Should return the same root node reference")
    void testReturnsSameRootReference() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2),
                new TreeNode(3));

        TreeNode result = invertBinaryTree.invertTree(root);

        assertSame(root, result);
    }

    @Test
    @DisplayName("Should handle tree with negative values")
    void testNegativeValues() {
        TreeNode root = new TreeNode(-1,
                new TreeNode(-2),
                new TreeNode(-3));

        TreeNode result = invertBinaryTree.invertTree(root);

        assertEquals(-1, result.val);
        assertEquals(-3, result.left.val);
        assertEquals(-2, result.right.val);
    }

    @Test
    @DisplayName("Should handle tree with zero value")
    void testZeroValue() {
        TreeNode root = new TreeNode(0,
                new TreeNode(1),
                new TreeNode(2));

        TreeNode result = invertBinaryTree.invertTree(root);

        assertEquals(0, result.val);
        assertEquals(2, result.left.val);
        assertEquals(1, result.right.val);
    }
}