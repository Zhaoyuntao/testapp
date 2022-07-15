package com.sdk.chat.file.audio;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 13/06/2022
 * description:
 */
public class MessageAudioHelper {
    private static AudioRecorder audioRecorder;
    private static AudioPlayer audioPlayer;

    public static void startRecord(@NonNull String uuid, @NonNull AudioRecordListener audioRecordListener) {
        Preconditions.checkNotEmpty(uuid);
        stopPlaying();
        if (audioRecorder != null) {
            if (audioRecorder.isRecording()) {
                return;
            }
            if (audioRecorder.isPaused()) {
                audioRecorder.startRecord();
                return;
            }
            audioRecorder = null;
        }
        audioRecorder = new AudioRecorder(uuid, audioRecordListener);
        audioRecorder.startRecord();
    }

    public static void finishRecord() {
        if (audioRecorder != null) {
            audioRecorder.finishRecord();
            audioRecorder = null;
        }
    }

    public static void pauseRecord() {
        if (audioRecorder != null) {
            audioRecorder.pauseRecord();
        }
    }

    public static void resumeRecord() {
        if (audioRecorder != null) {
            audioRecorder.startRecord();
        }
    }

    public static boolean isRecording() {
        return audioRecorder != null && audioRecorder.isRecording();
    }

    public static void startPlay(@NonNull AudioFilePacket audioFilePacket, int position) {
        if (isRecording()) {
            AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), position, audioFilePacket.getDuration(), "is recording");
            return;
        }
        if (audioFilePacket.hasError()) {
            AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), position, audioFilePacket.getDuration(), "play error:" + audioFilePacket.getErrorMessage());
            return;
        }
        if (audioPlayer != null) {
            audioPlayer.stop(!TextUtils.equals(audioFilePacket.getUuid(), audioPlayer.getUUID()));
        }
        audioPlayer = new AudioPlayer(audioFilePacket, new AudioPlayer.AudioPlayListener() {
            @Override
            public void onStart(String uuid, int progress, int total) {
                AudioNotifier.getInstance().notifyPlayingStart(uuid, progress, total);
            }

            @Override
            public void onPlaying(String uuid, int progress, int total) {
                AudioNotifier.getInstance().notifyPlaying(uuid, progress, total);
            }

            @Override
            public void onStop(String uuid, int progress, int total) {
                AudioNotifier.getInstance().notifyPlayingStopped(uuid, progress, total, null);
                audioPlayer = null;
            }

            @Override
            public void onError(String uuid, int errorCode, String errorMessage) {
                AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), position, audioFilePacket.getDuration(), errorMessage);
            }
        });
        audioPlayer.start();
    }

    public static void stopPlaying() {
        if (audioPlayer != null) {
            audioPlayer.stop(true);
            audioPlayer = null;
        }
    }

    public static boolean isPlaying() {
        return audioPlayer != null && audioPlayer.isPlaying();
    }

    public static void cancelRecord() {
        if (audioRecorder != null) {
            audioRecorder.dropAudio();
            audioRecorder = null;
        }
    }

    @Nullable
    public static AudioFilePacket getAudioDraft() {
        return audioRecorder == null ? null : audioRecorder.getAudioDraft();
    }
}
