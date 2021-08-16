package com.test.test3app.huaweimeeting.conference.entry;

/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public abstract class ConferenceBaseItem {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ITEM_NOT_START = 1;
    public static final int TYPE_ITEM_ONGOING = 2;
    public static final int TYPE_ITEM_CLOSED = 3;
    public static final int TYPE_ITEM_DIVIDER = 4;

    public abstract int getType();
}
