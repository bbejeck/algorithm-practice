package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 2/12/25
 * Time: 1:45 PM
 */
public class TreeNode<T> {
    T val;
    TreeNode<T> left;
    TreeNode<T> right;
    TreeNode(T x) { val = x; }

    public TreeNode(T val, TreeNode<T> left, TreeNode<T> right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
    TreeNode() {}

}
