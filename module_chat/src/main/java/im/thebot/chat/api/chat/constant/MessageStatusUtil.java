package im.thebot.chat.api.chat.constant;

import static im.thebot.api.chat.constant.MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_HAS_READ;
import static im.thebot.api.chat.constant.MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_NOT_RECEIVE;
import static im.thebot.api.chat.constant.MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_RECEIVED;
import static im.thebot.api.chat.constant.MessageStatusCode.STATUS_MESSAGE_SEND_CANCEL;
import static im.thebot.api.chat.constant.MessageStatusCode.STATUS_MESSAGE_SEND_FAILED;
import static im.thebot.api.chat.constant.MessageStatusCode.STATUS_MESSAGE_SEND_SENDING;
import static im.thebot.api.chat.constant.MessageStatusCode.STATUS_MESSAGE_SEND_SENDING_WAIT;

import android.annotation.SuppressLint;

import im.thebot.api.chat.constant.MessageStatusCode;

/**
 * created by zhaoyuntao
 * on 2020/6/17
 * description:
 */
public class MessageStatusUtil {

    public static boolean isSendSuccess(@MessageStatusCode int messageStatus) {
        return messageStatus == STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_NOT_RECEIVE ||
                messageStatus == STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_RECEIVED ||
                messageStatus == STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_HAS_READ;
    }

    public static boolean isSendCanceled(@MessageStatusCode int messageStatus) {
        return messageStatus == STATUS_MESSAGE_SEND_CANCEL;
    }

    public static boolean isSendFailed(@MessageStatusCode int messageStatus) {
        return messageStatus == STATUS_MESSAGE_SEND_FAILED;
    }

    public static boolean isSending(@MessageStatusCode int messageStatus) {
        return messageStatus == STATUS_MESSAGE_SEND_SENDING_WAIT || messageStatus == STATUS_MESSAGE_SEND_SENDING;
    }

    public static boolean isSendingWaiting(@MessageStatusCode int messageStatus) {
        return messageStatus == STATUS_MESSAGE_SEND_SENDING_WAIT;
    }

    @SuppressLint("SwitchIntDef")
    public static String messageStatusStr(@MessageStatusCode int status) {
        switch (status) {
            case STATUS_MESSAGE_SEND_SENDING:
                return "Sending";
            case STATUS_MESSAGE_SEND_SENDING_WAIT:
                return "SendingWait";
            case STATUS_MESSAGE_SEND_FAILED:
                return "SendFailed";
            case STATUS_MESSAGE_SEND_CANCEL:
                return "SendCancel";
            case STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_NOT_RECEIVE:
                return "SentNotReceive";
            case STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_RECEIVED:
                return "SentReceived";
            case STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_HAS_READ:
                return "SentHasRead";
            default:
                return "UnknownStatus-" + status;
        }
    }

    public static String messageFailedReasonStr(@MessageFailedReason int reason) {
        switch (reason) {
            case MessageFailedReason.FAIL_REASON_CANCEL:
                return "Canceled";
            case MessageFailedReason.FAIL_REASON_NETWORK:
                return "Network error";
            case MessageFailedReason.FAIL_REASON_TIMEOUT:
                return "Timeout";
            case MessageFailedReason.FAIL_REASON_UNKNOWN:
            default:
                return "Unknown";
        }
    }
}
