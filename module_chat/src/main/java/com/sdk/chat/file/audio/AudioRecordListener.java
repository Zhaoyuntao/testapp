package com.sdk.chat.file.audio;

import androidx.annotation.NonNull;

/**
 * created by zhaoyuntao
 * on 28/06/2022
 * description:
 */
public interface AudioRecordListener {
    void onRecordStart(float[] volumes, long duration);

    void onRecording(float pitch, long duration);

    void onRecordEnd(@NonNull AudioFilePacket audioFilePacket);

    void onError(String errorMessage);

    void onAudioDropped(@NonNull AudioFilePacket audioFilePacket);

    void onRecordPause(@NonNull AudioFilePacket audioFilePacket);
}
