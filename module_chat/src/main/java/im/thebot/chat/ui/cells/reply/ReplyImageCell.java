package im.thebot.chat.ui.cells.reply;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.ImageMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.thebot.chat.ui.view.ChatImageView;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyImageCell extends BaseReplyCell<ImageMessageForUI> {
    private ChatImageView imageView;
    private TextView textView;

    public ReplyImageCell(Context context) {
        super(context);
    }

    @Override
    protected int setReplyLayout() {
        return R.layout.layout_reply_chat_cell_picture;
    }

    @Override
    protected void initReplyView(Context context) {
        imageView = findViewById(R.id.reply_cell_image_image_view);
        textView = findViewById(R.id.reply_cell_image_text_view);
    }

    @Override
    protected void onReplyDataInit(@NonNull ImageMessageForUI messageReply) {
        textView.setText(TextUtils.isEmpty(messageReply.getContent()) ? "photo" : messageReply.getContent());
        loadPicture(messageReply);
    }

    @Override
    public void onMessageChanged(@NonNull ImageMessageForUI message) {
    }

    private void loadPicture(@NonNull ImageMessageForUI message) {
//        if (TextUtils.isEmpty(message.getFileLocalPath())) {
//            imageView.setImageBase64(message.getImagePreviewBase64());
//        } else {
//            imageView.setImageLocal(message.getFileLocalPath());
//        }
    }
}
