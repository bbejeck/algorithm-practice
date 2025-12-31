package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the InsertBst class.
 */
public class InsertBstTest {

    private InsertBst insertBst;

    @BeforeEach
    public void setUp() {
        insertBst = new InsertBst();
    }

    @Test
    public void testInsertIntoNullTree() {
        // Inserting into an empty tree should create a new root
        TreeNode<Integer> result = insertBst.insert(null, 5);

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNull(result.left);
        assertNull(result.right);
    }

    @Test
    public void testInsertLeftChild() {
        // Insert a smaller value, should go to the left
        TreeNode<Integer> root = new TreeNode<Integer>(10);
        TreeNode<Integer> result = insertBst.insert(root, 5);

        assertSame(root, result);
        assertNotNull(result.left);
        assertEquals(5, result.left.val);
        assertNull(result.right);
    }

    @Test
    public void testInsertRightChild() {
        // Insert a larger value, should go to the right
        TreeNode<Integer> root = new TreeNode<Integer>(10);
        TreeNode<Integer> result = insertBst.insert(root, 15);

        assertSame(root, result);
        assertNotNull(result.right);
        assertEquals(15, result.right.val);
        assertNull(result.left);
    }

    @Test
    public void testInsertDuplicateValue() {
        // Inserting a duplicate value should not modify the tree
        TreeNode<Integer> root = new TreeNode<Integer>(10);
        TreeNode<Integer> result = insertBst.insert(root, 10);

        assertSame(root, result);
        assertNull(result.left);
        assertNull(result.right);
    }

    @Test
    public void testInsertMultipleValues() {
        // Build a tree by inserting multiple values
        //        10
        //       /  \
        //      5   15
        //     /     \
        //    3      20
        TreeNode<Integer> root = insertBst.insert(null, 10);
        insertBst.insert(root, 5);
        insertBst.insert(root, 15);
        insertBst.insert(root, 3);
        insertBst.insert(root, 20);

        assertEquals(10, root.val);
        assertEquals(5, root.left.val);
        assertEquals(15, root.right.val);
        assertEquals(3, root.left.left.val);
        assertEquals(20, root.right.right.val);
    }

    @Test
    public void testInsertMaintainsBstProperty() {
        // Insert values and verify BST property is maintained
        TreeNode<Integer> root = insertBst.insert(null, 50);
        insertBst.insert(root, 30);
        insertBst.insert(root, 70);
        insertBst.insert(root, 20);
        insertBst.insert(root, 40);
        insertBst.insert(root, 60);
        insertBst.insert(root, 80);

        assertTrue(isValidBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testInsertDeepLeftChain() {
        // Insert descending values, creating a left-skewed tree
        TreeNode<Integer> root = insertBst.insert(null, 50);
        insertBst.insert(root, 40);
        insertBst.insert(root, 30);
        insertBst.insert(root, 20);

        assertEquals(50, root.val);
        assertEquals(40, root.left.val);
        assertEquals(30, root.left.left.val);
        assertEquals(20, root.left.left.left.val);
    }

    @Test
    public void testInsertDeepRightChain() {
        // Insert ascending values, creating a right-skewed tree
        TreeNode<Integer> root = insertBst.insert(null, 10);
        insertBst.insert(root, 20);
        insertBst.insert(root, 30);
        insertBst.insert(root, 40);

        assertEquals(10, root.val);
        assertEquals(20, root.right.val);
        assertEquals(30, root.right.right.val);
        assertEquals(40, root.right.right.right.val);
    }

    @Test
    public void testInsertIntoExistingTree() {
        //        10              10
        //       /  \    =>      /  \
        //      5   15          5   15
        //                     /
        //                    3
        TreeNode<Integer> root = new TreeNode<Integer>(10,
                new TreeNode<Integer>(5),
                new TreeNode<Integer>(15));

        insertBst.insert(root, 3);

        assertEquals(3, root.left.left.val);
    }

    @Test
    public void testInsertBetweenValues() {
        //        10              10
        //       /  \    =>      /  \
        //      5   15          5   15
        //     /               / \
        //    3               3   7
        TreeNode<Integer> root = new TreeNode<Integer>(10,
                new TreeNode<Integer>(5, new TreeNode<Integer>(3), null),
                new TreeNode<Integer>(15));

        insertBst.insert(root, 7);

        assertEquals(7, root.left.right.val);
    }

    // Helper method to verify BST property
    private boolean isValidBST(TreeNode<Integer> node, int min, int max) {
        if (node == null) {
            return true;
        }
        if (node.val <= min || node.val >= max) {
            return false;
        }
        return isValidBST(node.left, min, node.val) && isValidBST(node.right, node.val, max);
    }
}