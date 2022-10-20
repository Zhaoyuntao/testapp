package im.thebot.chat.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import im.thebot.chat.api.chat.message.MessageBeanForUI;

/**
 * created by zhaoyuntao
 * on 2020-03-25
 * description:
 */
final public class ChatItemHolder extends RecyclerView.ViewHolder {

    private final ChatItemView chatItemView;

    public ChatItemHolder(final ChatItemView itemView) {
        super(itemView);
        this.chatItemView = itemView;
    }

    public void refreshBubble(@NonNull MessageBeanForUI message, boolean needShowTail) {
        this.chatItemView.refreshBubble(message, needShowTail);
    }

    public void setUnreadMessageTitle(int count) {
        this.chatItemView.setUnreadMessageTitle(count);
    }

    public void refreshCheckState(@NonNull MessageBeanForUI message) {
        this.chatItemView.setCheckState(message, false);
    }

    public void setMessageContent(MessageBeanForUI messageBean) {
        this.chatItemView.setContentView(messageBean);
    }

    public void bindSenderName(String senderUid) {
        this.chatItemView.showSenderName(senderUid);
    }

    public void changeMessageContent(MessageBeanForUI messageBean) {
        this.chatItemView.changeMessageContent(messageBean);
    }

    public void setMessageStatusView(@NonNull MessageBeanForUI messageBean, boolean animate) {
        this.chatItemView.setMessageStatusView(messageBean, animate);
    }

    public void bindMessage(@NonNull MessageBeanForUI message, ChatAdapter.OnMessageClickListener onMessageClickListener) {
        bindSenderName(message.getSenderUid());
        this.chatItemView.setSendTime(message);
        setMessageStatusView(message, false);
        setMessageContent(message);
        setDebugInfo(message);
        refreshCheckState(message);
        initClickListener(message, onMessageClickListener);
        tryToHighLight();
    }

    public void initClickListener(@NonNull MessageBeanForUI messageBean, ChatAdapter.OnMessageClickListener onMessageClickListener) {
        this.chatItemView.setOnClickMessageListener(messageBean, onMessageClickListener);
        this.chatItemView.setOnSlideLayoutClickMessageListener(messageBean, onMessageClickListener);
        this.chatItemView.setOnSlideLongClickMessageListener(messageBean, onMessageClickListener);
        this.chatItemView.setSlideListener(messageBean, onMessageClickListener);
        this.chatItemView.setOnClickNameListener(messageBean, onMessageClickListener);
        this.chatItemView.setOnClickReplyListener(messageBean, onMessageClickListener);
        this.chatItemView.setForwardView(messageBean, onMessageClickListener);
    }

    public void tryToHighLight() {
        this.chatItemView.tryToHighLight(getBindingAdapterPosition());
    }

    public void setDebugInfo(@NonNull MessageBeanForUI messageBean) {
        this.chatItemView.setDebugInfo(messageBean, getBindingAdapterPosition());
    }

    public void onClickMessage(@NonNull MessageBeanForUI message) {
        this.chatItemView.onClickMessage(message);
    }

    public boolean canSelect(@NonNull MessageBeanForUI messageBean) {
        return this.chatItemView.canSelect(messageBean);
    }

    public boolean canBeReplied(@NonNull MessageBeanForUI messageBean) {
        return this.chatItemView.canBeReplied(messageBean);
    }
}