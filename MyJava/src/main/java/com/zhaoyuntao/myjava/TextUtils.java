package com.zhaoyuntao.myjava;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
class TextUtils {
    public static boolean isEmpty(CharSequence text) {
        return text == null || text.toString().trim().equals("");
    }
}
