package dev.bbejeck.binary_tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * User: Bill Bejeck
 * Date: 3/6/25
 * Time: 9:14 PM
 */
public class BinaryTreeZigZagLevelTraversal {
    public List<List<Integer>> zigzagLevelOrder(TreeNode<Integer> root) {
        List<List<Integer>> results = new ArrayList<>();
        if (root == null) {
            return results;
        }

        if (root.left == null && root.right == null){
            results.add(List.of(root.val));
            return results;
        }

        Queue<TreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(root);
        int currentLevel = 0;
        while(!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> levelVals = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode<Integer> current = queue.poll();
                levelVals.add(current.val);
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            if (currentLevel % 2 == 0) {
                results.add(levelVals.reversed());
            }  else {
                results.add(levelVals);
            }
            currentLevel += 1;
        }
        return results;
    }
}
