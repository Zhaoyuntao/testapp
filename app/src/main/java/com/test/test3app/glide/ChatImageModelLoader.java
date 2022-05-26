package com.test.test3app.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 25/05/2022
 * description:
 */
class ChatImageModelLoader implements ModelLoader<ChatImageModule, Bitmap> {
    @Nullable
    @Override
    public LoadData<Bitmap> buildLoadData(@NonNull ChatImageModule chatImageModule, int i, int i1, @NonNull Options options) {
        if (chatImageModule.needLoad()) {
            S.s("ChatImageModelLoader.buildLoadData: need load");
            return new LoadData<>(new ObjectKey(chatImageModule.getKey()), new ChatImageDataFetcher(chatImageModule));
        }
        S.e("ChatImageModelLoader.buildLoadData: no need to load");
        return null;
    }

    @Override
    public boolean handles(@NonNull ChatImageModule chatImageModule) {
        return true;
    }
}
