package dev.bbejeck.divide_and_conquer;

/**
 * User: Bill Bejeck
 * Date: 1/11/25
 * Time: 12:40 PM
 */
public class SortedArrayToBST {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        TreeNode<Integer> treeNode = sortedArrayToBST(nums);
        printTree(treeNode);
    }


    public static TreeNode<Integer> sortedArrayToBST(int[] nums) {
        return toBSTHelper(nums, 0, nums.length - 1);
    }

    public static TreeNode<Integer> toBSTHelper(int[] nums, int start, int end) {
        TreeNode<Integer> root = null;
        int mid = start + (end - start) / 2;
        if (start <= end) {
            root = new TreeNode<>(nums[mid]);
            root.left = toBSTHelper(nums, start, mid - 1);
            root.right = toBSTHelper(nums, mid + 1, end);
        }
        return root;
    }

    public static void printTree(TreeNode<Integer> root) {
        printTreeHelper(root, 0);
    }

    private static void printTreeHelper(TreeNode<Integer> root, int level) {
        if (root == null) {
            return;
        }

        // Print right subtree first with indentation
        printTreeHelper(root.right, level + 1);

        // Print current node with indentation based on the level
        for (int i = 0; i < level; i++) {
            System.out.print("    "); // 4 spaces for indentation
        }
        System.out.println(root.val);

        // Print left subtree with indentation
        printTreeHelper(root.left, level + 1);
    }


    public static class TreeNode<T> {
        T val;
        TreeNode<T> left;
        TreeNode<T> right;
        TreeNode() {}
        TreeNode(T x) { val = x; }
        TreeNode(T x, TreeNode<T> left, TreeNode<T> right) {
            this.val = x;
            this.left = left;
            this.right = right;
        }
    }
}
