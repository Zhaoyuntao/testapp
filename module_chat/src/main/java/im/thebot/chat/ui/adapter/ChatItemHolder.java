package im.thebot.chat.ui.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.module_chat.R;
import com.zhaoyuntao.androidutils.tools.B;

import im.thebot.api.chat.constant.MessageStatusCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.ui.cells.drawable.HighLightDrawable;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.thebot.chat.ui.view.BubbleView;
import im.thebot.chat.ui.view.ChatTimeUtils;
import im.thebot.chat.ui.view.ChatHeadLayout;
import im.thebot.chat.ui.view.SlideLayout;
import im.thebot.chat.ui.view.SlideLayoutContent;
import im.thebot.common.UserNameView;
import im.thebot.user.ContactUtil;
import im.turbo.baseui.imageview.AnimateImageView;
import im.turbo.baseui.utils.UiUtils;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 2020-03-25
 * description:
 */
final public class ChatItemHolder extends RecyclerView.ViewHolder {

    private final BubbleView bubbleView;
    private final UserNameView nameView;
    private final View nameViewContainer;
    private final BaseMessageCell<MessageBeanForUI> baseMessageCell;
    private final ImageView messageForwardViewLeft;
    private final ImageView messageForwardViewRight;
    private final ChatHeadLayout replyMessageContainerView;
    private final ViewGroup messageDebugContainer;
    private final SlideLayout slideLayout;
    private final SlideLayoutContent slideContentView;
    private final ViewGroup messageContainerView;

    private TextView messageSendTimeView;
    private AnimateImageView messageStatusView;
    private TextView newMessagesDividerView;
    private ViewGroup newMessagesDividerViewContainer;
    private BaseReplyCell<MessageBeanForUI> replyCell;
    private TextView timeTitleView;
    private ValueAnimator highLightAnimator;
    private Drawable defaultBack = new ColorDrawable(Color.parseColor("#7700aa33"));

    public ChatItemHolder(final View itemView, BaseMessageCell<MessageBeanForUI> baseMessageCell) {
        super(itemView);
        this.baseMessageCell = baseMessageCell;
        this.nameView = itemView.findViewById(R.id.name_view_message);
        this.slideLayout = itemView.findViewById(R.id.chat_cell_slide_view);
        this.bubbleView = itemView.findViewById(R.id.bubble_view);
        this.messageDebugContainer = itemView.findViewById(R.id.debug_message_container);
        this.messageForwardViewLeft = itemView.findViewById(R.id.message_forward_view_left);
        this.messageForwardViewRight = itemView.findViewById(R.id.message_forward_view_right);
        this.slideContentView = itemView.findViewById(R.id.chat_cell_root_view_slide_content);
        this.nameViewContainer = itemView.findViewById(R.id.name_view_container_message);
        this.replyMessageContainerView = itemView.findViewById(R.id.container_reply_message);
        this.messageContainerView = itemView.findViewById(R.id.chat_cell_message_container);
        changeLayoutParams();
        initTitle();
        initSenderName();
        initSendTime();
        initElevation();

        ThreadPool.runUiDelayed(200, new SafeRunnable(itemView) {
            @Override
            protected void runSafely() {
                replyMessageContainerView.setHeadColor(B.getRandomColor());
                ThreadPool.runUiDelayed(200, this);
            }
        });
    }

    private void changeLayoutParams() {
        LinearLayout.LayoutParams bubbleViewLayoutParams = (LinearLayout.LayoutParams) bubbleView.getLayoutParams();
        if (baseMessageCell.isMaxWidth()) {
            bubbleViewLayoutParams.width = 0;
            bubbleViewLayoutParams.weight = 1;
        } else {
            bubbleViewLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        slideContentView.setGravity(baseMessageCell.getGravity());
        MessageCellManager.addCellToContainer(baseMessageCell, messageContainerView);
    }

    private void initElevation() {
        if (baseMessageCell.needElevation()) {
            messageContainerView.setElevation(UiUtils.dipToPx(2));
        } else {
            messageContainerView.setElevation(0);
        }
    }

    private void initSendTime() {
        this.messageSendTimeView = baseMessageCell.findViewById(baseMessageCell.getSendTimeViewResId());
        if (this.messageSendTimeView != null) {
            this.messageSendTimeView.setTextColor(ContextCompat.getColor(itemView.getContext(), baseMessageCell.getTimestampColor()));
        }
        this.messageStatusView = baseMessageCell.findViewById(baseMessageCell.getSendStatusViewResId());
    }

    private void initSenderName() {
        if (baseMessageCell.showAsSender() && baseMessageCell.needShowSenderName()) {
            this.nameViewContainer.setVisibility(View.VISIBLE);
            this.nameView.setFollowView(itemView.findViewById(R.id.second_name_view_message));
        } else {
            this.nameViewContainer.setVisibility(View.GONE);
        }
    }

    private void initTitle() {
        ViewGroup titleContainer = itemView.findViewById(R.id.chat_cell_title_container);
        if (baseMessageCell.needShowNewMessageLine()) {
            View newMessageLineRootView = LayoutInflater.from(titleContainer.getContext()).inflate(R.layout.layout_chat_cell_component_new_message_line, titleContainer, true);
            newMessagesDividerView = newMessageLineRootView.findViewById(R.id.text_view_new_message_count);
            newMessagesDividerViewContainer = newMessageLineRootView.findViewById(R.id.text_view_new_message_count_container);
        }
        if (baseMessageCell.needShowTimeTitle()) {
            LayoutInflater.from(titleContainer.getContext()).inflate(R.layout.layout_chat_cell_component_time_title, titleContainer, true);
            this.timeTitleView = titleContainer.findViewById(R.id.time_title_message);
        }
        itemView.findViewById(R.id.top_space_chat_cell).setVisibility(baseMessageCell.needShowTopSpace() ? View.VISIBLE : View.GONE);
    }

    public void refreshTail(boolean needShowTail) {
        if (this.bubbleView.getGravity() != baseMessageCell.getGravity()) {
            this.bubbleView.setGravity(baseMessageCell.getGravity(), needShowTail);
        }
    }

    public void setUnreadMessageTitle(int count) {
        if (newMessagesDividerViewContainer != null && newMessagesDividerView != null) {
            if (count > 0) {
                newMessagesDividerViewContainer.setVisibility(View.VISIBLE);
                newMessagesDividerView.setText(ResourceUtils.getString(R.string.chats_listview_messagenumber, count));
            } else {
                newMessagesDividerViewContainer.setVisibility(View.GONE);
            }
        }
    }

    public void setCheckState(boolean checked) {
        if (checked) {
            slideLayout.setBackgroundResource(R.color.color_bubble_background_checked);
        } else {
            slideLayout.setBackground(defaultBack);
        }
    }

    public void setOnSlideLayoutClickMessageListener(View.OnClickListener listener) {
        if (slideLayout != null) {
            slideLayout.setOnClickListener(listener);
        }
    }

    public void setOnClickMessageListener(View.OnClickListener listener) {
        if (bubbleView != null) {
            bubbleView.setOnClickListener(listener);
        }
    }

    public void setOnMessageLongClickMessageListener(View.OnLongClickListener listener) {
        if (bubbleView != null) {
            bubbleView.setOnLongClickListener(listener);
        }
    }

    public void setOnSlideLongClickMessageListener(View.OnLongClickListener listener) {
        if (slideLayout != null) {
            slideLayout.setOnLongClickListener(listener);
        }
    }

    public void setOnClickNameListener(View.OnClickListener listener) {
        if (nameViewContainer != null) {
            nameViewContainer.setOnClickListener(listener);
        }
    }

    public void setOnClickReplyListener(View.OnClickListener listener) {
        if (this.replyCell != null) {
            this.replyCell.setOnClickListener(listener);
        }
    }

    public void setLongClickReplyListener(View.OnLongClickListener listener) {
        if (this.replyCell != null) {
            this.replyCell.setOnLongClickListener(listener);
        }
    }

    public void setSlideListener(SlideLayout.SlideListener slideListener) {
        if (slideLayout != null) {
            slideLayout.setSlideListener(new SlideLayout.SlideListener() {
                @Override
                public void onFingerUp(boolean slideToRight) {
                    slideListener.onFingerUp(slideToRight);
                }

                @Override
                public void onSlideDistanceChange(float percent) {
                    float alpha = (1 - percent * 2) <= 0 ? 0 : (1 - percent * 2);
                    if (messageForwardViewLeft != null && messageForwardViewRight != null) {
                        messageForwardViewLeft.setAlpha(alpha);
                        messageForwardViewRight.setAlpha(alpha);
                    }
                }

                @Override
                public boolean canSlide() {
                    return slideListener.canSlide();
                }
            });
        }
    }

    public void setForwardView(MessageBeanForUI messageBean, View.OnClickListener listener) {
        if (this.messageForwardViewLeft != null && this.messageForwardViewRight != null) {
            boolean showForwardView = baseMessageCell.showMessageForwardView();
            boolean isMeSender = ContactUtil.isMySelf(messageBean.getSenderUid());
            this.messageForwardViewLeft.setOnClickListener(listener);
            this.messageForwardViewRight.setOnClickListener(listener);
            this.messageForwardViewLeft.setVisibility(showForwardView && isMeSender ? View.VISIBLE : View.GONE);
            this.messageForwardViewRight.setVisibility(showForwardView && !isMeSender ? View.VISIBLE : View.GONE);
        }
    }

    public void setContentView(MessageBeanForUI messageBean, boolean showReplySourceMessage) {
        stopHighLight();
        if (timeTitleView != null) {
            long timeSend = messageBean.getTimeSend();
            if (timeSend <= 0 || !baseMessageCell.needShowTimeTitle()) {
                timeTitleView.setVisibility(View.GONE);
            } else {
                timeTitleView.setVisibility(View.VISIBLE);
                String timeString = ChatTimeUtils.getTimeTitleString(timeSend);
                timeTitleView.setText(timeString);
            }
        }
        baseMessageCell.initMessage(messageBean);
        initReplyContentView(messageBean, showReplySourceMessage);
    }

    @SuppressWarnings({"unchecked"})
    private void initReplyContentView(MessageBeanForUI messageBean, boolean showReplySourceMessage) {
        //Reply message.
        MessageBeanForUI messageReply = messageBean.getMessageReply();
        if (messageReply == null || !showReplySourceMessage) {
            replyCell = null;
            if (replyMessageContainerView != null) {
                replyMessageContainerView.removeAllViews();
                replyMessageContainerView.setVisibility(View.GONE);
            }
        } else {
            if (replyMessageContainerView != null) {
                if (messageBean.isSelf()) {
                    replyMessageContainerView.setBackgroundResource(R.drawable.shape_round_rect_background_reply);
                } else {
                    replyMessageContainerView.setBackgroundResource(R.drawable.shape_round_rect_background_reply);
                }
                this.replyCell = MessageCellManager.getReplyCellByViewType(itemView.getContext(), messageReply);
                MessageCellManager.addCellToContainer(replyCell, replyMessageContainerView);
                replyMessageContainerView.setVisibility(View.VISIBLE);
                this.replyCell.setPresenter(baseMessageCell.getPresenter());
                this.replyCell.initMessage(messageReply);
            }
        }
    }

    public void changeMessageContent(MessageBeanForUI messageBean) {
        itemView.setTag(messageBean.getUUID());
        baseMessageCell.changeMessage(messageBean);
    }

    public void setTime(MessageBeanForUI messageBean, String timeString) {
        if (messageSendTimeView != null) {
            messageSendTimeView.setText(timeString);
        }
        baseMessageCell.onSetTimestamp(messageBean, timeString);
    }

    public void setMessageStatusView(@NonNull MessageBeanForUI messageBean, boolean animate) {
        int messageStatusCode = messageBean.getMessageStatus();
        int visible = View.GONE;
        if (messageBean.isDeletedMessage()) {
            visible = (messageBean.isSelf() ? View.INVISIBLE : View.GONE);
        } else if (ContactUtil.isUser(messageBean.getSessionId()) && messageBean.isSelf() && baseMessageCell.showMessageStatusView()) {
            if (messageStatusCode != MessageStatusCode.STATUS_MESSAGE_SEND_FAILED) {
                visible = View.VISIBLE;
            }
        }
        if (messageStatusView != null) {
            messageStatusView.setVisibility(visible);
            messageStatusView.setImageResource(MessageStatusDrawableUtils.getMessageStatusIcon(messageStatusCode), animate);
        }
        baseMessageCell.onSetStatusDrawable(messageBean, visible, MessageStatusDrawableUtils.getMessageStatusIcon(messageStatusCode), animate);
    }

    public void showSenderName(String senderUid) {
        if (nameViewContainer != null && nameViewContainer.getVisibility() == View.VISIBLE && nameView != null) {
            nameView.bindUid(senderUid);
        }
    }

    public void hideSenderName() {
        if (nameViewContainer != null && nameViewContainer.getVisibility() == View.VISIBLE && nameView != null) {
            nameView.clear();
        }
    }

    public void highLight() {
        if (slideLayout == null || baseMessageCell == null) {
            return;
        }
        int highLightColor = ResourceUtils.getColor(R.color.chat_message_select_bg);
        HighLightDrawable colorDrawable = new HighLightDrawable(highLightColor);
        stopHighLight();
        if (highLightAnimator == null) {
            highLightAnimator = ValueAnimator.ofFloat(3, 0);
            highLightAnimator.setDuration(3000);
            highLightAnimator.setRepeatMode(ValueAnimator.REVERSE);
            highLightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (Float) animation.getAnimatedValue();
                    colorDrawable.setAlpha((int) (255 * (percent > 1 ? 1 : percent)));
                    slideLayout.setBackground(colorDrawable);
                }
            });
            highLightAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    slideLayout.setBackground(defaultBack);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        highLightAnimator.start();
    }

    public void stopHighLight() {
        if (slideLayout == null || baseMessageCell == null) {
            return;
        }
        if (highLightAnimator != null && highLightAnimator.isRunning()) {
            highLightAnimator.cancel();
        }
        slideLayout.setBackground(defaultBack);
    }

    public void reset() {
        if (nameView != null) {
            nameView.clear();
        }
    }

    BaseMessageCell<MessageBeanForUI> getBaseMessageCell() {
        return baseMessageCell;
    }

    public void setDebugInfo(@NonNull MessageBeanForUI messageBean, int position) {
        if (messageDebugContainer == null) {
            return;
        }
//        if (BuildConfig.DEBUG) {
//            if (messageDebugContainer.getChildCount() <= 0) {
//                LayoutInflater.from(messageDebugContainer.getContext()).inflate(R.layout.layout_chat_cell_part_debug, messageDebugContainer, true);
//            }
//            TextView messageDebugPayloadView = messageDebugContainer.findViewById(R.id.debug_message_payload_text_view);
//            TextView messageDebugInfoView = messageDebugContainer.findViewById(R.id.debug_message_info_text_view);
//            ImageView messageDebugOpenPayloadView = messageDebugContainer.findViewById(R.id.debug_message_open_payload);
//
//            String errorInfo = messageBean.getErrorDescription();
//            boolean hasError = !TextUtils.isEmpty(errorInfo);
//            int infoColor = hasError ? ContextCompat.getColor(messageDebugContainer.getContext(), R.color.color_bubble_debug_info_text_color_error) : ContextCompat.getColor(itemView.getContext(),
//                    R.color.color_bubble_debug_info_text_color_normal);
//            //Position.
//            SpannableString positionString = ColorStringUtils.get(" " + position + " ", Color.WHITE, 11);
//            //Conversation id.
//            SpannableString conversationId = ColorStringUtils.get(messageBean.getSessionId(), infoColor, 9);
//            //Sender info.
//            SpannableString sender = ColorStringUtils.get(messageBean.getSenderUid(), infoColor, 9);
//            //Device type.
//            SpannableString deviceType = ColorStringUtils.get(messageBean.getDeviceDetails(), infoColor, 9);
//            //Body.
//            SpannableString body = ColorStringUtils.get(messageBean.getContent(), infoColor, 9);
//            //Message type.
//            SpannableString messageType = ColorStringUtils.get(messageBean.isSystemEvent() ? "Event" : MessageTypeStringHelper.toString(messageBean.getType()), infoColor, 9);
//            //Message type.
//            SpannableString time = ColorStringUtils.get(messageBean.getTimeSend() + "  " + TimeUtils.getTimeString(messageBean.getTimeSend(), TimeUtils.TIME_FORMAT_YMD_HMSS), infoColor, 9);
//            //UUID.
//            SpannableString uuid = ColorStringUtils.get(messageBean.getUUID(), infoColor, 11);
//            //App info.
//            SpannableString installTime = ColorStringUtils.get(TimeUtils.getTimeString(messageBean.getInstallTime(), TimeUtils.TIME_FORMAT_YMD_HMSS), infoColor, 9);
//            SpannableString updateTime = ColorStringUtils.get(TimeUtils.getTimeString(messageBean.getUpdateTime(), TimeUtils.TIME_FORMAT_YMD_HMSS), infoColor, 9);
//            SpannableString appVersion = ColorStringUtils.get(messageBean.getVersionName() + " - " + messageBean.getVersionCode(), infoColor, 9);
//            //Debug info.
//            SpannableStringBuilder debugInfo = new SpannableStringBuilder();
//            debugInfo
//                    .append(positionString).append("  ").append(uuid)
//                    .append("\n[ Body : ").append("  ").append(body.length() > 40 ? body.subSequence(0, 40) + "..." : body).append(" ]")
//                    .append("\n[ Conversation Uid:").append(conversationId).append(" ]").append("\n[ Sender : ").append(sender).append(" ]")
//                    .append("\n[ Time : ").append(time).append(" ]")
//                    .append("\n[ Type : ").append(messageType).append(" ]")
//                    .append("[ Device : ").append(deviceType).append(" ]");
////                if (messageBean.getDeviceOs() == DeviceParam.DeviceOsId.DEVICE_FLAGS_ANDROID) {
////                    debugInfo.append("[ Version : ").append(appVersion).append(" ]");
////                }
////                debugInfo.append("\n[ Time : ").append(time).append(" ]");
////                if (messageBean.getDeviceOs() == DeviceParam.DeviceOsId.DEVICE_FLAGS_ANDROID) {
////                    debugInfo.append("\n[ Install Time : ").append(installTime).append(" ]")
////                            .append("\n[ Update Time : ").append(updateTime).append(" ]");
////                }
//            if (hasError) {
//                //UUID.
//                SpannableString errorInfoString = ColorStringUtils.get(errorInfo, infoColor, 11);
//                debugInfo.append("\nError : ").append(errorInfoString);
//                messageDebugInfoView.setBackgroundResource(R.drawable.shape_cell_bubble_debug_error);
//            } else {
//                messageDebugInfoView.setBackgroundResource(R.drawable.shape_cell_bubble_debug_info);
//            }
//            messageDebugInfoView.setText(debugInfo);
//            messageDebugInfoView.setOnLongClickListener(v -> {
//                ClipBoardUtils.copy(debugInfo, "copied");
//                return true;
//            });
//
//            messageDebugInfoView.setOnClickListener(v -> {
//                messageDebugPayloadView.setVisibility(View.GONE);
//                messageDebugOpenPayloadView.setVisibility(View.VISIBLE);
//            });
//            messageDebugOpenPayloadView.setOnClickListener(v -> {
//                messageDebugPayloadView.setVisibility(View.VISIBLE);
//                messageDebugOpenPayloadView.setVisibility(View.GONE);
//            });
//
//            String sourceJson = messageBean.getPayloadSourceJson();
//            messageDebugPayloadView.setText(sourceJson);
//            messageDebugPayloadView.setOnClickListener(v -> {
//                messageDebugPayloadView.setVisibility(View.GONE);
//                messageDebugOpenPayloadView.setVisibility(View.VISIBLE);
//            });
//
//            messageDebugOpenPayloadView.setOnLongClickListener(v -> {
//                ClipBoardUtils.copy(sourceJson, "copied");
//                return true;
//            });
//            messageDebugPayloadView.setOnLongClickListener(v -> {
//                ClipBoardUtils.copy(sourceJson, "copied");
//                return true;
//            });
//        } else {
//            if (messageDebugContainer.getChildCount() > 0) {
//                messageDebugContainer.removeAllViews();
//            }
//        }
    }
}