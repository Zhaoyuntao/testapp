package im.thebot.chat.ui.cells.reply;

import android.content.Context;

import androidx.annotation.NonNull;

import im.thebot.chat.api.chat.message.AudioMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.turbo.basetools.time.TimeUtils;


/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyAudioCell extends BaseReplyCell<AudioMessageForUI> {

    public ReplyAudioCell(Context context) {
        super(context);
    }

    @Override
    protected void initReplyView(Context context) {
    }

    @Override
    protected void onReplyDataInit(@NonNull AudioMessageForUI messageReply) {
        String content = TimeUtils.formatLongToDuration(messageReply.getAudioDuration());
        //todo lv3
//        iconView.setImageResource(R.drawable.svg_chat_reply_cell_audio);
//        descriptionView.setText(content);
    }
}
