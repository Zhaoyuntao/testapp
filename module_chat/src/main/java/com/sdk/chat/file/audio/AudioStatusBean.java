package com.sdk.chat.file.audio;


/**
 * created by zhaoyuntao
 * on 06/11/2020
 * description:
 */
public class AudioStatusBean {
    private String key;
    @AudioStatusCode
    private int audioStatusCode;
    private long progress;
    private long total;
    private String errorMessage;

    public AudioStatusBean(String key, @AudioStatusCode int audioStatusCode) {
        this.key = key;
        this.audioStatusCode = audioStatusCode;
    }

    @AudioStatusCode
    public int getAudioStatusCode() {
        return audioStatusCode;
    }

    public void setAudioStatusCode(@AudioStatusCode int audioStatusCode) {
        this.audioStatusCode = audioStatusCode;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public float getPercent() {
        return total == 0 ? 0 : (float) ((double) progress / total);
    }

    @Override
    public String toString() {
        return "AudioStatusBean{" +
                "key='" + key + '\'' +
                ", progress=" + progress +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
