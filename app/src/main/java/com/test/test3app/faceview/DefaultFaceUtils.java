package com.test.test3app.faceview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.test.test3app.R;
import com.test.test3app.utils.StringUtils;

/**
 * created by zhaoyuntao
 * on 2019-12-17
 * description:
 */
public class DefaultFaceUtils {
    private static int[] nameBackgroundColors;
    private static int nameBackgroundColorDefault;


    private static void initColors(@NonNull Context context) {
        if (nameBackgroundColors != null) {
            return;
        }

        nameBackgroundColors = new int[]{
                ContextCompat.getColor(context, R.color.color_default_face_0),
                ContextCompat.getColor(context, R.color.color_default_face_1),
                ContextCompat.getColor(context, R.color.color_default_face_2),
                ContextCompat.getColor(context, R.color.color_default_face_3),
                ContextCompat.getColor(context, R.color.color_default_face_4)
        };
        nameBackgroundColorDefault = ContextCompat.getColor(context, R.color.color_main_white_e0e0e0);
    }

    @ColorInt
    public static int getNameBackgroundColor(@NonNull Context context, @NonNull String name) {
        Preconditions.checkNotNull(context);
        initColors(context);
        if (!TextUtils.isEmpty(name)) {
            int sum = 0;
            int count = name.length();
            for (int i = 0; i < count; i++) {
                sum += name.charAt(i);
            }
            int index = sum % nameBackgroundColors.length;
            return nameBackgroundColors[index];
        }
        return nameBackgroundColorDefault;
    }

    public static Drawable getFaceDrawable(@NonNull Context context, @NonNull String uid, String firstName, String lastName, int colorBack, float percentText) {
        AnyHolder<String> firstCh = new AnyHolder<>("");
        if (!TextUtils.isEmpty(firstName)) {
            firstName = firstName.toUpperCase();
            StringUtils.getCharCount(firstName, (item, codePoint) -> {
                firstCh.value = item;
                return false;
            });
        }

        AnyHolder<String> lastCh = new AnyHolder<>("");
        if (!TextUtils.isEmpty(lastName)) {
            lastName = lastName.toUpperCase();
            StringUtils.getCharCount(lastName, (item, codePoint) -> {
                lastCh.value = item;
                return false;
            });
        }

        String displayName = firstCh.value + lastCh.value;
        return new BlueFaceDrawable(context, displayName, colorBack, percentText);
    }
}
