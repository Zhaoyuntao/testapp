package im.thebot.chat.ui.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.module_chat.BuildConfig;
import com.example.module_chat.R;

import java.util.List;
import java.util.Objects;

import im.thebot.api.chat.constant.MessageStatusCode;
import im.thebot.chat.api.chat.constant.ChatDebugParam;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.mvp.presenter.ChatPresenter;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;
import im.thebot.chat.ui.view.SlideLayout;
import im.thebot.user.ContactUtil;
import im.turbo.basetools.selector.DiffHelper;
import im.turbo.basetools.time.TimeUtils;
import im.turbo.baseui.clicklistener.AvoidDoubleClickListener;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 2022/12/29
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatItemHolder> implements BaseChatAdapter {
    protected OnMessageClickListener onMessageClickListener;
    private final ChatPresenter presenter;

    public ChatAdapter(ChatPresenter presenter) {
        this.presenter = presenter;
    }

    protected ChatPresenter getPresenter() {
        return presenter;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @NonNull
    @Override
    public ChatItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int originType) {
        BaseMessageCell baseMessageCell = MessageCellManager.getChatCellByViewType(MessageCellManager.getItemViewTypeFromOriginType(originType));
        baseMessageCell.setPresenter(presenter);
        return new ChatItemHolder(new ChatItemView(parent.getContext(), baseMessageCell, MessageCellManager.getFlagsFromOriginType(originType)));
    }

    @Override
    public int getItemViewType(int position) {
        MessageBeanForUI messageBean = getMessage(position);
        int originType = MessageCellManager.getItemViewType(messageBean);
        boolean needShowNewMessageLine = TextUtils.equals(messageBean.getUUID(), getPresenter().getUUIDForNewMessageLine());
        boolean needShowTimeTitle = needShowTimeTitle(messageBean, position);
        if (needShowNewMessageLine) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_NEW_MESSAGE_LINE);
        }
        if (needShowTimeTitle) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_TIME);
        }
        if (needShowSenderName(messageBean, position, needShowNewMessageLine, needShowTimeTitle)) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_SENDER_NAME);
        }
        if (needShowTopSpace(messageBean, position)) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_TOP_SPACE);
        }
        if (needShowBottomSpace(messageBean, position)) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_BOTTOM_SPACE);
        }
        if (needShowForwardHead(messageBean, position)) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_FORWARD_HEAD);
        }
        if (messageBean.getMessageReply() != null) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_REPLY);
        }
        return MessageCellManager.addFlagToItemViewType(originType, getMessageCellGravityFlag(messageBean, position));
    }

    @Override
    public int getItemCount() {
        return messageSize();
    }

    @Override
    final public void onBindViewHolder(@NonNull ChatItemHolder holder, int positionDeprecated, @NonNull List<Object> payloads) {
        if (payloads.size() > 0) {
            // merge the payloads first
            Bundle mergedPayload = (Bundle) payloads.get(0);
            final int size = payloads.size();
            for (int i = 1; i < size; i++) {
                Bundle p = (Bundle) payloads.get(i);
                mergedPayload.putAll(p); // the latest payload will override the previous version
            }
            doBindViewHolder(holder, positionDeprecated, mergedPayload);
        } else {
            onBindViewHolder(holder, positionDeprecated);
        }
    }

    protected void doBindViewHolder(@NonNull ChatItemHolder holder, int positionDeprecated, @NonNull Bundle payload) {
        int position = holder.getBindingAdapterPosition();
        MessageBeanForUI messageBean = Objects.requireNonNull(getMessage(position));
        holder.itemView.setTag(messageBean.getUUID());
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_SELECT)) {
            holder.refreshCheckState(messageBean);
            return;
        }

        if (BuildConfig.DEBUG && payload.containsKey(ChatDiffer.CHANGE_MESSAGE_MESSAGE_DEBUG_INFO)) {
            holder.setDebugInfo(messageBean);
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_MESSAGE_STATUS)) {
            holder.setMessageStatusView(messageBean,
                    messageBean.getMessageStatus() == MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_HAS_READ);
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_HIGH_LIGHT)) {
            holder.tryToHighLight();
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_UNREAD_TITLE)) {
            holder.setUnreadMessageTitle(getPresenter().getUnreadCountForNewMessageLine());
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_CONTENT)) {
            holder.setMessageContent(messageBean);
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_SENDER_DETAIL)) {
            holder.bindSenderName(messageBean.getSenderUid());
            holder.refreshBubble(messageBean, needShowTail(messageBean, position));
        }

        holder.initClickListener(messageBean, onMessageClickListener);
        holder.changeMessageContent(messageBean);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemHolder holder, int positionDeprecated) {
        int position = holder.getBindingAdapterPosition();
        MessageBeanForUI message = Objects.requireNonNull(getMessage(position));
        holder.itemView.setTag(message.getUUID());
        holder.setUnreadMessageTitle(getPresenter().getUnreadCountForNewMessageLine());
        holder.refreshBubble(message, needShowTail(message, position));
        holder.bindMessage(message, onMessageClickListener);
    }

    public boolean forceShowSenderDetails(MessageBeanForUI messageBean, int position) {
        return false;
    }

    @Override
    public int messageSize() {
        return getPresenter().messageSize();
    }

    @Override
    public MessageBeanForUI getMessage(int position) {
        return getPresenter().getMessage(position);
    }

    public void quitSelectMode() {
        notifyItemRangeChanged(0, messageSize(), DiffHelper.getPayload(ChatDiffer.CHANGE_MESSAGE_SELECT, false));
    }

    public void setMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    public interface OnMessageClickListener {
        void onClickReplyMessage(@NonNull MessageBeanForUI messageBean);

        void onNameClick(@NonNull MessageBeanForUI messageBean);

        void onClickForwardButton(@NonNull MessageBeanForUI message);

        void onSlideMessage(@NonNull MessageBeanForUI messageBean);

        void onSelectItem(@NonNull MessageBeanForUI messageBean, boolean selected);
    }
}
