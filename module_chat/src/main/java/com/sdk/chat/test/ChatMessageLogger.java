package com.sdk.chat.test;

import androidx.annotation.Nullable;


import com.sdk.chat.helper.MessageTypeStringHelper;

import java.util.List;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.turbo.basetools.time.TimeUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 28/12/2021
 * description:
 */
public class ChatMessageLogger {

    public static void ss(MessageBeanForUI message) {
        log("", message, false, 2, 0);
    }

    public static void ss(boolean open, String tag, MessageBeanForUI message) {
        if (!open) {
            return;
        }
        log(tag, message, false, 2, 0);
    }

    public static void ss(String tag, MessageBeanForUI message) {
        log(tag, message, false, 2, 0);
    }

    public static void ee(MessageBeanForUI message) {
        log("", message, true, 2, 0);
    }

    public static void ee(String tag, MessageBeanForUI message) {
        log(tag, message, true, 2, 0);
    }

    public static void ss(String tag, List<MessageBeanForUI> messages) {
        S.llll(tag);
        if (messages != null) {
            for (MessageBeanForUI model : messages) {
                log(tag, model, false, 2, 0);
            }
        }
        S.llll(tag);
    }

    public static void ss(boolean open, String tag, List<MessageBeanForUI> messages) {
        if (!open) {
            return;
        }
        S.llll(tag);
        if (messages != null) {
            for (MessageBeanForUI model : messages) {
                log(tag, model, false, 2, 0);
            }
        }
        S.llll(tag);
    }

    public static void log(String tag, @Nullable MessageBeanForUI message, boolean error, int offset, int depth) {
        String log = message == null ? "<null>" : "[" + message.getClass().getSimpleName() + "]" +
                "[" + message.getContent() + "] " +
                "[uuid:" + message.getUUID() + "] " +
                "[sessionId:" + message.getSessionId() + "] " +
                "[status:" + message.getMessageStatus() + "] " +
                "[sender:" + message.getSenderUid() + "] " +
                "[type:" + message.getType() + "|" + MessageTypeStringHelper.toString(message.getType()) + "]:" +
                "        time(" + message.getTimeSend() + "):" + TimeUtils.getTimeString(message.getTimeSend(), TimeUtils.TIME_FORMAT_YMD_HMS);
        if (error) {
            S.ed(tag + " " + log, offset, depth);
        } else {
            S.sd(tag + " " + log, offset, depth);
        }
    }
}
