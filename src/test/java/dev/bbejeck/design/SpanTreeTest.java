package dev.bbejeck.design;

import dev.bbejeck.design.SpanTree.Span;
import dev.bbejeck.design.SpanTree.SpanNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SpanTree
 */
public class SpanTreeTest {

    private final SpanTree spanTree = new SpanTree();

    /** Renders a tree as "id[child,child[grandchild]]" so structure and order can be compared. */
    private static String render(SpanNode node) {
        StringBuilder sb = new StringBuilder(node.span().id());
        if (!node.children().isEmpty()) {
            sb.append('[');
            for (int i = 0; i < node.children().size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(render(node.children().get(i)));
            }
            sb.append(']');
        }
        return sb.toString();
    }

    private static List<String> renderAll(List<SpanNode> roots) {
        List<String> rendered = new ArrayList<>();
        for (SpanNode root : roots) {
            rendered.add(render(root));
        }
        return rendered;
    }

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(
                new Span("c", "a", "db query"),
                new Span("a", null, "GET /home"),
                new Span("b", "a", "auth"),
                new Span("d", "b", "cache lookup")));
        assertEquals(List.of("a[c,b[d]]"), renderAll(roots));
    }

    @Test
    @DisplayName("Empty input returns no roots")
    void testEmpty() {
        assertEquals(List.of(), spanTree.buildTrees(List.of()));
    }

    @Test
    @DisplayName("A single root span")
    void testSingleSpan() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(new Span("a", null, "root")));
        assertEquals(List.of("a"), renderAll(roots));
    }

    @Test
    @DisplayName("Parents listed before children")
    void testParentFirst() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(
                new Span("a", null, "root"),
                new Span("b", "a", "child"),
                new Span("c", "b", "grandchild")));
        assertEquals(List.of("a[b[c]]"), renderAll(roots));
    }

    @Test
    @DisplayName("Children listed before parents")
    void testChildFirst() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(
                new Span("c", "b", "grandchild"),
                new Span("b", "a", "child"),
                new Span("a", null, "root")));
        assertEquals(List.of("a[b[c]]"), renderAll(roots));
    }

    @Test
    @DisplayName("Children are ordered by input position, even when the parent comes last")
    void testChildOrder() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(
                new Span("z", "p", ""),
                new Span("x", "p", ""),
                new Span("y", "p", ""),
                new Span("p", null, "")));
        assertEquals(List.of("p[z,x,y]"), renderAll(roots));
    }

    @Test
    @DisplayName("Multiple roots are returned in input order")
    void testMultipleRoots() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(
                new Span("r2-child", "r2", ""),
                new Span("r1", null, ""),
                new Span("r2", null, ""),
                new Span("r1-child", "r1", "")));
        assertEquals(List.of("r1[r1-child]", "r2[r2-child]"), renderAll(roots));
    }

    @Test
    @DisplayName("An orphan becomes a root and keeps its own children")
    void testOrphan() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(
                new Span("a", null, ""),
                new Span("orphan", "missing", ""),
                new Span("orphan-child", "orphan", ""),
                new Span("b", "a", "")));
        assertEquals(List.of("a[b]", "orphan[orphan-child]"), renderAll(roots));
    }

    @Test
    @DisplayName("Orphans and real roots are interleaved in input order")
    void testOrphanOrderWithRoots() {
        List<SpanNode> roots = spanTree.buildTrees(List.of(
                new Span("o1", "gone", ""),
                new Span("r", null, ""),
                new Span("o2", "gone", "")));
        assertEquals(List.of("o1", "r", "o2"), renderAll(roots));
    }

    @Test
    @DisplayName("Nodes hold the original Span objects")
    void testSpanPreserved() {
        Span span = new Span("a", null, "GET /home");
        List<SpanNode> roots = spanTree.buildTrees(List.of(span));
        assertSame(span, roots.get(0).span());
    }

    @Test
    @DisplayName("Duplicate span ids throw IllegalArgumentException")
    void testDuplicateIds() {
        assertThrows(IllegalArgumentException.class, () -> spanTree.buildTrees(List.of(
                new Span("a", null, "first"),
                new Span("a", null, "second"))));
    }

    @Test
    @DisplayName("100,000 spans in one chain, listed leaf first, build quickly")
    void testLargeChain() {
        int n = 100_000;
        List<Span> spans = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            spans.add(new Span("s" + i, i == 0 ? null : "s" + (i - 1), ""));
        }

        List<SpanNode> roots = assertTimeoutPreemptively(Duration.ofSeconds(2),
                () -> spanTree.buildTrees(spans));

        assertEquals(1, roots.size());
        // Walk the chain iteratively to check every link.
        SpanNode node = roots.get(0);
        for (int i = 0; i < n; i++) {
            assertEquals("s" + i, node.span().id());
            if (i < n - 1) {
                assertEquals(1, node.children().size());
                node = node.children().get(0);
            }
        }
        assertTrue(node.children().isEmpty());
    }
}
