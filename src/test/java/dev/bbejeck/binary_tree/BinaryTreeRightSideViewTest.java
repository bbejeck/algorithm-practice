package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeRightSideViewTest {

    private BinaryTreeRightSideView rightSideView;

    @BeforeEach
    void setUp() {
        rightSideView = new BinaryTreeRightSideView();
    }

    @Test
    void testNullRoot() {
        List<Integer> result = rightSideView.rightSideView(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleNode() {
        TreeNode root = new TreeNode(1);
        List<Integer> result = rightSideView.rightSideView(root);

        assertEquals(List.of(1), result);
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

        List<Integer> result = rightSideView.rightSideView(root);
        assertEquals(List.of(1, 3, 7), result);
    }

    @Test
    void testLeftSkewedTree() {
        /*
            1
           /
          2
         /
        3
       /
      4
        */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(3);
        root.left.left.left = new TreeNode(4);

        List<Integer> result = rightSideView.rightSideView(root);
        assertEquals(List.of(1, 2, 3, 4), result);
    }

    @Test
    void testRightSkewedTree() {
        /*
        1
         \
          2
           \
            3
             \
              4
        */
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.right = new TreeNode(3);
        root.right.right.right = new TreeNode(4);

        List<Integer> result = rightSideView.rightSideView(root);
        assertEquals(List.of(1, 2, 3, 4), result);
    }

    @Test
    void testTreeWithHiddenNodes() {
        /*
             1
            / \
           2   3
          /     \
         4       6
          \
           5
        */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.left.right = new TreeNode(5);
        root.right.right = new TreeNode(6);

        List<Integer> result = rightSideView.rightSideView(root);
        assertEquals(List.of(1, 3, 6, 5), result);
    }

    @Test
    void testTreeWithVisibleLeftNodes() {
        /*
             1
            / \
           2   3
          /
         4
          \
           5
        */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.left.right = new TreeNode(5);

        List<Integer> result = rightSideView.rightSideView(root);
        assertEquals(List.of(1, 3, 4, 5), result);
    }
}