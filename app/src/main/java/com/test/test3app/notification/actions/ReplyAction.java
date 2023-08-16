package com.test.test3app.notification.actions;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.RemoteInput;

import com.test.test3app.notification.TNotificationItem;
import com.test.test3app.notification.constant.NotificationActionKeys;

/**
 * created by zhaoyuntao
 * on 15/08/2023
 */
public class ReplyAction extends BaseAction<ReplyCallback> {
    private final CharSequence replyHint;

    public ReplyAction(@NonNull String title, int icon, CharSequence replyHint, ReplyCallback replyCallback) {
        super(NotificationActionKeys.ACTION_REPLY, title, icon, replyCallback);
        this.replyHint = replyHint;
    }

    public String getReplyKey() {
        return "replyKey";
    }

    public CharSequence getReplyHint() {
        return replyHint;
    }

    @Override
    public void notifyCallback(@NonNull TNotificationItem item, @NonNull ReplyCallback replyCallback, @NonNull Intent intent) {
        Bundle result = RemoteInput.getResultsFromIntent(intent);
        if (result != null) {
            CharSequence replyContent = result.getCharSequence(getReplyKey());
            replyCallback.onInputReply(item, replyContent);
        }
    }
}
