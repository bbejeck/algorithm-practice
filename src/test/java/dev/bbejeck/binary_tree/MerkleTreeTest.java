
package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MerkleTree class
 *
 * These tests demonstrate how Merkle trees work, particularly focusing on:
 * 1. How proof generation creates minimal hash paths
 * 2. How proof verification detects any changes to data
 * 3. How even tiny data modifications cause complete tree invalidation
 * 4. Why Merkle trees are perfect for data integrity verification
 *
 * User: Bill Bejeck
 * Date: 1/19/26
 */
class MerkleTreeTest {

    private MerkleTree merkleTree;
    private List<String> originalBlocks;

    @BeforeEach
    void setUp() {
        // Initialize with 4 data blocks for easy visualization
        // This creates a perfect binary tree with 2 levels of internal nodes
        originalBlocks = Arrays.asList("block1", "block2", "block3", "block4");
        merkleTree = new MerkleTree(originalBlocks);
    }

    @Nested
    @DisplayName("Tree Construction and Root Hash Tests")
    class TreeConstructionTests {

        @Test
        @DisplayName("Tree builds successfully with valid data blocks")
        void testTreeConstructsWithValidBlocks() {
            // GIVEN: A list of data blocks
            // WHEN: MerkleTree is created
            // THEN: Root hash is generated and is not null/empty
            assertNotNull(merkleTree.getRootHash());
            assertFalse(merkleTree.getRootHash().isEmpty());
        }

        @Test
        @DisplayName("Root hash is a valid SHA-256 hex string (64 characters)")
        void testRootHashIsValidSHA256() {
            // SHA-256 produces 256 bits = 64 hexadecimal characters
            String rootHash = merkleTree.getRootHash();
            assertEquals(64, rootHash.length(), "SHA-256 hash should be 64 hex characters");
            assertTrue(rootHash.matches("[0-9a-f]+"), "Hash should contain only hex characters");
        }

        @Test
        @DisplayName("Empty data blocks throws IllegalArgumentException")
        void testEmptyBlocksThrowsException() {
            // GIVEN: Empty list of blocks
            List<String> emptyBlocks = Arrays.asList();

            // WHEN & THEN: Creating tree should throw exception
            assertThrows(IllegalArgumentException.class,
                    () -> new MerkleTree(emptyBlocks),
                    "Should not allow empty data blocks");
        }

        @Test
        @DisplayName("Single data block creates valid tree")
        void testSingleDataBlock() {
            // GIVEN: Single block
            List<String> singleBlock = Arrays.asList("onlyblock");
            MerkleTree tree = new MerkleTree(singleBlock);

            // THEN: Root hash should exist
            assertNotNull(tree.getRootHash());
            assertEquals(64, tree.getRootHash().length());
        }

        @Test
        @DisplayName("Same data produces same root hash (deterministic)")
        void testDeterministicRootHash() {
            // KEY CONCEPT: Merkle trees are deterministic
            // Same input data always produces same root hash

            // GIVEN: Two trees with identical data
            MerkleTree tree1 = new MerkleTree(originalBlocks);
            MerkleTree tree2 = new MerkleTree(originalBlocks);

            // WHEN & THEN: Root hashes must be identical
            assertEquals(tree1.getRootHash(), tree2.getRootHash(),
                    "Identical data should produce identical root hash");
        }
    }

    @Nested
    @DisplayName("Merkle Proof Generation Tests")
    class ProofGenerationTests {

        @Test
        @DisplayName("Proof size is logarithmic: 4 blocks = 2 hashes needed")
        void testProofSizeIsLogarithmic() {
            // KEY CONCEPT: Merkle proofs are O(log n)
            // For 4 blocks arranged in binary tree, we only need 2 sibling hashes
            // to reconstruct path from any leaf to root
            //
            //          Root
            //         /    \
            //       H(1+2) H(3+4)
            //       /  \    /  \
            //      H1  H2  H3  H4
            //      |   |   |   |
            //      B1  B2  B3  B4
            //
            // To prove B1 exists: need H2 (sibling) and H(3+4) (uncle)

            List<String> proof = merkleTree.generateProof(0);
            assertEquals(2, proof.size(),
                    "4 blocks need 2 hashes (log2(4) = 2)");
        }

        @Test
        @DisplayName("All blocks generate valid proofs with same size")
        void testAllBlocksHaveValidProofs() {
            // GIVEN: Merkle tree with 4 blocks
            // WHEN: Generate proofs for each block
            // THEN: All proofs should be valid and same size

            for (int i = 0; i < originalBlocks.size(); i++) {
                List<String> proof = merkleTree.generateProof(i);
                assertNotNull(proof, "Proof should not be null");
                assertEquals(2, proof.size(),
                        "All 4 blocks should need 2 hashes for proof");
                assertTrue(proof.stream().allMatch(h -> h.length() == 64),
                        "All hashes in proof should be valid SHA-256");
            }
        }

        @Test
        @DisplayName("Invalid leaf index throws exception")
        void testInvalidLeafIndexThrowsException() {
            // GIVEN: Index outside valid range
            assertThrows(IllegalArgumentException.class,
                    () -> merkleTree.generateProof(4),
                    "Index 4 is invalid for 4 blocks (valid: 0-3)");

            assertThrows(IllegalArgumentException.class,
                    () -> merkleTree.generateProof(-1),
                    "Negative index should be invalid");
        }

        @Test
        @DisplayName("Proofs for different blocks are different")
        void testDifferentBlocksHaveDifferentProofs() {
            // GIVEN: Two different blocks
            List<String> proof0 = merkleTree.generateProof(0);
            List<String> proof1 = merkleTree.generateProof(1);

            // THEN: At least one sibling hash should differ
            // (they might share one hash, but not all)
            assertNotEquals(proof0, proof1,
                    "Different blocks should generate different proofs");
        }
    }

    @Nested
    @DisplayName("Proof Verification Tests - Core Learning")
    class ProofVerificationTests {

        @Test
        @DisplayName("Valid data passes verification")
        void testValidDataPassesVerification() {
            // KEY CONCEPT: Verification works by reconstructing the root hash
            // 1. Start with data block
            // 2. Hash it to get leaf hash
            // 3. Combine with proof hashes level by level
            // 4. Compare final computed hash with known root hash

            // GIVEN: Original data and its proof
            String originalData = "block1";
            int blockIndex = 0;
            List<String> proof = merkleTree.generateProof(blockIndex);

            // WHEN: Verify with original data
            boolean isValid = merkleTree.verifyProof(originalData, blockIndex, proof);

            // THEN: Should be valid
            assertTrue(isValid, "Original data should pass verification");
        }

        @Test
        @DisplayName("CRITICAL: Single character change fails verification")
        void testSingleCharacterChangeDetected() {
            // KEY CONCEPT: Cryptographic hashing is avalanche-sensitive
            // Even ONE character change cascades all the way up to root hash
            //
            // Original:  hash("block1") → H1 → H(1+2) → Root
            // Modified:  hash("block2") → H1' → H(1'+2) → Root' (DIFFERENT!)

            // GIVEN: Original data
            String originalData = "block1";
            int blockIndex = 0;
            List<String> proof = merkleTree.generateProof(blockIndex);

            // WHEN: Try to verify with modified data (only one letter changed)
            String modifiedData = "block2";  // Changed last character from 1 to 2
            boolean isValid = merkleTree.verifyProof(modifiedData, blockIndex, proof);

            // THEN: Should fail - modification detected!
            assertFalse(isValid,
                    "Even tiny data change should fail verification - " +
                            "hash of 'block2' != hash of 'block1'");
        }

        @Test
        @DisplayName("Whitespace changes fail verification")
        void testWhitespaceChangeDetected() {
            // KEY CONCEPT: Hash functions are sensitive to ALL changes
            // Including whitespace, case, punctuation

            String originalData = "block1";
            int blockIndex = 0;
            List<String> proof = merkleTree.generateProof(blockIndex);

            // Try various modifications
            assertFalse(merkleTree.verifyProof("block1 ", blockIndex, proof),
                    "Extra space should fail");
            assertFalse(merkleTree.verifyProof(" block1", blockIndex, proof),
                    "Leading space should fail");
            assertFalse(merkleTree.verifyProof("BLOCK1", blockIndex, proof),
                    "Case change should fail");
        }

        @Test
        @DisplayName("Completely different data fails verification")
        void testCompletelyDifferentDataFails() {
            // GIVEN: Proof for block1
            List<String> proof = merkleTree.generateProof(0);

            // WHEN & THEN: Verify with completely different data
            assertFalse(merkleTree.verifyProof("malicious_data", 0, proof),
                    "Completely different data should fail");
            assertFalse(merkleTree.verifyProof("", 0, proof),
                    "Empty string should fail");
            assertFalse(merkleTree.verifyProof("12345", 0, proof),
                    "Different format should fail");
        }

        @Test
        @DisplayName("Proof from one block fails for different block")
        void testProofNotInterchangeable() {
            // KEY CONCEPT: Proofs are specific to their block position
            // You can't use proof from block1 to verify block2

            // GIVEN: Proof for block1
            List<String> proof1 = merkleTree.generateProof(0);

            // WHEN & THEN: Try to verify different block with wrong proof
            assertFalse(merkleTree.verifyProof("block2", 0, proof1),
                    "Proof for block1 should not verify block2");
            assertFalse(merkleTree.verifyProof("block2", 1, proof1),
                    "Proof from different index should fail");
        }

        @Test
        @DisplayName("All original blocks verify successfully")
        void testAllOriginalBlocksVerify() {
            // GIVEN: Merkle tree with 4 blocks
            // WHEN: Generate and verify proof for each original block
            // THEN: All should verify successfully

            for (int i = 0; i < originalBlocks.size(); i++) {
                String data = originalBlocks.get(i);
                List<String> proof = merkleTree.generateProof(i);

                boolean isValid = merkleTree.verifyProof(data, i, proof);
                assertTrue(isValid,
                        "Block " + i + " (" + data + ") should verify successfully");
            }
        }
    }

    @Nested
    @DisplayName("Tampering Detection - Real World Scenario")
    class TamperingDetectionTests {

        @Test
        @DisplayName("Simulates data transmission: detect which block was corrupted")
        void testDetectCorruptedBlockInTransmission() {
            // SCENARIO: We download 4 blocks from a server
            // One block gets corrupted during transmission
            // We have the root hash but want to find which block is wrong

            // GIVEN: Proofs for all original blocks
            List<String> proof0 = merkleTree.generateProof(0);
            List<String> proof1 = merkleTree.generateProof(1);
            List<String> proof2 = merkleTree.generateProof(2);
            List<String> proof3 = merkleTree.generateProof(3);

            String rootHashFromServer = merkleTree.getRootHash();

            // WHEN: Receive blocks, but block2 is corrupted
            List<String> receivedBlocks = Arrays.asList(
                    "block1",                    // ✓ OK
                    "block2",                    // ✓ OK
                    "block2_corrupted",          // ✗ CORRUPTED (modified)
                    "block4"                     // ✓ OK
            );

            // Verify each received block
            boolean block0Valid = merkleTree.verifyProof(receivedBlocks.get(0), 0, proof0);
            boolean block1Valid = merkleTree.verifyProof(receivedBlocks.get(1), 1, proof1);
            boolean block2Valid = merkleTree.verifyProof(receivedBlocks.get(2), 2, proof2);
            boolean block3Valid = merkleTree.verifyProof(receivedBlocks.get(3), 3, proof3);

            // THEN: Only block2 should fail
            assertTrue(block0Valid, "Block 0 should be valid");
            assertTrue(block1Valid, "Block 1 should be valid");
            assertFalse(block2Valid, "Block 2 should be detected as corrupted");
            assertTrue(block3Valid, "Block 3 should be valid");
        }

        @Test
        @DisplayName("Demonstrates tamper-proof property: root hash catches any modification")
        void testTamperProofProperty() {
            // KEY CONCEPT: ANY modification anywhere in the tree
            // cascades all the way to root hash

            // GIVEN: Original root hash
            String originalRoot = merkleTree.getRootHash();

            // WHEN: Create new tree with one modified block
            List<String> modifiedBlocks = Arrays.asList(
                    "block1_modified",  // CHANGED
                    "block2",
                    "block3",
                    "block4"
            );
            MerkleTree modifiedTree = new MerkleTree(modifiedBlocks);
            String modifiedRoot = modifiedTree.getRootHash();

            // THEN: Root hashes are completely different
            assertNotEquals(originalRoot, modifiedRoot,
                    "Modifying ANY block changes the entire root hash - " +
                            "this is why Merkle trees are perfect for integrity verification");

            // The proof for original block1 won't work on modified tree
            List<String> proof = merkleTree.generateProof(0);
            assertFalse(modifiedTree.verifyProof("block1", 0, proof),
                    "Proof from original tree fails on modified tree");
        }

        @Test
        @DisplayName("Multiple blocks modified still detected")
        void testMultipleBlocksModifiedDetected() {
            // SCENARIO: What if attacker modifies multiple blocks?
            // Still can't bypass verification!

            List<String> proof0 = merkleTree.generateProof(0);
            List<String> proof2 = merkleTree.generateProof(2);

            // Attacker modifies blocks 0 and 2
            assertFalse(merkleTree.verifyProof("hacked_block1", 0, proof0),
                    "Modified block 0 detected");
            assertFalse(merkleTree.verifyProof("hacked_block3", 2, proof2),
                    "Modified block 2 detected");

            // Can't forge a valid proof without knowing the root hash
            // and can't know root hash without having all original data
        }
    }

    @Nested
    @DisplayName("Edge Cases and Scalability Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Odd number of blocks: 3 blocks handled correctly")
        void testOddNumberOfBlocks() {
            // GIVEN: 3 blocks (odd number)
            // Merkle tree duplicates last node to make even: [B1, B2, B3, B3]
            List<String> oddBlocks = Arrays.asList("block1", "block2", "block3");
            MerkleTree tree = new MerkleTree(oddBlocks);

            // THEN: All blocks should still verify
            for (int i = 0; i < oddBlocks.size(); i++) {
                List<String> proof = tree.generateProof(i);
                assertTrue(tree.verifyProof(oddBlocks.get(i), i, proof),
                        "Block " + i + " should verify in odd-length tree");
            }
        }

        @Test
        @DisplayName("Large number of blocks: 16 blocks = log2(16) = 4 hashes in proof")
        void testLargeNumberOfBlocks() {
            // KEY CONCEPT: Scalability advantage of Merkle trees
            // 16 blocks → proof size = log2(16) = 4 (still small!)
            // 1,000,000 blocks → proof size = log2(1,000,000) ≈ 20

            List<String> manyBlocks = Arrays.asList(
                    "block1", "block2", "block3", "block4",
                    "block5", "block6", "block7", "block8",
                    "block9", "block10", "block11", "block12",
                    "block13", "block14", "block15", "block16"
            );
            MerkleTree tree = new MerkleTree(manyBlocks);

            // THEN: Proof for first block should be 4 hashes (logarithmic)
            List<String> proof = tree.generateProof(0);
            assertEquals(4, proof.size(),
                    "16 blocks need 4 hashes for proof (log2(16))");

            // And verification should work for all blocks
            for (int i = 0; i < manyBlocks.size(); i++) {
                List<String> blockProof = tree.generateProof(i);
                assertTrue(tree.verifyProof(manyBlocks.get(i), i, blockProof),
                        "Block " + i + " should verify");
            }
        }

        @Test
        @DisplayName("Very long data blocks are hashed same as short ones")
        void testLongDataBlocksHandledEfficiently() {
            // KEY CONCEPT: Hash function produces same-size output
            // regardless of input size

            List<String> blocks = Arrays.asList(
                    "short",
                    "this is a much longer block with lots of data and information",
                    "another short",
                    "yet another extremely long block with repetitive data: " +
                            "Lorem ipsum dolor sit amet consectetur adipiscing elit"
            );
            MerkleTree tree = new MerkleTree(blocks);

            // All hashes are 64 characters regardless of input length
            for (int i = 0; i < blocks.size(); i++) {
                List<String> proof = tree.generateProof(i);
                // All proof hashes should be 64 chars (SHA-256)
                assertTrue(proof.stream().allMatch(h -> h.length() == 64),
                        "All hashes in proof should be 64 chars");
                // Most importantly: verification works with correct leafIndex
                assertTrue(tree.verifyProof(blocks.get(i), i, proof),
                        "Block " + i + " should verify with correct index");
            }
        }
    }


    @Nested
    @DisplayName("Learning: How Verification Works Step-by-Step")
    class VerificationProcessTests {

        @Test
        @DisplayName("Step-by-step verification walkthrough: block0 proof verification")
        void testVerificationStepByStep() {
            // EDUCATIONAL: Walk through the verification process
            //
            // Tree structure with 4 blocks:
            //          Root (computed during verification)
            //         /    \
            //       H12    H34       <- Generate proof must include H34
            //       /  \    /  \
            //      H1  H2  H3  H4    <- Generate proof must include H2
            //      |   |   |   |
            //      B1  B2  B3  B4
            //
            // VERIFICATION STEPS for block B1:
            // 1. Hash B1 → H1_computed
            // 2. Combine H1_computed + H2 (from proof) → H_temp1
            // 3. Hash H_temp1 → H12_computed
            // 4. Combine H12_computed + H34 (from proof) → H_temp2
            // 5. Hash H_temp2 → Root_computed
            // 6. Compare Root_computed with Root_known → VALID if match!

            String blockData = "block1";
            int blockIndex = 0;
            List<String> proof = merkleTree.generateProof(blockIndex);
            String knownRoot = merkleTree.getRootHash();

            // Perform verification
            boolean verified = merkleTree.verifyProof(blockData, blockIndex, proof);

            // The process worked!
            assertTrue(verified);

            // Now test with modified data - hash change propagates
            String modifiedData = "BLOCK1";  // Different case
            boolean modifiedVerified = merkleTree.verifyProof(modifiedData, blockIndex, proof);

            // Modified data fails because hash changes cascade:
            // hash("BLOCK1") ≠ hash("block1") → different H1 → different H12 → different Root
            assertFalse(modifiedVerified);
        }

        @Test
        @DisplayName("Why proof fails: hash mismatch at any level fails verification")
        void testWhyProofFailsOnModification() {
            // CONCEPT: If ANY intermediate hash doesn't match,
            // the final computed root won't match the known root

            String blockData = "block1";
            List<String> proof = merkleTree.generateProof(0);

            // These all fail because modifying data breaks the chain:
            String[] invalidData = {
                    "block1 ",          // Extra space → different hash
                    "Block1",           // Capital B → different hash
                    "block1\n",         // Newline → different hash
                    "block0",           // Different data → completely different hash
                    ""                  // Empty → definitely different hash
            };

            for (String invalid : invalidData) {
                assertFalse(merkleTree.verifyProof(invalid, 0, proof),
                        "Data '" + invalid + "' should fail verification");
            }
        }
    }
}