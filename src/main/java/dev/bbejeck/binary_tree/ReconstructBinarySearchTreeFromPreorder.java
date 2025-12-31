package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 12/24/25
 * Time: 1:37 PM
 */
public class ReconstructBinarySearchTreeFromPreorder {

    public TreeNode<Integer> buildTree(int[] preorder) {
        return buildTreeHelper(preorder,
                               Integer.MIN_VALUE,
                               Integer.MAX_VALUE,
                               new int[]{0});
    }

    public TreeNode<Integer> buildTreeHelper(int[] preorder,
                                    int min,
                                    int max,
                                    int[] index) {
            if (index[0] >= preorder.length) {
                return null;
            }
            int val = preorder[index[0]];
            if (val < min || val > max) {
                return null;
            }
            index[0]++;
            TreeNode<Integer> left = buildTreeHelper(preorder, min, val, index);
            TreeNode<Integer> right = buildTreeHelper(preorder, val, max, index);
            return new TreeNode<Integer>(val, left, right);
        }
}
