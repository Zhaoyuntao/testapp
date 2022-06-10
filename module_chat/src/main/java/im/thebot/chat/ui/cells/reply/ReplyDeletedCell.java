package im.thebot.chat.ui.cells.reply;

import android.content.Context;

import androidx.annotation.NonNull;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyDeletedCell extends BaseReplyCell<MessageBeanForUI> {

    public ReplyDeletedCell(Context context) {
        super(context);
    }

    @Override
    protected void initReplyView(Context context) {

    }

    @Override
    protected void onReplyDataInit(@NonNull MessageBeanForUI messageReply) {
        //todo lv3
//        iconView.setImageResource(R.drawable.svg_chat_reply_cell_deleted);
//        descriptionView.setText(R.string.string_this_message_is_deleted);
    }
}
