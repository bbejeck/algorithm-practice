package dev.bbejeck.lru;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Bill Bejeck
 * Date: 8/4/25
 * Time: 10:59 PM
 */
class LRUCache {

    private final Map<Integer, Node<Integer, Integer>> cache = new HashMap<>();
    private final int capacity;
    private Node<Integer, Integer> head;
    private Node<Integer, Integer> tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        if (cache.containsKey(key)) {
            Node<Integer, Integer> node = cache.get(key);
            moveToFront(node);
            return node.val;
        }
        return -1;
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            Node<Integer, Integer> node = cache.get(key);
            node.val = value;
            moveToFront(node);
        } else {
            if (cache.size() == capacity) {
                removeTail();
            }
            Node<Integer, Integer> newNode = new Node<>(key, value);
            cache.put(key, newNode);
            addToFront(newNode);
        }
    }

    private void moveToFront(Node<Integer, Integer> node) {
        if (node == head) {
            return;
        }
        removeNode(node);
        addToFront(node);
    }

    private void removeNode(Node<Integer, Integer> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private void addToFront(Node<Integer, Integer> node) {
        node.next = head;
        node.prev = null;

        if (head != null) {
            head.prev = node;
        }
        head = node;

        if (tail == null) {
            tail = node;
        }
    }

    private void removeTail() {
        if (tail != null) {
            cache.remove(tail.key);
            if (tail.prev != null) {
                tail.prev.next = null;
                tail = tail.prev;
            } else {
                head = null;
                tail = null;
            }
        }
    }


    static class Node<K,V> {
        public Node<K,V> next;
        public Node<K,V> prev;
        public V val;
        public K key;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }
}
