package dev.bbejeck.design;

import java.util.SortedMap;

/**
 * Versioned Key-Value Store (snapshot reads / time travel)
 *
 * Every write gets the next version from one global counter that starts at 1.
 *
 *   long put(key, value)       store value, return the write's version
 *   long delete(key)           record a delete, return its version (even if the key didn't exist)
 *   String get(key)            current value, or null if missing or deleted
 *   String get(key, version)   value as of version: the latest write to key with version <= v,
 *                              or null if there's no such write or it was a delete
 *   long currentVersion()      version of the latest write, 0 before any writes
 *
 * Reading at a version newer than currentVersion() returns the current values.
 * Null keys or values, and negative versions, throw IllegalArgumentException.
 *
 * Example:
 *   put("a", "1") -> 1, put("b", "2") -> 2, put("a", "3") -> 3, delete("b") -> 4
 *   get("a") = "3", get("a", 2) = "1", get("b", 3) = "2", get("b") = null
 *
 * Follow-up 1, snapshot(version):
 *   Return every key that had a value as of version, sorted by key.
 *   snapshot(2) = {a=1, b=2}, snapshot(4) = {a=3}
 *
 * Follow-up 2, compactBefore(version):
 *   Keeping all history forever uses unbounded memory. Discard every write that no read at
 *   version or later needs. Afterward, reads at version or later return the same results as
 *   before, and reads older than version throw IllegalArgumentException.
 *   Throws IllegalArgumentException if version > currentVersion(). Compacting to an older version
 *   than a previous compaction does nothing.
 *
 * Constraints: up to 1,000,000 writes, and a single key can have most of them.
 */
public class VersionedKeyValueStore {

    public long put(String key, String value) {
        throw new UnsupportedOperationException("TODO");
    }

    public long delete(String key) {
        throw new UnsupportedOperationException("TODO");
    }

    public String get(String key) {
        throw new UnsupportedOperationException("TODO");
    }

    public String get(String key, long version) {
        throw new UnsupportedOperationException("TODO");
    }

    public long currentVersion() {
        throw new UnsupportedOperationException("TODO");
    }

    public SortedMap<String, String> snapshot(long version) {
        throw new UnsupportedOperationException("TODO");
    }

    public void compactBefore(long version) {
        throw new UnsupportedOperationException("TODO");
    }

    // Total writes kept in memory, across all keys. The tests use it to check compaction frees space.
    int storedWrites() {
        throw new UnsupportedOperationException("TODO");
    }
}
