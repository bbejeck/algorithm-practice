package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BinaryTreeZigZagLevelTraversalTest {

    private BinaryTreeZigZagLevelTraversal traversal;

    @BeforeEach
    void setUp() {
        traversal = new BinaryTreeZigZagLevelTraversal();
    }

    @Test
    void testZigzagTraversal() {
        // Create tree:
        //      3
        //     / \
        //    9  20
        //       / \
        //      15  7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        List<List<Integer>> result = traversal.zigzagLevelOrder(root);

        assertEquals(3, result.size(), "Should have three levels");
        assertEquals(List.of(3), result.get(0), "Level 1: left to right");
        assertEquals(List.of(9, 20), result.get(1), "Level 2: left to right");
        assertEquals(List.of(7, 15), result.get(2), "Level 3: right to left");
    }

    @Test
    void testEmptyTree() {
        List<List<Integer>> result = traversal.zigzagLevelOrder(null);
        assertTrue(result.isEmpty(), "Empty tree should return empty list");
    }

    @Test
    void testSingleNodeTree() {
        TreeNode root = new TreeNode(1);
        List<List<Integer>> result = traversal.zigzagLevelOrder(root);

        assertEquals(1, result.size(), "Should have one level");
        assertEquals(List.of(1), result.get(0), "Should contain only root value");
    }

    @Test
    void testFullZigzagPattern() {
        // Create tree:
        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6  7
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        List<List<Integer>> result = traversal.zigzagLevelOrder(root);

        assertEquals(3, result.size(), "Should have three levels");
        assertEquals(List.of(1), result.get(0), "Level 1: left to right");
        assertEquals(List.of(2, 3), result.get(1), "Level 2: left to right");
        assertEquals(List.of(7, 6, 5, 4), result.get(2), "Level 3: right to left");
    }
}