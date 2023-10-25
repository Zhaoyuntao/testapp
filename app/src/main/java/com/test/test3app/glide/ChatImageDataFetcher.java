package com.test.test3app.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 25/05/2022
 * description:
 */
public class ChatImageDataFetcher implements DataFetcher<Bitmap> {
    private final ChatImageModule chatImageModule;

    public ChatImageDataFetcher(ChatImageModule chatImageModule) {
        this.chatImageModule = chatImageModule;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super Bitmap> dataCallback) {
        String sessionId = chatImageModule.getSessionId();
        String uuid = chatImageModule.getKey();
        S.s("ChatImageDataFetcher.loadData:" + sessionId + " " + uuid);
    }

    @Override
    public void cleanup() {
        S.s("ChatImageDataFetcher.cleanup");
    }

    @Override
    public void cancel() {
        S.s("ChatImageDataFetcher.cancel");
    }

    @NonNull
    @Override
    public Class<Bitmap> getDataClass() {
        return Bitmap.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
