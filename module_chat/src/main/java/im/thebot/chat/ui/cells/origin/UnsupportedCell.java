package im.thebot.chat.ui.cells.origin;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.UnsupportedMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class UnsupportedCell extends BaseMessageCell<UnsupportedMessageForUI> {
    private TextView textView;

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_unsupported;
    }

    @Override
    public void initView(@NonNull Context context) {
        textView = findViewById(R.id.text_conversation_cell_unsupported_type);
    }

    @Override
    public void onMessageInit(@NonNull UnsupportedMessageForUI messageBean) {
        textView.setText("Unknown message");
    }

}
