package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 12/31/25
 * Time: 12:38 PM
 */
public class LowestCommonAncestorBST {

      int lowestCommonAncestor(TreeNode root, int p, int q) {
          if (p < root.val && q < root.val) {
              return lowestCommonAncestor(root.left, p, q);
          } else if (p > root.val && q > root.val) {
              return lowestCommonAncestor(root.right, p, q);
          } else {
              return root.val;
          }
    }
}
