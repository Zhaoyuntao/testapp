package com.sdk.chat.file.audio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 06/11/2020
 * description:
 */
public class AudioStatusCache {
    private final Map<String, AudioStatusBean> audioStatusCache;

    public AudioStatusCache() {
        audioStatusCache = new ConcurrentHashMap<>();
    }

    @NonNull
    public AudioStatusBean setPlaying(@NonNull String key, long progress, long total) {
        return set(key, AudioPlayStatusCode.STATUS_AUDIO_PLAYING, progress, total);
    }

    @NonNull
    public AudioStatusBean setPlayingStopped(@NonNull String key, long progress, long total, @Nullable String errorMessage) {
        return set(key, AudioPlayStatusCode.STATUS_AUDIO_NOT_PLAYING, progress, total, errorMessage);
    }

    public AudioStatusBean setPlayingPaused(@NonNull String key, long progress, long total) {
        return set(key, AudioPlayStatusCode.STATUS_AUDIO_PAUSED, progress, total);
    }

    public AudioStatusBean setPlayingStart(@NonNull String key, long progress, long total) {
        return set(key, AudioPlayStatusCode.STATUS_AUDIO_START, progress, total);
    }

    @Nullable
    public AudioStatusBean get(@NonNull String key) {
        Preconditions.checkNotEmpty(key);
        return audioStatusCache.get(key);
    }

    public boolean contains(@NonNull String key) {
        Preconditions.checkNotEmpty(key);
        return audioStatusCache.containsKey(key);
    }

    //---------- private

    @NonNull
    private AudioStatusBean set(@NonNull String key, @AudioPlayStatusCode int status) {
        return set(key, status, null);
    }

    @NonNull
    private AudioStatusBean set(@NonNull String key, @AudioPlayStatusCode int status, String errorMessage) {
        return set(key, status, 0, 0, errorMessage);
    }

    private AudioStatusBean set(@NonNull String key, @AudioPlayStatusCode int status, long progress, long total) {
        return set(key, status, progress, total, null);
    }

    private AudioStatusBean set(@NonNull String key, @AudioPlayStatusCode int status, long progress, long total, String errorMessage) {
        Preconditions.checkNotEmpty(key);
        AudioStatusBean statusBean = audioStatusCache.get(key);
        if (statusBean == null) {
            statusBean = new AudioStatusBean(key, status);
            audioStatusCache.put(key, statusBean);
        } else {
            statusBean.setAudioStatusCode(status);
        }
        statusBean.setProgress(progress);
        statusBean.setTotal(total);
        statusBean.setErrorMessage(errorMessage);
        return statusBean;
    }
}
