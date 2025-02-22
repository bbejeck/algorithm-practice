package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 2/12/25
 * Time: 1:45 PM
 */
public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
    TreeNode() {}

}
