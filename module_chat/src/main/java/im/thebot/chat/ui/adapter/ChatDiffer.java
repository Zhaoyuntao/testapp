package im.thebot.chat.ui.adapter;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.module_chat.BuildConfig;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.turbo.basetools.selector.DiffHelper;

/**
 * created by zhaoyuntao
 * on 2022/12/29
 */
public class ChatDiffer extends DiffUtil.ItemCallback<MessageBeanForUI> {
    public static final String CHANGE_MESSAGE_MESSAGE_DEBUG_INFO = "debugInfo";
    public static final String CHANGE_MESSAGE_MESSAGE_STATUS = "messageStatus";
    public static final String CHANGE_MESSAGE_AUDIO_IS_PLAYING = "audioPlaying";
    public static final String CHANGE_MESSAGE_AUDIO_PLAYING_PROGRESS = "audioPlayingProgress";
    public static final String CHANGE_MESSAGE_HIGH_LIGHT = "highLight";
    public static final String CHANGE_MESSAGE_SENDER_DETAIL = "senderDetails";
    public static final String CHANGE_MESSAGE_REPLIED_LIST_COUNT = "replyListCount";
    public static final String CHANGE_MESSAGE_SELECT = "selectState";
    public static final String CHANGE_MESSAGE_UNREAD_TITLE = "unreadTitle";
    public static final String CHANGE_MESSAGE_CONTENT = "content";

    @Override
    public boolean areItemsTheSame(@NonNull MessageBeanForUI messageA, @NonNull MessageBeanForUI messageB) {
        return messageA.getTimeSend() == messageB.getTimeSend();//TextUtils.equals(messageA.getUUID(), messageB.getUUID());
    }

    @Override
    public boolean areContentsTheSame(@NonNull MessageBeanForUI oldItem, @NonNull MessageBeanForUI newItem) {
        if (BuildConfig.DEBUG && !TextUtils.equals(oldItem.getErrorDescription(), newItem.getErrorDescription())
                || !TextUtils.equals(oldItem.getPayloadSourceJson(), newItem.getPayloadSourceJson())) {
            return false;
        }
        return oldItem.getTimeSend() == newItem.getTimeSend()
                && oldItem.getMessageStatus() == newItem.getMessageStatus();
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull MessageBeanForUI oldItem, @NonNull MessageBeanForUI newItem) {
        return getPayload(oldItem, newItem);
    }

    public static Bundle getPayload(@NonNull MessageBeanForUI oldItem, @NonNull MessageBeanForUI newItem) {
        if (oldItem.getType() != newItem.getType()) {
            return null;
        }
        Bundle bundle = new Bundle();
        if (!TextUtils.equals(oldItem.getContent(), newItem.getContent())) {
            DiffHelper.addPayload(bundle, CHANGE_MESSAGE_CONTENT, newItem.getContent());
        }
        if (oldItem.getMessageStatus() != newItem.getMessageStatus()) {
            DiffHelper.addPayload(bundle, CHANGE_MESSAGE_MESSAGE_STATUS, newItem.getMessageStatus());
        }

        if (BuildConfig.DEBUG && !TextUtils.equals(oldItem.getErrorDescription(), newItem.getErrorDescription())
                || !TextUtils.equals(oldItem.getPayloadSourceJson(), newItem.getPayloadSourceJson())) {
            DiffHelper.addPayload(bundle, CHANGE_MESSAGE_MESSAGE_DEBUG_INFO, newItem.getErrorDescription());
        }

        return bundle;
    }
}
