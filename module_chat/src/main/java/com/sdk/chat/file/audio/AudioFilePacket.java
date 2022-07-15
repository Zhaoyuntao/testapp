package com.sdk.chat.file.audio;

import android.text.TextUtils;

import java.io.File;

/**
 * created by zhaoyuntao
 * on 13/06/2022
 * description:
 */
public class AudioFilePacket {
    private String filePath;
    private int duration;
    private int startPosition;
    private long fileSize;
    private String uuid;
    private String errorMessage;
    private float[] volumes;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean hasError() {
        return !TextUtils.isEmpty(errorMessage) || TextUtils.isEmpty(filePath) || !new File(filePath).exists() || duration <= 0 || TextUtils.isEmpty(uuid);
    }

    public float[] getVolumes() {
        return volumes;
    }

    public void setVolumes(float[] volumes) {
        this.volumes = volumes;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public String toString() {
        return "AudioFilePacket{" +
                "filePath='" + filePath + '\'' +
                ", duration=" + duration +
                ", fileSize=" + fileSize +
                ", uuid='" + uuid + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
