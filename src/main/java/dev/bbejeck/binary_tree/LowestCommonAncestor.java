package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 7/26/25
 * Time: 11:27 AM
 */
public class LowestCommonAncestor {

    public TreeNode<Integer> lowestCommonAncestor(TreeNode<Integer> root, TreeNode<Integer> p, TreeNode<Integer> q) {
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode<Integer> left = lowestCommonAncestor(root.left, p, q);
        TreeNode<Integer> right = lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null) {
            return root;
        }
        return left != null ? left : right;
    }
}
