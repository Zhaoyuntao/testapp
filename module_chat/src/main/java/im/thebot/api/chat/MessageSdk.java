package im.thebot.api.chat;

import android.text.TextUtils;

import java.io.File;

import im.thebot.api.chat.constant.FileStatusCode;
import im.thebot.api.chat.listener.FileStatusBean;
import im.thebot.chat.api.chat.listener.FileListener;
import im.thebot.chat.api.chat.message.BaseFileMessageForUI;
import im.thebot.chat.api.chat.message.MessageBeanForUI;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class MessageSdk {
    public void registerFileListener(FileListener fileListener) {

    }

    public FileStatusBean getFileStatus(String sessionId, String uuid) {
        return new FileStatusBean("1234", FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED);
    }

    public void unregisterFileListener(FileListener fileListener) {

    }

    public boolean isFileExists(String fileLocalPath) {
        return !TextUtils.isEmpty(fileLocalPath) && new File(fileLocalPath).exists();
    }

    public void downloadFile(String sessionId, String uuid) {

    }

    public void cancelDownloadFile(String sessionId, String uuid) {

    }

    public <M extends BaseFileMessageForUI> void resendMessage(M message) {


    }

    public void cancelUploadFile(String sessionId, String uuid) {

    }

    public MessageBeanForUI loadMessage(String sessionId, String uuid) {
        return null;
    }
}
