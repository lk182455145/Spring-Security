package com.test;

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {
        Random random = new Random();
        StringBuffer collect = random
                .ints(6, 0, 10)
                .collect(StringBuffer::new, StringBuffer::append, (stringBuffer, sb) -> {
                    System.out.println("测试第三个参数！！！！！！！！");
                    stringBuffer.append(sb);
                }); // 测试后第三个参数无用

        String collect1 = random.ints(6, 0, 10)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining());
        System.out.println(collect);
        System.out.println(collect1);


        List<String> list = Arrays.asList("a","b","c","d","e","b","d");
        Set<String> set = list.stream().collect(Collectors.toSet());
        HashSet<String> set1 = list.stream().collect(HashSet::new, Set::add, Set::addAll);
        set.forEach(x -> System.out.println(x));
        System.out.println("----------set--------------");
        set1.forEach(x-> System.out.println(x));

    }
}
