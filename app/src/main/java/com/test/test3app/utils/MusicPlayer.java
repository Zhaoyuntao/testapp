package com.test.test3app.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * created by zhaoyuntao
 * on 2019-12-08
 * description:
 */
public class MusicPlayer {

    public static void play(Context context, int ringId, final PlayListener playRingListener) {
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, ringId);
        if (playRingListener != null) {
            playRingListener.start();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (playRingListener != null) {
                    playRingListener.end();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer.setOnCompletionListener(null);
                }
            }
        });
        mediaPlayer.start();
    }

    public interface PlayListener {
        void start();

        void end();
    }
}
