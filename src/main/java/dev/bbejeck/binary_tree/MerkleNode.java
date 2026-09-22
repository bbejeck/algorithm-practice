package dev.bbejeck.binary_tree;

/**
 * User: Bill Bejeck
 * Date: 1/19/26
 * Time: 7:59 PM
 */
public class MerkleNode {
    String hash;
    MerkleNode left;
    MerkleNode right;
    String data;  // Only set for leaf nodes

    public MerkleNode(String hash) {
        this.hash = hash;
    }

    public MerkleNode(String hash, String data) {
        this.hash = hash;
        this.data = data;
    }

    @Override
    public String toString() {
        return "MerkleNode{" +
                "hash='" + hash.substring(0, 8) + "...'" +
                ", isLeaf=" + (data != null) +
                '}';
    }
}
