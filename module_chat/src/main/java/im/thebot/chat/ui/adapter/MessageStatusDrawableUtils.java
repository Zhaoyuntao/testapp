package im.thebot.chat.ui.adapter;

import com.example.module_chat.R;

import im.thebot.api.chat.constant.MessageStatusCode;

/**
 * created by zhaoyuntao
 * on 17/08/2021
 * description:
 */
public class MessageStatusDrawableUtils {
    public static int getMessageStatusIcon(@MessageStatusCode int messageStatus) {
        switch (messageStatus) {
            case MessageStatusCode.STATUS_MESSAGE_SEND_SENDING:
            case MessageStatusCode.STATUS_MESSAGE_SEND_SENDING_WAIT:
                return R.drawable.svg_message_send_sending;
            case MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_NOT_RECEIVE:
                return R.drawable.svg_message_send_success_not_received;
            case MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_RECEIVED:
                return R.drawable.svg_message_send_success_received;
            case MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_HAS_READ:
                return R.drawable.svg_message_send_success_received_and_read;
            case MessageStatusCode.STATUS_MESSAGE_SEND_FAILED:
                return R.drawable.svg_message_send_failed;
            default:
                return R.drawable.svg_message_send_error;
        }
    }
}
