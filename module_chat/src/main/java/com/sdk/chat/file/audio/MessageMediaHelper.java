package com.sdk.chat.file.audio;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import java.io.File;

import im.turbo.basetools.util.ValueSafeTransfer;

/**
 * created by zhaoyuntao
 * on 27/05/2022
 * description:
 */
public class MessageMediaHelper {

    public static int getAudioDuration(String localPath) {
        if (TextUtils.isEmpty(localPath) || !new File(localPath).exists()) {
            return 0;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(localPath);
        } catch (Exception e) {
            return 0;
        }
        return ValueSafeTransfer.intValue(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }
}
