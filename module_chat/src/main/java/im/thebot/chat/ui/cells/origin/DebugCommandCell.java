package im.thebot.chat.ui.cells.origin;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.DebugCommandMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;
import im.turbo.basetools.clipboard.ClipBoardUtils;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class DebugCommandCell extends BaseMessageCell<DebugCommandMessageForUI> {
    private TextView contentView;

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_debug_command;
    }

    @Override
    public void initView(@NonNull Context context) {
        contentView = findViewById(R.id.text_conversation_cell_debug_command);
    }

    @Override
    public void onMessageInit(@NonNull DebugCommandMessageForUI messageBean) {
        contentView.setText(messageBean.getContent());
        contentView.setOnLongClickListener(v -> {
            ClipBoardUtils.copy(messageBean.getContent(),"copied");
            return true;
        });
    }

    @Override
    public boolean showAsSender() {
        return false;
    }

    @Override
    public boolean isMaxWidth() {
        return true;
    }
}
