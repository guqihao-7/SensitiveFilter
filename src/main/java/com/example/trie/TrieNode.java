package com.example.trie;

import java.util.HashMap;
import java.util.Map;

/*
    内存布局:
    com.example.trie.TrieNode object internals:
    OFF  SZ                  TYPE DESCRIPTION               VALUE
      0   8                       (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
      8   4                       (object header: class)    0x01019168
     12   1               boolean TrieNode.isEndingChar     false
     13   3                       (alignment/padding gap)
     16   4   java.lang.Character TrieNode.data             null
     20   4         java.util.Map TrieNode.children         (object)
    Instance size: 24 bytes
    Space losses: 3 bytes internal + 0 bytes external = 3 bytes total

    The Intel486 processor (and newer processors since) guarantees that the following basic memory operations will
always be carried out atomically:
    • Reading or writing a byte
    • Reading or writing a word aligned on a 16-bit boundary
    • Reading or writing a doubleword aligned on a 32-bit boundary
    The Pentium processor (and newer processors since) guarantees that the following additional memory operations
    will always be carried out atomically:
    • Reading or writing a quadword aligned on a 64-bit boundary
    • 16-bit accesses to uncached memory locations that fit within a 32-bit data bus
    The P6 family processors (and newer processors since) guarantee that the following additional memory operation
    will always be carried out atomically:
    • Unaligned 16-, 32-, and 64-bit accesses to cached memory that fit within a cache line
 */
public class TrieNode {
    // 默认为 false
    volatile boolean isEndingChar;  // 根据 Intel 手册和内存布局可知, 读写就是原子操作
    volatile Character data;
    // 因为包含汉字, 所以不用数组
    volatile Map<Character, TrieNode> children = new HashMap<>();

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
