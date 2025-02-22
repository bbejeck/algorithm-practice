package dev.bbejeck.binary_tree;

import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 2/12/25
 * Time: 1:50 PM
 */
public class TreeUtils {
    public static TreeNode fromPreOrderList(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return buildTree(values, new int[]{0});
    }

    private static TreeNode buildTree(List<Integer> values, int[] index) {
        if (index[0] >= values.size() || values.get(index[0]) == null) {
            index[0]++;
            return null;
        }

        TreeNode node = new TreeNode(values.get(index[0]++));
        node.left = buildTree(values, index);
        node.right = buildTree(values, index);

        return node;
    }
}
