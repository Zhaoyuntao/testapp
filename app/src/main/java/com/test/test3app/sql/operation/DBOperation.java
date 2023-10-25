package com.test.test3app.sql.operation;

import android.database.Cursor;
import android.database.sqlite.*;
//import  com.tencent.wcdb.database.*;
import androidx.annotation.NonNull;

import com.test.test3app.sql.DBOperationCode;
import com.test.test3app.sql.DBTaskState;

/**
 * created by zhaoyuntao
 * on 27/04/2023
 */
public abstract class DBOperation {
    private final String tag;
    @DBOperationCode
    private final int dbOperationCode;
    private final SQLiteDatabase sqLiteDatabase;
    private boolean dump;

    public DBOperation(@NonNull SQLiteDatabase sqLiteDatabase, @NonNull String tag, @DBOperationCode int dbOperationCode) {
        this.tag = tag;
        this.dbOperationCode = dbOperationCode;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    final public String getTag() {
        return tag;
    }

    final public int getDbOperationCode() {
        return dbOperationCode;
    }

    @NonNull
    final public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public DBOperation setDump(boolean dump) {
        this.dump = dump;
        return this;
    }

    final public boolean dump() {
        return dump;
    }

    public abstract Cursor op(@NonNull SQLiteDatabase sqLiteDatabase, DBTaskState state);
}
