package im.thebot.chat.ui.cells.reply;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.TextMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyTextCell extends BaseReplyCell<TextMessageForUI> {

    @Override
    protected void initReplyView(Context context) {
    }

    @Override
    public void onReplyDataInit(@NonNull TextMessageForUI messageReply) {
        replyTextView.setText(messageReply.getContent());
        replyTextView.setTextColor(ResourceUtils.getColor(messageReply.isSelf() ? R.color.bot_message_other_reply_color : R.color.bot_message_oneself_reply_color));
    }
}
