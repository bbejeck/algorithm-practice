package dev.bbejeck.binary_tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * User: Bill Bejeck
 * Date: 3/4/25
 * Time: 8:44 PM
 */
public class BinaryTreeLevelOrderTraversal {

    public List<List<Integer>> levelOrder(TreeNode<Integer> root) {
        List<List<Integer>> results = new ArrayList<>();
        if (root == null) {
            return results;
        }

        Queue<TreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            int numLevel = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            for (int i = 0; i < numLevel; i++) {
                TreeNode<Integer> current = queue.poll();
                currentLevel.add(current.val);
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            results.add(currentLevel);
        }
        return results;
    }

}
