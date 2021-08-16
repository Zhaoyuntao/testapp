package com.test.test3app.ndk;

public class BlueTest {
    static {
        System.loadLibrary("blue_test");
    }

    public native String test();

}
