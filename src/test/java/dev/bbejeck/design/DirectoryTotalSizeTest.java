package dev.bbejeck.design;

import dev.bbejeck.design.DirectoryTotalSize.Entry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.bbejeck.design.DirectoryTotalSize.Entry.dir;
import static dev.bbejeck.design.DirectoryTotalSize.Entry.file;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DirectoryTotalSize
 */
public class DirectoryTotalSizeTest {

    private final DirectoryTotalSize sizer = new DirectoryTotalSize();

    /** The tree from the problem statement. */
    private Entry exampleTree() {
        return dir("",
                file("a.txt", 100),
                dir("logs",
                        file("app.log", 250),
                        dir("old",
                                file("app.1.log", 50))));
    }

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        Entry root = exampleTree();
        assertEquals(400, sizer.totalSize(root));
        assertEquals(300, sizer.sizeOf(root, "/logs"));
        assertEquals(100, sizer.sizeOf(root, "/a.txt"));
        assertEquals(-1, sizer.sizeOf(root, "/missing"));
    }

    @Test
    @DisplayName("Empty root directory has size 0")
    void testEmptyRoot() {
        Entry root = dir("");
        assertEquals(0, sizer.totalSize(root));
        assertEquals(0, sizer.sizeOf(root, "/"));
    }

    @Test
    @DisplayName("Flat directory sums its files")
    void testFlatDirectory() {
        Entry root = dir("", file("a", 1), file("b", 2), file("c", 3));
        assertEquals(6, sizer.totalSize(root));
    }

    @Test
    @DisplayName("Empty subdirectories add nothing")
    void testEmptySubdirectories() {
        Entry root = dir("", dir("empty"), dir("x", dir("y")), file("f", 9));
        assertEquals(9, sizer.totalSize(root));
        assertEquals(0, sizer.sizeOf(root, "/x/y"));
    }

    @Test
    @DisplayName("sizeOf(\"/\") equals totalSize")
    void testRootPath() {
        Entry root = exampleTree();
        assertEquals(sizer.totalSize(root), sizer.sizeOf(root, "/"));
    }

    @Test
    @DisplayName("sizeOf a nested file and a nested directory")
    void testNestedPaths() {
        Entry root = exampleTree();
        assertEquals(250, sizer.sizeOf(root, "/logs/app.log"));
        assertEquals(50, sizer.sizeOf(root, "/logs/old"));
        assertEquals(50, sizer.sizeOf(root, "/logs/old/app.1.log"));
    }

    @Test
    @DisplayName("Missing paths return -1 at every depth")
    void testMissingPaths() {
        Entry root = exampleTree();
        assertEquals(-1, sizer.sizeOf(root, "/logs/missing"));
        assertEquals(-1, sizer.sizeOf(root, "/logs/old/missing.log"));
        assertEquals(-1, sizer.sizeOf(root, "/nope/old"));
    }

    @Test
    @DisplayName("A path that goes through a file returns -1")
    void testPathThroughFile() {
        Entry root = exampleTree();
        assertEquals(-1, sizer.sizeOf(root, "/a.txt/b"));
    }

    @Test
    @DisplayName("Names must match exactly, not by prefix")
    void testExactNameMatch() {
        Entry root = dir("", dir("logs", file("x", 5)), dir("logs2", file("y", 7)));
        assertEquals(5, sizer.sizeOf(root, "/logs"));
        assertEquals(7, sizer.sizeOf(root, "/logs2"));
        assertEquals(-1, sizer.sizeOf(root, "/log"));
    }

    @Test
    @DisplayName("A zero-byte file that exists returns 0, not -1")
    void testZeroByteFile() {
        Entry root = dir("", file("empty.txt", 0));
        assertEquals(0, sizer.sizeOf(root, "/empty.txt"));
    }

    @Test
    @DisplayName("Totals larger than Integer.MAX_VALUE don't overflow")
    void testLargeSizes() {
        Entry root = dir("", file("big1", 3_000_000_000L), dir("d", file("big2", 3_000_000_000L)));
        assertEquals(6_000_000_000L, sizer.totalSize(root));
        assertEquals(3_000_000_000L, sizer.sizeOf(root, "/d"));
    }

    @Test
    @DisplayName("A tree 100,000 levels deep doesn't overflow the stack")
    void testDeepTree() {
        int depth = 100_000;
        Entry root = dir("");
        Entry current = root;
        StringBuilder midPath = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            current.children().add(file("f", 1));
            Entry next = dir("d");
            current.children().add(next);
            current = next;
            if (i < depth / 2) {
                midPath.append("/d");
            }
        }
        current.children().add(file("last", 7));

        assertEquals(depth + 7, sizer.totalSize(root));
        // The directory at depth/2 holds the files of the remaining levels.
        assertEquals(depth / 2 + 7, sizer.sizeOf(root, midPath.toString()));
    }
}
