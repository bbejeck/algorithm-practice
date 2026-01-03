package dev.bbejeck.binary_tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BinaryTreeSerialization class.
 */
class BinaryTreeSerializationTest {

    private BinaryTreeSerialization serialization;

    @BeforeEach
    void setUp() {
        serialization = new BinaryTreeSerialization();
    }

    @Test
    @DisplayName("Serialize null tree returns single x")
    void testSerializeNullTree() {
        String result = serialization.serialize(null);
        assertEquals("x", result);
    }

    @Test
    @DisplayName("Serialize single node tree")
    void testSerializeSingleNode() {
        TreeNode<Integer> root = new TreeNode<>(5);
        String result = serialization.serialize(root);
        assertEquals("5 x x", result);
    }

    @Test
    @DisplayName("Serialize tree with left child only")
    void testSerializeLeftChildOnly() {
        //   5
        //  /
        // 3
        TreeNode<Integer> root = new TreeNode<>(5, new TreeNode<>(3), null);
        String result = serialization.serialize(root);
        assertEquals("5 3 x x x", result);
    }

    @Test
    @DisplayName("Serialize tree with right child only")
    void testSerializeRightChildOnly() {
        // 5
        //  \
        //   8
        TreeNode<Integer> root = new TreeNode<>(5, null, new TreeNode<>(8));
        String result = serialization.serialize(root);
        assertEquals("5 x 8 x x", result);
    }

    @Test
    @DisplayName("Serialize balanced tree with three nodes")
    void testSerializeBalancedTree() {
        //     5
        //    / \
        //   3   8
        TreeNode<Integer> root = new TreeNode<>(5, new TreeNode<>(3), new TreeNode<>(8));
        String result = serialization.serialize(root);
        assertEquals("5 3 x x 8 x x", result);
    }

    @Test
    @DisplayName("Serialize larger tree")
    void testSerializeLargerTree() {
        //       10
        //      /  \
        //     5   15
        //    / \    \
        //   3   7   20
        TreeNode<Integer> root = new TreeNode<>(10,
                new TreeNode<>(5, new TreeNode<>(3), new TreeNode<>(7)),
                new TreeNode<>(15, null, new TreeNode<>(20)));

        String result = serialization.serialize(root);
        assertEquals("10 5 3 x x 7 x x 15 x 20 x x", result);
    }

    @Test
    @DisplayName("Deserialize null marker returns null")
    void testDeserializeNull() {
        TreeNode<Integer> result = serialization.deserialize("x");
        assertNull(result);
    }

    @Test
    @DisplayName("Deserialize single node")
    void testDeserializeSingleNode() {
        TreeNode<Integer> result = serialization.deserialize("5 x x");

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNull(result.left);
        assertNull(result.right);
    }

    @Test
    @DisplayName("Deserialize tree with left child only")
    void testDeserializeLeftChildOnly() {
        TreeNode<Integer> result = serialization.deserialize("5 3 x x x");

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNotNull(result.left);
        assertEquals(3, result.left.val);
        assertNull(result.right);
    }

    @Test
    @DisplayName("Deserialize tree with right child only")
    void testDeserializeRightChildOnly() {
        TreeNode<Integer> result = serialization.deserialize("5 x 8 x x");

        assertNotNull(result);
        assertEquals(5, result.val);
        assertNull(result.left);
        assertNotNull(result.right);
        assertEquals(8, result.right.val);
    }

    @Test
    @DisplayName("Deserialize balanced tree")
    void testDeserializeBalancedTree() {
        TreeNode<Integer> result = serialization.deserialize("5 3 x x 8 x x");

        assertNotNull(result);
        assertEquals(5, result.val);
        assertEquals(3, result.left.val);
        assertEquals(8, result.right.val);
        assertNull(result.left.left);
        assertNull(result.left.right);
        assertNull(result.right.left);
        assertNull(result.right.right);
    }

    @Test
    @DisplayName("Deserialize larger tree")
    void testDeserializeLargerTree() {
        TreeNode<Integer> result = serialization.deserialize("10 5 3 x x 7 x x 15 x 20 x x");

        assertNotNull(result);
        assertEquals(10, result.val);

        // Left subtree
        assertEquals(5, result.left.val);
        assertEquals(3, result.left.left.val);
        assertEquals(7, result.left.right.val);

        // Right subtree
        assertEquals(15, result.right.val);
        assertNull(result.right.left);
        assertEquals(20, result.right.right.val);
    }

    @Test
    @DisplayName("Round trip: serialize then deserialize preserves tree structure")
    void testRoundTripSingleNode() {
        TreeNode<Integer> original = new TreeNode<>(42);

        String serialized = serialization.serialize(original);
        TreeNode<Integer> deserialized = serialization.deserialize(serialized);

        assertNotNull(deserialized);
        assertEquals(original.val, deserialized.val);
        assertNull(deserialized.left);
        assertNull(deserialized.right);
    }

    @Test
    @DisplayName("Round trip: complex tree")
    void testRoundTripComplexTree() {
        //       10
        //      /  \
        //     5   15
        //    / \    \
        //   3   7   20
        TreeNode<Integer> original = new TreeNode<>(10,
                new TreeNode<>(5, new TreeNode<>(3), new TreeNode<>(7)),
                new TreeNode<>(15, null, new TreeNode<>(20)));

        String serialized = serialization.serialize(original);
        TreeNode<Integer> deserialized = serialization.deserialize(serialized);

        // Verify structure matches
        assertEquals(10, deserialized.val);
        assertEquals(5, deserialized.left.val);
        assertEquals(3, deserialized.left.left.val);
        assertEquals(7, deserialized.left.right.val);
        assertEquals(15, deserialized.right.val);
        assertNull(deserialized.right.left);
        assertEquals(20, deserialized.right.right.val);
    }

    @Test
    @DisplayName("Round trip: null tree")
    void testRoundTripNullTree() {
        String serialized = serialization.serialize(null);
        TreeNode<Integer> deserialized = serialization.deserialize(serialized);

        assertNull(deserialized);
    }

    @Test
    @DisplayName("Round trip: left-skewed tree")
    void testRoundTripLeftSkewedTree() {
        //     5
        //    /
        //   4
        //  /
        // 3
        TreeNode<Integer> original = new TreeNode<>(5,
                new TreeNode<>(4, new TreeNode<>(3), null),
                null);

        String serialized = serialization.serialize(original);
        TreeNode<Integer> deserialized = serialization.deserialize(serialized);

        assertEquals(5, deserialized.val);
        assertEquals(4, deserialized.left.val);
        assertEquals(3, deserialized.left.left.val);
        assertNull(deserialized.right);
    }

    @Test
    @DisplayName("Round trip: right-skewed tree")
    void testRoundTripRightSkewedTree() {
        // 5
        //  \
        //   6
        //    \
        //     7
        TreeNode<Integer> original = new TreeNode<>(5,
                null,
                new TreeNode<>(6, null, new TreeNode<>(7)));

        String serialized = serialization.serialize(original);
        TreeNode<Integer> deserialized = serialization.deserialize(serialized);

        assertEquals(5, deserialized.val);
        assertNull(deserialized.left);
        assertEquals(6, deserialized.right.val);
        assertEquals(7, deserialized.right.right.val);
    }

    @Test
    @DisplayName("Serialize tree with negative values")
    void testSerializeWithNegativeValues() {
        TreeNode<Integer> root = new TreeNode<>(-5, new TreeNode<>(-10), new TreeNode<>(3));

        String result = serialization.serialize(root);
        assertEquals("-5 -10 x x 3 x x", result);
    }

    @Test
    @DisplayName("Deserialize tree with negative values")
    void testDeserializeWithNegativeValues() {
        TreeNode<Integer> result = serialization.deserialize("-5 -10 x x 3 x x");

        assertEquals(-5, result.val);
        assertEquals(-10, result.left.val);
        assertEquals(3, result.right.val);
    }

    @Test
    @DisplayName("Serialize tree with zero value")
    void testSerializeWithZero() {
        TreeNode<Integer> root = new TreeNode<>(0, new TreeNode<>(-1), new TreeNode<>(1));

        String result = serialization.serialize(root);
        assertEquals("0 -1 x x 1 x x", result);
    }

    @Test
    @DisplayName("Round trip: tree with zero and negative values")
    void testRoundTripWithMixedValues() {
        TreeNode<Integer> original = new TreeNode<>(0,
                new TreeNode<>(-5, new TreeNode<>(-10), null),
                new TreeNode<>(5, null, new TreeNode<>(10)));

        String serialized = serialization.serialize(original);
        TreeNode<Integer> deserialized = serialization.deserialize(serialized);

        assertEquals(0, deserialized.val);
        assertEquals(-5, deserialized.left.val);
        assertEquals(-10, deserialized.left.left.val);
        assertEquals(5, deserialized.right.val);
        assertEquals(10, deserialized.right.right.val);
    }

    // Helper to compare two trees for structural equality
    private boolean treesAreEqual(TreeNode<Integer> t1, TreeNode<Integer> t2) {
        if (t1 == null && t2 == null) return true;
        if (t1 == null || t2 == null) return false;
        return t1.val.equals(t2.val)
                && treesAreEqual(t1.left, t2.left)
                && treesAreEqual(t1.right, t2.right);
    }

    @Test
    @DisplayName("Round trip preserves full tree equality")
    void testRoundTripTreeEquality() {
        TreeNode<Integer> original = new TreeNode<>(10,
                new TreeNode<>(5, new TreeNode<>(3), new TreeNode<>(7)),
                new TreeNode<>(15, new TreeNode<>(12), new TreeNode<>(20)));

        String serialized = serialization.serialize(original);
        TreeNode<Integer> deserialized = serialization.deserialize(serialized);

        assertTrue(treesAreEqual(original, deserialized));
    }
}