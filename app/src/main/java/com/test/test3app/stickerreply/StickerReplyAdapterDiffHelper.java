package com.test.test3app.stickerreply;

import android.os.Bundle;

/**
 * created by zhaoyuntao
 * on 28/12/2020
 * description:
 */
class StickerReplyAdapterDiffHelper {
    public static final String DIFF_COUNT = "count";
    public static final String DIFF_STICKER = "sticker";

    public static Bundle getIntBundle(String key, int count) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, count);
        return bundle;
    }
}
