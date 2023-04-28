package com.test.test3app.sql;

import android.database.Cursor;

/**
 * created by zhaoyuntao
 * on 27/04/2023
 */
public class CloseUtils {
    public static void close(Cursor cursor) {
        if (cursor == null) {
            return;
        }
        try {
            cursor.close();
        } catch (Throwable ignore) {
        }
    }
}
