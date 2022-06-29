package com.sdk.chat.file.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;

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

    public AudioPlayer(@NonNull AudioFilePacket audioFilePacket) {
        Preconditions.checkNotNull(audioFilePacket);
        Preconditions.checkNotEmpty(audioFilePacket.getFilePath());
        this.audioFilePacket = audioFilePacket;
    }

    public void start(long startPosition) {
        if (!PermissionUtils.hasPermission(ResourceUtils.getApplication(), Permission.READ_EXTERNAL_STORAGE)) {
            S.e("no permission");
            return;
        }
        ThreadPool.runIO(new Runnable() {
            @Override
            public void run() {
                _startPlay(startPosition);
            }
        });
    }

    private void _startPlay(long startPosition) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            S.e("isPlaying");
            return;
        }
        if (mediaPlayer != null && mediaPlayer.isPaused()) {
            S.s("isPaused");
        } else {
            mediaPlayer = new TMediaPlayer();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(audioFilePacket.getFilePath());
            } catch (IOException e) {
                S.e(e);
                AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration(), "stopped by set data error:" + e.getMessage());
                return;
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                S.e(e);
                AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration(), "stopped by prepare error:" + e.getMessage());
                return;
            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(1, 1);
            mediaPlayer.seekTo((int) startPosition);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), mp.getCurrentPosition(), mp.getDuration(), "stopped after playing completed");
                    releaseMediaPlayer();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), mp.getCurrentPosition(), mp.getDuration(), "stopped by error:" + what + " " + extra);
                    releaseMediaPlayer();
                    return false;
                }
            });
        }
        mediaPlayer.seekTo((int) startPosition);
        mediaPlayer.start();
        AudioNotifier.getInstance().notifyPlayingStart(audioFilePacket.getUuid(), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
        startProgressLoop();
    }

    private void startProgressLoop() {
        new Thread(() -> {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                AudioNotifier.getInstance().notifyPlaying(audioFilePacket.getUuid(), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            AudioNotifier.getInstance().notifyPlayingPaused(audioFilePacket.getUuid(), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
        }
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            AudioNotifier.getInstance().notifyPlayingStopped(audioFilePacket.getUuid(), mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration(), "stopped by user");
        }
        releaseMediaPlayer();
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

    public long getDuration() {
        return mediaPlayer == null ? audioFilePacket.getDuration() : mediaPlayer.getDuration();
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

    public boolean isPaused() {
        return mediaPlayer != null && mediaPlayer.isPaused();
    }
}
