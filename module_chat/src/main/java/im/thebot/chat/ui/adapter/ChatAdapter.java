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

import com.example.module_chat.R;

import java.util.List;
import java.util.Objects;

import im.thebot.api.chat.constant.MessageStatusCode;
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
        int viewType = MessageCellManager.getItemViewTypeFromOriginType(originType);
        BaseMessageCell baseMessageCell = MessageCellManager.getChatCellByViewType(viewType);
        int flags = MessageCellManager.getFlagsFromOriginType(originType);
        baseMessageCell.setPresenter(presenter);
        if (MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_GRAVITY_LEFT)) {
            baseMessageCell.setGravity(Gravity.START);
        } else if (MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_GRAVITY_RIGHT)) {
            baseMessageCell.setGravity(Gravity.END);
        } else {
            baseMessageCell.setGravity(Gravity.CENTER);
        }
        baseMessageCell.setShowTimeTitle(MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_TIME));
        baseMessageCell.setShowTopSpace(MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_TOP_SPACE));
        baseMessageCell.setShowSenderName(MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_SENDER_NAME));
        baseMessageCell.setShowNewMessageLine(MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_NEW_MESSAGE_LINE));
        return new ChatItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_cell_a_container, parent, false), baseMessageCell);
    }

    @Override
    public int getItemViewType(int position) {
        MessageBeanForUI messageBean = getMessage(position);
        int originType = MessageCellManager.getItemViewType(messageBean);
//        ChatMessageLogger.ss("getItemViewType:" + position + " " + originType, messageBean);
        boolean needShowNewMessageLine = TextUtils.equals(messageBean.getUUID(), getPresenter().getUUIDForNewMessageLine());
        boolean needShowTimeTitle = needShowTimeTitle(messageBean, position);
        boolean needShowSenderName = needShowSenderName(messageBean, position, needShowNewMessageLine, needShowTimeTitle);
        boolean needShowTopSpace = needShowTopSpace(messageBean, position);
        if (needShowNewMessageLine) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_NEW_MESSAGE_LINE);
        }
        if (needShowTimeTitle) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_TIME);
        }
        if (needShowSenderName) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_SENDER_NAME);
        }
        if (needShowTopSpace) {
            originType = MessageCellManager.addFlagToItemViewType(originType, MessageCellFlag.FLAG_LAYOUT_SHOW_TOP_SPACE);
        }
        return MessageCellManager.addFlagToItemViewType(originType, getMessageCellGravityFlag(messageBean, position));
    }

    @Override
    public int getItemCount() {
        return messageSize();
    }

    private int getMessageCellGravityFlag(MessageBeanForUI messageBean, int position) {
        return ContactUtil.isSelf(messageBean.getSenderUid()) ? MessageCellFlag.FLAG_GRAVITY_RIGHT : MessageCellFlag.FLAG_GRAVITY_LEFT;
    }

    @Override
    final public void onBindViewHolder(@NonNull ChatItemHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() > 0) {
            // merge the payloads first
            Bundle mergedPayload = (Bundle) payloads.get(0);
            final int size = payloads.size();
            for (int i = 1; i < size; i++) {
                Bundle p = (Bundle) payloads.get(i);
                mergedPayload.putAll(p); // the latest payload will override the previous version
            }
            doBindViewHolder(holder, position, mergedPayload);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    protected void doBindViewHolder(@NonNull ChatItemHolder holder, int position, @NonNull Bundle payload) {
        MessageBeanForUI messageBean = Objects.requireNonNull(getMessage(position));
        holder.itemView.setTag(messageBean.getUUID());

        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_MESSAGE_STATUS)) {
            holder.setMessageStatusView(messageBean,
                    messageBean.getMessageStatus() == MessageStatusCode.STATUS_MESSAGE_SENDER_SEND_SUCCESS_RECEIVER_HAS_READ);
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_HIGH_LIGHT)) {
            holder.highLight();
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_UNREAD_TITLE)) {
            holder.setUnreadMessageTitle(getPresenter().getUnreadCountForNewMessageLine());
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_CONTENT)) {
            holder.setContentView(messageBean, showReplySourceMessagePart());
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_SELECT)) {
            holder.setCheckState(isSelected(messageBean.getUUID()));
        }
        if (payload.containsKey(ChatDiffer.CHANGE_MESSAGE_SENDER_DETAIL)) {
            holder.showSenderName(messageBean.getSenderUid());
            holder.refreshTail(needShowTail(messageBean, position));
        }

        initClickListener(messageBean, holder);
        holder.changeMessageContent(messageBean);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemHolder holder, int positionNotUse) {
//        S.s("onBindViewHolder:" + positionNotUse);
        int position = holder.getBindingAdapterPosition();
        MessageBeanForUI messageBean = Objects.requireNonNull(getMessage(position));
        holder.reset();
        holder.setCheckState(isSelected(messageBean.getUUID()));
        holder.setUnreadMessageTitle(getPresenter().getUnreadCountForNewMessageLine());
        holder.itemView.setTag(messageBean.getUUID());
        if (messageBean.isSystemEvent()) {
            bindSystemEvent(messageBean, holder);
        } else {
            bindMessageDetails(messageBean, holder, position);
        }
        //Debug info.
        setDebugInfo(holder, position, messageBean);
    }

    private void bindSystemEvent(MessageBeanForUI messageBean, ChatItemHolder holder) {
        holder.setContentView(messageBean, showReplySourceMessagePart());
        holder.hideSenderName();
    }

    private void bindMessageDetails(@NonNull MessageBeanForUI message,
                                    @NonNull ChatItemHolder holder, int position) {
        holder.showSenderName(message.getSenderUid());
        holder.refreshTail(needShowTail(message, position));
        holder.setForwardView(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessageClickListener.onClickForwardButton(message);
            }
        });
        holder.setTime(message, TimeUtils.getTimeStringHMS(message.getTimeSend()));
        holder.setMessageStatusView(message, false);
        //init content view (cell) data
        holder.setContentView(message, showReplySourceMessagePart());
        //On message bind to view.
        if (!TextUtils.equals("?", message.getUUID())) {
            onMessageClickListener.onMessageBindToAdapter(message);
        }
        //Change click event.
        initClickListener(message, holder);
    }

    private void initClickListener(@NonNull MessageBeanForUI messageBean, ChatItemHolder holder) {
        holder.setOnClickMessageListener(new AvoidDoubleClickListener() {
            @Override
            public void onClickView(View view) {
            }

            @Override
            public void onClickView(View view, boolean timeShort) {
                int position = holder.getBindingAdapterPosition();
                S.s("click message position:"+position+" isSelecting:"+isSelecting());
                if (isSelecting()) {
                    boolean selected = !isSelected(position);
                    holder.setCheckState(selected);
                    onMessageClickListener.onSelectItem(messageBean, selected);
                } else {
                    if (timeShort) {
                        return;
                    }
                    if (onMessageClickListener.canClickMessage(messageBean)) {
                        BaseMessageCell<MessageBeanForUI> baseCell = holder.getBaseMessageCell();
                        if (baseCell != null && baseCell.isSupportClick()) {
                            baseCell.onClickMessage(messageBean);
                        }
                    }
                }
            }

            @Override
            protected boolean controlByHand() {
                return true;
            }
        });
        holder.setOnSlideLayoutClickMessageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                S.s("click slide position:"+position+" isSelecting:"+isSelecting());
                if (isSelecting()) {
                    boolean selected = !isSelected(position);
                    holder.setCheckState(selected);
                    onMessageClickListener.onSelectItem(messageBean, selected);
                }
            }
        });
        holder.setOnMessageLongClickMessageListener(view -> {
            S.s("long click message");
            BaseMessageCell<MessageBeanForUI> baseCell = holder.getBaseMessageCell();
            if (baseCell != null && baseCell.isSupportClick()) {
                if (!isSelecting()) {
                    holder.setCheckState(true);
                    onMessageClickListener.onSelectItem(messageBean, true);
                    return true;
                }
            }
            return false;
        });
        holder.setOnSlideLongClickMessageListener(view -> {
            S.s("long click slide");
            BaseMessageCell<MessageBeanForUI> baseCell = holder.getBaseMessageCell();
            if (baseCell != null && baseCell.isSupportClick()) {
                if (!isSelecting()) {
                    holder.setCheckState(true);
                    onMessageClickListener.onSelectItem(messageBean, true);
                    return true;
                }
            }
            return false;
        });
        holder.setSlideListener(new SlideLayout.SlideListener() {
            @Override
            public void onFingerUp(boolean slideToRight) {
                if (slideToRight) {
                    onMessageClickListener.onSlideMessage(messageBean);
                }
            }

            @Override
            public void onSlideDistanceChange(float percent) {

            }

            @Override
            public boolean canSlide() {
                return !isSelecting() && messageBean.canBeReplied();
            }
        });
        holder.setOnClickNameListener(new AvoidDoubleClickListener() {
            @Override
            public void onClickView(View view) {
                onMessageClickListener.onNameClick(messageBean);
            }
        });
        holder.setOnClickReplyListener(new AvoidDoubleClickListener() {
            @Override
            public void onClickView(View view) {
                onMessageClickListener.onClickReplyMessage(messageBean);
            }
        });
        holder.setLongClickReplyListener(v -> onMessageClickListener.onLongClickReplyMessage(messageBean));
    }

    private void setDebugInfo(ChatItemHolder holder, int position, MessageBeanForUI messageBean) {
        //Debug info.
        holder.setDebugInfo(messageBean, position);
    }

    @Override
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

    @Override
    public boolean showReplySourceMessagePart() {
        return true;
    }

    private boolean isSelected(String uuid) {
        return getPresenter().isMessageSelected(uuid);
    }

    private boolean isSelected(int position) {
        return getPresenter().isMessageSelected(position);
    }

    private boolean isSelecting() {
        return getPresenter().isSelecting();
    }

    public void quitSelectMode() {
        notifyItemRangeChanged(0, messageSize(), DiffHelper.getPayload(ChatDiffer.CHANGE_MESSAGE_SELECT, false));
    }

    public void setMessageCallback(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    public interface OnMessageClickListener {
        void onMessageBindToAdapter(@NonNull MessageBeanForUI messageBean);

        boolean canClickMessage(@NonNull MessageBeanForUI messageBean);

        void onClickReplyMessage(@NonNull MessageBeanForUI messageBean);

        boolean onLongClickReplyMessage(@NonNull MessageBeanForUI messageBean);

        void onNameClick(@NonNull MessageBeanForUI messageBean);

        void onClickForwardButton(@NonNull MessageBeanForUI message);

        void onSlideMessage(@NonNull MessageBeanForUI messageBean);

        void onSelectItem(@NonNull MessageBeanForUI messageBean, boolean selected);
    }
}
