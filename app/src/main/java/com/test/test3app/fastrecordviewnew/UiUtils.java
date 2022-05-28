package com.test.test3app.fastrecordviewnew;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.test.test3app.threadpool.ResourceUtils;
import com.test.test3app.utils.BitmapUtils;

/**
 * created by zhaoyuntao
 * on 2020-03-19
 * description:
 */
public class UiUtils {

    public static Application application;
    private static float sDensity;

    public static int dipToPx(int i) {
        return BitmapUtils.dip2px(application, i);
    }

    static public int pxToDip(float px) {
        return (int) (px / getDensity() + 0.5f);
    }

    public static float getDensity() {
        if (sDensity == 0f) {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) ResourceUtils.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return sDensity;
    }
    public static int getScreenWidthPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) ResourceUtils.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
