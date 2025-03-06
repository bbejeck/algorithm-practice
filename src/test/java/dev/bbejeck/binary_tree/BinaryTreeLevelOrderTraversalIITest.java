package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeLevelOrderTraversalIITest {

    private BinaryTreeLevelOrderTraversalII traversal;

    @BeforeEach
    void setUp() {
        traversal = new BinaryTreeLevelOrderTraversalII();
    }

    @Test
    void testNullRoot() {
        List<List<Integer>> result = traversal.levelOrderBottom(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleNode() {
        TreeNode root = new TreeNode(1);
        List<List<Integer>> result = traversal.levelOrderBottom(root);

        assertEquals(1, result.size());
        assertEquals(List.of(1), result.get(0));
    }

    @Test
    void testTwoLevelTree() {
        /*
            1
           / \
          2   3
        */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);

        List<List<Integer>> result = traversal.levelOrderBottom(root);

        assertEquals(2, result.size());
        assertEquals(List.of(2, 3), result.get(0)); // Bottom level first
        assertEquals(List.of(1), result.get(1));    // Root level last
    }

    @Test
    void testCompleteBinaryTree() {
        /*
             1
            / \
           2   3
          / \ / \
         4  5 6  7
        */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        List<List<Integer>> result = traversal.levelOrderBottom(root);

        assertEquals(3, result.size());
        assertEquals(List.of(4, 5, 6, 7), result.get(0)); // Bottom level
        assertEquals(List.of(2, 3), result.get(1));       // Middle level
        assertEquals(List.of(1), result.get(2));          // Top level
    }

    @Test
    void testUnbalancedTree() {
        /*
             1
            / \
           2   3
          /     \
         4       6
        */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.right.right = new TreeNode(6);

        List<List<Integer>> result = traversal.levelOrderBottom(root);

        assertEquals(3, result.size());
        assertEquals(List.of(4, 6), result.get(0));    // Bottom level
        assertEquals(List.of(2, 3), result.get(1));    // Middle level
        assertEquals(List.of(1), result.get(2));       // Top level
    }

    @Test
    void testLeftSkewedTree() {
        /*
            1
           /
          2
         /
        3
        */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(3);

        List<List<Integer>> result = traversal.levelOrderBottom(root);

        assertEquals(3, result.size());
        assertEquals(List.of(3), result.get(0));    // Bottom level
        assertEquals(List.of(2), result.get(1));    // Middle level
        assertEquals(List.of(1), result.get(2));    // Top level
    }

    @Test
    void testRightSkewedTree() {
        /*
        1
         \
          2
           \
            3
        */
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.right = new TreeNode(3);

        List<List<Integer>> result = traversal.levelOrderBottom(root);

        assertEquals(3, result.size());
        assertEquals(List.of(3), result.get(0));    // Bottom level
        assertEquals(List.of(2), result.get(1));    // Middle level
        assertEquals(List.of(1), result.get(2));    // Top level
    }
}