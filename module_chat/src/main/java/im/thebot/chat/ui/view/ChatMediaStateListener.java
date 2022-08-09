package im.thebot.chat.ui.view;

import androidx.annotation.NonNull;

import im.thebot.chat.api.chat.message.BaseFileMessageForUI;

/**
 * created by zhaoyuntao
 * on 20/06/2022
 * description:
 */
public interface ChatMediaStateListener {
    default void onFileNotExists(@NonNull BaseFileMessageForUI message) {
    }

    default void onDownloadWaiting(@NonNull BaseFileMessageForUI message) {
    }

    default void onDownloading(@NonNull BaseFileMessageForUI message, long progress, long total, float percent) {
    }

    default void onDownloadSuccess(@NonNull BaseFileMessageForUI message) {
    }

    default void onDownloadFailed(@NonNull BaseFileMessageForUI message) {
    }

    default void onDownloadCanceled(@NonNull BaseFileMessageForUI message) {
    }

    default void onUploadWaiting(@NonNull BaseFileMessageForUI message) {
    }

    default void onUploading(@NonNull BaseFileMessageForUI message, float progress, long total, float percent) {
    }

    default void onUploadSuccess(@NonNull BaseFileMessageForUI message) {
    }

    default void onUploadFailed(@NonNull BaseFileMessageForUI message) {
    }

    default void onUploadCanceled(@NonNull BaseFileMessageForUI message) {
    }

    default void loadMedia(@NonNull BaseFileMessageForUI message) {
    }
}
