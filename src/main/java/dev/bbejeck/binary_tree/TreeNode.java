package dev.bbejeck.binary_tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 2/12/25
 * Time: 1:45 PM
 */
public class TreeNode<T> {
    public T val;
    TreeNode<T> left;
    TreeNode<T> right;
    List<TreeNode<T>> children = new ArrayList<>();
    TreeNode(T x) { val = x; }

    public TreeNode(T val, TreeNode<T> left, TreeNode<T> right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public TreeNode(T val, TreeNode<T> left, TreeNode<T> right, List<TreeNode<T>> children) {
        this.val = val;
        this.left = left;
        this.right = right;
        this.children = children;
    }

    TreeNode() {}

    public void addChild(TreeNode<T> child) {
        children.add(child);
    }
    public List<TreeNode<T>> children() {
        return children;
    }

}
