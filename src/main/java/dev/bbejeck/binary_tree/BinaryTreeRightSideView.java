package dev.bbejeck.binary_tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * User: Bill Bejeck
 * Date: 3/5/25
 * Time: 9:19 PM
 */
public class BinaryTreeRightSideView {

    public List<Integer> rightSideView(TreeNode<Integer> root) {
        if (root == null) {
            return Collections.emptyList();
        }
        Queue<TreeNode<Integer>> queue = new LinkedList<>();

        List<Integer> results = new ArrayList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int level = queue.size();
            for (int i = 0; i < level; i++) {
                TreeNode<Integer> current = queue.poll();
                   if (i == level - 1) {
                      results.add(current.val);
                   }
                    if (current.left != null) {
                        queue.offer(current.left);
                    }
                    if (current.right != null) {
                        queue.offer(current.right);
                    }
            }
        }
        return results;
    }
}
