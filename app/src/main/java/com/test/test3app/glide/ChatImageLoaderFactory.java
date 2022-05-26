package com.test.test3app.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

/**
 * created by zhaoyuntao
 * on 25/05/2022
 * description:
 */
public class ChatImageLoaderFactory implements ModelLoaderFactory<ChatImageModule, Bitmap> {
    @NonNull
    @Override
    public ModelLoader<ChatImageModule, Bitmap> build(@NonNull MultiModelLoaderFactory multiModelLoaderFactory) {
        return new ChatImageModelLoader();
    }

    @Override
    public void teardown() {

    }
}
