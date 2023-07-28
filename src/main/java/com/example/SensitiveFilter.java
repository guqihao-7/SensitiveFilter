package com.example;

import com.example.trie.Trie;

public class SensitiveFilter {
    static Trie trie = new Trie();

    static String filter(String text, boolean greedy) {
		return trie.filter(text, greedy);
    }

    static void delete(String... words) {
        trie.refresh(words, false);
    }

    static String filter(String text) {
        return trie.filter(text);
    }
}
