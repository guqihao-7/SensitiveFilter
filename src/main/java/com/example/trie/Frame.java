package com.example.trie;

public class Frame {
    TrieNode node;
    int j;

    public Frame(TrieNode node, int j) {
        this.node = node;
        this.j = j;
    }

    public Frame() {
    }

    public TrieNode getNode() {
        return node;
    }

    public void setNode(TrieNode node) {
        this.node = node;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "Frame{" +
                "node=" + node +
                ", j=" + j +
                '}';
    }
}
