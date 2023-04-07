package com.test.test3app.appbase;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.test.test3app.R;
import im.turbo.baseui.utils.UiUtils;

import im.turbo.thread.ThreadPool;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 2020-03-31
 * description:
 */
public class ZApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new TestExceptionHandler(defaultUncaughtExceptionHandler));
        initEmojiCompat();
        ThreadPool.startup();
    }

    private void initEmojiCompat() {
        final EmojiCompat.Config config;
        // Use a downloadable font for EmojiCompat
        final FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);
        config = new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
//                        S.s("EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
//                        S.e("EmojiCompat initialization failed", throwable);
                    }
                });

        EmojiCompat.init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        ResourceUtils.initApplication(this);
        super.attachBaseContext(base);
    }
}
