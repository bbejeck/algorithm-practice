package dev.bbejeck.binary_tree;


import java.util.Arrays;
import java.util.List;

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

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(3,9,20,null,null,15,7);
        TreeNode root = TreeUtils.fromPreOrderList(values);
        MaxDepthOfTree maxDepthOfTree = new MaxDepthOfTree();
        System.out.println(maxDepthOfTree.maxDepth(root));
    }
}
