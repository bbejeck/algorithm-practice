package dev.bbejeck.binary_tree;

import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 2/21/25
 * Time: 2:16 PM
 */
public class FlattenBinaryTreeToLinkedList {

    public void flatten(TreeNode root) {
        if (root != null) {
            flattenHelper(root);
        }
    }

    public void flattenHelper(TreeNode node) {
        if (node == null) {
            return;
        }
        TreeNode rightSubTree = node.right;
        TreeNode leftSubTree = node.left;
        node.left = null;
        flattenHelper(leftSubTree);
        flattenHelper(rightSubTree);
        node.right = leftSubTree;

        TreeNode current = node;
        while(current.right != null) {
            current = current.right;
        }
        current.right = rightSubTree;
    }

    public static void main(String[] args) {
       TreeNode node = TreeUtils.fromPreOrderList(List.of(1,2,5,3,4,6));
       FlattenBinaryTreeToLinkedList flattenBinaryTreeToLinkedList = new FlattenBinaryTreeToLinkedList();
       flattenBinaryTreeToLinkedList.flatten(node);
    }
}
