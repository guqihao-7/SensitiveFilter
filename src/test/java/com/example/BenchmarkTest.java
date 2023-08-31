package com.example;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.SensitiveProcessor;
import cn.hutool.dfa.SensitiveUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@State(Scope.Benchmark)
@Threads(5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkTest {

    static List<String> list = new ArrayList<>();
    static StringBuilder sb = new StringBuilder();

    static String text1 = "";
    static String text2 = "";

    @Param({"50", "100", "76047", "422807", "2690513"})
    private String lengthStr;

    @Setup(Level.Trial)
    public void init() {
        String textCase = null;
        if ("422807".equals(lengthStr)) {
            textCase = "\\test_case\\BigBreastsandWideHips-ch13-23.txt";
        } else if ("76047".equals(lengthStr)) {
            textCase = "\\test_case\\text.txt";
        } else if ("2690513".equals(lengthStr)) {
            textCase = "\\test_case\\text2.txt";
        } else if ("50".equals(lengthStr)) {
            textCase = "\\test_case\\text3.txt";
        } else if ("100".equals(lengthStr)) {
            textCase = "\\test_case\\text4.txt";
        }

        InputStream inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\sensitive-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String keyword;
        try {
            while ((keyword = reader.readLine()) != null) {
                list.add(keyword);
            }
            inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream(Objects.requireNonNull(textCase));
            reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            text1 = sb.toString();
            text2 = sb.toString();
            SensitiveUtil.init(list, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void hutool_dfa() {
        SensitiveUtil.sensitiveFilter(text1, true, new SensitiveProcessor() {
            @Override
            public String process(FoundWord foundWord) {
                return SensitiveProcessor.super.process(foundWord);
            }
        });
    }

    @Benchmark
    public void trie() {
        SensitiveFilter.filter(text2, true);
    }


    /*

        # JMH version: 1.36
        # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
        # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
        # VM options: -Dvisualgc.id=101955276405100 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=9871:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
        # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
        # Warmup: 5 iterations, 10 s each
        # Measurement: 5 iterations, 10 s each
        # Timeout: 10 min per iteration
        # Threads: 1 thread, will synchronize iterations
        # Benchmark mode: Throughput, ops/time
        # Benchmark: com.example.BenchmarkTest.hutool_dfa

        # Run progress: 0.00% complete, ETA 00:06:40
        # Fork: 1 of 1
        # Warmup Iteration   1: 0.007 ops/ms
        # Warmup Iteration   2: 0.007 ops/ms
        # Warmup Iteration   3: 0.007 ops/ms
        # Warmup Iteration   4: 0.007 ops/ms
        # Warmup Iteration   5: 0.007 ops/ms
        Iteration   1: 0.007 ops/ms
                         mem.alloc.norm: 184702446.261 B/op
                         mem.alloc.rate: 1143.810 MB/sec

        Iteration   2: 0.007 ops/ms
                         mem.alloc.norm: 184702446.171 B/op
                         mem.alloc.rate: 1156.438 MB/sec

        Iteration   3: 0.007 ops/ms
                         mem.alloc.norm: 184702446.261 B/op
                         mem.alloc.rate: 1154.109 MB/sec

        Iteration   4: 0.007 ops/ms
                         mem.alloc.norm: 184702446.261 B/op
                         mem.alloc.rate: 1148.041 MB/sec

        Iteration   5: 0.007 ops/ms
                         mem.alloc.norm: 184702446.171 B/op
                         mem.alloc.rate: 1157.912 MB/sec



        Result "com.example.BenchmarkTest.hutool_dfa":
          0.007 ±(99.9%) 0.001 ops/ms [Average]
          (min, avg, max) = (0.007, 0.007, 0.007), stdev = 0.001
          CI (99.9%): [0.007, 0.007] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
          184702446.225 ±(99.9%) 0.189 B/op [Average]
          (min, avg, max) = (184702446.171, 184702446.225, 184702446.261), stdev = 0.049
          CI (99.9%): [184702446.036, 184702446.414] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
          1152.062 ±(99.9%) 22.928 MB/sec [Average]
          (min, avg, max) = (1143.810, 1152.062, 1157.912), stdev = 5.954
          CI (99.9%): [1129.134, 1174.990] (assumes normal distribution)


        # JMH version: 1.36
        # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
        # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
        # VM options: -Dvisualgc.id=101955276405100 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=9871:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
        # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
        # Warmup: 5 iterations, 10 s each
        # Measurement: 5 iterations, 10 s each
        # Timeout: 10 min per iteration
        # Threads: 1 thread, will synchronize iterations
        # Benchmark mode: Throughput, ops/time
        # Benchmark: com.example.BenchmarkTest.trie

        # Run progress: 25.00% complete, ETA 00:05:21
        # Fork: 1 of 1
        # Warmup Iteration   1: 前缀树构建耗时: 16 ms
        0.012 ops/ms
        # Warmup Iteration   2: 0.013 ops/ms
        # Warmup Iteration   3: 0.013 ops/ms
        # Warmup Iteration   4: 0.013 ops/ms
        # Warmup Iteration   5: 0.013 ops/ms
        Iteration   1: 0.012 ops/ms
                         mem.alloc.norm: 95258243.512 B/op
                         mem.alloc.rate: 1054.497 MB/sec

        Iteration   2: 0.012 ops/ms
                         mem.alloc.norm: 95258243.570 B/op
                         mem.alloc.rate: 1044.616 MB/sec

        Iteration   3: 0.013 ops/ms
                         mem.alloc.norm: 95258243.429 B/op
                         mem.alloc.rate: 1083.920 MB/sec

        Iteration   4: 0.012 ops/ms
                         mem.alloc.norm: 95258243.484 B/op
                         mem.alloc.rate: 1065.766 MB/sec

        Iteration   5: 0.012 ops/ms
                         mem.alloc.norm: 95258243.484 B/op
                         mem.alloc.rate: 1067.059 MB/sec



        Result "com.example.BenchmarkTest.trie":
          0.012 ±(99.9%) 0.001 ops/ms [Average]
          (min, avg, max) = (0.012, 0.012, 0.013), stdev = 0.001
          CI (99.9%): [0.012, 0.013] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
          95258243.496 ±(99.9%) 0.198 B/op [Average]
          (min, avg, max) = (95258243.429, 95258243.496, 95258243.570), stdev = 0.052
          CI (99.9%): [95258243.297, 95258243.694] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
          1063.172 ±(99.9%) 56.851 MB/sec [Average]
          (min, avg, max) = (1044.616, 1063.172, 1083.920), stdev = 14.764
          CI (99.9%): [1006.321, 1120.023] (assumes normal distribution)


        # JMH version: 1.36
        # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
        # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
        # VM options: -Dvisualgc.id=101955276405100 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=9871:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
        # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
        # Warmup: 5 iterations, 10 s each
        # Measurement: 5 iterations, 10 s each
        # Timeout: 10 min per iteration
        # Threads: 1 thread, will synchronize iterations
        # Benchmark mode: Average time, time/op
        # Benchmark: com.example.BenchmarkTest.hutool_dfa

        # Run progress: 50.00% complete, ETA 00:03:33
        # Fork: 1 of 1
        # Warmup Iteration   1: 282.168 ms/op
        # Warmup Iteration   2: 159.203 ms/op
        # Warmup Iteration   3: 153.402 ms/op
        # Warmup Iteration   4: 152.613 ms/op
        # Warmup Iteration   5: 149.454 ms/op
        Iteration   1: 170.905 ms/op
                         mem.alloc.norm: 184702423.322 B/op
                         mem.alloc.rate: 981.819 MB/sec

        Iteration   2: 165.989 ms/op
                         mem.alloc.norm: 184702423.082 B/op
                         mem.alloc.rate: 1011.062 MB/sec

        Iteration   3: 159.877 ms/op
                         mem.alloc.norm: 184702422.857 B/op
                         mem.alloc.rate: 1048.326 MB/sec

        Iteration   4: 151.681 ms/op
                         mem.alloc.norm: 184702422.545 B/op
                         mem.alloc.rate: 1104.894 MB/sec

        Iteration   5: 161.689 ms/op
                         mem.alloc.norm: 184702422.968 B/op
                         mem.alloc.rate: 1036.634 MB/sec



        Result "com.example.BenchmarkTest.hutool_dfa":
          162.028 ±(99.9%) 27.653 ms/op [Average]
          (min, avg, max) = (151.681, 162.028, 170.905), stdev = 7.181
          CI (99.9%): [134.375, 189.682] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
          184702422.955 ±(99.9%) 1.103 B/op [Average]
          (min, avg, max) = (184702422.545, 184702422.955, 184702423.322), stdev = 0.286
          CI (99.9%): [184702421.852, 184702424.058] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
          1036.547 ±(99.9%) 177.032 MB/sec [Average]
          (min, avg, max) = (981.819, 1036.547, 1104.894), stdev = 45.975
          CI (99.9%): [859.515, 1213.579] (assumes normal distribution)


        # JMH version: 1.36
        # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
        # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
        # VM options: -Dvisualgc.id=101955276405100 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=9871:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
        # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
        # Warmup: 5 iterations, 10 s each
        # Measurement: 5 iterations, 10 s each
        # Timeout: 10 min per iteration
        # Threads: 1 thread, will synchronize iterations
        # Benchmark mode: Average time, time/op
        # Benchmark: com.example.BenchmarkTest.trie

        # Run progress: 75.00% complete, ETA 00:01:46
        # Fork: 1 of 1
        # Warmup Iteration   1: 前缀树构建耗时: 17 ms
        85.496 ms/op
        # Warmup Iteration   2: 85.731 ms/op
        # Warmup Iteration   3: 81.090 ms/op
        # Warmup Iteration   4: 80.846 ms/op
        # Warmup Iteration   5: 80.282 ms/op
        Iteration   1: 79.903 ms/op
                         mem.alloc.norm: 95258243.429 B/op
                         mem.alloc.rate: 1082.491 MB/sec

        Iteration   2: 80.416 ms/op
                         mem.alloc.norm: 95258243.456 B/op
                         mem.alloc.rate: 1075.838 MB/sec

        Iteration   3: 79.830 ms/op
                         mem.alloc.norm: 95258243.429 B/op
                         mem.alloc.rate: 1083.774 MB/sec

        Iteration   4: 80.552 ms/op
                         mem.alloc.norm: 95258243.712 B/op
                         mem.alloc.rate: 1074.207 MB/sec

        Iteration   5: 81.407 ms/op
                         mem.alloc.norm: 95258243.512 B/op
                         mem.alloc.rate: 1061.710 MB/sec



        Result "com.example.BenchmarkTest.trie":
          80.421 ±(99.9%) 2.441 ms/op [Average]
          (min, avg, max) = (79.830, 80.421, 81.407), stdev = 0.634
          CI (99.9%): [77.981, 82.862] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
          95258243.507 ±(99.9%) 0.459 B/op [Average]
          (min, avg, max) = (95258243.429, 95258243.507, 95258243.712), stdev = 0.119
          CI (99.9%): [95258243.048, 95258243.967] (assumes normal distribution)

        Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
          1075.604 ±(99.9%) 33.856 MB/sec [Average]
          (min, avg, max) = (1061.710, 1075.604, 1083.774), stdev = 8.792
          CI (99.9%): [1041.748, 1109.460] (assumes normal distribution)


        # Run complete. Total time: 00:07:07

        REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
        why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
        experiments, perform baseline and negative tests that provide experimental control, make sure
        the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
        Do not assume the numbers tell you what you want them to tell.

        Benchmark                                 Mode  Cnt          Score     Error   Units
        BenchmarkTest.hutool_dfa                 thrpt    5          0.007 ±   0.001  ops/ms
        BenchmarkTest.hutool_dfa:mem.alloc.norm  thrpt    5  184702446.225 ±   0.189    B/op
        BenchmarkTest.hutool_dfa:mem.alloc.rate  thrpt    5       1152.062 ±  22.928  MB/sec
        BenchmarkTest.trie                       thrpt    5          0.012 ±   0.001  ops/ms
        BenchmarkTest.trie:mem.alloc.norm        thrpt    5   95258243.496 ±   0.198    B/op
        BenchmarkTest.trie:mem.alloc.rate        thrpt    5       1063.172 ±  56.851  MB/sec
        BenchmarkTest.hutool_dfa                  avgt    5        162.028 ±  27.653   ms/op
        BenchmarkTest.hutool_dfa:mem.alloc.norm   avgt    5  184702422.955 ±   1.103    B/op
        BenchmarkTest.hutool_dfa:mem.alloc.rate   avgt    5       1036.547 ± 177.032  MB/sec
        BenchmarkTest.trie                        avgt    5         80.421 ±   2.441   ms/op
        BenchmarkTest.trie:mem.alloc.norm         avgt    5   95258243.507 ±   0.459    B/op
        BenchmarkTest.trie:mem.alloc.rate         avgt    5       1075.604 ±  33.856  MB/sec

        Benchmark result is saved to jmh-result.json

        Process finished with exit code 0

     */

    // 测试吞吐量, 时间, 空间
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkTest.class.getSimpleName())
                .forks(1)
                .resultFormat(ResultFormatType.JSON)
                .addProfiler(GcProfiler.class)
                .build();
        new Runner(opt).run();
    }

    /*
    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 50)

    # Run progress: 0.00% complete, ETA 00:33:20
    # Fork: 1 of 1
    # Warmup Iteration   1: 889.004 ops/ms
    # Warmup Iteration   2: 870.850 ops/ms
    # Warmup Iteration   3: 926.968 ops/ms
    # Warmup Iteration   4: 915.443 ops/ms
    # Warmup Iteration   5: 862.914 ops/ms
    Iteration   1: 848.184 ops/ms
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 8741.999 MB/sec

    Iteration   2: 926.460 ops/ms
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9540.879 MB/sec

    Iteration   3: 914.574 ops/ms
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9416.515 MB/sec

    Iteration   4: 916.350 ops/ms
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9437.093 MB/sec

    Iteration   5: 863.115 ops/ms
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 8888.214 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      893.736 ±(99.9%) 136.536 ops/ms [Average]
      (min, avg, max) = (848.184, 893.736, 926.460), stdev = 35.458
      CI (99.9%): [757.201, 1030.272] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      11352.000 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (11352.000, 11352.000, 11352.000), stdev = 0.001
      CI (99.9%): [11352.000, 11352.000] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      9204.940 ±(99.9%) 1396.548 MB/sec [Average]
      (min, avg, max) = (8741.999, 9204.940, 9540.879), stdev = 362.679
      CI (99.9%): [7808.392, 10601.487] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 100)

    # Run progress: 5.00% complete, ETA 00:33:41
    # Fork: 1 of 1
    # Warmup Iteration   1: 419.415 ops/ms
    # Warmup Iteration   2: 452.752 ops/ms
    # Warmup Iteration   3: 450.923 ops/ms
    # Warmup Iteration   4: 455.491 ops/ms
    # Warmup Iteration   5: 455.771 ops/ms
    Iteration   1: 450.781 ops/ms
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9307.662 MB/sec

    Iteration   2: 454.826 ops/ms
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9402.517 MB/sec

    Iteration   3: 453.223 ops/ms
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9356.445 MB/sec

    Iteration   4: 451.334 ops/ms
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9316.123 MB/sec

    Iteration   5: 454.667 ops/ms
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9385.916 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      452.966 ±(99.9%) 7.167 ops/ms [Average]
      (min, avg, max) = (450.781, 452.966, 454.826), stdev = 1.861
      CI (99.9%): [445.799, 460.133] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      22760.001 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (22760.001, 22760.001, 22760.001), stdev = 0.001
      CI (99.9%): [22760.001, 22760.001] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      9353.733 ±(99.9%) 160.622 MB/sec [Average]
      (min, avg, max) = (9307.662, 9353.733, 9402.517), stdev = 41.713
      CI (99.9%): [9193.111, 9514.355] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 76047)

    # Run progress: 10.00% complete, ETA 00:31:54
    # Fork: 1 of 1
    # Warmup Iteration   1: 0.521 ops/ms
    # Warmup Iteration   2: 0.533 ops/ms
    # Warmup Iteration   3: 0.533 ops/ms
    # Warmup Iteration   4: 0.536 ops/ms
    # Warmup Iteration   5: 0.530 ops/ms
    Iteration   1: 0.531 ops/ms
                     mem.alloc.norm: 10415248.508 B/op
                     mem.alloc.rate: 5018.363 MB/sec

    Iteration   2: 0.532 ops/ms
                     mem.alloc.norm: 10415248.495 B/op
                     mem.alloc.rate: 5029.840 MB/sec

    Iteration   3: 0.519 ops/ms
                     mem.alloc.norm: 10415248.532 B/op
                     mem.alloc.rate: 4910.984 MB/sec

    Iteration   4: 0.532 ops/ms
                     mem.alloc.norm: 10415248.489 B/op
                     mem.alloc.rate: 5028.843 MB/sec

    Iteration   5: 0.533 ops/ms
                     mem.alloc.norm: 10415248.506 B/op
                     mem.alloc.rate: 5040.766 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      0.529 ±(99.9%) 0.022 ops/ms [Average]
      (min, avg, max) = (0.519, 0.529, 0.533), stdev = 0.006
      CI (99.9%): [0.507, 0.551] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      10415248.506 ±(99.9%) 0.063 B/op [Average]
      (min, avg, max) = (10415248.489, 10415248.506, 10415248.532), stdev = 0.016
      CI (99.9%): [10415248.443, 10415248.569] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      5005.759 ±(99.9%) 206.282 MB/sec [Average]
      (min, avg, max) = (4910.984, 5005.759, 5040.766), stdev = 53.571
      CI (99.9%): [4799.477, 5212.042] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 422807)

    # Run progress: 15.00% complete, ETA 00:30:07
    # Fork: 1 of 1
    # Warmup Iteration   1: 0.297 ops/ms
    # Warmup Iteration   2: 0.300 ops/ms
    # Warmup Iteration   3: 0.303 ops/ms
    # Warmup Iteration   4: 0.293 ops/ms
    # Warmup Iteration   5: 0.302 ops/ms
    Iteration   1: 0.297 ops/ms
                     mem.alloc.norm: 21011608.866 B/op
                     mem.alloc.rate: 5654.642 MB/sec

    Iteration   2: 0.289 ops/ms
                     mem.alloc.norm: 21011608.900 B/op
                     mem.alloc.rate: 5511.836 MB/sec

    Iteration   3: 0.300 ops/ms
                     mem.alloc.norm: 21011608.825 B/op
                     mem.alloc.rate: 5715.126 MB/sec

    Iteration   4: 0.301 ops/ms
                     mem.alloc.norm: 21011608.852 B/op
                     mem.alloc.rate: 5746.753 MB/sec

    Iteration   5: 0.302 ops/ms
                     mem.alloc.norm: 21011608.872 B/op
                     mem.alloc.rate: 5760.808 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      0.298 ±(99.9%) 0.021 ops/ms [Average]
      (min, avg, max) = (0.289, 0.298, 0.302), stdev = 0.005
      CI (99.9%): [0.277, 0.318] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      21011608.863 ±(99.9%) 0.105 B/op [Average]
      (min, avg, max) = (21011608.825, 21011608.863, 21011608.900), stdev = 0.027
      CI (99.9%): [21011608.757, 21011608.968] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      5677.833 ±(99.9%) 390.411 MB/sec [Average]
      (min, avg, max) = (5511.836, 5677.833, 5760.808), stdev = 101.388
      CI (99.9%): [5287.422, 6068.244] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 2690513)

    # Run progress: 20.00% complete, ETA 00:28:22
    # Fork: 1 of 1
    # Warmup Iteration   1: 0.024 ops/ms
    # Warmup Iteration   2: 0.025 ops/ms
    # Warmup Iteration   3: 0.025 ops/ms
    # Warmup Iteration   4: 0.025 ops/ms
    # Warmup Iteration   5: 0.025 ops/ms
    Iteration   1: 0.025 ops/ms
                     mem.alloc.norm: 184702425.650 B/op
                     mem.alloc.rate: 4176.392 MB/sec

    Iteration   2: 0.025 ops/ms
                     mem.alloc.norm: 184702426.193 B/op
                     mem.alloc.rate: 4235.420 MB/sec

    Iteration   3: 0.025 ops/ms
                     mem.alloc.norm: 184702426.031 B/op
                     mem.alloc.rate: 4187.283 MB/sec

    Iteration   4: 0.025 ops/ms
                     mem.alloc.norm: 184702425.699 B/op
                     mem.alloc.rate: 4240.029 MB/sec

    Iteration   5: 0.025 ops/ms
                     mem.alloc.norm: 184702426.182 B/op
                     mem.alloc.rate: 4124.764 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      0.025 ±(99.9%) 0.001 ops/ms [Average]
      (min, avg, max) = (0.025, 0.025, 0.025), stdev = 0.001
      CI (99.9%): [0.024, 0.026] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      184702425.951 ±(99.9%) 1.005 B/op [Average]
      (min, avg, max) = (184702425.650, 184702425.951, 184702426.193), stdev = 0.261
      CI (99.9%): [184702424.946, 184702426.956] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      4192.777 ±(99.9%) 182.405 MB/sec [Average]
      (min, avg, max) = (4124.764, 4192.777, 4240.029), stdev = 47.370
      CI (99.9%): [4010.373, 4375.182] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 50)

    # Run progress: 25.00% complete, ETA 00:26:44
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 17 ms
    2906.236 ops/ms
    # Warmup Iteration   2: 2964.049 ops/ms
    # Warmup Iteration   3: 2854.260 ops/ms
    # Warmup Iteration   4: 2949.834 ops/ms
    # Warmup Iteration   5: 2928.055 ops/ms
    Iteration   1: 2884.305 ops/ms
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8357.303 MB/sec

    Iteration   2: 2938.775 ops/ms
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8518.075 MB/sec

    Iteration   3: 2940.042 ops/ms
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8511.512 MB/sec

    Iteration   4: 2902.884 ops/ms
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8413.627 MB/sec

    Iteration   5: 2957.553 ops/ms
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8569.790 MB/sec



    Result "com.example.BenchmarkTest.trie":
      2924.712 ±(99.9%) 115.847 ops/ms [Average]
      (min, avg, max) = (2884.305, 2924.712, 2957.553), stdev = 30.085
      CI (99.9%): [2808.865, 3040.559] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      3192.000 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (3192.000, 3192.000, 3192.000), stdev = 0.001
      CI (99.9%): [3192.000, 3192.000] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      8474.061 ±(99.9%) 332.299 MB/sec [Average]
      (min, avg, max) = (8357.303, 8474.061, 8569.790), stdev = 86.297
      CI (99.9%): [8141.763, 8806.360] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 100)

    # Run progress: 30.00% complete, ETA 00:24:55
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 18 ms
    1455.651 ops/ms
    # Warmup Iteration   2: 1485.925 ops/ms
    # Warmup Iteration   3: 1497.366 ops/ms
    # Warmup Iteration   4: 1488.834 ops/ms
    # Warmup Iteration   5: 1503.276 ops/ms
    Iteration   1: 1498.946 ops/ms
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8528.025 MB/sec

    Iteration   2: 1499.346 ops/ms
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8518.439 MB/sec

    Iteration   3: 1491.912 ops/ms
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8487.602 MB/sec

    Iteration   4: 1486.982 ops/ms
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8460.159 MB/sec

    Iteration   5: 1487.048 ops/ms
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8459.794 MB/sec



    Result "com.example.BenchmarkTest.trie":
      1492.847 ±(99.9%) 23.449 ops/ms [Average]
      (min, avg, max) = (1486.982, 1492.847, 1499.346), stdev = 6.090
      CI (99.9%): [1469.398, 1516.296] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      6264.000 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (6264.000, 6264.000, 6264.000), stdev = 0.001
      CI (99.9%): [6264.000, 6264.000] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      8490.804 ±(99.9%) 122.679 MB/sec [Average]
      (min, avg, max) = (8459.794, 8490.804, 8528.025), stdev = 31.859
      CI (99.9%): [8368.125, 8613.483] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 76047)

    # Run progress: 35.00% complete, ETA 00:23:07
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 16 ms
    1.292 ops/ms
    # Warmup Iteration   2: 1.293 ops/ms
    # Warmup Iteration   3: 1.315 ops/ms
    # Warmup Iteration   4: 1.312 ops/ms
    # Warmup Iteration   5: 1.304 ops/ms
    Iteration   1: 1.297 ops/ms
                     mem.alloc.norm: 4084720.196 B/op
                     mem.alloc.rate: 4805.470 MB/sec

    Iteration   2: 1.305 ops/ms
                     mem.alloc.norm: 4084720.202 B/op
                     mem.alloc.rate: 4836.175 MB/sec

    Iteration   3: 1.300 ops/ms
                     mem.alloc.norm: 4084720.213 B/op
                     mem.alloc.rate: 4819.116 MB/sec

    Iteration   4: 1.303 ops/ms
                     mem.alloc.norm: 4084720.207 B/op
                     mem.alloc.rate: 4829.847 MB/sec

    Iteration   5: 1.306 ops/ms
                     mem.alloc.norm: 4084720.199 B/op
                     mem.alloc.rate: 4841.858 MB/sec



    Result "com.example.BenchmarkTest.trie":
      1.302 ±(99.9%) 0.014 ops/ms [Average]
      (min, avg, max) = (1.297, 1.302, 1.306), stdev = 0.004
      CI (99.9%): [1.288, 1.316] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      4084720.203 ±(99.9%) 0.025 B/op [Average]
      (min, avg, max) = (4084720.196, 4084720.203, 4084720.213), stdev = 0.007
      CI (99.9%): [4084720.178, 4084720.229] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      4826.493 ±(99.9%) 55.717 MB/sec [Average]
      (min, avg, max) = (4805.470, 4826.493, 4841.858), stdev = 14.470
      CI (99.9%): [4770.776, 4882.210] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 422807)

    # Run progress: 40.00% complete, ETA 00:21:20
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 17 ms
    0.515 ops/ms
    # Warmup Iteration   2: 0.545 ops/ms
    # Warmup Iteration   3: 0.544 ops/ms
    # Warmup Iteration   4: 0.541 ops/ms
    # Warmup Iteration   5: 0.543 ops/ms
    Iteration   1: 0.547 ops/ms
                     mem.alloc.norm: 7409376.470 B/op
                     mem.alloc.rate: 3677.487 MB/sec

    Iteration   2: 0.541 ops/ms
                     mem.alloc.norm: 7409376.481 B/op
                     mem.alloc.rate: 3633.934 MB/sec

    Iteration   3: 0.544 ops/ms
                     mem.alloc.norm: 7409376.478 B/op
                     mem.alloc.rate: 3658.436 MB/sec

    Iteration   4: 0.537 ops/ms
                     mem.alloc.norm: 7409376.516 B/op
                     mem.alloc.rate: 3606.659 MB/sec

    Iteration   5: 0.542 ops/ms
                     mem.alloc.norm: 7409376.473 B/op
                     mem.alloc.rate: 3644.039 MB/sec



    Result "com.example.BenchmarkTest.trie":
      0.542 ±(99.9%) 0.015 ops/ms [Average]
      (min, avg, max) = (0.537, 0.542, 0.547), stdev = 0.004
      CI (99.9%): [0.527, 0.558] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      7409376.484 ±(99.9%) 0.071 B/op [Average]
      (min, avg, max) = (7409376.470, 7409376.484, 7409376.516), stdev = 0.019
      CI (99.9%): [7409376.412, 7409376.555] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      3644.111 ±(99.9%) 102.339 MB/sec [Average]
      (min, avg, max) = (3606.659, 3644.111, 3677.487), stdev = 26.577
      CI (99.9%): [3541.772, 3746.450] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 2690513)

    # Run progress: 45.00% complete, ETA 00:19:33
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 15 ms
    0.053 ops/ms
    # Warmup Iteration   2: 0.054 ops/ms
    # Warmup Iteration   3: 0.053 ops/ms
    # Warmup Iteration   4: 0.054 ops/ms
    # Warmup Iteration   5: 0.054 ops/ms
    Iteration   1: 0.054 ops/ms
                     mem.alloc.norm: 95258244.727 B/op
                     mem.alloc.rate: 4643.739 MB/sec

    Iteration   2: 0.054 ops/ms
                     mem.alloc.norm: 95258244.744 B/op
                     mem.alloc.rate: 4623.881 MB/sec

    Iteration   3: 0.054 ops/ms
                     mem.alloc.norm: 95258244.651 B/op
                     mem.alloc.rate: 4667.226 MB/sec

    Iteration   4: 0.054 ops/ms
                     mem.alloc.norm: 95258244.526 B/op
                     mem.alloc.rate: 4674.787 MB/sec

    Iteration   5: 0.054 ops/ms
                     mem.alloc.norm: 95258244.826 B/op
                     mem.alloc.rate: 4674.744 MB/sec



    Result "com.example.BenchmarkTest.trie":
      0.054 ±(99.9%) 0.001 ops/ms [Average]
      (min, avg, max) = (0.054, 0.054, 0.054), stdev = 0.001
      CI (99.9%): [0.053, 0.055] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      95258244.695 ±(99.9%) 0.436 B/op [Average]
      (min, avg, max) = (95258244.526, 95258244.695, 95258244.826), stdev = 0.113
      CI (99.9%): [95258244.259, 95258245.131] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      4656.875 ±(99.9%) 86.285 MB/sec [Average]
      (min, avg, max) = (4623.881, 4656.875, 4674.787), stdev = 22.408
      CI (99.9%): [4570.591, 4743.160] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 50)

    # Run progress: 50.00% complete, ETA 00:17:47
    # Fork: 1 of 1
    # Warmup Iteration   1: 0.006 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   2: 0.005 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   3: 0.005 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   4: 0.005 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   5: 0.005 ±(99.9%) 0.001 ms/op
    Iteration   1: 0.005 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9497.021 MB/sec

    Iteration   2: 0.005 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9582.145 MB/sec

    Iteration   3: 0.005 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9572.191 MB/sec

    Iteration   4: 0.005 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9608.499 MB/sec

    Iteration   5: 0.005 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 11352.000 B/op
                     mem.alloc.rate: 9490.311 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      0.005 ±(99.9%) 0.001 ms/op [Average]
      (min, avg, max) = (0.005, 0.005, 0.005), stdev = 0.001
      CI (99.9%): [0.005, 0.006] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      11352.000 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (11352.000, 11352.000, 11352.000), stdev = 0.001
      CI (99.9%): [11352.000, 11352.000] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      9550.033 ±(99.9%) 204.822 MB/sec [Average]
      (min, avg, max) = (9490.311, 9550.033, 9608.499), stdev = 53.192
      CI (99.9%): [9345.211, 9754.856] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 100)

    # Run progress: 55.00% complete, ETA 00:16:00
    # Fork: 1 of 1
    # Warmup Iteration   1: 0.011 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   2: 0.011 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   3: 0.011 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   4: 0.011 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   5: 0.011 ±(99.9%) 0.001 ms/op
    Iteration   1: 0.011 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9245.296 MB/sec

    Iteration   2: 0.011 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9344.219 MB/sec

    Iteration   3: 0.011 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9368.760 MB/sec

    Iteration   4: 0.011 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9390.757 MB/sec

    Iteration   5: 0.011 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 22760.001 B/op
                     mem.alloc.rate: 9387.526 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      0.011 ±(99.9%) 0.001 ms/op [Average]
      (min, avg, max) = (0.011, 0.011, 0.011), stdev = 0.001
      CI (99.9%): [0.011, 0.011] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      22760.001 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (22760.001, 22760.001, 22760.001), stdev = 0.001
      CI (99.9%): [22760.001, 22760.001] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      9347.312 ±(99.9%) 230.890 MB/sec [Average]
      (min, avg, max) = (9245.296, 9347.312, 9390.757), stdev = 59.961
      CI (99.9%): [9116.421, 9578.202] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 76047)

    # Run progress: 60.00% complete, ETA 00:14:13
    # Fork: 1 of 1
    # Warmup Iteration   1: 9.502 ±(99.9%) 0.558 ms/op
    # Warmup Iteration   2: 9.718 ±(99.9%) 0.252 ms/op
    # Warmup Iteration   3: 9.388 ±(99.9%) 0.182 ms/op
    # Warmup Iteration   4: 9.344 ±(99.9%) 0.140 ms/op
    # Warmup Iteration   5: 9.330 ±(99.9%) 0.258 ms/op
    Iteration   1: 9.345 ±(99.9%) 0.123 ms/op
                     mem.alloc.norm: 10415248.498 B/op
                     mem.alloc.rate: 5056.423 MB/sec

    Iteration   2: 9.290 ±(99.9%) 0.086 ms/op
                     mem.alloc.norm: 10415248.501 B/op
                     mem.alloc.rate: 5086.610 MB/sec

    Iteration   3: 9.271 ±(99.9%) 0.234 ms/op
                     mem.alloc.norm: 10415248.477 B/op
                     mem.alloc.rate: 5096.162 MB/sec

    Iteration   4: 9.293 ±(99.9%) 0.228 ms/op
                     mem.alloc.norm: 10415248.489 B/op
                     mem.alloc.rate: 5087.484 MB/sec

    Iteration   5: 9.284 ±(99.9%) 0.175 ms/op
                     mem.alloc.norm: 10415248.495 B/op
                     mem.alloc.rate: 5089.466 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      9.297 ±(99.9%) 0.110 ms/op [Average]
      (min, avg, max) = (9.271, 9.297, 9.345), stdev = 0.028
      CI (99.9%): [9.187, 9.406] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      10415248.492 ±(99.9%) 0.038 B/op [Average]
      (min, avg, max) = (10415248.477, 10415248.492, 10415248.501), stdev = 0.010
      CI (99.9%): [10415248.454, 10415248.530] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      5083.229 ±(99.9%) 59.476 MB/sec [Average]
      (min, avg, max) = (5056.423, 5083.229, 5096.162), stdev = 15.446
      CI (99.9%): [5023.753, 5142.705] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 422807)

    # Run progress: 65.00% complete, ETA 00:12:26
    # Fork: 1 of 1
    # Warmup Iteration   1: 16.290 ±(99.9%) 0.828 ms/op
    # Warmup Iteration   2: 15.989 ±(99.9%) 0.771 ms/op
    # Warmup Iteration   3: 15.801 ±(99.9%) 0.474 ms/op
    # Warmup Iteration   4: 15.926 ±(99.9%) 0.742 ms/op
    # Warmup Iteration   5: 15.746 ±(99.9%) 0.238 ms/op
    Iteration   1: 15.879 ±(99.9%) 0.474 ms/op
                     mem.alloc.norm: 21077848.825 B/op
                     mem.alloc.rate: 6020.553 MB/sec

    Iteration   2: 15.800 ±(99.9%) 0.232 ms/op
                     mem.alloc.norm: 21077848.811 B/op
                     mem.alloc.rate: 6049.186 MB/sec

    Iteration   3: 15.911 ±(99.9%) 0.532 ms/op
                     mem.alloc.norm: 21077848.828 B/op
                     mem.alloc.rate: 6006.591 MB/sec

    Iteration   4: 15.747 ±(99.9%) 0.336 ms/op
                     mem.alloc.norm: 21077848.829 B/op
                     mem.alloc.rate: 6068.176 MB/sec

    Iteration   5: 15.939 ±(99.9%) 0.514 ms/op
                     mem.alloc.norm: 21077848.869 B/op
                     mem.alloc.rate: 5998.175 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      15.855 ±(99.9%) 0.307 ms/op [Average]
      (min, avg, max) = (15.747, 15.855, 15.939), stdev = 0.080
      CI (99.9%): [15.548, 16.162] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      21077848.833 ±(99.9%) 0.083 B/op [Average]
      (min, avg, max) = (21077848.811, 21077848.833, 21077848.869), stdev = 0.022
      CI (99.9%): [21077848.749, 21077848.916] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      6028.536 ±(99.9%) 113.330 MB/sec [Average]
      (min, avg, max) = (5998.175, 6028.536, 6068.176), stdev = 29.431
      CI (99.9%): [5915.207, 6141.866] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.hutool_dfa
    # Parameters: (lengthStr = 2690513)

    # Run progress: 70.00% complete, ETA 00:10:39
    # Fork: 1 of 1
    # Warmup Iteration   1: 206.291 ±(99.9%) 17.099 ms/op
    # Warmup Iteration   2: 199.702 ±(99.9%) 21.779 ms/op
    # Warmup Iteration   3: 206.852 ±(99.9%) 29.567 ms/op
    # Warmup Iteration   4: 198.686 ±(99.9%) 15.781 ms/op
    # Warmup Iteration   5: 199.037 ±(99.9%) 11.647 ms/op
    Iteration   1: 205.497 ±(99.9%) 21.494 ms/op
                     mem.alloc.norm: 184702426.176 B/op
                     mem.alloc.rate: 4069.824 MB/sec

    Iteration   2: 201.271 ±(99.9%) 25.661 ms/op
                     mem.alloc.norm: 184702426.063 B/op
                     mem.alloc.rate: 4146.199 MB/sec

    Iteration   3: 200.284 ±(99.9%) 12.297 ms/op
                     mem.alloc.norm: 184702425.899 B/op
                     mem.alloc.rate: 4181.866 MB/sec

    Iteration   4: 204.453 ±(99.9%) 18.702 ms/op
                     mem.alloc.norm: 184702426.222 B/op
                     mem.alloc.rate: 4108.795 MB/sec

    Iteration   5: 203.317 ±(99.9%) 20.059 ms/op
                     mem.alloc.norm: 184702425.880 B/op
                     mem.alloc.rate: 4097.506 MB/sec



    Result "com.example.BenchmarkTest.hutool_dfa":
      202.964 ±(99.9%) 8.349 ms/op [Average]
      (min, avg, max) = (200.284, 202.964, 205.497), stdev = 2.168
      CI (99.9%): [194.616, 211.313] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.norm":
      184702426.048 ±(99.9%) 0.600 B/op [Average]
      (min, avg, max) = (184702425.880, 184702426.048, 184702426.222), stdev = 0.156
      CI (99.9%): [184702425.448, 184702426.648] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.hutool_dfa:mem.alloc.rate":
      4120.838 ±(99.9%) 168.501 MB/sec [Average]
      (min, avg, max) = (4069.824, 4120.838, 4181.866), stdev = 43.759
      CI (99.9%): [3952.337, 4289.339] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 50)

    # Run progress: 75.00% complete, ETA 00:08:54
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 17 ms
    0.002 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   2: 0.002 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   3: 0.002 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   4: 0.002 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   5: 0.002 ±(99.9%) 0.001 ms/op
    Iteration   1: 0.002 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8479.326 MB/sec

    Iteration   2: 0.002 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8401.266 MB/sec

    Iteration   3: 0.002 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8410.954 MB/sec

    Iteration   4: 0.002 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8430.190 MB/sec

    Iteration   5: 0.002 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 3192.000 B/op
                     mem.alloc.rate: 8337.355 MB/sec



    Result "com.example.BenchmarkTest.trie":
      0.002 ±(99.9%) 0.001 ms/op [Average]
      (min, avg, max) = (0.002, 0.002, 0.002), stdev = 0.001
      CI (99.9%): [0.002, 0.002] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      3192.000 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (3192.000, 3192.000, 3192.000), stdev = 0.001
      CI (99.9%): [3192.000, 3192.000] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      8411.818 ±(99.9%) 197.772 MB/sec [Average]
      (min, avg, max) = (8337.355, 8411.818, 8479.326), stdev = 51.361
      CI (99.9%): [8214.046, 8609.591] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 100)

    # Run progress: 80.00% complete, ETA 00:07:07
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 17 ms
    0.003 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   2: 0.003 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   3: 0.003 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   4: 0.003 ±(99.9%) 0.001 ms/op
    # Warmup Iteration   5: 0.003 ±(99.9%) 0.001 ms/op
    Iteration   1: 0.003 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8338.500 MB/sec

    Iteration   2: 0.003 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8354.795 MB/sec

    Iteration   3: 0.003 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8331.349 MB/sec

    Iteration   4: 0.003 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8371.689 MB/sec

    Iteration   5: 0.003 ±(99.9%) 0.001 ms/op
                     mem.alloc.norm: 6264.000 B/op
                     mem.alloc.rate: 8454.112 MB/sec



    Result "com.example.BenchmarkTest.trie":
      0.003 ±(99.9%) 0.001 ms/op [Average]
      (min, avg, max) = (0.003, 0.003, 0.003), stdev = 0.001
      CI (99.9%): [0.003, 0.003] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      6264.000 ±(99.9%) 0.001 B/op [Average]
      (min, avg, max) = (6264.000, 6264.000, 6264.000), stdev = 0.001
      CI (99.9%): [6264.000, 6264.000] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      8370.089 ±(99.9%) 190.549 MB/sec [Average]
      (min, avg, max) = (8331.349, 8370.089, 8454.112), stdev = 49.485
      CI (99.9%): [8179.540, 8560.638] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 76047)

    # Run progress: 85.00% complete, ETA 00:05:20
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 19 ms
    3.928 ±(99.9%) 0.231 ms/op
    # Warmup Iteration   2: 3.908 ±(99.9%) 0.104 ms/op
    # Warmup Iteration   3: 3.873 ±(99.9%) 0.077 ms/op
    # Warmup Iteration   4: 3.934 ±(99.9%) 0.082 ms/op
    # Warmup Iteration   5: 3.891 ±(99.9%) 0.092 ms/op
    Iteration   1: 3.895 ±(99.9%) 0.052 ms/op
                     mem.alloc.norm: 4084720.200 B/op
                     mem.alloc.rate: 4759.765 MB/sec

    Iteration   2: 3.908 ±(99.9%) 0.042 ms/op
                     mem.alloc.norm: 4084720.206 B/op
                     mem.alloc.rate: 4742.386 MB/sec

    Iteration   3: 4.181 ±(99.9%) 0.161 ms/op
                     mem.alloc.norm: 4084720.218 B/op
                     mem.alloc.rate: 4432.120 MB/sec

    Iteration   4: 4.244 ±(99.9%) 0.085 ms/op
                     mem.alloc.norm: 4084720.232 B/op
                     mem.alloc.rate: 4367.964 MB/sec

    Iteration   5: 4.342 ±(99.9%) 0.048 ms/op
                     mem.alloc.norm: 4084720.237 B/op
                     mem.alloc.rate: 4266.398 MB/sec



    Result "com.example.BenchmarkTest.trie":
      4.114 ±(99.9%) 0.779 ms/op [Average]
      (min, avg, max) = (3.895, 4.114, 4.342), stdev = 0.202
      CI (99.9%): [3.335, 4.893] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      4084720.219 ±(99.9%) 0.061 B/op [Average]
      (min, avg, max) = (4084720.200, 4084720.219, 4084720.237), stdev = 0.016
      CI (99.9%): [4084720.157, 4084720.280] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      4513.727 ±(99.9%) 865.106 MB/sec [Average]
      (min, avg, max) = (4266.398, 4513.727, 4759.765), stdev = 224.665
      CI (99.9%): [3648.621, 5378.833] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 422807)

    # Run progress: 90.00% complete, ETA 00:03:33
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 18 ms
    9.465 ±(99.9%) 0.366 ms/op
    # Warmup Iteration   2: 9.342 ±(99.9%) 0.230 ms/op
    # Warmup Iteration   3: 9.221 ±(99.9%) 0.204 ms/op
    # Warmup Iteration   4: 9.334 ±(99.9%) 0.379 ms/op
    # Warmup Iteration   5: 9.344 ±(99.9%) 0.255 ms/op
    Iteration   1: 9.249 ±(99.9%) 0.242 ms/op
                     mem.alloc.norm: 7409376.481 B/op
                     mem.alloc.rate: 3633.541 MB/sec

    Iteration   2: 9.223 ±(99.9%) 0.227 ms/op
                     mem.alloc.norm: 7409376.474 B/op
                     mem.alloc.rate: 3643.198 MB/sec

    Iteration   3: 9.232 ±(99.9%) 0.181 ms/op
                     mem.alloc.norm: 7409376.492 B/op
                     mem.alloc.rate: 3640.987 MB/sec

    Iteration   4: 9.596 ±(99.9%) 0.289 ms/op
                     mem.alloc.norm: 7409376.493 B/op
                     mem.alloc.rate: 3503.484 MB/sec

    Iteration   5: 9.494 ±(99.9%) 0.480 ms/op
                     mem.alloc.norm: 7409376.524 B/op
                     mem.alloc.rate: 3541.596 MB/sec



    Result "com.example.BenchmarkTest.trie":
      9.359 ±(99.9%) 0.671 ms/op [Average]
      (min, avg, max) = (9.223, 9.359, 9.596), stdev = 0.174
      CI (99.9%): [8.688, 10.030] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      7409376.493 ±(99.9%) 0.074 B/op [Average]
      (min, avg, max) = (7409376.474, 7409376.493, 7409376.524), stdev = 0.019
      CI (99.9%): [7409376.419, 7409376.567] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      3592.561 ±(99.9%) 251.921 MB/sec [Average]
      (min, avg, max) = (3503.484, 3592.561, 3643.198), stdev = 65.423
      CI (99.9%): [3340.640, 3844.483] (assumes normal distribution)


    # JMH version: 1.36
    # VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
    # VM invoker: D:\code\sdk\Java\jdk1.8.0_221\jre\bin\java.exe
    # VM options: -Dvisualgc.id=101440562785400 -javaagent:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\lib\idea_rt.jar=5556:D:\software\jetbrains\jetbrains 2023.1\IntelliJ IDEA 2023.1.1\bin -Dfile.encoding=UTF-8
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 5 threads, will synchronize iterations
    # Benchmark mode: Average time, time/op
    # Benchmark: com.example.BenchmarkTest.trie
    # Parameters: (lengthStr = 2690513)

    # Run progress: 95.00% complete, ETA 00:01:46
    # Fork: 1 of 1
    # Warmup Iteration   1: 前缀树构建耗时: 12 ms
    95.592 ±(99.9%) 5.857 ms/op
    # Warmup Iteration   2: 91.791 ±(99.9%) 2.595 ms/op
    # Warmup Iteration   3: 92.923 ±(99.9%) 4.688 ms/op
    # Warmup Iteration   4: 92.607 ±(99.9%) 2.690 ms/op
    # Warmup Iteration   5: 92.815 ±(99.9%) 6.619 ms/op
    Iteration   1: 91.179 ±(99.9%) 2.620 ms/op
                     mem.alloc.norm: 95258244.468 B/op
                     mem.alloc.rate: 4735.494 MB/sec

    Iteration   2: 91.668 ±(99.9%) 2.161 ms/op
                     mem.alloc.norm: 95258244.716 B/op
                     mem.alloc.rate: 4700.442 MB/sec

    Iteration   3: 100.305 ±(99.9%) 3.022 ms/op
                     mem.alloc.norm: 95258244.911 B/op
                     mem.alloc.rate: 4303.686 MB/sec

    Iteration   4: 93.203 ±(99.9%) 3.527 ms/op
                     mem.alloc.norm: 95258244.635 B/op
                     mem.alloc.rate: 4629.380 MB/sec

    Iteration   5: 94.801 ±(99.9%) 1.571 ms/op
                     mem.alloc.norm: 95258244.893 B/op
                     mem.alloc.rate: 4547.949 MB/sec



    Result "com.example.BenchmarkTest.trie":
      94.231 ±(99.9%) 14.169 ms/op [Average]
      (min, avg, max) = (91.179, 94.231, 100.305), stdev = 3.680
      CI (99.9%): [80.062, 108.400] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.norm":
      95258244.725 ±(99.9%) 0.712 B/op [Average]
      (min, avg, max) = (95258244.468, 95258244.725, 95258244.911), stdev = 0.185
      CI (99.9%): [95258244.012, 95258245.437] (assumes normal distribution)

    Secondary result "com.example.BenchmarkTest.trie:mem.alloc.rate":
      4583.390 ±(99.9%) 662.609 MB/sec [Average]
      (min, avg, max) = (4303.686, 4583.390, 4735.494), stdev = 172.077
      CI (99.9%): [3920.782, 5245.999] (assumes normal distribution)


    # Run complete. Total time: 00:35:35

    REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
    why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
    experiments, perform baseline and negative tests that provide experimental control, make sure
    the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
    Do not assume the numbers tell you what you want them to tell.

    Benchmark                                (lengthStr)   Mode  Cnt          Score      Error   Units
    BenchmarkTest.hutool_dfa                          50  thrpt    5        893.736 ±  136.536  ops/ms
    BenchmarkTest.hutool_dfa:mem.alloc.norm           50  thrpt    5      11352.000 ±    0.001    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate           50  thrpt    5       9204.940 ± 1396.548  MB/sec
    BenchmarkTest.hutool_dfa                         100  thrpt    5        452.966 ±    7.167  ops/ms
    BenchmarkTest.hutool_dfa:mem.alloc.norm          100  thrpt    5      22760.001 ±    0.001    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate          100  thrpt    5       9353.733 ±  160.622  MB/sec
    BenchmarkTest.hutool_dfa                       76047  thrpt    5          0.529 ±    0.022  ops/ms
    BenchmarkTest.hutool_dfa:mem.alloc.norm        76047  thrpt    5   10415248.506 ±    0.063    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate        76047  thrpt    5       5005.759 ±  206.282  MB/sec
    BenchmarkTest.hutool_dfa                      422807  thrpt    5          0.298 ±    0.021  ops/ms
    BenchmarkTest.hutool_dfa:mem.alloc.norm       422807  thrpt    5   21011608.863 ±    0.105    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate       422807  thrpt    5       5677.833 ±  390.411  MB/sec
    BenchmarkTest.hutool_dfa                     2690513  thrpt    5          0.025 ±    0.001  ops/ms
    BenchmarkTest.hutool_dfa:mem.alloc.norm      2690513  thrpt    5  184702425.951 ±    1.005    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate      2690513  thrpt    5       4192.777 ±  182.405  MB/sec
    BenchmarkTest.trie                                50  thrpt    5       2924.712 ±  115.847  ops/ms
    BenchmarkTest.trie:mem.alloc.norm                 50  thrpt    5       3192.000 ±    0.001    B/op
    BenchmarkTest.trie:mem.alloc.rate                 50  thrpt    5       8474.061 ±  332.299  MB/sec
    BenchmarkTest.trie                               100  thrpt    5       1492.847 ±   23.449  ops/ms
    BenchmarkTest.trie:mem.alloc.norm                100  thrpt    5       6264.000 ±    0.001    B/op
    BenchmarkTest.trie:mem.alloc.rate                100  thrpt    5       8490.804 ±  122.679  MB/sec
    BenchmarkTest.trie                             76047  thrpt    5          1.302 ±    0.014  ops/ms
    BenchmarkTest.trie:mem.alloc.norm              76047  thrpt    5    4084720.203 ±    0.025    B/op
    BenchmarkTest.trie:mem.alloc.rate              76047  thrpt    5       4826.493 ±   55.717  MB/sec
    BenchmarkTest.trie                            422807  thrpt    5          0.542 ±    0.015  ops/ms
    BenchmarkTest.trie:mem.alloc.norm             422807  thrpt    5    7409376.484 ±    0.071    B/op
    BenchmarkTest.trie:mem.alloc.rate             422807  thrpt    5       3644.111 ±  102.339  MB/sec
    BenchmarkTest.trie                           2690513  thrpt    5          0.054 ±    0.001  ops/ms
    BenchmarkTest.trie:mem.alloc.norm            2690513  thrpt    5   95258244.695 ±    0.436    B/op
    BenchmarkTest.trie:mem.alloc.rate            2690513  thrpt    5       4656.875 ±   86.285  MB/sec
    BenchmarkTest.hutool_dfa                          50   avgt    5          0.005 ±    0.001   ms/op
    BenchmarkTest.hutool_dfa:mem.alloc.norm           50   avgt    5      11352.000 ±    0.001    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate           50   avgt    5       9550.033 ±  204.822  MB/sec
    BenchmarkTest.hutool_dfa                         100   avgt    5          0.011 ±    0.001   ms/op
    BenchmarkTest.hutool_dfa:mem.alloc.norm          100   avgt    5      22760.001 ±    0.001    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate          100   avgt    5       9347.312 ±  230.890  MB/sec
    BenchmarkTest.hutool_dfa                       76047   avgt    5          9.297 ±    0.110   ms/op
    BenchmarkTest.hutool_dfa:mem.alloc.norm        76047   avgt    5   10415248.492 ±    0.038    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate        76047   avgt    5       5083.229 ±   59.476  MB/sec
    BenchmarkTest.hutool_dfa                      422807   avgt    5         15.855 ±    0.307   ms/op
    BenchmarkTest.hutool_dfa:mem.alloc.norm       422807   avgt    5   21077848.833 ±    0.083    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate       422807   avgt    5       6028.536 ±  113.330  MB/sec
    BenchmarkTest.hutool_dfa                     2690513   avgt    5        202.964 ±    8.349   ms/op
    BenchmarkTest.hutool_dfa:mem.alloc.norm      2690513   avgt    5  184702426.048 ±    0.600    B/op
    BenchmarkTest.hutool_dfa:mem.alloc.rate      2690513   avgt    5       4120.838 ±  168.501  MB/sec
    BenchmarkTest.trie                                50   avgt    5          0.002 ±    0.001   ms/op
    BenchmarkTest.trie:mem.alloc.norm                 50   avgt    5       3192.000 ±    0.001    B/op
    BenchmarkTest.trie:mem.alloc.rate                 50   avgt    5       8411.818 ±  197.772  MB/sec
    BenchmarkTest.trie                               100   avgt    5          0.003 ±    0.001   ms/op
    BenchmarkTest.trie:mem.alloc.norm                100   avgt    5       6264.000 ±    0.001    B/op
    BenchmarkTest.trie:mem.alloc.rate                100   avgt    5       8370.089 ±  190.549  MB/sec
    BenchmarkTest.trie                             76047   avgt    5          4.114 ±    0.779   ms/op
    BenchmarkTest.trie:mem.alloc.norm              76047   avgt    5    4084720.219 ±    0.061    B/op
    BenchmarkTest.trie:mem.alloc.rate              76047   avgt    5       4513.727 ±  865.106  MB/sec
    BenchmarkTest.trie                            422807   avgt    5          9.359 ±    0.671   ms/op
    BenchmarkTest.trie:mem.alloc.norm             422807   avgt    5    7409376.493 ±    0.074    B/op
    BenchmarkTest.trie:mem.alloc.rate             422807   avgt    5       3592.561 ±  251.921  MB/sec
    BenchmarkTest.trie                           2690513   avgt    5         94.231 ±   14.169   ms/op
    BenchmarkTest.trie:mem.alloc.norm            2690513   avgt    5   95258244.725 ±    0.712    B/op
    BenchmarkTest.trie:mem.alloc.rate            2690513   avgt    5       4583.390 ±  662.609  MB/sec

    Benchmark result is saved to jmh-result.csv
     */
}
