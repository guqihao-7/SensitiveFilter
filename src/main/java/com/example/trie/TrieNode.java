package com.example.trie;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    // 默认为 false
    boolean isEndingChar;
    Character data;
    // 因为包含汉字, 所以不用数组
    Map<Character, TrieNode> children = new HashMap<>();

    public TrieNode(char data) {
        this.data = data;
    }

    public TrieNode() {
    }

    public void setEndingChar(boolean endingChar) {
        isEndingChar = endingChar;
    }

    public void setData(char data) {
        this.data = data;
    }

    public void setChildren(Map<Character, TrieNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "TrieNode{" +
                "isEndingChar=" + isEndingChar +
                ", data=" + data +
                ", children=" + children +
                '}';
    }
}
