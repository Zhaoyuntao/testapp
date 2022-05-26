package com.test.test3app.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * created by zhaoyuntao
 * on 25/05/2022
 * description:
 */
@GlideModule
public class ChatGlideModule extends AppGlideModule {
    private final long maxCacheSize = 1024 * 1024 * 100;
    private final long deskCacheSize = 1024 * 1024 * 1024 * 2L;

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.append(ChatImageModule.class, Bitmap.class, new ChatImageLoaderFactory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setLogLevel(Log.ERROR);
        builder.setLogRequestOrigins(false);
        builder.setMemoryCache(new LruResourceCache(maxCacheSize));
        builder.setBitmapPool(new LruBitmapPool(maxCacheSize));
        String deskCacheFolder = context.getCacheDir().getAbsolutePath();
        String deskCacheName = context.getPackageName();
        builder.setDiskCache(new DiskLruCacheFactory(deskCacheFolder, deskCacheName, deskCacheSize));
    }
}
