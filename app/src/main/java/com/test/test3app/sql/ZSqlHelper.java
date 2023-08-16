package com.test.test3app.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.test3app.sql.operation.DBOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import im.turbo.basetools.util.ValueSafeTransfer;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 26/04/2023
 */
public class ZSqlHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;

    public ZSqlHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : createSQL(ZEntry.TABLE_NAME)) {
            db.execSQL(sql);
        }
        for (String sql : createSQL(ZEntry.TABLE_NAME2)) {
            db.execSQL(sql);
        }
    }

    public static String[] createSQL(String tableName) {
        String createTable = "CREATE TABLE " + tableName + " (" +
                ZEntry._ID + " INTEGER PRIMARY KEY," +
                ZEntry.COLUMN_NAME_TITLE + " TEXT," +
                ZEntry.COLUMN_NAME_SUBTITLE + " TEXT," +
                ZEntry.COLUMN_TIME + " INTEGER," +
                ZEntry.COLUMN_TIME2 + " INTEGER" +
                ")";
        return new String[]{createTable};
    }

    public static String createSqlIndex(String tableName, String indexName) {
        return "create index if not exists index_"
                + indexName
                + " on "
                + tableName
                + "(" + indexName + ")";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        S.s("onUpgrade:" + newVersion);
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME, ZEntry.COLUMN_TIME));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME, ZEntry.COLUMN_TIME2));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME2, ZEntry.COLUMN_TIME));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME2, ZEntry.COLUMN_TIME2));
    }

    public void insert(String tag, String table, ZEntry zEntry, boolean dump) {
        SQLiteDatabase db = getWritableDatabase();
        TDBMonitor.execute(new DBOperation(db, tag, DBOperationCode.INSERT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase) {
                ContentValues values = new ContentValues();
                values.put(ZEntry.COLUMN_NAME_TITLE, zEntry.title);
                values.put(ZEntry.COLUMN_NAME_SUBTITLE, zEntry.subtitle);
                values.put(ZEntry.COLUMN_TIME, zEntry.time);
                zEntry.id = sqLiteDatabase.insert(table, null, values);
                return null;
            }
        }.setDump(dump));
    }

    public List<ZEntry> select(String tag, String table, @Nullable Map<String, String> whereMap, boolean dump) {
        List<ZEntry> itemIds = new ArrayList<>();
        TDBMonitor.execute(new DBOperation(getReadableDatabase(), tag, DBOperationCode.SELECT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase) {
                String[] projection = {ZEntry._ID, ZEntry.COLUMN_NAME_TITLE, ZEntry.COLUMN_NAME_SUBTITLE, ZEntry.COLUMN_TIME, ZEntry.COLUMN_TIME2};
                String selectionString;
                String[] selectionArgs;
                if (whereMap != null && !whereMap.isEmpty()) {
                    StringBuilder selection = new StringBuilder();
                    selectionArgs = new String[whereMap.size()];
                    int i = 0;
                    ValueSafeTransfer.iterate(whereMap.entrySet(), (position, entry) -> {
                        String column = entry.getKey();
                        String value = entry.getValue();
                        selection.append(column).append(" = ? ");
                        if (position < whereMap.size() - 1) {
                            selection.append(" and ");
                        }
                        selectionArgs[position] = value;
                        return null;
                    });
                    selectionString = selection.toString();
                } else {
                    selectionString = null;
                    selectionArgs = null;
                }
//                String sortOrder = " case when " + ZEntry.COLUMN_NAME_TITLE + " = 'hello' then " + ZEntry.COLUMN_TIME + " else " + ZEntry.COLUMN_TIME2 + " end DESC";
                S.s("selection:" + selectionString);
                S.s("selectionArgs:" + Arrays.toString(selectionArgs));
                String sortOrder = ZEntry.COLUMN_TIME + " DESC";
                Cursor cursor = sqLiteDatabase.query(
                        table,   // The table to query
                        projection,             // The array of columns to return (pass null to get all)
                        selectionString,              // The columns for the WHERE clause
                        selectionArgs,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        sortOrder,               // The sort order
                        "1000"
                );
                while (cursor.moveToNext()) {
                    ZEntry zEntry = new ZEntry();
                    zEntry.id = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry._ID));
                    zEntry.title = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_TITLE));
                    zEntry.subtitle = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_SUBTITLE));
                    zEntry.time = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_TIME));
                    zEntry.time2 = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_TIME2));
                    itemIds.add(zEntry);
                }
                return cursor;
            }
        }.setDump(dump));
        return itemIds;
    }
}
