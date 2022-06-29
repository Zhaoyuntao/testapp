package im.thebot.chat.ui.cells.origin;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.WithdrawMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class WithdrawCell extends BaseMessageCell<WithdrawMessageForUI> {
    private ImageView iconView;
    private TextView contentView;

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_withdraw;
    }

    @Override
    public void initView(@NonNull Context context) {
        iconView = findViewById(R.id.withdraw_message_cell_icon);
        contentView = findViewById(R.id.withdraw_message_cell_content);
    }

    @Override
    public void onMessageInit(@NonNull WithdrawMessageForUI messageBean) {
        boolean isMeSender = messageBean.isSelf();
        contentView.setText("deleted message");
        iconView.setImageResource(isMeSender ? R.drawable.ic_recall_send : R.drawable.ic_recall_receive);
    }
}
