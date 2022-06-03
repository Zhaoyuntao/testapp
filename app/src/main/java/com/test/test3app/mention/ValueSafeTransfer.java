package com.test.test3app.mention;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.test.test3app.faceview.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * created by zhaoyuntao
 * on 23/03/2022
 * description:
 */
public class ValueSafeTransfer {
    public static long value(Long value) {
        return value(value, 0L);
    }

    public static long value(Long value, long defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static int value(Integer value) {
        return value(value, 0);
    }

    public static int value(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static double value(Double value) {
        return value(value, 0d);
    }

    public static double value(Double value, double defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static float value(Float value) {
        return value(value, 0f);
    }

    public static float value(Float value, float defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static short value(Short value) {
        short defaultValue = 0;
        return value(value, defaultValue);
    }

    public static short value(Short value, short defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static boolean value(Boolean value) {
        return value(value, false);
    }

    public static boolean value(Boolean value, boolean defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static long longValue(String value) {
        return longValue(value, 0L);
    }

    public static long longValue(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static String stringValue(Object o) {
        return stringValue(o, "null");
    }

    public static String stringValue(Object o, String defaultValue) {
        return o == null ? defaultValue : String.valueOf(o);
    }

    public static <T> List<T> list(T t) {
        return new ArrayList<>(Collections.singletonList(t));
    }

    public static List<String> stringListValue(List<Long> longs) {
        if (longs == null) {
            return null;
        }
        List<String> list = new ArrayList<>(longs.size());
        for (Long longValue : longs) {
            list.add(stringValue(longValue));
        }
        return list;
    }

    public static List<Long> longListValue(List<String> strings) {
        if (strings == null) {
            return null;
        }
        List<Long> list = new ArrayList<>(strings.size());
        for (String string : strings) {
            list.add(longValue(string));
        }
        return list;
    }

    public static <T extends Cloneable> List<T> clone(Collection<T> list, ElementIterator<T> iterator) {
        if (list == null) {
            return null;
        }
        List<T> cloneList = new ArrayList<>(list.size());
        int i = 0;
        for (T t : list) {
            cloneList.add(iterator.element(i++, t));
        }
        return cloneList;
    }

    public static <T> T iterate(Collection<T> list, ElementIterator<T> iterator) {
        if (list == null) {
            return null;
        }
        int i = 0;
        for (T t : list) {
            T tResult = iterator.element(i++, t);
            if (tResult != null) {
                return tResult;
            }
        }
        return null;
    }

    public static <T> List<T> singletonList(T t) {
        return t == null ? new ArrayList<>(0) : new ArrayList<>(Collections.singletonList(t));
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T from(@NonNull String classPath) {
        Preconditions.checkNotEmpty(classPath);
        try {
            Class<?> aClass = Class.forName(classPath);
            return (T) aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("classPath not found:" + classPath);
        }
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableRes) {
        try {
            return ContextCompat.getDrawable(context, drawableRes);
        } catch (Throwable ignore) {
            return null;
        }
    }

    public static String getString(@NonNull Context context, @StringRes int stringRes) {
        try {
            return context.getString(stringRes);
        } catch (Throwable ignore) {
            return null;
        }
    }

    public static int intValue(String numberString) {
        try {
            return Integer.parseInt(numberString);
        } catch (Exception e) {
            return 0;
        }
    }

    public interface ElementIterator<T> {
        T element(int position, T t);
    }
}
