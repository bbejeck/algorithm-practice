package dev.bbejeck.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Span Tree from Unordered Spans
 * <p>
 * A distributed trace is made of spans. Each span has an id and the id of its parent
 * span. A root span has a null parentId. Spans arrive in any order, so a child can
 * arrive before its parent.
 * <p>
 * Implement buildTrees(spans), which links the spans into trees and returns the roots.
 * <p>
 * - A span whose parentId is null is a root.
 * - A span whose parentId doesn't match any span (an orphan) is also returned as a root.
 * - Roots are returned in the order they appear in the input.
 * - Each node's children are in the order they appear in the input.
 * - Throw IllegalArgumentException if two spans have the same id.
 * - The input never contains cycles.
 * - Must handle 100,000 spans quickly (aim for O(n)).
 * <p>
 * Example
 * input (in this order):
 * Span("c", "a", "db query")
 * Span("a", null, "GET /home")
 * Span("b", "a", "auth")
 * Span("d", "b", "cache lookup")
 * <p>
 * result: one root
 * a  GET /home
 * ├── c  db query
 * └── b  auth
 * └── d  cache lookup
 */
public class SpanTree {

    // ---------------------------------------------------------------
    // Provided: do not modify
    // ---------------------------------------------------------------
    public record Span(String id, String parentId, String name) {
    }

    public static class SpanNode {
        private final Span span;
        private final List<SpanNode> children = new ArrayList<>();

        public SpanNode(Span span) {
            this.span = span;
        }

        public Span span() {
            return span;
        }

        public List<SpanNode> children() {
            return children;
        }
    }
    // ---------------------------------------------------------------

    public List<SpanNode> buildTrees(List<Span> spans) {
        Set<SpanNode> roots = new LinkedHashSet<>();
        Map<String, SpanNode> map = new HashMap<>();
        Map<String, List<SpanNode>> futureChildren = new HashMap<>();
        for (Span span : spans) {
            SpanNode spanNode = new SpanNode(span);
            if (span.parentId == null) {
                roots.add(spanNode);
            }
            if (!map.containsKey(span.id)) {
                map.put(span.id, spanNode);
            } else {
                throw new IllegalArgumentException("Duplicate span id: " + span.id);
            }

            if (span.parentId != null) {
                SpanNode parent = map.get(span.parentId);
                if (parent != null) {
                    parent.children.add(spanNode);
                } else {
                    List<SpanNode> children = futureChildren.get(span.parentId);
                    if (children == null) {
                        children = new ArrayList<>();
                        children.add(spanNode);
                        futureChildren.put(span.parentId, children);
                    } else {
                        children.add(spanNode);
                    }
                    roots.add(spanNode);
                }
            }
            List<SpanNode> children = futureChildren.remove(span.id);
            if (children != null) {
                spanNode.children.addAll(children);
                for (SpanNode child : children) {
                    roots.remove(child);
                }
            }
        }
        return new ArrayList<>(roots);
    }
}
