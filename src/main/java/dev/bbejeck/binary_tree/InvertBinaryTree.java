package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 12/22/25
 * Time: 8:33 PM
 */
public class InvertBinaryTree {

    public TreeNode<Integer> invertTree(TreeNode<Integer> node) {
        if (node == null) {
            return null;
        }
        TreeNode<Integer> tempLeft = node.left;
        node.left = node.right;
        node.right = tempLeft;
        invertTree(node.left);
        invertTree(node.right);
        return node;
    }
}
