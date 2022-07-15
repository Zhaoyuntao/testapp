package com.sdk.chat.file.audio;

import androidx.annotation.NonNull;

import im.turbo.basetools.observer.ListenerManager;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 2020/6/27
 * description:
 */
public class AudioNotifier extends ListenerManager<AudioListener> {
    private final AudioStatusCache audioStatusCache;

    private static volatile AudioNotifier audioNotifier;

    private AudioNotifier() {
        super(true);
        audioStatusCache = new AudioStatusCache();
    }

    public static AudioNotifier getInstance() {
        if (audioNotifier == null) {
            synchronized (AudioNotifier.class) {
                if (audioNotifier == null) {
                    audioNotifier = new AudioNotifier();
                }
            }
        }
        return audioNotifier;
    }

    private void notifyAudioStatusChanged(@NonNull AudioStatusBean status) {
        notifyListeners(listener -> {
            if (listener.match(status.getKey())) {
                listener.onAudioStatusChanged(status);
            }
        });
    }

    public void notifyPlaying(String key, long progress, long total) {
        AudioStatusBean statusBean = audioStatusCache.setPlaying(key, progress, total);
        notifyAudioStatusChanged(statusBean);
    }

    public void notifyPlayingStopped(String key, long progress, long total, String errorMessage) {
        S.sd("stoped");
        AudioStatusBean audioStatus = audioStatusCache.setPlayingStopped(key, progress, total, errorMessage);
        notifyAudioStatusChanged(audioStatus);
    }

    public void notifyPlayingPaused(String key, long progress, long total) {
        AudioStatusBean audioStatus = audioStatusCache.setPlayingPaused(key, progress, total);
        notifyAudioStatusChanged(audioStatus);
    }

    public void notifyPlayingStart(String key, long progress, long total) {
        AudioStatusBean audioStatus = audioStatusCache.setPlayingStart(key, progress, total);
        notifyAudioStatusChanged(audioStatus);
    }

    @NonNull
    public AudioStatusBean getStatus(String key) {
        AudioStatusBean audioStatus = audioStatusCache.get(key);
        if (audioStatus == null) {
            audioStatus = new AudioStatusBean(key, AudioPlayStatusCode.STATUS_AUDIO_NOT_PLAYING);
            audioStatus.setErrorMessage(null);
            audioStatus.setProgress(-1);
        }
        return audioStatus;
    }

    public boolean contains(String key) {
        return audioStatusCache.contains(key);
    }
}
