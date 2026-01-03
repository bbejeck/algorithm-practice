package dev.bbejeck.binary_tree;

import java.util.StringJoiner;

/**
 * User: Bill Bejeck
 * Date: 1/2/26
 * Time: 8:44 PM
 */
public class BinaryTreeSerialization {

    public String serialize(TreeNode<Integer> root) {
        StringJoiner sj = new StringJoiner(" ");
        serialize(root, sj);
        return sj.toString();
    }

    private void serialize(TreeNode<Integer> root, StringJoiner sj) {
        if (root == null) {
            sj.add("x");
            return;
        }
        sj.add(Integer.toString(root.val));
        serialize(root.left, sj);
        serialize(root.right, sj);
    }

    public TreeNode<Integer> deserialize(String serializedTree) {
        String[] tokens = serializedTree.split(" ");
        return deserialize(tokens, new int[1]);
    }

    private TreeNode<Integer> deserialize(String[] tokens, int[] index) {
        if (tokens[index[0]].equals("x")) {
            index[0]++;
            return null;
        }
        TreeNode<Integer> node = new TreeNode<>(Integer.parseInt(tokens[index[0]]));
        index[0]++;
        node.left = deserialize(tokens, index);
        node.right = deserialize(tokens, index);
        return node;
    }
}
