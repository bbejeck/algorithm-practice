package dev.bbejeck.binary_tree;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 12/23/25
 * Time: 2:56 PM
 */
public class ReconstructTreeFromPreorderInorderArrays {
    public TreeNode constructBinaryTree(int[] preorder, int[] inorder) {
       Map<Integer, Integer> indxMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            indxMap.put(inorder[i], i);
        }
        return buildTree(preorder, indxMap, 0, 0, inorder.length);
    }

    public TreeNode buildTree(int[] preorder,
                              Map<Integer, Integer> indxMap,
                              int preOrderStart,
                              int inOrderStart,
                              int size) {
        if (size <= 0) {
            return null;
        }
        int rootVal = preorder[preOrderStart];
        int rootIndx = indxMap.get(rootVal);
        int leftSize = rootIndx - inOrderStart;

        TreeNode left = buildTree(preorder,
                                 indxMap,
                     preOrderStart + 1,
                                 inOrderStart,
                                 leftSize);
        TreeNode right = buildTree(preorder,
                                   indxMap,
                       preOrderStart + 1 + leftSize,
                        rootIndx + 1,
                              size - 1 - leftSize);

        return new TreeNode(rootVal, left, right);
    }
}
