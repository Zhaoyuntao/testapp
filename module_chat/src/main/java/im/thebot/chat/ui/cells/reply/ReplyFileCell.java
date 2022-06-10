package im.thebot.chat.ui.cells.reply;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.FileMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyFileCell extends BaseReplyCell<FileMessageForUI> {


    public ReplyFileCell(Context context) {
        super(context);
    }

    @Override
    protected int setReplyLayout() {
        return R.layout.layout_chat_cell_reply_item_file;
    }

    @Override
    protected void initReplyView(Context context) {
    }

    @Override
    protected void onReplyDataInit(@NonNull FileMessageForUI messageReply) {
        //todo lv3
//        iconView.setImageResource(R.drawable.svg_chat_reply_cell_file);
//        descriptionView.setText(messageReply.getFileName());
    }
}
