package im.thebot.chat.ui.cells.origin;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.TextMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseTextCell;
import im.turbo.basetools.utils.StringUtils;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class TextCell extends BaseTextCell<TextMessageForUI> {
    public TextCell(Context context) {
        super(context);
    }

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_text;
    }

    @Override
    public int getTimestampColor() {
        return R.color.color_chat_cell_text_text_color;
    }

    @Override
    public void onTextTypeMessageInit(@NonNull TextMessageForUI messageBean) {
        initTextSize(messageBean);
    }

    @Override
    protected void onTextTypeMessageChanged(@NonNull TextMessageForUI messageBean) {

    }

    /**
     * If content has less than 3 stickers, show big font.
     */
    private void initTextSize(TextMessageForUI messageBean) {
        String text = messageBean.getContent();
        if (!TextUtils.isEmpty(text) && text.length() < 30) {
            int charCount = StringUtils.getCharCount(text);
            int stickerCount = StringUtils.getEmojiCount(text);
            if (stickerCount == charCount) {
                if (stickerCount == 1) {
                    setContentViewTextSize(36);
                } else if (stickerCount == 2) {
                    setContentViewTextSize(28);
                } else if (stickerCount == 3) {
                    setContentViewTextSize(22);
                }
            }
        }
    }
}
