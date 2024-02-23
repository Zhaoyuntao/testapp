package com.zhaoyuntao.rename;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 16/11/2023
 */
public class Person {
    private String age;
    static List<String> ages = new ArrayList<>();

    static {
        ages.add("18");
        ages.add("30");
        ages.add("40");
        ages.add("100");
    }
}
