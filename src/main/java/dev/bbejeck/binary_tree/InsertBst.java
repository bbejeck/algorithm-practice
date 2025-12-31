package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 12/29/25
 * Time: 4:31 PM
 */
public class InsertBst {

    public TreeNode<Integer> insert(TreeNode<Integer> root, int val) {
        if (root == null) {
            return new TreeNode<>(val);
        }
        if (val < root.val) {
            root.left = insert(root.left, val);
        } else if (val > root.val) {
            root.right = insert(root.right, val);
        }
        return root;
    }
}
