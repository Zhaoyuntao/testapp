package com.test.test3app.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

//import com.tencent.wcdb.database.SQLiteDatabase;
import com.test.test3app.sql.operation.DBOperation;

import java.util.ArrayList;
import java.util.List;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 27/04/2023
 */
public class TDBMonitor {
    private static final List<DBTaskState> cache = new ArrayList<>();

    public static void addListener(@NonNull OnDBTaskStateChangeListener listener) {
        TDBStateNotifier.getInstance().addListener(listener);
    }

    public static void removeListener(@NonNull OnDBTaskStateChangeListener listener) {
        TDBStateNotifier.getInstance().removeListener(listener);
    }

    public static void execute(@NonNull DBOperation operation) {
        @NonNull String tag = operation.getTag();
        @DBOperationCode int op = operation.getDbOperationCode();
        @NonNull SQLiteDatabase sqLiteDatabase = operation.getSqLiteDatabase();
        boolean dump = operation.dump();
        DBTaskState state = new DBTaskState(tag, op);
        state.startWaiting();
        addCache(state);
        TSQLiteTransactionListener listener = new TSQLiteTransactionListener(state).setDump(dump);
        if (dump) {
            TDBStateNotifier.getInstance().notifyWaiting(state);
            listener.setStatesWhenStartWaiting(dump());
        }
        sqLiteDatabase.beginTransactionWithListener(listener);
        Cursor r = null;
        try {
            r = operation.op(sqLiteDatabase, state);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Throwable e) {
            e.printStackTrace();
            S.e(e);
        } finally {
            CloseUtils.close(r);
            sqLiteDatabase.endTransaction();
            removeCache(state);
        }
    }

    @NonNull
    private static List<DBTaskState> dump() {
        List<DBTaskState> dbStatesDump;
        synchronized (cache) {
            dbStatesDump = new ArrayList<>(cache);
        }
        return dbStatesDump;
    }

    private static void addCache(@NonNull DBTaskState dbTaskState) {
        synchronized (cache) {
            cache.add(dbTaskState);
        }
    }

    private static void removeCache(@NonNull DBTaskState dbTaskState) {
        synchronized (cache) {
            cache.remove(dbTaskState);
        }
    }
}
