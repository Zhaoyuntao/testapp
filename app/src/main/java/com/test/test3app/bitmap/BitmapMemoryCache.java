
package com.test.test3app.bitmap;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import im.turbo.utils.log.S;

import java.util.WeakHashMap;

public class BitmapMemoryCache extends LruCache<String, Bitmap> {
    public static BitmapMemoryCache createCache(Context appCxt) {
        DisplayMetrics dm = null;
        Resources res = appCxt.getResources();
        if (res != null) {
            dm = res.getDisplayMetrics();
        }
        final int screenSize = dm != null ? Math.max(dm.heightPixels * dm.widthPixels * 4, 480 * 800 * 4)
                : 480 * 800 * 4; // full screen of ARGB888 pixels

        ActivityManager am = (ActivityManager) appCxt.getSystemService(Context.ACTIVITY_SERVICE);
        int memClass = 32;
        try {
            memClass = am.getMemoryClass() * 1024 * 1024;
        } catch (Throwable e) {
            memClass = 32;
        }

        final int maxSize = Math.min(Math.min(screenSize * 8, memClass / 3), 64 * 1024 * 1024);

        MemoryInfo mi = new MemoryInfo();
        if (am != null) {
            am.getMemoryInfo(mi);
        }
        return new BitmapMemoryCache(maxSize);
    }

    public BitmapMemoryCache(int maxSize) {
        super(maxSize);
    }

    final WeakHashMap<Bitmap, Integer> mBitmapSizeCache = new WeakHashMap<Bitmap, Integer>();

    protected synchronized int sizeOf(String key, Bitmap value) {
        if (value == null) {
            return 0;
        }

        synchronized (mBitmapSizeCache) {
            if (mBitmapSizeCache.containsKey(value)) {
                Integer size = mBitmapSizeCache.get(value);
                if (size != null && size instanceof Integer) {
                    // LOG.logI("bitmap size: " + key + ", " + size);
                    return size;
                } else {
                    mBitmapSizeCache.remove(value);
                }
            }
        }

        if (value == null || value.isRecycled()) {
            S.s("Sizeof error, program might crash in near future. key: " + key + ", bmp: " + value
                    + ", recycled: " + (value == null ? "null" : value.isRecycled()), new Throwable());
            return 0;
        }

        int pixel = 1;
        Bitmap.Config config = value.getConfig();

        if (config == Bitmap.Config.ALPHA_8) {
            pixel = 4;
        } else if (config == Bitmap.Config.ARGB_4444) {
            pixel = 2;
        } else if (config == Bitmap.Config.ARGB_8888) {
            pixel = 4;
        } else if (config == Bitmap.Config.RGB_565) {
            pixel = 2;
        }

        int size = pixel * value.getHeight() * value.getWidth();

        synchronized (mBitmapSizeCache) {
            mBitmapSizeCache.put(value, size);
        }

        return size;
    }

    ;

    @Override
    protected synchronized void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        // do not recycle bitmap here, or you are in trouble. bitmap might be used outside of cache
        //
        // on device 4.0+, bitmap will be gc'ed as ordinary objects
        // on device 2.3, we just make things right, bitmap will be gc'ed in lowest priority

        S.s("evicted: " + evicted + ", key: " + key + ", oldSize: " + sizeOf(key, oldValue) + ", newSize: "
                + sizeOf(key, newValue) + ", size: " + size());
    }

    @Override
    public synchronized void trimToSize(int maxSize) {
        super.trimToSize(maxSize);

        if (maxSize < 0) {
            S.s("memory cache for bitmap is cleared. Is it necessary?", new Throwable());

            mBitmapSizeCache.clear();

            // prune all entries
            System.gc();
        }
    }
}
