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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkTest {

    static List<String> list = new ArrayList<>();
    static StringBuilder sb = new StringBuilder();

    static String text1 = "";
    static String text2 = "";

    @Setup(Level.Trial)
    public void init() {
        InputStream inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\sensitive-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String keyword;
        try {
            while ((keyword = reader.readLine()) != null) {
                list.add(keyword);
            }
            inputStream = SensitiveFilterTest.class.getClassLoader().getResourceAsStream("\\test_case\\text2.txt");
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
                .resultFormat(ResultFormatType.CSV)
                .addProfiler(GcProfiler.class)
                .build();
        new Runner(opt).run();
    }
}
