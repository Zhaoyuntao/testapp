package com.sdk.chat.file.audio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * created by zhaoyuntao
 * on 13/06/2022
 * description:
 */
public class MessageAudioHelper {
    private static AudioRecorder audioRecorder;
    private static AudioPlayer audioPlayer;

    public static void startRecord(@NonNull AudioRecorder.AudioRecordListener audioRecordListener) {
        pausePlaying();
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
        audioRecorder = new AudioRecorder(audioRecordListener);
        audioRecorder.startRecord();
    }

    public static void stopRecord() {
        if (audioRecorder != null) {
            audioRecorder.stopRecord();
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

    public static void startPlaying(@NonNull String sessionId, @NonNull String uuid, long position) {
        if (isRecording()) {
            AudioNotifier.getInstance().notifyPlayingStopped(uuid, 0, 0, "is recording");
            return;
        }
    }

    public static void playDebug(@NonNull AudioFilePacket audioFilePacket, long position) {
        if (isRecording()) {
            AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), position, audioFilePacket.getDuration(), "is recording");
            return;
        }
        if (audioFilePacket.hasError()) {
            AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), position, audioFilePacket.getDuration(), "play error:" + audioFilePacket.getErrorMessage());
            return;
        }
        pausePlaying();
        audioPlayer = new AudioPlayer(audioFilePacket);
        audioPlayer.start(position);
    }

    public static void pausePlaying() {
        if (audioPlayer != null) {
            if (audioPlayer.isPaused()) {
                audioPlayer.cancel();
            } else if (audioPlayer.isPlaying()) {
                audioPlayer.pause();
                audioPlayer.cancel();
            }
        }
    }

    public static void stopPlaying() {
        if (audioPlayer != null) {
            audioPlayer.stop();
        }
    }

    private static long getPlayingProgress() {
        //todo
        return 0;
    }

    public static boolean isPlaying() {
        return audioPlayer != null && audioPlayer.isPlaying();
    }

    public static void dropAudio() {
        if (audioRecorder != null) {
            audioRecorder.dropAudio();
        }
    }

    @Nullable
    public static AudioFilePacket getAudioDraft() {
        return audioRecorder == null ? null : audioRecorder.getAudioDraft();
    }
}
