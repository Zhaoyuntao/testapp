package com.test.test3app.activity;

import android.app.Activity;

/**
 * created by zhaoyuntao
 * on 27/03/2023
 */
public class ZRouter {
    private String name;
    private Class<? extends Activity> clazz;

    public ZRouter(String name, Class<? extends Activity> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getActivity() {
        return clazz;
    }
}
