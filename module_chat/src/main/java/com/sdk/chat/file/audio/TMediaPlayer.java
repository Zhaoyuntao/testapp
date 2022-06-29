package com.sdk.chat.file.audio;

import android.media.MediaPlayer;

/**
 * created by zhaoyuntao
 * on 27/06/2022
 * description:
 */
public class TMediaPlayer extends MediaPlayer {
    private boolean paused;

    @Override
    public void start() throws IllegalStateException {
        super.start();
        paused = false;
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean isPlaying() {
        try {
            return super.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }
}
