package com.example;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.SensitiveProcessor;
import cn.hutool.dfa.SensitiveUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SensitiveFilterTest {

    // 贪婪匹配
    @org.junit.Test
    public void test() throws IOException {
        InputStream inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\sensitive-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String keyword;
        List<String> list = new ArrayList<>();
        while ((keyword = reader.readLine()) != null) {
            list.add(keyword);
        }

        SensitiveUtil.init(list, false);

        inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\test_case\\text2.txt");
        reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null)
            sb.append(line);
        String text1 = sb.toString();
        String text2 = sb.toString();
        /*
        text2 = text1 = "少先队里的少妇";
       本来想的是过滤后应该为 ***里的**, 但测试了 hutool-dfa 屏蔽后也不是这样, 暂时先放置
            7 少先队里的少妇
            hutool-dfa 过滤耗时: 6 ms
            hutool-dfa 过滤后所得字符串为: 7 ***里***
            前缀树构建耗时: 18 ms
            前缀树过滤耗时: 22
            前缀树过滤后所得字符串为: 7 ***里***
            最长公共子序列的长度为: 7 , 与 hutool 的百分比为: 1.0
         */
        //System.out.println(text1.length() + " " + text1);
        System.out.println(text1.length());
        long start = System.currentTimeMillis();
        String s1 = SensitiveUtil.sensitiveFilter(text1, true, new SensitiveProcessor() {
            @Override
            public String process(FoundWord foundWord) {
                return SensitiveProcessor.super.process(foundWord);
            }
        });
        System.out.println("hutool-dfa 过滤耗时: " + (System.currentTimeMillis() - start) + " ms");
        //System.out.println("hutool-dfa 过滤后所得字符串为: " + s1.length() + " " + s1);
        System.out.println("hutool-dfa 过滤后所得字符串为: " + s1.length());
        start = System.currentTimeMillis();
        String s2 = SensitiveFilter.filter(text2);
        System.out.println("前缀树过滤耗时: " + (System.currentTimeMillis() - start) + " ms");
        //System.out.println("前缀树过滤后所得字符串为: " + s2.length() + " " + s2);
        System.out.println("前缀树过滤后所得字符串为: " + s2.length());
        int len = longestCommonSubsequence(s1, s2);
        System.out.println("最长公共子序列的长度为: " + len + " , 与 hutool 的百分比为: " + (double) len / s1.length());
    }

    // 密集匹配
    @org.junit.Test
    public void test3() throws IOException {
        InputStream inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\sensitive-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String keyword;
        List<String> list = new ArrayList<>();
        while ((keyword = reader.readLine()) != null) {
            list.add(keyword);
        }

        SensitiveUtil.init(list, false);

        inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\test_case\\text2.txt");
        reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null)
            sb.append(line);
        String text1 = sb.toString();
        String text2 = sb.toString();
        System.out.println(text1.length() + " " + text1);
        long start = System.currentTimeMillis();
        String s1 = SensitiveUtil.sensitiveFilter(text1, false, new SensitiveProcessor() {
            @Override
            public String process(FoundWord foundWord) {
                return SensitiveProcessor.super.process(foundWord);
            }
        });
        System.out.println("hutool-dfa 过滤耗时: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("hutool-dfa 过滤后所得字符串为: " + s1.length() + " " + s1);
        start = System.currentTimeMillis();
        String s2 = SensitiveFilter.filter(text2, false);
        System.out.println("前缀树过滤耗时: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("前缀树过滤后所得字符串为: " + s2.length() + " " + s2);
        int len = longestCommonSubsequence(s1, s2);
        System.out.println("最长公共子序列的长度为: " + len + " , 与 hutool 的百分比为: " + (double) len / s1.length());
    }

    @Test
    public void test2() {
        String s = "兼职女老师";
        System.out.println(SensitiveFilter.filter(s, false));
        SensitiveFilter.delete("兼职");
        System.out.println(SensitiveFilter.filter(s, false));
    }

    @Test
    public void test4() throws IOException {
        InputStream inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\sensitive-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String keyword;
        List<String> list = new ArrayList<>();
        while ((keyword = reader.readLine()) != null) {
            list.add(keyword);
        }
        SensitiveUtil.init(list, false);
        inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\test_case\\BigBreastsandWideHips-ch13-18.txt");
        reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null)
            sb.append(line.replaceAll(" ", ""));
        String text1 = sb.toString();
        String text2 = sb.toString();
        System.out.println(text1.length() + " " + text1);
        long start = System.currentTimeMillis();
        String s1 = SensitiveUtil.sensitiveFilter(text1, true, new SensitiveProcessor() {
            @Override
            public String process(FoundWord foundWord) {
                return SensitiveProcessor.super.process(foundWord);
            }
        });
        System.out.println("hutool-dfa 过滤耗时: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("hutool-dfa 过滤后所得字符串为: " + s1.length() + " " + s1);
        start = System.currentTimeMillis();
        String s2 = SensitiveFilter.filter(text2, true);
        System.out.println("前缀树过滤耗时: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("前缀树过滤后所得字符串为: " + s2.length() + " " + s2);
        int len = longestCommonSubsequence(s1, s2);
        System.out.println("最长公共子序列的长度为: " + len + " , 与 hutool 的百分比为: " + (double) len / s1.length());
    }

    @Test
    public void test5() throws IOException {
        InputStream inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\sensitive-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String keyword;
        int idx = 0;
        while ((keyword = reader.readLine()) != null) {
            idx++;
            if (keyword.equals("大"))
                System.out.println(idx);
        }
    }

    public int longestCommonSubsequence(String text1, String text2) {
        char[] t = text2.toCharArray();
        int m = t.length;
        int[] f = new int[m + 1];
        for (char x : text1.toCharArray())
            for (int j = 0, pre = 0; j < m; ++j) {
                int tmp = f[j + 1];
                f[j + 1] = x == t[j] ? pre + 1 : Math.max(f[j + 1], f[j]);
                pre = tmp;
            }
        return f[m];
    }
}
