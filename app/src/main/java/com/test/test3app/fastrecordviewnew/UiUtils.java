package com.test.test3app.fastrecordviewnew;

import android.app.Application;

import com.test.test3app.utils.BitmapUtils;

/**
 * created by zhaoyuntao
 * on 2020-03-19
 * description:
 */
public class UiUtils {

    public static Application application;

    public static int dipToPx(int i) {
        return BitmapUtils.dip2px(application, i);
    }
}
