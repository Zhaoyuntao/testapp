package im.thebot.chat.api.chat.listener;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import im.thebot.api.chat.listener.FileStatusBean;
import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 2020-10-14
 * description:
 */
public abstract class FileListener {
    public static final String ALL = "all";

    private String key = "";

    public FileListener() {
    }

    public FileListener(@NonNull String key) {
        Preconditions.checkNotEmpty(key);
        this.key = key;
    }

    public void setKey(@NonNull String key) {
        Preconditions.checkNotEmpty(key);
        this.key = key;
    }

    public abstract void onFileStatusChanged(@NonNull FileStatusBean fileStatus);

    final public boolean match(String key) {
        return TextUtils.equals(this.key, ALL) || TextUtils.equals(this.key, key);
    }

    final public void changeFileStatus(@NonNull FileStatusBean fileStatus) {
        onFileStatusChanged(fileStatus);
    }

    public String getKey() {
        return key;
    }
}
