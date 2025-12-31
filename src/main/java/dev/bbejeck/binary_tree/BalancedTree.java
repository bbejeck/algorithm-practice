package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 12/21/25
 * Time: 7:59 PM
 */
public class BalancedTree {

    public boolean isBalanced(TreeNode<Integer> root) {
        return dfs(root) [0] == 1;
    }

    private int[] dfs(TreeNode<Integer> node) {
          if (node == null) {
              return new int[] {1, -1};
          }
          int [] left = dfs(node.left);
          int [] right = dfs(node.right);
          boolean balanced = left[0] == 1 && right[0] == 1 && Math.abs(left[1] - right[1]) <=1;
          return new int[] { balanced ? 1 : 0, Math.max(left[1], right[1]) + 1};
    }

}
