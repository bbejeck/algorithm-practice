package dev.bbejeck.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KeyValueSerde
 *
 * These only check round trips and the stated errors, so they pass with any format you pick.
 * Malformed-input checks depend on the format, so add your own for follow-up 1.
 */
public class KeyValueSerdeTest {

    private final KeyValueSerde serde = new KeyValueSerde();

    // Keys and values that break naive delimiter-based formats
    private static final List<String> TRICKY = List.of(
            "", ":", "::", "1:", "1:a", "0:", "123", "=", "a=b;c=d", "&", "\n", "\r\n", "line1\nline2",
            ",", "\"quoted\"", "\\", "\\:", "\\n", "\t", "\0", "héllo", "日本語", "😀🚀", " ", "  padded  ");

    private static Map<String, String> ordered(String... keysAndValues) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }

    private static Map<String, String> trickyMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < TRICKY.size(); i++) {
            map.put(TRICKY.get(i), TRICKY.get(TRICKY.size() - 1 - i));
        }
        return map;
    }

    // Map.equals ignores order, so also compare the entries in order
    private static void assertSameEntries(Map<String, String> expected, Map<String, String> actual) {
        assertEquals(new ArrayList<>(expected.entrySet()), new ArrayList<>(actual.entrySet()));
    }

    private void assertStringRoundTrip(Map<String, String> map) {
        assertSameEntries(map, serde.deserialize(serde.serialize(map)));
    }

    private void assertBytesRoundTrip(Map<String, String> map) {
        assertSameEntries(map, serde.fromBytes(serde.toBytes(map)));
    }

    // ---------------- string format ----------------

    @Test
    @DisplayName("Simple map round trips")
    void testSimple() {
        assertStringRoundTrip(ordered("a", "b", "key", "value"));
    }

    @Test
    @DisplayName("Empty map round trips")
    void testEmptyMap() {
        assertStringRoundTrip(Map.of());
    }

    @Test
    @DisplayName("Empty keys and values round trip")
    void testEmptyStrings() {
        assertStringRoundTrip(ordered("", "v"));
        assertStringRoundTrip(ordered("k", ""));
        assertStringRoundTrip(ordered("", ""));
        assertStringRoundTrip(ordered("a", "", "", "b", "c", ""));
    }

    @Test
    @DisplayName("Delimiters, escapes, digits, newlines, quotes and unicode all round trip")
    void testTrickyRoundTrip() {
        assertStringRoundTrip(trickyMap());
    }

    @Test
    @DisplayName("Each tricky string works alone as a key and as a value")
    void testEachTrickyString() {
        for (String s : TRICKY) {
            assertStringRoundTrip(ordered(s, "v"));
            assertStringRoundTrip(ordered("k", s));
        }
    }

    @Test
    @DisplayName("A value that looks like your own serialized output stays one value")
    void testValueIsSerializedMap() {
        String inner = serde.serialize(ordered("x", "1", "y", "2"));
        Map<String, String> map = ordered("nested", inner, inner, "as key");
        Map<String, String> result = serde.deserialize(serde.serialize(map));
        assertEquals(2, result.size());
        assertSameEntries(map, result);
    }

    @Test
    @DisplayName("Different maps serialize differently")
    void testDistinctOutputs() {
        // A format that loses information can map two of these to the same string
        List<Map<String, String>> maps = List.of(
                Map.of(),
                ordered("", ""),
                ordered("a", "b"),
                ordered("ab", ""),
                ordered("", "ab"),
                ordered("a", "", "b", ""),
                ordered("b", "", "a", ""));
        assertEquals(maps.size(), maps.stream().map(serde::serialize).distinct().count());
    }

    @Test
    @DisplayName("Entry order is preserved")
    void testOrderPreserved() {
        assertStringRoundTrip(ordered("z", "1", "a", "2", "m", "3"));
    }

    @Test
    @DisplayName("Long strings and many entries")
    void testLarge() {
        String big = "x:=\n\\".repeat(20_000);
        assertStringRoundTrip(ordered(big, "v", "k", big));
        Map<String, String> many = new LinkedHashMap<>();
        for (int i = 0; i < 10_000; i++) {
            many.put("key" + i, "value" + i);
        }
        assertStringRoundTrip(many);
    }

    @Test
    @DisplayName("Unpaired surrogates round trip in the string format")
    void testUnpairedSurrogateString() {
        assertStringRoundTrip(ordered("\uD800", "x\uDC00"));
    }

    @Test
    @DisplayName("Null keys and values are rejected")
    void testNulls() {
        Map<String, String> nullValue = new HashMap<>();
        nullValue.put("k", null);
        Map<String, String> nullKey = new HashMap<>();
        nullKey.put(null, "v");
        assertThrows(IllegalArgumentException.class, () -> serde.serialize(nullValue));
        assertThrows(IllegalArgumentException.class, () -> serde.serialize(nullKey));
        assertThrows(IllegalArgumentException.class, () -> serde.toBytes(nullValue));
        assertThrows(IllegalArgumentException.class, () -> serde.toBytes(nullKey));
    }

    // ---------------- follow-up 2: bytes ----------------

    @Test
    @DisplayName("Bytes: simple, empty and tricky maps round trip")
    void testBytesRoundTrip() {
        assertBytesRoundTrip(ordered("a", "b", "key", "value"));
        assertBytesRoundTrip(Map.of());
        assertBytesRoundTrip(ordered("", ""));
        assertBytesRoundTrip(trickyMap());
    }

    @Test
    @DisplayName("Bytes: multi-byte UTF-8 characters round trip")
    void testBytesMultiByte() {
        assertBytesRoundTrip(ordered("é", "日本語", "😀", "a😀b"));
    }

    @Test
    @DisplayName("Bytes: unpaired surrogates can't be UTF-8 encoded")
    void testBytesUnpairedSurrogate() {
        assertThrows(IllegalArgumentException.class, () -> serde.toBytes(ordered("\uD800", "v")));
    }
}
