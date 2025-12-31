package dev.bbejeck.binary_tree;

import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 2/21/25
 * Time: 2:16 PM
 */
public class FlattenBinaryTreeToLinkedList {

    public void flatten(TreeNode<Integer> root) {
        if (root != null) {
            flattenHelper(root);
        }
    }

    public void flattenHelper(TreeNode<Integer> node) {
        if (node == null) {
            return;
        }
        TreeNode<Integer> rightSubTree = node.right;
        TreeNode<Integer> leftSubTree = node.left;
        node.left = null;
        flattenHelper(leftSubTree);
        flattenHelper(rightSubTree);
        node.right = leftSubTree;

        TreeNode<Integer> current = node;
        while(current.right != null) {
            current = current.right;
        }
        current.right = rightSubTree;
    }

    public static void main(String[] args) {
       TreeNode<Integer> node = TreeUtils.fromPreOrderList(List.of(1,2,5,3,4,6));
       FlattenBinaryTreeToLinkedList flattenBinaryTreeToLinkedList = new FlattenBinaryTreeToLinkedList();
       flattenBinaryTreeToLinkedList.flatten(node);
    }
}
