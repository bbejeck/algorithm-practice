package dev.bbejeck.binary_tree;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MerkleTree {

    private final MerkleNode root;
    private final List<String> dataBlocks;

    public MerkleTree(List<String> dataBlocks) {
        this.dataBlocks = new ArrayList<>(dataBlocks);
        this.root = buildTree();
    }

    /**
     * Build the Merkle tree from bottom-up
     */
    private MerkleNode buildTree() {
        if (dataBlocks == null || dataBlocks.isEmpty()) {
            throw new IllegalArgumentException("Data blocks cannot be empty");
        }

        // Create leaf nodes (hash each data block)
        List<MerkleNode> leaves = new ArrayList<>();
        for (String data : dataBlocks) {
            String hash = sha256(data);
            leaves.add(new MerkleNode(hash, data));
        }

        // Build tree bottom-up
        return buildTreeRecursive(leaves);
    }

    /**
     * Recursively build tree by combining nodes level by level
     */
    private MerkleNode buildTreeRecursive(List<MerkleNode> nodes) {
        // Base case: only one node left, it's the root
        if (nodes.size() == 1) {
            return nodes.get(0);
        }

        // Handle odd number of nodes (duplicate last node)
        if (nodes.size() % 2 != 0) {
            nodes.add(nodes.get(nodes.size() - 1));
        }

        // Combine pairs of nodes into parent nodes
        List<MerkleNode> parentLevel = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i += 2) {
            MerkleNode left = nodes.get(i);
            MerkleNode right = nodes.get(i + 1);

            // Parent hash = hash(left.hash + right.hash)
            String combinedHash = sha256(left.hash + right.hash);
            MerkleNode parent = new MerkleNode(combinedHash);
            parent.left = left;
            parent.right = right;

            parentLevel.add(parent);
        }

        // Recursively build next level
        return buildTreeRecursive(parentLevel);
    }

    /**
     * Generate SHA-256 hash of input string
     */
    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());

            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Get the root hash
     */
    public String getRootHash() {
        return root.hash;
    }

    /**
     * Generate Merkle proof for a specific leaf index
     * Returns the hashes needed to reconstruct the root from that leaf
     */
    public List<String> generateProof(int leafIndex) {
        if (leafIndex < 0 || leafIndex >= dataBlocks.size()) {
            throw new IllegalArgumentException("Invalid leaf index: " + leafIndex);
        }

        List<String> proof = new ArrayList<>();
        generateProofHelper(root, leafIndex, 0, proof);
        return proof;
    }

    /**
     * Helper to traverse tree and collect sibling hashes
     */
    private boolean generateProofHelper(MerkleNode node, int targetIndex,
                                        int currentIndex, List<String> proof) {
        // Leaf node
        if (node.left == null && node.right == null) {
            if (currentIndex == targetIndex) {
                return true;  // Found the target
            }
            return false;
        }

        int leftSubtreeSize = getSubtreeSize(node.left);

        // Search left subtree
        if (targetIndex < currentIndex + leftSubtreeSize) {
            if (generateProofHelper(node.left, targetIndex, currentIndex, proof)) {
                // Add right sibling to proof
                proof.add(node.right.hash);
                return true;
            }
        } else {
            // Search right subtree
            if (generateProofHelper(node.right, targetIndex,
                    currentIndex + leftSubtreeSize, proof)) {
                // Add left sibling to proof
                proof.add(node.left.hash);
                return true;
            }
        }

        return false;
    }

    /**
     * Get number of leaves in subtree
     */
    private int getSubtreeSize(MerkleNode node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;  // Leaf
        return getSubtreeSize(node.left) + getSubtreeSize(node.right);
    }

    /**
     * Verify that a data block belongs to the tree using a Merkle proof
     *
     * KEY: The proof hashes are generated from sibling nodes as we traverse UP the tree.
     * We need to reconstruct the exact same path, combining hashes in the same order.
     *
     * IMPORTANT: The order of concatenation matters!
     * If we went LEFT at a node, the sibling (right) goes on the right: hash(our_hash + sibling_hash)
     * If we went RIGHT at a node, the sibling (left) goes on the left: hash(sibling_hash + our_hash)
     */
    public boolean verifyProof(String data, int leafIndex, List<String> proof) {
        String computedHash = sha256(data);

        // Determine which side we start on (left or right) based on leaf index
        // and reconstruct the path, using proof hashes in correct order
        int position = leafIndex;

        // Rebuild the hash path from leaf to root using proof
        // We need to combine hashes in the correct order based on position
        for (String proofHash : proof) {
            // If position is even, we're on the left, so proof is on the right
            // If position is odd, we're on the right, so proof is on the left
            if (position % 2 == 0) {
                // We are left child, proof hash is right sibling
                computedHash = sha256(computedHash + proofHash);
            } else {
                // We are right child, proof hash is left sibling
                computedHash = sha256(proofHash + computedHash);
            }

            // Move to next level (parent level)
            // Position is divided by 2 and floored
            position = position / 2;
        }

        // Check if computed hash matches root
        return computedHash.equals(root.hash);
    }

    /**
     * Print tree structure for visualization
     */
    public void printTree() {
        System.out.println("\n=== MERKLE TREE ===");
        printTreeHelper(root, "", true);
        System.out.println("Root Hash: " + root.hash.substring(0, 16) + "...");
    }

    private void printTreeHelper(MerkleNode node, String prefix, boolean isLast) {
        if (node == null) return;

        System.out.println(prefix + (isLast ? "└── " : "├── ") +
                node.hash.substring(0, 12) + "..." +
                (node.data != null ? " (data: " + node.data + ")" : ""));

        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printTreeHelper(node.left, prefix + (isLast ? "    " : "│   "),
                        node.right == null);
            }
            if (node.right != null) {
                printTreeHelper(node.right, prefix + (isLast ? "    " : "│   "),
                        true);
            }
        }
    }

    /**
     * Main demonstration
     */
    public static void main(String[] args) {
        // Create Merkle tree from data blocks
        List<String> blocks = Arrays.asList("block1", "block2", "block3", "block4");
        MerkleTree tree = new MerkleTree(blocks);

        tree.printTree();

        // Get root hash
        System.out.println("\nRoot Hash: " + tree.getRootHash());

        // Generate proof for block at index 0
        System.out.println("\n=== MERKLE PROOF ===");
        int targetIndex = 0;
        List<String> proof = tree.generateProof(targetIndex);
        System.out.println("Proof for 'block1' (index " + targetIndex + "):");
        for (int i = 0; i < proof.size(); i++) {
            System.out.println("  Hash " + i + ": " + proof.get(i).substring(0, 16) + "...");
        }

        // Verify the proof
        System.out.println("\n=== VERIFICATION ===");
        boolean isValid = tree.verifyProof("block1", targetIndex, proof);
        System.out.println("Verification result: " + isValid);

        // Try with tampered data
        System.out.println("\nVerification with tampered data (block1_modified): " +
                tree.verifyProof("block1_modified", targetIndex, proof));

        // Demonstrate all leaves
        System.out.println("\n=== ALL PROOFS ===");
        for (int i = 0; i < blocks.size(); i++) {
            List<String> leafProof = tree.generateProof(i);
            boolean verified = tree.verifyProof(blocks.get(i), i, leafProof);
            System.out.println("Block " + i + " (" + blocks.get(i) + "): " +
                    "Proof size=" + leafProof.size() +
                    ", Valid=" + verified);
        }
    }
}