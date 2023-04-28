package com.test.test3app.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.test.test3app.sql.operation.DBOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 26/04/2023
 */
public class ZSqlHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ZEntry.TABLE_NAME + " (" +
                    ZEntry._ID + " INTEGER PRIMARY KEY," +
                    ZEntry.COLUMN_NAME_TITLE + " TEXT," +
                    ZEntry.COLUMN_NAME_SUBTITLE + " TEXT)";
    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + ZEntry.TABLE_NAME2 + " (" +
                    ZEntry._ID + " INTEGER PRIMARY KEY," +
                    ZEntry.COLUMN_NAME_TITLE + " TEXT," +
                    ZEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    public static final int DATABASE_VERSION = 1;

    public ZSqlHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String tag, String table, ZEntry zEntry, boolean dump) {
        SQLiteDatabase db = getWritableDatabase();
        TDBMonitor.execute(new DBOperation(db, tag, DBOperationCode.INSERT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase) {
                ContentValues values = new ContentValues();
                values.put(ZEntry.COLUMN_NAME_TITLE, zEntry.title);
                values.put(ZEntry.COLUMN_NAME_SUBTITLE, zEntry.subtitle);
                zEntry.id = sqLiteDatabase.insert(table, null, values);
                return null;
            }
        }.setDump(dump));
    }

    public List<ZEntry> select(String tag, String table, String title, boolean dump) {
        List<ZEntry> itemIds = new ArrayList<>();
        TDBMonitor.execute(new DBOperation(getReadableDatabase(), tag, DBOperationCode.SELECT) {
            @Override
            public Cursor op(@NonNull SQLiteDatabase sqLiteDatabase) {
                String[] projection = {ZEntry._ID, ZEntry.COLUMN_NAME_TITLE, ZEntry.COLUMN_NAME_SUBTITLE};
                String selection = ZEntry.COLUMN_NAME_TITLE + " = ?";
                String[] selectionArgs = {title};
                String sortOrder = null;//ZEntry.COLUMN_NAME_SUBTITLE + " DESC";
                Cursor cursor = sqLiteDatabase.query(
                        table,   // The table to query
                        projection,             // The array of columns to return (pass null to get all)
                        selection,              // The columns for the WHERE clause
                        selectionArgs,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        sortOrder               // The sort order
                );
                while (cursor.moveToNext()) {
                    ZEntry zEntry = new ZEntry();
                    zEntry.id = cursor.getLong(cursor.getColumnIndexOrThrow(ZEntry._ID));
                    zEntry.title = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_TITLE));
                    zEntry.subtitle = cursor.getString(cursor.getColumnIndexOrThrow(ZEntry.COLUMN_NAME_SUBTITLE));
                    itemIds.add(zEntry);
                }
                return cursor;
            }
        }.setDump(dump));
        return itemIds;
    }
}
