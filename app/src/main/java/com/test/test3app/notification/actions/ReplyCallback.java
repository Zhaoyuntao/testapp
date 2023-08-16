package com.test.test3app.notification.actions;

import androidx.annotation.NonNull;

import com.test.test3app.notification.TNotificationItem;

/**
 * created by zhaoyuntao
 * on 15/08/2023
 */
public interface ReplyCallback extends BaseCallback {
    void onInputReply(@NonNull TNotificationItem item, CharSequence replyContent);
}
