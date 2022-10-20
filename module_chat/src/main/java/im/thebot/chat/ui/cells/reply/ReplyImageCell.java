package im.thebot.chat.ui.cells.reply;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.ImageMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyImageCell extends BaseReplyCell<ImageMessageForUI> {

    @Override
    protected void initReplyView(Context context) {
        replyRightImageView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onReplyDataInit(@NonNull ImageMessageForUI messageReply) {
        boolean hasNoText = TextUtils.isEmpty(messageReply.getContent());
        replyTextView.setText(hasNoText ? ResourceUtils.getString(R.string.Photo) : messageReply.getContent());
        replySmallIconView.setVisibility(hasNoText ? View.VISIBLE : View.GONE);
//        int flagResId = messageReply.isSelf() ? R.drawable.ic_reply_send_flag : R.drawable.ic_reply_receive_flag;
//        replySmallIconView.setImageResource(flagResId);
        replyRightImageView.bindMessage(messageReply);
        int replyColor = messageReply.isSelf() ? R.color.bot_message_oneself_reply_color : R.color.bot_message_other_reply_color;
        replyTextView.setTextColor(ResourceUtils.getColor(replyColor));
    }
}
