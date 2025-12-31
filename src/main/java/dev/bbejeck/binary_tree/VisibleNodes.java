package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 12/20/25
 * Time: 8:15 PM
 */
public class VisibleNodes {

    public int visibleNodes(TreeNode<Integer> root) {
          return dfs(root, Integer.MIN_VALUE);
    }

    private int dfs(TreeNode<Integer> node, int maxSoFar) {
         if (node == null) {
             return 0;
         }
         int total = 0;
         if (node.val >= maxSoFar) {
             total++;
         }
         total+= dfs(node.left, Math.max(maxSoFar, node.val));
         total+= dfs(node.right, Math.max(maxSoFar, node.val));
         return total;
    }
}
