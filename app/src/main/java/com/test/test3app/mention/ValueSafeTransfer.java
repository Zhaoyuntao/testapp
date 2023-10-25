package com.test.test3app.mention;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.utils.log.S;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * created by zhaoyuntao
 * on 23/03/2022
 * description:
 */
public class ValueSafeTransfer {
    public static long value(@Nullable Long value) {
        return value(value, 0L);
    }

    public static long value(@Nullable Long value, long defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static int value(@Nullable Integer value) {
        return value(value, 0);
    }

    public static int value(@Nullable Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static double value(@Nullable Double value) {
        return value(value, 0d);
    }

    public static double value(@Nullable Double value, double defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static float value(@Nullable Float value) {
        return value(value, 0f);
    }

    public static float value(@Nullable Float value, float defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static short value(@Nullable Short value) {
        short defaultValue = 0;
        return value(value, defaultValue);
    }

    public static short value(@Nullable Short value, short defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static boolean value(@Nullable Boolean value) {
        return value(value, false);
    }

    public static boolean value(@Nullable Boolean value, boolean defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String value(@Nullable Object o) {
        return o == null ? "<NULL>" : o.toString();
    }

    public static long longValue(String value) {
        return longValue(value, 0L);
    }

    public static long longValue(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (Throwable e) {
            e.printStackTrace();
            S.e(e);
            return defaultValue;
        }
    }

    public static float floatValue(String value) {
        return floatValue(value, 0);
    }

    public static float floatValue(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (Throwable e) {
            e.printStackTrace();
            S.e(e);
            return defaultValue;
        }
    }

    public static String stringValue(Object o) {
        return stringValue(o, "null");
    }

    public static String stringValue(Object o, String defaultValue) {
        return o == null ? defaultValue : String.valueOf(o);
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

    @NonNull
    public static <T1, T2> List<T2> transformList(@Nullable Collection<T1> originList, @NonNull Transfer<T1, T2> transformer) {
        if (originList == null) {
            return new ArrayList<>(0);
        }
        List<T2> list = new ArrayList<>(originList.size());
        int i = 0;
        for (T1 t1 : originList) {
            if (t1 == null) {
                continue;
            }
            T2 t2 = transformer.transform(i++, t1);
            if (t2 != null) {
                list.add(t2);
            }
        }
        return list;
    }

    @NonNull
    public static <T extends Cloneable> List<T> clone(@Nullable Collection<T> list, @NonNull im.turbo.basetools.util.ValueSafeTransfer.ElementIterator<T> iterator) {
        if (list == null) {
            return new ArrayList<>(0);
        }
        List<T> cloneList = new ArrayList<>(list.size());
        int i = 0;
        for (T t : list) {
            cloneList.add(iterator.element(i++, t));
        }
        return cloneList;
    }

    public static <T> T iterate(@Nullable Collection<T> list, @NonNull im.turbo.basetools.util.ValueSafeTransfer.ElementIterator<T> iterator) {
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

    public static <T> void iterateAll(@Nullable Collection<T> list, @NonNull ElementIterator2<T> iterator) {
        if (list == null) {
            return;
        }
        int i = 0;
        for (T t : list) {
            iterator.element(i++, t);
        }
    }

    public static <T> void split(@Nullable List<T> list, int size, Splitter<T> splitter) {
        Preconditions.checkArgument(size > 0, "split size must be greater than 0");
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); ) {
            int start = i;
            int end = Math.min(i + size, list.size());
            splitter.onSplit(new ArrayList<>(list.subList(start, end)));
            i = end;
        }
    }

    public static <T> T iterateFromEnd(@Nullable List<T> list, @NonNull im.turbo.basetools.util.ValueSafeTransfer.ElementIterator<T> iterator) {
        if (list == null) {
            return null;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            T t = list.get(i);
            T tResult = iterator.element(i, t);
            if (tResult != null) {
                return tResult;
            }
        }
        return null;
    }

    public static <T> int findPosition(@Nullable Collection<T> list, @NonNull ElementFinder<T> finder) {
        if (list == null) {
            return -1;
        }
        int i = 0;
        for (T t : list) {
            int position = i++;
            if (finder.onFind(position, t)) {
                return position;
            }
        }
        return -1;
    }

    @Nullable
    public static <T> T find(@Nullable Collection<T> list, @NonNull ElementFinder<T> finder) {
        if (list == null) {
            return null;
        }
        int i = 0;
        for (T t : list) {
            if (finder.onFind(i++, t)) {
                return t;
            }
        }
        return null;
    }

    @Nullable
    public static <T> T find(@Nullable List<T> list, @NonNull ElementFinder<T> finder, boolean reverse) {
        if (list == null) {
            return null;
        }
        if (reverse) {
            for (int i = list.size() - 1; i >= 0; i--) {
                T t = list.get(i);
                if (finder.onFind(i, t)) {
                    return t;
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                T t = list.get(i);
                if (finder.onFind(i, t)) {
                    return t;
                }
            }
        }
        return null;
    }

    public static <T> boolean contains(@Nullable Collection<T> list, @NonNull ElementFinder<T> finder) {
        return find(list, finder) != null;
    }

    /**
     * @return removed child position.
     */
    public static <T> int removeOneMatched(@Nullable List<T> list, @NonNull ElementFinder<T> finder) {
        if (list == null) {
            return -1;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            T t = list.get(i);
            if (finder.onFind(i, t)) {
                list.remove(i);
                return i;
            }
        }
        return -1;
    }

    /**
     * @return removed child count.
     */
    public static <T> int removeAllMatched(@Nullable List<T> list, @NonNull ElementFinder<T> finder) {
        if (list == null) {
            return 0;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            T t = list.get(i);
            if (finder.onFind(i, t)) {
                list.remove(i);
            }
        }
        return 0;
    }

    public static <T> List<T> singletonList(@NonNull T t) {
        Preconditions.checkNotNull(t);
        return new ArrayList<>(Collections.singletonList(t));
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
            S.e(e);
            throw new RuntimeException("classPath not found:" + classPath);
        }
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableRes) {
        try {
            return ContextCompat.getDrawable(context, drawableRes);
        } catch (Throwable e) {
            e.printStackTrace();
            S.e(e);
            return null;
        }
    }

    public static String getString(@NonNull Context context, @StringRes int stringRes) {
        try {
            return context.getString(stringRes);
        } catch (Throwable e) {
            e.printStackTrace();
            S.e(e);
            return null;
        }
    }

    public static int intValue(String numberString) {
        return intValue(numberString, 0);
    }

    public static int intValue(String numberString, int defaultValue) {
        try {
            return Integer.parseInt(numberString);
        } catch (Exception e) {
            e.printStackTrace();
            S.e(e);
            return defaultValue;
        }
    }

    public static boolean equals(@Nullable List<?> list1, @Nullable List<?> list2) {
        if (list1 == null && list2 == null) {
            return true;
        } else if (list1 != null && list2 != null) {
            if (list1.size() != list2.size()) {
                return false;
            }
            for (int i = 0; i < list1.size(); i++) {
                Object o1 = list1.get(i);
                Object o2 = list2.get(i);
                if (o1 == o2) {
                    continue;
                }
                if (o1 != null && o2 != null) {
                    if (!o1.equals(o2)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static <T> boolean equals(@Nullable List<T> list1, @Nullable List<T> list2, Comparator<T> comparator) {
        if (list1 == null && list2 == null) {
            return true;
        } else if (list1 != null && list2 != null) {
            if (list1.size() != list2.size()) {
                return false;
            }
            for (int i = 0; i < list1.size(); i++) {
                T o1 = list1.get(i);
                T o2 = list2.get(i);
                if (o1 == o2) {
                    continue;
                }
                if (o1 != null && o2 != null) {
                    if (!comparator.compare(o1, o2)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean equals(@Nullable float[] array1, @Nullable float[] array2) {
        if (array1 == null && array2 == null) {
            return true;
        } else if (array1 != null && array2 != null) {
            if (array1.length != array2.length) {
                return false;
            }
            for (int i = 0; i < array1.length; i++) {
                float o1 = array1[i];
                float o2 = array2[i];
                if (o1 != o2) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean equalsEmpty(String o1, String o2) {
        if (TextUtils.isEmpty(o1) && TextUtils.isEmpty(o2)) {
            return true;
        }
        return TextUtils.equals(o1, o2);
    }

    public static boolean equals(@Nullable Integer a, @Nullable Integer b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    @SafeVarargs
    @NonNull
    public static <T> List<T> asList(@Nullable T... tt) {
        return tt == null ? new ArrayList<>(0) : Arrays.asList(tt);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public interface Transfer<T1, T2> {
        @Nullable
        T2 transform(int position, @NonNull T1 t);
    }

    public interface ElementIterator<T> {
        @Nullable
        T element(int position, T t);
    }

    public interface ElementIterator2<T> {
        void element(int position, T t);
    }

    public interface ElementFinder<T> {
        boolean onFind(int position, @Nullable T t);
    }

    public interface Comparator<T> {
        boolean compare(@NonNull T t1, @NonNull T t2);
    }

    public interface Splitter<T> {
        void onSplit(@NonNull List<T> list);
    }
}
