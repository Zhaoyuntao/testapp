package com.test.test3app.observer;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class VLog {
    public static void e(String tag, String s) {
        S.e(s);
    }

    public static void d(Object tagBaseMisc, String s, int i, String tag) {
        S.s(s);
    }

    public static void d(String s, Object tagBaseMisc, int size) {
        S.s(s);
    }
}
