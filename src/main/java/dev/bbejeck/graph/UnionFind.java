package dev.bbejeck.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 12/14/24
 * Time: 1:23 PM
 */
public class UnionFind<T> {

    private final Map<T, T> parent = new HashMap<>();
    private final Map<T, Integer> size = new HashMap<>();

    public void set(T item) {
        if (!parent.containsKey(item)) {
            parent.put(item, item);
            size.put(item, 1);
        }
    }


    public T find(T item) {

        if (!parent.containsKey(item)) {
            throw new IllegalStateException("Item " + item + " not present");
        }

        T root = item;
        while (!root.equals(parent.get(root))) {
            root = parent.get(root);
        }
        T current = item;
        while (!current.equals(root)) {
            T next = parent.get(current);
            parent.put(current, root);
            current = next;
        }

        return root;
    }

    public void union(T item1, T item2) {

        T root1 = find(item1);
        T root2 = find(item2);

        if (root1.equals(root2)) {
            return;
        }

        int size1 = size.get(item1);
        int size2 = size.get(item2);

        if (size1 < size2) {
            parent.put(root1, root2);
            size.put(root2, size1 + size2);
        } else {
            parent.put(root2, root1);
            size.put(root1, size1 + size2);
        }

    }

    public boolean connected(T item1, T item2) {
        return find(item1).equals(find(item2));
    }

    public Map<T, T> unionFindMap() {
        return new HashMap<>(parent);}

}
