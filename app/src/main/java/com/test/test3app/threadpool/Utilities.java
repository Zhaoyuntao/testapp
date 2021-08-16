package com.test.test3app.threadpool;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * created by zhaoyuntao
 * on 2020-03-31
 * description:
 */
public class Utilities {
    private static Context context;
    public static Context getApplicationContext(){
        return context;
    }
    public static void setContext(Context context){
        Utilities.context=context;
    }

    public static void silentlyClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void silentlyClose(Cursor c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void silentlyClose(SQLiteDatabase c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void silentlyClose(Socket c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void silentlyClose(DatagramSocket c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void silentlyClose(AssetFileDescriptor afd) {
        if (afd != null) {
            try {
                afd.close();
            } catch (Throwable w) {
            }
        }
    }
}
