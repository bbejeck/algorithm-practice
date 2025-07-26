package dev.bbejeck.binary_tree;


/**
 * User: Bill Bejeck
 * Date: 2/12/25
 * Time: 1:44 PM
 */
public class MaxDepthOfTree {
    public int maxDepth(TreeNode root) {
        return recursiveCount(root, 0);
    }

    int recursiveCount(TreeNode root, int depth) {
        if (root == null) {
            return depth;
        }
        int leftDepth = recursiveCount(root.left, depth + 1);
        int rightDepth = recursiveCount(root.right, depth + 1);
        return Math.max(leftDepth, rightDepth);
    }
}
