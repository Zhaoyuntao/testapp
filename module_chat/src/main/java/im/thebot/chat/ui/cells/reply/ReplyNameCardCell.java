package im.thebot.chat.ui.cells.reply;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.ContactCardMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.thebot.common.UserFaceView;
import im.thebot.common.UserNameView;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyNameCardCell extends BaseReplyCell<ContactCardMessageForUI> {
    private UserNameView nameView;
    private UserFaceView faceView;

    public ReplyNameCardCell(Context context) {
        super(context);
    }

    @Override
    protected int setReplyLayout() {
        return R.layout.layout_reply_chat_cell_name_card;
    }

    @Override
    protected void initReplyView(Context context) {
        faceView = findViewById(R.id.reply_cell_name_card_face);
        nameView = findViewById(R.id.reply_cell_name_card_name);
    }

    @Override
    protected boolean useDefaultReplySnapshotView() {
        return false;
    }

    @Override
    protected void onReplyDataInit(@NonNull ContactCardMessageForUI messageReply) {
//        String uid = messageReply.getNameCardUid();
//        nameView.bindUid(uid);
//        faceView.bindUid(uid);
    }
}
