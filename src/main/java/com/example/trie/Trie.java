package com.example.trie;

import cn.hutool.core.util.CharUtil;
import com.example.SensitiveFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {
    public Trie() {
        long start = System.currentTimeMillis();
        InputStream inputStream = Trie.class.getClassLoader().getResourceAsStream("\\sensitive-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String keyword;
        try {
            while ((keyword = reader.readLine()) != null) {
                insert(keyword);
            }
        } catch (IOException e) {
            System.out.println("构建失败!" + e);
            root = null;
            return;
        }
        System.out.println("前缀树构建耗时: " + (System.currentTimeMillis() - start) + " ms");
    }

    volatile TrieNode root = new TrieNode('/'); // 存储无意义字符

    // 往 Trie 树中插入一个字符串
    public void insert(String s) {
        char[] text = s.toCharArray();
        TrieNode p = root;
        for (char c : text) {
            if (p.children.get(c) == null)
                p.children.put(c, new TrieNode(c));
            p = p.children.get(c);
        }
        p.isEndingChar = true;
    }

    // 在 Trie 树中查找有没有s这个字符串
    private boolean search(String s) {
        char[] pattern = s.toCharArray();
        TrieNode p = root;
        for (char c : pattern) {
            if (p.children.get(c) == null)
                return false;
            p = p.children.get(c);
        }
        // p.isEndingChar = false 时, 表示不能完全匹配，只是前缀
        // 为 true 时, 表示找到 pattern
        return p.isEndingChar;
    }

    /**
     * 热更新
     *
     * @param words 待新增或待删除的敏感词条
     * @param save  true 为新增，false 为删除
     */
    public void refresh(String[] words, boolean save) {
        // 1. cow
        TrieNode p = root, oldRoot = p;
        // 后序遍历做拷贝
        TrieNode newNode = copy(root);

        if (save) {
            for (String s : words) {
                char[] text = s.toCharArray();
                p = newNode;
                for (char c : text) {
                    if (p.children.get(c) == null)
                        p.children.put(c, new TrieNode(c));
                    p = p.children.get(c);
                }
                p.isEndingChar = true;
            }
        } else {
            // delete
            for (String word : words) {
                // 此时 newNode 完全是 root 的副本, root 没有, newNode 自然不可能有
                boolean find = search(word);
                if (!find) continue;

                // do delete
                p = newNode;
                for (char c : word.toCharArray())
                    p = p.children.get(c);
                p.isEndingChar = false;
            }
        }

        root = newNode;
    }

    // cow
    private TrieNode copy(TrieNode root) {
        if (root == null) return null;
        TrieNode trieNode = new TrieNode();
        trieNode.setEndingChar(root.isEndingChar);
        trieNode.setData(root.data);
        Map<Character, TrieNode> children = root.children;
        // base case
        if (isLeaf(root)) {
            trieNode.setChildren(new HashMap<>(0));
            return trieNode;
        }
        Map<Character, TrieNode> newMap = new HashMap<>(children.size());
        for (Character key : children.keySet())
            newMap.put(key, copy(children.get(key)));
        trieNode.setChildren(newMap);
        return trieNode;
    }

    private boolean isLeaf(TrieNode node) {
        return node.children.size() == 0;
    }

    /**
     * @param text   待过滤文本
     * @param greedy 是否贪婪匹配, 否则默认密集匹配
     * @return 过滤后的文本, 敏感词默认替换为*
     */
    public String filter(String text, boolean greedy) {
        if (greedy) return greedyFilter(text);
        return destinyFilter(text);
    }

    public String filter(String text) {
        return this.filter(text, true);
    }

    // 贪婪匹配
    private String greedyFilter(String text) {
        if (text == null || text.length() == 0)
            return text;
        int i = 0, j = 0;
        StringBuilder sb = new StringBuilder();
        Deque<Frame> stack = new ArrayDeque<>();
        TrieNode p = root;
        while (i < text.length()) {
            if (j >= text.length()) j = text.length() - 1;
            char c = text.charAt(j);
            // 跳过符号
            if (isSymbol(c)) {
                if (p == root) {
                    sb.append(c);
                    i++;
                }
                j++;
                continue;
            }
            p = p.children.get(c);
            if (p == null) {
                // 失配
                Frame top = null;
                // 回退到上个匹配点
                //while (!stack.isEmpty() && !(top = stack.pop()).node.isEndingChar) ;
                while (!stack.isEmpty()) {
                    top = stack.pop();
                    if (top.node.isEndingChar) {
                        stack.clear();
                        break;
                    }
                }
                if (top == null || !top.node.isEndingChar) {
                    // 说明该条路径上没有能匹配的
                    sb.append(text.charAt(i));
                    p = root;
                    j = ++i;
                } else {
                    for (int k = i; k <= top.j; k++)
                        sb.append("*");
                    j = top.j;
                    i = ++j;
                    p = root;
                }
            } else {
                // 匹配上
                stack.push(new Frame(p, j));
                j++;
            }
        }
        return sb.toString();
    }

    // 密集匹配
    private String destinyFilter(String text) {
        if (text == null || text.length() == 0)
            return text;
        int i = 0, j = 0;
        StringBuilder sb = new StringBuilder();
        TrieNode p = root;
        while (j < text.length()) {
            char c = text.charAt(j);

            // 跳过符号
            if (isSymbol(c)) {
                if (p == root) {
                    sb.append(c);
                    i++;
                }
                j++;
                continue;
            }

            p = p.children.get(c);
            if (p == null) {
                // 中途遇到没匹配上的节点
                // 只能判断以 i 开头到 j 的字符串不是敏感词
                // 不是的话只能确定 i 一定不是, 但是 i + 1 ~ j 不一定不是
                sb.append(text.charAt(i));
                p = root;
                // 来到 i 的下一位继续比
                j = ++i;
            } else if (p.isEndingChar) {
                // 匹配到叶子, 并且是敏感词
                //sb.append(REPLACEMENT);	// 这里是全部替换为 ***
                for (int k = i; k <= j; k++)    // 这里是按照长度做替换
                    sb.append("*");
                i = ++j;
                p = root;
            } else {
                // 正常情况匹配上了, 但还不是叶子, 需要继续匹配
                j++;
            }

            // 有可能有的敏感词比较短, 此时被包含在 i, j 中
            if (j == text.length() && i != j) {
                sb.append(text.charAt(i));
                j = ++i;
                p = root;
            }
        }
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF为东亚的文字范围
        return !CharUtil.isLetterOrNumber(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    // 查询一个单词是否是别的单词的前缀
    private boolean prefix(String s) {
        TrieNode p = root;
        char[] pattern = s.toCharArray();
        for (char c : pattern) {
            if (p.children.get(c) == null)
                return false;
            p = p.children.get(c);
        }
        return true;
    }

    // 查询一个单词有多少个单词前缀
    private int numOfPrefix(String s) {
        int num = 0;
        TrieNode p = root;
        char[] pattern = s.toCharArray();
        for (char c : pattern) {
            p = root.children.get(c);
            if (p == null) break;
            if (p.isEndingChar) num++;
        }
        return num;
    }
}
