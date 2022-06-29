package com.sdk.chat.file.audio;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 2020-10-14
 * description:
 */
public abstract class AudioListener {
    public static final String ALL = "all";

    private String key;

    public AudioListener(@NonNull String key) {
        Preconditions.checkNotEmpty(key);
        this.key = key;
    }

    public void setKey(@NonNull String key) {
        Preconditions.checkNotEmpty(key);
        this.key = key;
    }

    public abstract void onAudioStatusChanged(@NonNull AudioStatusBean status);

    final public boolean match(String key) {
        return TextUtils.equals(this.key, ALL) || TextUtils.equals(this.key, key);
    }

    public String getKey() {
        return key;
    }
}
