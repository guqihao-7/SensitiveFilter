package com.example.trie;

import java.util.Objects;

public class Frame {
    TrieNode node;
    int j;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frame frame = (Frame) o;
        return j == frame.j && Objects.equals(node, frame.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, j);
    }

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
