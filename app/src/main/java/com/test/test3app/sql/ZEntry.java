package com.test.test3app.sql;

import android.provider.BaseColumns;

/**
 * created by zhaoyuntao
 * on 26/04/2023
 */
public class ZEntry implements BaseColumns {
    public static final String TABLE_NAME = "ZEntry";
    public static final String TABLE_NAME2 = "ZEntry2";
    public static final String TABLE_NAME3 = "ZEntry3";
    public static final String TABLE_NAME_FTS1 = "ZEntryFts";
    public static final String TABLE_NAME_FTS2 = "ZEntryFts2";
    public static final String TABLE_NAME_FTS3 = "ZEntryFts3";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_CONTENT2 = "content2";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TIME2 = "time2";

    public long id;
    public String name;
    public String content;
    public String content2;
    public long time;
    public long time2;

    @Override
    public String toString() {
        return "ZEntry{" +
                "id=" + id +
                ", title='" + name + '\'' +
                ", subtitle='" + content + '\'' +
                "\n";
    }
}
