package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 12/28/25
 * Time: 3:29 PM
 */
public class IsValidBST {

    public boolean valid(TreeNode root) {
        return bstHelper(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean bstHelper(TreeNode root, int min, int max) {
         if (root == null) {
             return true;
         }
         if (!(root.val > min && root.val < max)) {
             return false;
         }
         return bstHelper(root.left, min, root.val) && bstHelper(root.right, root.val, max);
    }
}
