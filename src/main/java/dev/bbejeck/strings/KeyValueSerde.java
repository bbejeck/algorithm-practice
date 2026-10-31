package dev.bbejeck.strings;

import java.util.Map;

/**
 * Key-Value Serialization (reported OpenAI interview question)
 *
 * serialize(map) turns a Map<String, String> into one String, and deserialize(s) turns it back.
 * deserialize(serialize(m)) must equal m for every map, including ones with:
 *   - empty keys or values
 *   - any character in a key or value: whatever delimiters your format uses, digits, newlines, unicode
 *   - no entries
 * Entry order is preserved. Null keys or values throw IllegalArgumentException.
 *
 * You choose the format. Be ready to explain why no key or value can break it.
 *
 * Follow-up 1: deserialize gets input from outside, so it may be corrupt. Throw
 *   IllegalArgumentException on malformed input instead of returning garbage.
 *
 * Follow-up 2, toBytes(map) / fromBytes(bytes):
 *   The output goes to a file or over the network, so produce bytes instead of a String.
 *   fromBytes(toBytes(m)) must equal m. Strings that can't be encoded as UTF-8 (unpaired
 *   surrogates) throw IllegalArgumentException.
 */
public class KeyValueSerde {

    public String serialize(Map<String, String> map) {
        throw new UnsupportedOperationException("TODO");
    }

    public Map<String, String> deserialize(String s) {
        throw new UnsupportedOperationException("TODO");
    }

    public byte[] toBytes(Map<String, String> map) {
        throw new UnsupportedOperationException("TODO");
    }

    public Map<String, String> fromBytes(byte[] bytes) {
        throw new UnsupportedOperationException("TODO");
    }
}
