package im.thebot.api.chat.listener;


import im.thebot.api.chat.constant.FileStatusCode;
import im.thebot.chat.api.chat.constant.FileStatusUtils;

/**
 * created by zhaoyuntao
 * on 06/11/2020
 * description:
 */
public class FileStatusBean {
    private String key;
    @FileStatusCode
    private int fileStatusCode;
    private long progress;
    private long total;
    private String errorMessage;

    public FileStatusBean(String key, @FileStatusCode int fileStatusCode) {
        this.key = key;
        this.fileStatusCode = fileStatusCode;
    }

    @FileStatusCode
    public int getFileStatusCode() {
        return fileStatusCode;
    }

    public void setFileStatusCode(@FileStatusCode int fileStatusCode) {
        this.fileStatusCode = fileStatusCode;
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
        return (float) ((double) progress / total);
    }

    @Override
    public String toString() {
        return "FileStatus{" +
                "key='" + key + '\'' +
                ", fileStatusCode=" + FileStatusUtils.fileStatusStr(fileStatusCode) +
                ", progress=" + progress +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
