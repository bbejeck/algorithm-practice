package dev.bbejeck.binary_tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * User: Bill Bejeck
 * Date: 3/4/25
 * Time: 8:44 PM
 */
public class BinaryTreeLevelOrderTraversalII {
    public List<List<Integer>> levelOrderBottom(TreeNode<Integer> root) {
        List<List<Integer>> results = new ArrayList<>();
        Stack<List<Integer>> stack = new Stack<>();
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
            stack.push(currentLevel);
        }
        while (!stack.isEmpty()) {
            results.add(stack.pop());
        }
        return results;
    }

}
