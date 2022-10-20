package im.thebot.chat.ui.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.module_chat.R;

import im.thebot.chat.api.chat.constant.ChatDebugParam;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.ui.cells.drawable.HighLightDrawable;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.thebot.chat.ui.view.BubbleView;
import im.thebot.chat.ui.view.ChatMatchParentLayout;
import im.thebot.chat.ui.view.ChatReplyLayout;
import im.thebot.chat.ui.view.ChatTimeUtils;
import im.thebot.chat.ui.view.SlideLayout;
import im.thebot.chat.ui.view.SlideLayoutContent;
import im.thebot.common.UserNameView;
import im.thebot.user.ContactUtil;
import im.turbo.basetools.clipboard.ClipBoardUtils;
import im.turbo.basetools.time.TimeUtils;
import im.turbo.baseui.clicklistener.AvoidDoubleClickListener;
import im.turbo.baseui.imageview.AnimateImageView;
import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 02/09/2022
 * description:
 */
@SuppressLint("ViewConstructor")
public class ChatItemView extends LinearLayout {
    private final boolean showTimeTitle;
    private final boolean showTopSpace;
    private final boolean showBottomSpace;
    private final boolean showSenderName;
    private final boolean showNewMessageLine;
    private final boolean showForwardHead;
    private final boolean showForwardButton;
    private final boolean showReply;
    private final int gravity;
    private final BaseMessageCell<MessageBeanForUI> baseMessageCell;

    private SlideLayout chat_cell_slide_view;
    private FrameLayout chat_cell_debug_message_container;
    private SlideLayoutContent chat_cell_root_view_slide_content;
    private AppCompatImageView chat_cell_forward_button;
    private BubbleView chat_cell_bubble_view;
    private LinearLayout chat_cell_name_view_container_message;
    private AppCompatTextView chat_cell_forward_head;
    private ChatReplyLayout chat_cell_container_reply_message;
    private UserNameView chat_cell_name_view;

    private TextView messageSendTimeView;
    private AnimateImageView messageStatusView;
    private TextView newMessagesDividerView;
    private ViewGroup newMessagesDividerViewContainer;
    private BaseReplyCell<MessageBeanForUI> replyCell;
    private TextView timeTitleView;
    private ValueAnimator highLightAnimator;

    public ChatItemView(@NonNull Context context, @NonNull BaseMessageCell<MessageBeanForUI> baseMessageCell, int flags) {
        super(context);
        this.showTimeTitle = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_TIME);
        this.showTopSpace = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_TOP_SPACE);
        this.showBottomSpace = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_BOTTOM_SPACE);
        this.showSenderName = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_SENDER_NAME) && baseMessageCell.showAsSender();
        this.showNewMessageLine = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_NEW_MESSAGE_LINE);
        this.showForwardHead = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_FORWARD_HEAD);
        this.showForwardButton = baseMessageCell.showForwardView();
        this.showReply = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_LAYOUT_SHOW_REPLY);
        if (!baseMessageCell.showAsSender()) {
            this.gravity = Gravity.CENTER;
        } else {
            this.gravity = MessageCellManager.containsFlagInItemViewType(flags, MessageCellFlag.FLAG_GRAVITY_LEFT) ? Gravity.START : Gravity.END;
        }
        this.baseMessageCell = baseMessageCell;
        setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        create();
    }

    private void create() {
        setOrientation(VERTICAL);
        Context context = getContext();

        if (showNewMessageLine || showTimeTitle) {
            LinearLayout chat_cell_title_container = new LinearLayout(context);
            chat_cell_title_container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            chat_cell_title_container.setOrientation(LinearLayout.VERTICAL);
            addView(chat_cell_title_container);

            if (showNewMessageLine) {
                LayoutInflater.from(getContext()).inflate(R.layout.layout_chat_cell_component_new_message_line, chat_cell_title_container, true);
                newMessagesDividerView = chat_cell_title_container.findViewById(R.id.text_view_new_message_count);
                newMessagesDividerViewContainer = chat_cell_title_container.findViewById(R.id.text_view_new_message_count_container);
            }
            if (showTimeTitle) {
                createTimeTitle(chat_cell_title_container);
            }
        }

        chat_cell_slide_view = new SlideLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chat_cell_slide_view.setLayoutParams(layoutParams);
        layoutParams.topMargin = showTopSpace && !showTimeTitle ? UiUtils.dipToPx(4) : 0;
        layoutParams.bottomMargin = showBottomSpace ? UiUtils.dipToPx(4) : 0;
        createSlideContent(chat_cell_slide_view);
        addView(chat_cell_slide_view);
        chat_cell_slide_view.setInterceptor(new SlideLayout.TouchEventInterceptor() {
            @Override
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return baseMessageCell.getPresenter().isSelecting();
            }
        });
    }

    private void createTimeTitle(LinearLayout chat_cell_title_container) {
        this.timeTitleView = new AppCompatTextView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, UiUtils.dipToPx(3), 0, UiUtils.dipToPx(5));
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        int paddingStartAndEnd = UiUtils.dipToPx(10);
        int paddingTop = UiUtils.dipToPx(6);
        int paddingBottom = UiUtils.dipToPx(8);
        this.timeTitleView.setPaddingRelative(paddingStartAndEnd, paddingTop, paddingStartAndEnd, paddingBottom);
        this.timeTitleView.setSingleLine();
        this.timeTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        this.timeTitleView.setTextColor(Color.WHITE);
        this.timeTitleView.setElevation(UiUtils.dipToPx(1));
        this.timeTitleView.setBackgroundResource(R.drawable.chat_message_call);
        this.timeTitleView.setLayoutParams(layoutParams);
        this.timeTitleView.setGravity(Gravity.CENTER);

        chat_cell_title_container.addView(timeTitleView);
    }

    private void createSlideContent(SlideLayout chat_cell_slide_view) {
        Context context = chat_cell_slide_view.getContext();
        int size = UiUtils.dipToPx(32);
        int margin = UiUtils.dipToPx(24);
        AppCompatImageView slideIcon = new AppCompatImageView(context);
        SlideLayout.SlideLayoutParams lpSlideIcon = new SlideLayout.SlideLayoutParams(size, size);
        lpSlideIcon.setMarginStart(margin);
        lpSlideIcon.setMarginEnd(margin);
        slideIcon.setLayoutParams(lpSlideIcon);
        slideIcon.setImageResource(R.drawable.chat_reply);
        slideIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        chat_cell_slide_view.addView(slideIcon);

        chat_cell_root_view_slide_content = new SlideLayoutContent(context);
        SlideLayout.SlideLayoutParams lpSlideContent = new SlideLayout.SlideLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chat_cell_root_view_slide_content.setLayoutParams(lpSlideContent);
        createSlideLayoutContent(chat_cell_root_view_slide_content);
        chat_cell_slide_view.addView(chat_cell_root_view_slide_content);
    }


    private void createSlideLayoutContent(SlideLayoutContent chat_cell_root_view_slide_content) {
        Context context = chat_cell_root_view_slide_content.getContext();
        int size = UiUtils.dipToPx(32);
        int margin = UiUtils.dipToPx(5);
        int padding = UiUtils.dipToPx(5);

        if (showForwardButton) {
            chat_cell_forward_button = new AppCompatImageView(context);
            LayoutParams lpForwardButton = new LayoutParams(size, size);
            lpForwardButton.gravity = Gravity.CENTER_VERTICAL;
            lpForwardButton.setMargins(margin, margin, margin, margin);
            chat_cell_forward_button.setPadding(padding, padding, padding, padding);
            chat_cell_forward_button.setLayoutParams(lpForwardButton);
            chat_cell_forward_button.setImageResource(R.drawable.tab_forward);
            chat_cell_forward_button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            chat_cell_forward_button.setVisibility(View.GONE);
            chat_cell_forward_button.setBackgroundResource(R.drawable.shape_oval_black_forward);
        }

        if (showForwardButton && gravity == Gravity.END) {
            chat_cell_root_view_slide_content.addView(chat_cell_forward_button);
        }
        createBubbleView(chat_cell_root_view_slide_content);
        if (showForwardButton && gravity == Gravity.START) {
            chat_cell_root_view_slide_content.addView(chat_cell_forward_button);
        }
    }

    private void createBubbleView(SlideLayoutContent chat_cell_root_view_slide_content) {
        Context context = chat_cell_root_view_slide_content.getContext();
        int margin = UiUtils.dipToPx(1.1f);
        chat_cell_bubble_view = new BubbleView(context);
        chat_cell_bubble_view.setMaxWidth(baseMessageCell.getMaxWidth());
        chat_cell_bubble_view.setBubbleGravity(gravity, baseMessageCell.needDrawBubble());

        LayoutParams lpBubbleView = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpBubbleView.setMargins(0, margin, 0, margin);
        if (baseMessageCell.isMaxWidth()) {
            if (baseMessageCell.getMaxWidth() > 0) {
                lpBubbleView.width = baseMessageCell.getMaxWidth();
            } else {
                lpBubbleView.width = 0;
                lpBubbleView.weight = 1;
            }
        } else {
            lpBubbleView.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        chat_cell_bubble_view.setLayoutParams(lpBubbleView);
        createMessageContentContainer(chat_cell_bubble_view);

        chat_cell_root_view_slide_content.addView(chat_cell_bubble_view);
    }

    private void createMessageContentContainer(BubbleView chat_cell_bubble_view) {
        if (showSenderName) {
            createUserNameContainer(chat_cell_bubble_view);
        }
        if (showForwardHead) {
            createForwardHead(chat_cell_bubble_view);
        }
        if (showReply) {
            createReply(chat_cell_bubble_view);
            createMessageContentWithReply(chat_cell_bubble_view);
        } else {
            addMessageContentViewToContainer(chat_cell_bubble_view);
        }
    }

    private void createReply(BubbleView chat_cell_bubble_view) {
        int margin = UiUtils.dipToPx(4);
        chat_cell_container_reply_message = new ChatReplyLayout(getContext());
        chat_cell_container_reply_message.setCornerRadius(UiUtils.dipToPx(4));
        chat_cell_container_reply_message.setHeadWidth(UiUtils.dipToPx(3));
        MarginLayoutParams lpReplyContainer = new SlideLayout.SlideLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpReplyContainer.topMargin = margin;
        lpReplyContainer.setMarginStart(margin);
        lpReplyContainer.setMarginEnd(margin);
        chat_cell_container_reply_message.setLayoutParams(lpReplyContainer);
        chat_cell_container_reply_message.setBackgroundResource(R.drawable.shape_round_rect_background_reply_self);
        chat_cell_bubble_view.addView(chat_cell_container_reply_message);
    }

    private void createMessageContentWithReply(BubbleView chat_cell_bubble_view) {
        Context context = getContext();
        ChatMatchParentLayout chat_cell_message_container = new ChatMatchParentLayout(context);
        MarginLayoutParams lpMessageContainer = new SlideLayout.SlideLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chat_cell_message_container.setLayoutParams(lpMessageContainer);
        if (baseMessageCell.needElevation()) {
            chat_cell_message_container.setElevation(UiUtils.dipToPx(2));
        } else {
            chat_cell_message_container.setElevation(0);
        }
        addMessageContentViewToContainer(chat_cell_message_container);
        chat_cell_bubble_view.addView(chat_cell_message_container);
    }

    private void createUserNameContainer(BubbleView chat_cell_bubble_view) {
        int paddingStartAndEnd = UiUtils.dipToPx(8);
        int paddingTop = UiUtils.dipToPx(2);
        chat_cell_name_view_container_message = new LinearLayout(getContext());
        chat_cell_name_view_container_message.setOrientation(LinearLayout.HORIZONTAL);
        chat_cell_name_view_container_message.setPaddingRelative(paddingStartAndEnd, paddingTop, paddingStartAndEnd, 0);
        MarginLayoutParams lpNameContainer = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chat_cell_name_view_container_message.setLayoutParams(lpNameContainer);
        createUserName(chat_cell_name_view_container_message);
        chat_cell_bubble_view.addView(chat_cell_name_view_container_message);
    }

    private void createUserName(LinearLayout chat_cell_name_view_container_message) {
        Context context = chat_cell_name_view_container_message.getContext();
        chat_cell_name_view = new UserNameView(context);
        LayoutParams lpNameView = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chat_cell_name_view.setLayoutParams(lpNameView);
        chat_cell_name_view_container_message.addView(chat_cell_name_view);

        int marginStart = UiUtils.dipToPx(6);
        AppCompatTextView second_name_view_message = new AppCompatTextView(context);
        LayoutParams lpSecondNameView = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpSecondNameView.setMargins(marginStart, 0, 0, 0);
        second_name_view_message.setMaxWidth(UiUtils.dipToPx(100));
        second_name_view_message.setEllipsize(TextUtils.TruncateAt.END);
        second_name_view_message.setSingleLine();
        second_name_view_message.setTextColor(Color.BLACK);
        second_name_view_message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        second_name_view_message.setLayoutParams(lpSecondNameView);
        chat_cell_name_view_container_message.addView(second_name_view_message);

        chat_cell_name_view.setFollowView(second_name_view_message);
    }

    private void createForwardHead(BubbleView chat_cell_bubble_view) {
        int drawablePadding = UiUtils.dipToPx(7);
        int paddingStartAndEnd = UiUtils.dipToPx(8);
        int paddingTop2 = UiUtils.dipToPx(1);
        chat_cell_forward_head = new AppCompatTextView(getContext());
        chat_cell_forward_head.setPaddingRelative(paddingStartAndEnd, paddingTop2, paddingStartAndEnd, 0);
        chat_cell_forward_head.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        chat_cell_forward_head.setTextColor(ResourceUtils.getColor(R.color.bot_forward_send_text_color));
        chat_cell_forward_head.setText(ResourceUtils.getString(R.string.forwarded_message_header));
        MarginLayoutParams lpForwardHead = new SlideLayout.SlideLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chat_cell_forward_head.setCompoundDrawablesRelative(ResourceUtils.getDrawable(R.drawable.ic_forward_sent_flag), null, null, null);
        chat_cell_forward_head.setCompoundDrawablePadding(drawablePadding);
        chat_cell_forward_head.setLayoutParams(lpForwardHead);
        chat_cell_bubble_view.addView(chat_cell_forward_head);
    }

    private void addMessageContentViewToContainer(@NonNull ViewGroup chat_cell_message_container) {
        chat_cell_root_view_slide_content.setGravity(gravity, baseMessageCell.getMarginStartPX());
        MessageCellManager.addCellToContainer(baseMessageCell, chat_cell_message_container);

        this.messageSendTimeView = baseMessageCell.findViewById(R.id.message_send_time_text_view);
        this.messageStatusView = baseMessageCell.findViewById(R.id.message_status_view_send);
    }

    public void refreshBubble(@NonNull MessageBeanForUI message, boolean needShowTail) {
        this.chat_cell_bubble_view.setBubbleColor(baseMessageCell.getBubbleColor(message), baseMessageCell.getBubbleClickColor(message), needShowTail);
    }

    public void setUnreadMessageTitle(int count) {
        if (newMessagesDividerViewContainer != null && newMessagesDividerView != null) {
            if (count > 0) {
                newMessagesDividerViewContainer.setVisibility(View.VISIBLE);
                if (count == 1) {
                    newMessagesDividerView.setText(ResourceUtils.getString(R.string.bot_unread_message, count));
                } else {
                    newMessagesDividerView.setText(ResourceUtils.getString(R.string.bot_unread_messages, count));
                }
            } else {
                newMessagesDividerViewContainer.setVisibility(View.GONE);
            }
        }
    }

    public boolean setCheckState(@NonNull MessageBeanForUI messageBean, boolean overturn) {
        boolean isSelected = false;
        if (chat_cell_slide_view != null) {
            isSelected = baseMessageCell.getPresenter().isMessageSelected(messageBean.getUUID());
            if (overturn) {
                isSelected = !isSelected;
            }
            if (isSelected) {
                chat_cell_slide_view.setBackgroundResource(R.color.color_bubble_background_checked);
            } else {
                chat_cell_slide_view.setBackground(null);
            }
        }
        return isSelected;
    }

    public void setOnSlideLayoutClickMessageListener(@NonNull MessageBeanForUI messageBean, @NonNull ChatAdapter.OnMessageClickListener listener) {
        if (chat_cell_slide_view != null) {
            chat_cell_slide_view.setOnClickListener(v -> {
                if (baseMessageCell.getPresenter().isSelecting()) {
                    changeSelectState(messageBean, listener);
                }
            });
        }
    }

    public void setOnSlideLongClickMessageListener(@NonNull MessageBeanForUI messageBean, @NonNull ChatAdapter.OnMessageClickListener listener) {
        if (chat_cell_slide_view != null) {
            chat_cell_slide_view.setOnLongClickListener(v -> {
                if (baseMessageCell.isSupportClick(messageBean)) {
                    if (!baseMessageCell.getPresenter().isSelecting()) {
                        return changeSelectState(messageBean, listener);
                    }
                }
                return false;
            });
        }
    }

    public void setSlideListener(@NonNull MessageBeanForUI messageBean, ChatAdapter.OnMessageClickListener listener) {
        if (chat_cell_slide_view != null) {
            chat_cell_slide_view.setSlideListener(new SlideLayout.SlideListener() {
                @Override
                public void onFingerUp(boolean slideToRight) {
                    if (slideToRight) {
                        listener.onSlideMessage(messageBean);
                    }
                }

                @Override
                public void onSlideDistanceChange(float percent) {
                    float alpha = (1 - percent * 2) <= 0 ? 0 : (1 - percent * 2);
                    if (chat_cell_forward_button != null) {
                        chat_cell_forward_button.setAlpha(alpha);
                    }
                }

                @Override
                public boolean canSlide() {
                    return baseMessageCell.canBeReplied(messageBean)
                            && !baseMessageCell.getPresenter().isSelecting();
                }
            });
        }
    }

    public void setOnClickMessageListener(@NonNull MessageBeanForUI messageBean, @NonNull ChatAdapter.OnMessageClickListener onMessageClickListener) {
        if (chat_cell_bubble_view != null) {
            chat_cell_bubble_view.setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void onClickView(View view) {
                }

                @Override
                public void onClickView(View view, boolean timeShort) {
                    if (baseMessageCell.getPresenter().isSelecting()) {
                        changeSelectState(messageBean, onMessageClickListener);
                    } else {
                        if (timeShort) {
                            return;
                        }
                        if (baseMessageCell.isSupportClick(messageBean)) {
                            baseMessageCell.onClickMessage(messageBean);
                        }
                    }
                }

                @Override
                protected boolean controlByHand() {
                    return true;
                }
            });
        }
    }

    private boolean changeSelectState(@NonNull MessageBeanForUI messageBean, @NonNull ChatAdapter.OnMessageClickListener onMessageClickListener) {
        if (baseMessageCell.canSelect(messageBean)) {
            boolean selected = setCheckState(messageBean, true);
            onMessageClickListener.onSelectItem(messageBean, selected);
            return true;
        }
        return false;
    }

    public void setOnClickNameListener(@NonNull MessageBeanForUI messageBean, @NonNull ChatAdapter.OnMessageClickListener onMessageClickListener) {
        if (chat_cell_name_view_container_message != null) {
            chat_cell_name_view_container_message.setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void onClickView(View view) {
                    onMessageClickListener.onNameClick(messageBean);
                }
            });
        }
    }

    public void setOnClickReplyListener(@NonNull MessageBeanForUI messageBean, @NonNull ChatAdapter.OnMessageClickListener onMessageClickListener) {
        if (this.replyCell != null) {
            this.replyCell.setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void onClickView(View view) {
                    if (baseMessageCell.getPresenter().isSelecting()) {
                        changeSelectState(messageBean, onMessageClickListener);
                    } else {
                        onMessageClickListener.onClickReplyMessage(messageBean);
                    }
                }
            });
        }
    }

    public void setForwardView(@NonNull MessageBeanForUI messageBean, @NonNull ChatAdapter.OnMessageClickListener onMessageClickListener) {
        if (this.chat_cell_forward_button != null) {
            this.chat_cell_forward_button.setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void onClickView(View view) {
                    onMessageClickListener.onClickForwardButton(messageBean);
                }
            });
            this.chat_cell_forward_button.setVisibility(baseMessageCell.setForwardViewState(messageBean));
        }
    }

    @SuppressWarnings({"unchecked"})
    public void setContentView(MessageBeanForUI messageBean) {
        stopHighLight();
        if (showTimeTitle && timeTitleView != null) {
            timeTitleView.setText(ChatTimeUtils.getTimeTitleString(messageBean.getTimeSend()));
        }
        if (showForwardHead && chat_cell_forward_head != null) {
            chat_cell_forward_head.setCompoundDrawablesWithIntrinsicBounds(ResourceUtils.getDrawable(messageBean.isSelf() ? R.drawable.ic_forward_sent_flag : R.drawable.ic_forward_sent_flag), null, null, null);
            chat_cell_forward_head.setTextColor(ResourceUtils.getColor(messageBean.isSelf() ? R.color.bot_forward_send_text_color : R.color.bot_forward_flag_text_color));
        }
        baseMessageCell.initMessage(messageBean);
        if (showReply) {
            //Reply part.
            MessageBeanForUI messageReply = messageBean.getMessageReply();
            if (messageReply != null) {
                if (chat_cell_container_reply_message != null) {
                    if (messageBean.isSelf()) {
                        chat_cell_container_reply_message.setBackgroundResource(R.drawable.shape_round_rect_background_reply_self);
                        chat_cell_container_reply_message.setHeadColor(ResourceUtils.getColor(R.color.bot_chat_item_reply_send_line_color));
                    } else {
                        chat_cell_container_reply_message.setBackgroundResource(R.drawable.shape_round_rect_background_reply_self);
                        chat_cell_container_reply_message.setHeadColor(RandomColor.getColor(messageReply.getSenderUid()));
                    }
                    this.replyCell = MessageCellManager.getReplyCellByViewType(messageReply);
                    chat_cell_container_reply_message.removeAllViews();
                    MessageCellManager.addCellToContainer(replyCell, chat_cell_container_reply_message);
                    chat_cell_container_reply_message.setVisibility(View.VISIBLE);
                    this.replyCell.setPresenter(baseMessageCell.getPresenter());
                    this.replyCell.initReplyMessage(messageBean, messageReply);
                }
            }
        } else {
            replyCell = null;
            if (chat_cell_container_reply_message != null) {
                chat_cell_container_reply_message.removeAllViews();
                chat_cell_container_reply_message.setVisibility(View.GONE);
            }
        }
    }

    public void changeMessageContent(MessageBeanForUI messageBean) {
        setTag(messageBean.getUUID());
        baseMessageCell.changeMessage(messageBean);
    }

    public void setSendTime(MessageBeanForUI message) {
        if (messageSendTimeView != null) {
            String timeString = TimeUtils.getTimeStringHMS(message.getTimeSend());
            messageSendTimeView.setText(timeString);
            messageSendTimeView.setTextColor(ResourceUtils.getColor(baseMessageCell.getTimestampColorRes(message)));
            baseMessageCell.onSetTimestamp(message, timeString);
        }
    }

    public void setMessageStatusView(@NonNull MessageBeanForUI messageBean, boolean animate) {
        int messageStatusCode = messageBean.getMessageStatus();
        int visible;
        if (ContactUtil.isGroup(messageBean.getSessionId())) {
            visible = View.GONE;
        } else if ((ContactUtil.isUser(messageBean.getSessionId())
                || ContactUtil.isOAAccount(messageBean.getSessionId()))
                && messageBean.isSelf()) {
            if (baseMessageCell.showMessageStatusView()) {
                if (messageBean.isDeletedMessage()) {
                    visible = View.INVISIBLE;
                } else {
                    visible = View.VISIBLE;
                }
            } else {
                visible = View.GONE;
            }
        } else {
            visible = View.GONE;
        }
        if (messageStatusView != null) {
            messageStatusView.setVisibility(visible);
            messageStatusView.setImageResource(MessageStatusDrawableUtils.getMessageStatusIcon(messageStatusCode, false), animate);
        }
        baseMessageCell.onSetStatusDrawable(messageBean, visible, MessageStatusDrawableUtils.getMessageStatusIcon(messageStatusCode, false), animate);
    }

    public void showSenderName(String senderUid) {
        if (showSenderName && chat_cell_name_view != null) {
            chat_cell_name_view.bindUid(senderUid);
            chat_cell_name_view .setTextColor(RandomColor.getColor(senderUid));
            chat_cell_name_view .setTypeface(null, Typeface.BOLD);
        }
    }

    public void tryToHighLight(int position) {
        if (!baseMessageCell.getPresenter().popHighLightPosition(position)) {
            return;
        }
        if (chat_cell_slide_view == null) {
            return;
        }
        stopHighLight();
        int highLightColor = ResourceUtils.getColor(R.color.chat_message_select_bg);
        if (highLightAnimator == null) {
            HighLightDrawable colorDrawable = new HighLightDrawable(highLightColor);
            highLightAnimator = ValueAnimator.ofFloat(1, 0);
            highLightAnimator.setDuration(3000);
            highLightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (Float) animation.getAnimatedValue();
                    colorDrawable.setAlpha((int) (255 * percent));
                    chat_cell_slide_view.setBackground(colorDrawable);
                }
            });
            highLightAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    chat_cell_slide_view.setBackground(null);
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
        if (chat_cell_slide_view == null) {
            return;
        }
        if (highLightAnimator != null && highLightAnimator.isRunning()) {
            highLightAnimator.cancel();
        }
        chat_cell_slide_view.setBackground(null);
    }

    public void onClickMessage(@NonNull MessageBeanForUI message) {
        if (this.baseMessageCell.isSupportClick(message)) {
            this.baseMessageCell.onClickMessage(message);
        }
    }

    public boolean canSelect(@NonNull MessageBeanForUI messageBean) {
        return this.baseMessageCell.canSelect(messageBean);
    }

    public boolean canBeReplied(@NonNull MessageBeanForUI messageBean) {
        return this.baseMessageCell.canBeReplied(messageBean);
    }

    public void setDebugInfo(@NonNull MessageBeanForUI messageBean, int position) {

    }
}
