package im.thebot.chat.api.chat.message;

import android.text.TextUtils;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.api.chat.constant.FileStatusCode;
import im.thebot.chat.api.chat.message.text.BaseTextMessageForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public abstract class BaseFileMessageForUI extends BaseTextMessageForUI {
    private long fileSize;
    private String fileLocalPath;
    private String fileName;
    private String fileAES256key;
    private String fileEncryptUrl;
    private String fileDownloadUrl;
    @FileStatusCode
    private int fileState;

    public BaseFileMessageForUI(@MessageTypeCode int messageType) {
        super(messageType);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileAES256key() {
        return fileAES256key;
    }

    public void setFileAES256key(String fileAES256key) {
        this.fileAES256key = fileAES256key;
    }

    public String getFileEncryptUrl() {
        return fileEncryptUrl;
    }

    public void setFileEncryptUrl(String fileEncryptUrl) {
        this.fileEncryptUrl = fileEncryptUrl;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileState(@FileStatusCode int fileState) {
        this.fileState = fileState;
    }

    @FileStatusCode
    public int getFileState() {
        return fileState;
    }

    public boolean isFileReady() {
        return !TextUtils.isEmpty(fileLocalPath) && fileState == FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED ||
                fileState == FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED;
    }
}
