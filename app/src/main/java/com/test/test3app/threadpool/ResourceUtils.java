package com.test.test3app.threadpool;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import java.io.Closeable;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * created by zhaoyuntao
 * on 2020-03-31
 * description:
 */
public class ResourceUtils {
    private static Context context;

    public static Context getApplicationContext() {
        return context;
    }

    public static void setContext(Context context) {
        ResourceUtils.context = context;
    }

    public static String getString(@StringRes int stringId, Object... formatArgs) {
        if (formatArgs != null && formatArgs.length > 0) {
            try {
                return getApplicationContext().getString(stringId, formatArgs);
            } catch (Exception e) {
                try {
                    return getApplicationContext().getString(stringId);
                } catch (Exception e2) {
                    return "?";
                }
            }
        } else {
            try {
                return getApplicationContext().getString(stringId);
            } catch (Exception e2) {
                return "?";
            }
        }
    }

    @ColorInt
    public static int getColor(@ColorRes int colorResourceId) {
        if (context != null) {
            return ContextCompat.getColor(context, colorResourceId);
        }
        return 0;
    }


    public static Drawable getDrawable(@DrawableRes int drawableResourceId) {
        if (context != null) {
            return ContextCompat.getDrawable(context, drawableResourceId);
        }
        return null;
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
