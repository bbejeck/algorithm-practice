package dev.bbejeck.strings;

/**
 * User: Bill Bejeck
 * Date: 7/26/25
 * Time: 2:34 PM
 */
public class Trie {
    private char val;
    public Trie[]children;
    public boolean endOfWord;
    private final Trie root;
    public Trie() {
        children = new Trie[27];
        endOfWord = false;
        root = this;
    }

    public Trie(char c) {
        this.val = c;
        children = new Trie[27];
        endOfWord = false;
        root = this;
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        Trie current = root;
        int index;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            index = getIndex(c);
            if (current.children[index] == null) {
                current.children[index] = new Trie(c);
            }
            current = current.children[index];

        }
        current.endOfWord = true;
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        Trie current = root;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            int index = getIndex(c);

            if (current.children[index] == null){
                return false;
            }
            current = current.children[index];
        }
        return current.endOfWord;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false;
        }
        Trie current = root;
        for (char c : prefix.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }
            int index = getIndex(c);
            if (current.children[index] == null){
                return false;
            }
            current = current.children[index];
        }
        return true;
    }

    int getIndex(char c) {
        if (c == '_') {
            return 26;
        }
        return c - 'a';
    }
}
