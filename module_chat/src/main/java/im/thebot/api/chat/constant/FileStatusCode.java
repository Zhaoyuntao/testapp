package im.thebot.api.chat.constant;

import androidx.annotation.IntDef;

//file and picture
@IntDef({
        FileStatusCode.STATUS_FILE_NOT_FOUND,
        FileStatusCode.STATUS_FILE_DOWNLOADING,
        FileStatusCode.STATUS_FILE_UPLOADING,
        FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED,
        FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED,
        FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED,
        FileStatusCode.STATUS_FILE_UPLOAD_FAILED,
        FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED,
        FileStatusCode.STATUS_FILE_UPLOAD_CANCELED,
        FileStatusCode.STATUS_FILE_UPLOAD_WAITING,
        FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING
})
public @interface FileStatusCode {
    int STATUS_FILE_NOT_FOUND = 0;
    int STATUS_FILE_DOWNLOADING = 1;
    int STATUS_FILE_UPLOADING = 2;
    int STATUS_FILE_DOWNLOAD_COMPLETED = 3;
    int STATUS_FILE_UPLOAD_COMPLETED = 4;
    int STATUS_FILE_DOWNLOAD_FAILED = 5;
    int STATUS_FILE_UPLOAD_FAILED = 6;
    int STATUS_FILE_DOWNLOAD_CANCELED = 7;
    int STATUS_FILE_UPLOAD_CANCELED = 8;
    int STATUS_FILE_UPLOAD_WAITING = 9;
    int STATUS_FILE_DOWNLOAD_WAITING = 10;
}
