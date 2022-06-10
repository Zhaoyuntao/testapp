package im.thebot.chat.ui.cells.origin;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import java.util.ArrayList;
import java.util.List;

import im.thebot.chat.api.chat.message.event.BaseEventMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class SystemEventCell extends BaseMessageCell<BaseEventMessageForUI> {
    private TextView contentView;
    private BaseEventMessageForUI messageBean;

    public SystemEventCell(Context context) {
        super();
    }

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_system_event;
    }

    @Override
    public boolean showAsSender() {
        return false;
    }

    @Override
    public void initView(@NonNull Context context) {
        contentView = findViewById(R.id.text_conversation_cell_system_notification);
        contentView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onMessageInit(@NonNull BaseEventMessageForUI messageBean) {
        this.messageBean = messageBean;
        initViewData(messageBean);
        List<String> list = messageBean.getMembersUid();
        if (list == null) {
            list = new ArrayList<>(1);
        }
        if (messageBean.getOperator() != null) {
            list.add(messageBean.getOperatorUid());
        }
    }

    private void initViewData(BaseEventMessageForUI messageBean) {
        String tag = messageBean.getUUID();
        contentView.setTag(tag);
        contentView.setText(messageBean.getCachedCharSequence());
    }

    @Override
    protected void onAttachedToCell(@Nullable BaseEventMessageForUI message) {
    }

    @Override
    protected void onDetachedFromCell(@Nullable BaseEventMessageForUI message) {
    }
}
