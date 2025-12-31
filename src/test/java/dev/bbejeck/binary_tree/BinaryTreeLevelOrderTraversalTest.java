package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeLevelOrderTraversalTest {

    private BinaryTreeLevelOrderTraversal traversal;

    @BeforeEach
    void setUp() {
        traversal = new BinaryTreeLevelOrderTraversal();
    }

    @Test
    void testLevelOrderWithNullRoot() {
        List<List<Integer>> result = traversal.levelOrder(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLevelOrderWithSingleNode() {
        TreeNode<Integer> root = new TreeNode<>(1);
        List<List<Integer>> result = traversal.levelOrder(root);

        assertEquals(1, result.size());
        assertEquals(List.of(1), result.get(0));
    }

    @Test
    void testLevelOrderWithCompleteTree() {
        // Create a tree:
        //     1
        //    / \
        //   2   3
        //  / \ / \
        // 4  5 6  7
        TreeNode<Integer> root = new TreeNode<>(1);
        root.left = new TreeNode<>(2);
        root.right = new TreeNode<>(3);
        root.left.left = new TreeNode<>(4);
        root.left.right = new TreeNode<>(5);
        root.right.left = new TreeNode<>(6);
        root.right.right = new TreeNode<>(7);

        List<List<Integer>> result = traversal.levelOrder(root);

        assertEquals(3, result.size());
        assertEquals(List.of(1), result.get(0));
        assertEquals(List.of(2, 3), result.get(1));
        assertEquals(List.of(4, 5, 6, 7), result.get(2));
    }

    @Test
    void testLevelOrderWithUnbalancedTree() {
        // Create a tree:
        //     1
        //    /
        //   2
        //  /
        // 3
        TreeNode<Integer> root = new TreeNode<>(1);
        root.left = new TreeNode<>(2);
        root.left.left = new TreeNode<>(3);

        List<List<Integer>> result = traversal.levelOrder(root);

        assertEquals(3, result.size());
        assertEquals(List.of(1), result.get(0));
        assertEquals(List.of(2), result.get(1));
        assertEquals(List.of(3), result.get(2));
    }

    @Test
    void testLevelOrderWithRightSkewedTree() {
        // Create a tree:
        //   1
        //    \
        //     2
        //      \
        //       3
        TreeNode<Integer> root = new TreeNode<>(1);
        root.right = new TreeNode<>(2);
        root.right.right = new TreeNode<>(3);

        List<List<Integer>> result = traversal.levelOrder(root);

        assertEquals(3, result.size());
        assertEquals(List.of(1), result.get(0));
        assertEquals(List.of(2), result.get(1));
        assertEquals(List.of(3), result.get(2));
    }


}