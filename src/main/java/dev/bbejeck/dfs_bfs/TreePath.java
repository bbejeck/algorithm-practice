package dev.bbejeck.dfs_bfs;

import dev.bbejeck.binary_tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 1/4/26
 * Time: 6:40 PM
 */
public class TreePath {

    public List<String> allPaths(TreeNode<Integer> root) {
        List<String> result = new ArrayList<>();
        dfs(root, new ArrayList<>(), result);
        return result;

    }

    public void dfs(TreeNode<Integer> root, List<String> path, List<String> result) {
        if (root.children().isEmpty()) {
            path.add(Integer.toString(root.val));
            result.add(String.join("->", path));
            path.removeLast();
            return;
        }
        for (TreeNode<Integer> child : root.children()) {
            path.add(Integer.toString(root.val));
            dfs(child, path, result);
            path.removeLast();
        }
    }
}
