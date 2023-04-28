package com.test.test3app.sql;

import android.provider.BaseColumns;

/**
 * created by zhaoyuntao
 * on 26/04/2023
 */
public class ZEntry implements BaseColumns {
    public static final String TABLE_NAME = "ZEntry";
    public static final String TABLE_NAME2 = "ZEntry2";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_SUBTITLE = "subtitle";

    public long id;
    public String title;
    public String subtitle;

    @Override
    public String toString() {
        return "ZEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                "\n";
    }
}
