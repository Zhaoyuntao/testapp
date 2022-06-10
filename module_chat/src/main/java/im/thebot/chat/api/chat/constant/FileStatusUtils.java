package im.thebot.chat.api.chat.constant;


import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOADING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_NOT_FOUND;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOADING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_CANCELED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_FAILED;

import im.thebot.api.chat.constant.FileStatusCode;

/**
 * created by zhaoyuntao
 * on 06/11/2020
 * description:
 */
public class FileStatusUtils {
    public static String fileStatusStr(@FileStatusCode int status) {
        switch (status) {
            case STATUS_FILE_NOT_FOUND:
                return "NotStarted";
            case STATUS_FILE_DOWNLOADING:
                return "Downloading";
            case STATUS_FILE_UPLOADING:
                return "Uploading";
            case STATUS_FILE_DOWNLOAD_COMPLETED:
                return "DownloadCompleted";
            case STATUS_FILE_UPLOAD_COMPLETED:
                return "UploadCompleted";
            case STATUS_FILE_DOWNLOAD_FAILED:
                return "Failed";
            case STATUS_FILE_UPLOAD_FAILED:
                return "UploadFailed";
            case STATUS_FILE_DOWNLOAD_CANCELED:
                return "DownloadCanceled";
            case STATUS_FILE_UPLOAD_CANCELED:
                return "UploadCanceled";
            default:
                return "UnknownStatus-" + status;
        }
    }
}
