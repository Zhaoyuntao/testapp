package com.sdk.chat.file.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.io.IOException;

import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.ResourceUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 27/06/2022
 * description:
 */
public class AudioPlayer {
    private TMediaPlayer mediaPlayer;
    private final AudioFilePacket audioFilePacket;
    private final AudioPlayListener audioPlayListener;
    private final Thread progressThread;

    public AudioPlayer(@NonNull AudioFilePacket audioFilePacket, @NonNull AudioPlayListener audioPlayListener) {
        Preconditions.checkNotNull(audioFilePacket);
        Preconditions.checkNotEmpty(audioFilePacket.getFilePath());
        Preconditions.checkNotNull(audioPlayListener);
        this.audioPlayListener = audioPlayListener;
        this.audioFilePacket = audioFilePacket;
        this.progressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int duration = getDuration();
                int startPosition = getStartPosition();
                long startTime = SystemClock.elapsedRealtime();
                while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    long now = SystemClock.elapsedRealtime();
                    int currentPosition = (int) (now - startTime) + startPosition;
                    int position = Math.min(duration, currentPosition);
                    audioPlayListener.onPlaying(audioFilePacket.getUuid(), position, duration);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
    }

    public void start() {
        if (!PermissionUtils.hasPermission(ResourceUtils.getApplication(), Permission.READ_EXTERNAL_STORAGE)) {
            S.e("no permission");
            return;
        }
        _startPlay();
    }

    private void _startPlay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            S.e("isPlaying");
            return;
        }
        mediaPlayer = new TMediaPlayer();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(audioFilePacket.getFilePath());
        } catch (IOException e) {
            S.e(e);
            audioPlayListener.onError(audioFilePacket.getUuid(), -1, "stopped by set data error:" + e.getMessage());
            return;
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            S.e(e);
            audioPlayListener.onError(audioFilePacket.getUuid(), -1, "stopped by prepare error:" + e.getMessage());
            return;
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioPlayListener.onStop(audioFilePacket.getUuid(), 0, getDuration());
                releaseMediaPlayer();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                audioPlayListener.onError(audioFilePacket.getUuid(), -1, "stopped by error:" + what + " " + extra);
                releaseMediaPlayer();
                return false;
            }
        });
        mediaPlayer.seekTo(audioFilePacket.getStartPosition());
        mediaPlayer.start();
        int duration = getDuration();
        audioPlayListener.onStart(audioFilePacket.getUuid(), Math.min(duration, mediaPlayer.getCurrentPosition()), duration);
        startProgressLoop();
    }

    private void startProgressLoop() {
        progressThread.start();
    }

    private int getDuration() {
        return audioFilePacket.getDuration();
    }

    private int getStartPosition() {
        return audioFilePacket.getStartPosition();
    }

    public void stop(boolean callListener) {
        int duration = getDuration();
        int position = 0;
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            position = mediaPlayer.getCurrentPosition();
            releaseMediaPlayer();
            if (progressThread != null) {
                progressThread.interrupt();
            }
            if (callListener) {
                audioPlayListener.onStop(audioFilePacket.getUuid(), Math.min(duration, position), duration);
            }
        }
    }

    public void cancel() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public long getCurrentPosition() {
        return mediaPlayer == null ? 0 : mediaPlayer.getCurrentPosition();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public String getUUID() {
        return audioFilePacket.getUuid();
    }

    public interface AudioPlayListener {
        void onStart(String uuid, int progress, int total);

        void onPlaying(String uuid, int progress, int total);

        void onStop(String uuid, int progress, int total);

        void onError(String uuid, int errorCode, String errorMessage);
    }
}
