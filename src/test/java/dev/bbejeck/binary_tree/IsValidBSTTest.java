package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the IsValidBST class.
 */
public class IsValidBSTTest {

    private IsValidBST isValidBST;

    @BeforeEach
    public void setUp() {
        isValidBST = new IsValidBST();
    }

    @Test
    public void testNullTree() {
        // An empty tree is a valid BST
        assertTrue(isValidBST.valid(null));
    }

    @Test
    public void testSingleNode() {
        // A single node tree is always a valid BST
        TreeNode<Integer> root = new TreeNode<Integer>(5);
        assertTrue(isValidBST.valid(root));
    }

    @Test
    public void testValidBSTTwoLevels() {
        //       5
        //      / \
        //     3   7
        TreeNode<Integer> root = new TreeNode<Integer>(5,
                new TreeNode<Integer>(3),
                new TreeNode<Integer>(7));
        assertTrue(isValidBST.valid(root));
    }

    @Test
    public void testValidBSTThreeLevels() {
        //        10
        //       /  \
        //      5   15
        //     / \    \
        //    3   7   20
        TreeNode<Integer> root = new TreeNode<Integer>(10,
                new TreeNode<Integer>(5, new TreeNode<Integer>(3), new TreeNode<Integer>(7)),
                new TreeNode<Integer>(15, null, new TreeNode<Integer>(20)));
        assertTrue(isValidBST.valid(root));
    }

    @Test
    public void testInvalidBSTLeftChildGreaterThanRoot() {
        //       5
        //      / \
        //     8   7
        TreeNode<Integer> root = new TreeNode<Integer>(5,
                new TreeNode<Integer>(8),
                new TreeNode<Integer>(7));
        assertFalse(isValidBST.valid(root));
    }

    @Test
    public void testInvalidBSTRightChildLessThanRoot() {
        //       5
        //      / \
        //     3   2
        TreeNode<Integer> root = new TreeNode<Integer>(5,
                new TreeNode<Integer>(3),
                new TreeNode<Integer>(2));
        assertFalse(isValidBST.valid(root));
    }

    @Test
    public void testInvalidBSTDeeperViolation() {
        // The subtree is valid locally but violates BST property globally
        //        10
        //       /  \
        //      5   15
        //         /  \
        //        6   20
        // Node 6 is less than 10, so it cannot be in the right subtree
        TreeNode<Integer> root = new TreeNode<Integer>(10,
                new TreeNode<Integer>(5),
                new TreeNode<Integer>(15, new TreeNode<Integer>(6), new TreeNode<Integer>(20)));
        assertFalse(isValidBST.valid(root));
    }

    @Test
    public void testInvalidBSTLeftSubtreeViolation() {
        //        10
        //       /  \
        //      5   15
        //     / \
        //    3  12
        // Node 12 is greater than 10, so it cannot be in the left subtree
        TreeNode<Integer> root = new TreeNode<Integer>(10,
                new TreeNode<Integer>(5, new TreeNode<Integer>(3), new TreeNode<Integer>(12)),
                new TreeNode<Integer>(15));
        assertFalse(isValidBST.valid(root));
    }

    @Test
    public void testValidBSTLeftOnlyChain() {
        //     5
        //    /
        //   3
        //  /
        // 1
        TreeNode<Integer> root = new TreeNode<Integer>(5,
                new TreeNode<Integer>(3, new TreeNode<Integer>(1), null),
                null);
        assertTrue(isValidBST.valid(root));
    }

    @Test
    public void testValidBSTRightOnlyChain() {
        // 5
        //  \
        //   7
        //    \
        //     9
        TreeNode<Integer> root = new TreeNode<Integer>(5,
                null,
                new TreeNode<Integer>(7, null, new TreeNode<Integer>(9)));
        assertTrue(isValidBST.valid(root));
    }

    @Test
    public void testInvalidBSTEqualValues() {
        // BST should have strictly greater values on right, strictly lesser on left
        //       5
        //      / \
        //     5   7
        TreeNode<Integer> root = new TreeNode<Integer>(5,
                new TreeNode<Integer>(5),
                new TreeNode<Integer>(7));
        assertFalse(isValidBST.valid(root));
    }

    @Test
    public void testInvalidBSTEqualValuesRight() {
        //       5
        //      / \
        //     3   5
        TreeNode<Integer> root = new TreeNode<Integer>(5,
                new TreeNode<Integer>(3),
                new TreeNode<Integer>(5));
        assertFalse(isValidBST.valid(root));
    }
}