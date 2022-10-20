package im.thebot.chat.ui.cells.origin.base;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.ui.adapter.RandomColor;
import im.thebot.chat.ui.view.ChatImageView;
import im.thebot.common.UserNameView;
import im.thebot.common.ui.chat.CellTextView;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public abstract class BaseReplyCell<M extends MessageBeanForUI> extends BaseCell<M> {
    private UserNameView replyUserNameView;
    protected CellTextView replyTextView;
    protected ImageView replySmallIconView;
    protected ChatImageView replyRightImageView;

    @Override
    final public int setLayout() {
        return R.layout.layout_chat_cell_reply_item;
    }

    @Override
    final public void initView(@NonNull Context context) {
        replyUserNameView = findViewById(R.id.reply_cell_sender_name);
        replyTextView = findViewById(R.id.reply_cell_text_view);
        replySmallIconView = findViewById(R.id.reply_cell_small_icon);
        replyRightImageView = findViewById(R.id.reply_cell_right_image_view);
        initReplyView(context);
    }

    @Override
    final protected void onMessageInit(@NonNull M messageReply) {
        initReplyMessage(null, messageReply);
    }

    final public void initReplyMessage(@Nullable MessageBeanForUI messageOrigin, @NonNull M messageReply) {
        if (replyUserNameView != null) {
            if (messageOrigin != null) {
                replyUserNameView.setTextColor(messageOrigin.isSelf() ? ResourceUtils.getColor(R.color.bot_chat_item_reply_send_line_color) : RandomColor.getColor(messageReply.getSenderUid()));
            } else {
                replyUserNameView.setTextColor(ResourceUtils.getColor(R.color.bot_chat_item_reply_send_line_color));
            }
            replyUserNameView.bindUid(messageReply.getSenderUid());
        }
        if (messageOrigin != null) {
            onOriginDataInit(messageOrigin);
        }
        onReplyDataInit(messageReply);
    }

    @Override
    final public void onMessageChanged(@NonNull M message) {
    }

    protected abstract void initReplyView(Context context);

    protected abstract void onReplyDataInit(@NonNull M messageReply);

    protected void onOriginDataInit(@NonNull MessageBeanForUI messageOrigin) {
    }
}
