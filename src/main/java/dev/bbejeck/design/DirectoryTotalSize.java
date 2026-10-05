package dev.bbejeck.design;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Directory Total Size
 *
 * A file system is a tree of Entry objects. A file has a size and no children.
 * A directory has children, and its own size is always 0. The root is always a directory.
 *
 * Part 1: totalSize(root) returns the sum of the sizes of every file in the tree.
 *
 * Part 2: sizeOf(root, path) returns the size of the entry at path:
 *   - for a file, its size
 *   - for a directory, the total size of every file beneath it
 *   - -1 if no entry exists at that path
 *
 * Paths:
 *   - "/" is the root directory (the root's own name is ignored).
 *   - "/logs/app.log" is the child "app.log" of the child directory "logs" of the root.
 *   - A path never ends with "/" except "/" itself.
 *
 * Both methods must work on a tree 100,000 levels deep without a StackOverflowError.
 *
 * Example
 *   /
 *   ├── a.txt (100)
 *   └── logs/
 *       ├── app.log (250)
 *       └── old/
 *           └── app.1.log (50)
 *
 *   totalSize(root)              -> 400
 *   sizeOf(root, "/logs")        -> 300
 *   sizeOf(root, "/a.txt")       -> 100
 *   sizeOf(root, "/missing")     -> -1
 */
public class DirectoryTotalSize {

    // ---------------------------------------------------------------
    // Provided: do not modify
    // ---------------------------------------------------------------
    public static class Entry {
        private final String name;
        private final long size;
        private final boolean directory;
        private final List<Entry> children = new ArrayList<>();

        private Entry(String name, long size, boolean directory) {
            this.name = name;
            this.size = size;
            this.directory = directory;
        }

        public static Entry file(String name, long size) {
            return new Entry(name, size, false);
        }

        public static Entry dir(String name, Entry... children) {
            Entry dir = new Entry(name, 0, true);
            dir.children.addAll(Arrays.asList(children));
            return dir;
        }

        public String name() {
            return name;
        }

        public long size() {
            return size;
        }

        public boolean isDirectory() {
            return directory;
        }

        /** Empty for files. Mutable for directories. */
        public List<Entry> children() {
            return children;
        }
    }
    // ---------------------------------------------------------------


    public long totalSize(Entry root) {
        long totalSize = 0;
        Queue<Entry> dq = new ArrayDeque<>();
        dq.offer(root);
        while(!dq.isEmpty()) {
            Entry e = dq.poll();
            totalSize += e.size();
            for(Entry child : e.children()) {
                dq.offer(child);
            }
        }
        return totalSize;
    }

    public long sizeOf(Entry root, String path) {
        Queue<String> names = new ArrayDeque<>();
        for (String n : path.split("/")) {
            if (!n.isEmpty()) {
                names.offer(n);
            }
        }

        // A path picks one entry per level, so walk down with a single "current" entry
        Entry current = root;
        while (!names.isEmpty()) {
            String name = names.poll();
            Entry next = null;
            for (Entry child : current.children()) {
                if (child.name().equals(name)) {
                    next = child;
                    break;
                }
            }
            if (next == null) {
                return -1;
            }
            current = next;
        }
        // File: its own size. Directory: everything beneath it.
        return totalSize(current);
    }

}
