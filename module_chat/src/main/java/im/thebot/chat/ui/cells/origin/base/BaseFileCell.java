package im.thebot.chat.ui.cells.origin.base;

import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOADING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_NOT_FOUND;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOADING;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_CANCELED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_COMPLETED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_FAILED;
import static im.thebot.api.chat.constant.FileStatusCode.STATUS_FILE_UPLOAD_WAITING;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdk.chat.test.ChatMessageLogger;

import org.jetbrains.annotations.NotNull;

import im.thebot.SdkFactory;
import im.thebot.api.chat.listener.FileStatusBean;
import im.thebot.chat.api.chat.listener.FileListener;
import im.thebot.chat.api.chat.message.BaseFileMessageForUI;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public abstract class BaseFileCell<M extends BaseFileMessageForUI> extends BaseTextCell<M> {
    private FileListener fileListener;
    private final boolean logOpen = false;

    @Override
    final public void onTextTypeMessageInit(@NonNull M message) {
        onInitViewMode(message);
        onFileMessageInit(message);
        registerListener();
        loadMedia(message);
    }

    @Override
    final public void onTextTypeMessageChanged(@NonNull M message) {
        onInitViewMode(message);
        onFileMessageChanged(message);
        registerListener();
        loadMedia(message);
    }

    protected abstract void onInitViewMode(@NonNull M message);

    protected abstract void onClearViewMode();

    private void registerListener() {
        if (fileListener == null) {
            fileListener = new FileListener(getMessage().getUUID()) {
                @Override
                public void onFileStatusChanged(@NotNull FileStatusBean fileStatus) {
                    ThreadPool.runUi(new SafeRunnable(BaseFileCell.this.getContext()) {
                        @Override
                        protected void runSafely() {
                            updateFileState(fileStatus);
                        }
                    });
                }
            };
        } else {
            fileListener.setKey(getMessage().getUUID());
        }
        if (isAttachedToWindow()) {
            SdkFactory.getChatSdk().registerFileListener(fileListener);
        }
        FileStatusBean fileStatus = SdkFactory.getChatSdk().getFileStatus(getMessage().getSessionId(), getMessage().getUUID());
        updateFileState(fileStatus);
        onRegisterListener();
    }

    protected void onRegisterListener() {

    }

    private void unregisterListener() {
        SdkFactory.getFileSdk().unregisterFileListener(fileListener);
        onUnregisterListener();
    }

    protected void onUnregisterListener() {

    }

    private void updateFileState(@NonNull FileStatusBean fileStatus) {
        if (!fileStatus.getKey().equals(getTag())) {
            return;
        }
        M message = getMessage();
        int fileStatusCode = fileStatus.getFileStatusCode();
        switch (fileStatusCode) {
            case STATUS_FILE_NOT_FOUND:
                onFileStateNotFound();
                break;
            case STATUS_FILE_DOWNLOAD_WAITING:
                _onDownloadWaiting(message);
                break;
            case STATUS_FILE_DOWNLOADING:
                if (message.getFileState() == STATUS_FILE_UPLOAD_CANCELED) {
                    _onDownloadCanceled(message);
                } else {
                    _onDownloading(message, fileStatus.getProgress(), fileStatus.getTotal(), fileStatus.getPercent());
                }
                break;
            case STATUS_FILE_DOWNLOAD_COMPLETED:
                unregisterListener();
                _onDownloadSuccess(message);
                break;
            case STATUS_FILE_DOWNLOAD_FAILED:
                _onDownloadFailed(message);
                break;
            case STATUS_FILE_DOWNLOAD_CANCELED:
                _onDownloadCanceled(message);
                break;
            case STATUS_FILE_UPLOAD_WAITING:
                _onUploadWaiting(message);
                break;
            case STATUS_FILE_UPLOADING:
                if (message.getFileState() == STATUS_FILE_UPLOAD_CANCELED) {
                    _onUploadCanceled(message);
                } else {
                    _onUploading(message, fileStatus.getProgress(), fileStatus.getTotal(), fileStatus.getPercent());
                }
                break;
            case STATUS_FILE_UPLOAD_COMPLETED:
                unregisterListener();
                _onUploadSuccess(message);
                break;
            case STATUS_FILE_UPLOAD_FAILED:
                _onUploadFailed(message);
                break;
            case STATUS_FILE_UPLOAD_CANCELED:
                _onUploadCanceled(message);
                break;
        }
    }

    private void onFileStateNotFound() {
        M message = getMessage();
        if (message.isSelf()) {
            if (message.isSendCanceled() || message.getFileState() == STATUS_FILE_UPLOAD_CANCELED) {
                _onUploadCanceled(message);
            } else if (message.isSendSuccess()) {
                if (SdkFactory.getChatSdk().isFileExists(getMessage().getFileLocalPath())) {
                    _onFileExists(message);
                } else {
                    if (message.getFileState() == STATUS_FILE_NOT_FOUND) {
                        _onFileNotExists(message);
                    } else if (message.getFileState() == STATUS_FILE_DOWNLOAD_CANCELED) {
                        _onDownloadCanceled(message);
                    } else {
                        _onDownloadFailed(message);
                    }
                }
            } else {
                _onUploadFailed(message);
            }
        } else {
            if (SdkFactory.getChatSdk().isFileExists(getMessage().getFileLocalPath())) {
                _onFileExists(message);
            } else {
                if (message.getFileState() == STATUS_FILE_NOT_FOUND) {
                    _onFileNotExists(message);
                } else if (message.getFileState() == STATUS_FILE_DOWNLOAD_CANCELED) {
                    _onDownloadCanceled(message);
                } else {
                    _onDownloadFailed(message);
                }
            }
        }
    }

    protected void downloadFile() {
        M message = getMessage();
        ChatMessageLogger.ss(logOpen, "start download[" + message.getFileDownloadUrl() + "]", message);
        if (TextUtils.isEmpty(message.getFileDownloadUrl())) {
            S.e(logOpen, "downloadFile error: file info missing: download url is empty!");
            return;
        }
//        S.sd("downloadFile:" + message.getTimeSend() + " " + message.getFileDownloadState());
        SdkFactory.getChatSdk().downloadFile(message.getSessionId(), message.getUUID());
    }

    protected void cancelDownloadFile() {
        M message = getMessage();
//        ChatMessageLogger.ss("cancel download", message);
        SdkFactory.getChatSdk().cancelDownloadFile(message.getSessionId(), message.getUUID());
    }

    protected void uploadFile() {
        M message = getMessage();
        ChatMessageLogger.ss("start resend file:", message);
        if (TextUtils.isEmpty(message.getFileLocalPath())) {
            S.e(logOpen, "uploadFile error: file info missing: local path is empty!");
            return;
        }
        SdkFactory.getChatSdk().resendMessage(message);
    }

    protected void cancelUploadFile() {
        M message = getMessage();
//        ChatMessageLogger.ss("cancel download", message);
        SdkFactory.getChatSdk().cancelUploadFile(message.getSessionId(), message.getUUID());
    }

    protected abstract void onFileMessageInit(@NonNull M message);

    protected abstract void onFileMessageChanged(@NonNull M message);

    private void _onFileExists(@NonNull M message) {
        S.sd(logOpen, "_onFileExists", 1);
        onFileExists(message);
    }

    private void _onFileNotExists(@NonNull M message) {
        S.ed(logOpen, "_onFileNotExists[" + message.getFileLocalPath() + "]", 1);
        onFileNotExists(message);
    }

    private void _onDownloadWaiting(@NonNull M message) {
        S.sd(logOpen, "_onDownloadWaiting", 1);
        onDownloadWaiting(message);
    }

    private void _onDownloading(@NonNull M message, long progress, long total, float percent) {
        S.sd(logOpen, "_onDownloading", 1);
        onDownloading(message, progress, total, percent);
    }

    private void _onDownloadSuccess(@NonNull M message) {
        S.sd(logOpen, "_onDownloadSuccess", 1);
        onDownloadSuccess(message);
        loadMedia(message);
    }

    private void _onDownloadFailed(@NonNull M message) {
        S.ed(logOpen, "_onDownloadFailed", 1);
        onDownloadFailed(message);
    }

    private void _onDownloadCanceled(@NonNull M message) {
        S.ed(logOpen, "_onDownloadCanceled", 1);
        onDownloadCanceled(message);
    }

    private void _onUploadWaiting(@NonNull M message) {
        S.sd(logOpen, "_onUploadWaiting", 1);
        onUploadWaiting(message);
    }

    private void _onUploading(@NonNull M message, float progress, long total, float percent) {
        S.sd(logOpen, "_onUploading", 1);
        onUploading(message, progress, total, percent);
    }

    private void _onUploadSuccess(@NonNull M message) {
        S.sd(logOpen, "_onUploadSuccess", 1);
        onUploadSuccess(message);
    }

    private void _onUploadFailed(@NonNull M message) {
        S.ed(logOpen, "_onUploadFailed", 1);
        onUploadFailed(message);
    }

    private void _onUploadCanceled(@NonNull M message) {
        S.ed(logOpen, "_onUploadCanceled", 1);
        onUploadCanceled(message);
    }

    protected abstract void onFileExists(@NonNull M message);

    protected abstract void onFileNotExists(@NonNull M message);

    protected abstract void onDownloadWaiting(@NonNull M message);

    protected abstract void onDownloading(@NonNull M message, long progress, long total, float percent);

    protected abstract void onDownloadSuccess(@NonNull M message);

    protected abstract void onDownloadFailed(@NonNull M message);

    protected abstract void onDownloadCanceled(@NonNull M message);

    protected abstract void onUploadWaiting(@NonNull M message);

    protected abstract void onUploading(@NonNull M message, float progress, long total, float percent);

    protected abstract void onUploadSuccess(@NonNull M message);

    protected abstract void onUploadFailed(@NonNull M message);

    protected abstract void onUploadCanceled(@NonNull M message);

    protected abstract void loadMedia(@NonNull M message);

    @Override
    final protected void onAttachedToTextTypeCell(@Nullable M message) {
        if (message != null) {
            onInitViewMode(message);
            registerListener();
        }
    }

    @Override
    final protected void onDetachedFromTextTypeCell(@Nullable M message) {
        onClearViewMode();
        unregisterListener();
    }
}
