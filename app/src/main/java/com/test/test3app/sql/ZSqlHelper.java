package com.test.test3app.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.test3app.sql.operation.DBOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import im.turbo.basetools.util.ValueSafeTransfer;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 26/04/2023
 */
public class ZSqlHelper extends SQLiteOpenHelper {

    private static String LICENSE_CODE = "OmNpZDowMDEzbzAwMDAyZnQxWThBQUk6cGxhdGZvcm06MTA6ZXhwaXJlOm5ldmVyOnZlcnNpb246MTpobWFjOjhiNWIwNDMwNGM0NDAyYjc2ZDgxN2UxZDFlZjZkMTJjNjI3MGYxYWM=";
    static String DB_NAME = "search_db_fts5.db";
    private static SQLiteDatabase sqLiteDatabase;

    public static final int DATABASE_VERSION = 4;

    public ZSqlHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSQL(ZEntry.TABLE_NAME));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME, ZEntry.COLUMN_NAME_CONTENT));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME, ZEntry.COLUMN_NAME_CONTENT2));
        db.execSQL(createSQL(ZEntry.TABLE_NAME2));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME2, ZEntry.COLUMN_NAME_CONTENT));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME2, ZEntry.COLUMN_NAME_CONTENT2));
        db.execSQL(createSQL(ZEntry.TABLE_NAME3));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME2, ZEntry.COLUMN_NAME_CONTENT));
        db.execSQL(createSqlIndex(ZEntry.TABLE_NAME2, ZEntry.COLUMN_NAME_CONTENT2));

        db.execSQL(createFTSSQL(ZEntry.TABLE_NAME_FTS1));
        db.execSQL(createFTSSQL(ZEntry.TABLE_NAME_FTS2));
        db.execSQL(createFTSSQL(ZEntry.TABLE_NAME_FTS3));
    }

    public static String createSQL(String tableName) {
        return "CREATE TABLE " + tableName + " (" +
                ZEntry._ID + " INTEGER PRIMARY KEY," +
                ZEntry.COLUMN_NAME_NAME + " TEXT," +
                ZEntry.COLUMN_NAME_CONTENT + " TEXT," +
                ZEntry.COLUMN_NAME_CONTENT2 + " TEXT," +
                ZEntry.COLUMN_TIME + " INTEGER," +
                ZEntry.COLUMN_TIME2 + " INTEGER" +
                ")";
    }

    public static String createSqlIndex(String tableName, String indexName) {
        return "create index if not exists index_"
                + indexName
                + " on "
                + tableName
                + "(" + indexName + ")";
    }

    public static String createFTSSQL(String tableName) {
        return "CREATE VIRTUAL TABLE " + tableName + " USING fts3(" +
                ZEntry.COLUMN_NAME_NAME + " , " +
                ZEntry.COLUMN_NAME_CONTENT + " , " +
                ZEntry.COLUMN_NAME_CONTENT2 + " , " +
                ", tokenize=icu)";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        S.s("onUpgrade:" + newVersion);
    }

    public void insert(String tag, String table, ZEntry zEntry, boolean dump) {
        SQLiteDatabase db = getWritableDatabase();
        TDBMonitor.execute(new DBOperation(db, tag, DBOperationCode.INSERT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase, DBTaskState state) {
                ContentValues values = new ContentValues();
                values.put(ZEntry.COLUMN_NAME_NAME, zEntry.name);
                values.put(ZEntry.COLUMN_NAME_CONTENT, zEntry.content);
                values.put(ZEntry.COLUMN_NAME_CONTENT2, zEntry.content2);
                values.put(ZEntry.COLUMN_TIME, zEntry.time);
                zEntry.id = sqLiteDatabase.insert(table, null, values);
                return null;
            }
        }.setDump(dump));
    }

    public void insertFts(String tag, String table, ZEntry zEntry, boolean dump) {
        SQLiteDatabase db = getWritableDatabase();
        TDBMonitor.execute(new DBOperation(db, tag, DBOperationCode.INSERT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase, DBTaskState state) {
                //FTS insert.
                ContentValues valuesFTS = new ContentValues();
                valuesFTS.put(ZEntry.COLUMN_NAME_NAME, zEntry.name);
                valuesFTS.put(ZEntry.COLUMN_NAME_CONTENT, zEntry.content);
                valuesFTS.put(ZEntry.COLUMN_NAME_CONTENT2, zEntry.content2);
                sqLiteDatabase.insert(table, null, valuesFTS);
                return null;
            }
        }.setDump(dump));
    }

    public List<ZEntry> select(String tag, String table, @Nullable Map<String, String> whereMap, boolean dump) {
        List<ZEntry> itemIds = new ArrayList<>();
        TDBMonitor.execute(new DBOperation(getReadableDatabase(), tag, DBOperationCode.SELECT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase, DBTaskState state) {
                String[] projection = {ZEntry._ID, ZEntry.COLUMN_NAME_NAME, ZEntry.COLUMN_NAME_CONTENT, ZEntry.COLUMN_TIME, ZEntry.COLUMN_TIME2};
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
                    zEntry.name = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_NAME));
                    zEntry.content = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_CONTENT));
                    zEntry.content2 = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_CONTENT2));
                    zEntry.time = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_TIME));
                    zEntry.time2 = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_TIME2));
                    itemIds.add(zEntry);
                }
                return cursor;
            }
        }.setDump(dump));
        return itemIds;
    }

    public List<ZEntry> search(String tag, String table, @Nullable String arg, boolean dump) {
        List<ZEntry> itemIds = new ArrayList<>();
        TDBMonitor.execute(new DBOperation(getReadableDatabase(), tag, DBOperationCode.SELECT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase, DBTaskState state) {
//                String sortOrder = " case when " + ZEntry.COLUMN_NAME_TITLE + " = 'hello' then " + ZEntry.COLUMN_TIME + " else " + ZEntry.COLUMN_TIME2 + " end DESC";
                String sql = "select * from " + table + " where " + ZEntry.COLUMN_NAME_CONTENT + " like '%" + arg + "%'";
                Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    ZEntry zEntry = new ZEntry();
                    zEntry.id = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry._ID));
                    zEntry.name = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_NAME));
                    zEntry.content = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_CONTENT));
                    zEntry.content2 = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_CONTENT2));
                    zEntry.time = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_TIME));
                    zEntry.time2 = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_TIME2));
                    itemIds.add(zEntry);
                }
                return cursor;
            }
        }.setDump(dump));
        return itemIds;
    }

    public List<ZEntry> fts(String tag, String table, @Nullable String arg, boolean dump, int limit) {
        tag = arg + "(limit:" + limit + ")";
        List<ZEntry> items = new ArrayList<>();

        TDBMonitor.execute(new DBOperation(getReadableDatabase(), tag, DBOperationCode.SELECT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase, DBTaskState state) {
                String sql = "select * from " + table
                        + " where " + table + "." + ZEntry.COLUMN_NAME_CONTENT + " match '" + arg + "'"
                        + (limit > 0 ? " limit " + limit : "");
                Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    ZEntry zEntry = new ZEntry();
                    zEntry.name = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_NAME));
                    zEntry.content = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_CONTENT));
                    zEntry.content2 = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_CONTENT2));
                    items.add(zEntry);
                }
                state.setLog("fts matched count: " + items.size());
                return cursor;
            }
        }.setDump(dump));
//        S.s(ZEntry.TABLE_NAME_FTS3 + "--------------------------------------------------[count: " + items.size() + "]  [" + arg + "]");
        return items;
    }

    public long ftsCount(String tag, String table) {
        final long[] count = new long[1];
        TDBMonitor.execute(new DBOperation(getReadableDatabase(), tag, DBOperationCode.SELECT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase, DBTaskState state) {
                count[0] = DatabaseUtils.queryNumEntries(sqLiteDatabase, table);
                state.setLog("fts count:" + count[0]);
                return null;
            }
        });
        return count[0];
    }
}
