package im.thebot.chat.ui.cells.origin;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.DebugLogMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;
import im.turbo.basetools.pattern.PatternSpannableBuilder;
import im.turbo.basetools.span.ItemProcessor;
import im.turbo.callback.CommonDataCallback;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class DebugLogCell extends BaseMessageCell<DebugLogMessageForUI> {
    private TextView contentView;

    public DebugLogCell(Context context) {
        super();
    }

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_debug_log;
    }

    @Override
    public void initView(@NonNull Context context) {
        contentView = findViewById(R.id.text_conversation_cell_debug_log);
    }

    @Override
    public void onMessageInit(@NonNull DebugLogMessageForUI messageBean) {
        contentView.setTextColor(messageBean.getError() ? Color.parseColor("#ff5555") : Color.parseColor("#ccccff"));
        String debugLog = messageBean.getContent();
        contentView.setText(debugLog);
        contentView.setTag(messageBean.getUUID());
        PatternSpannableBuilder.newBuilder(messageBean.getContent())
                .addPattern(new ItemProcessor("(\\(.*\\.java.*?\\))") {
                    @Override
                    public Integer getColor() {
                        return Color.rgb(155, 255, 155);
                    }
                }, new ItemProcessor("at\\s+(.*?)\\(") {
                    @Override
                    public Integer getColor() {
                        return Color.rgb(155, 155, 255);
                    }
                })
                .build(getContext(), new CommonDataCallback<CharSequence>() {
                    private final String tag = (String) contentView.getTag();

                    @Override
                    public void onSuccess(CharSequence charSequence) {
                        String tag = (String) contentView.getTag();
                        if (TextUtils.equals(tag, this.tag)) {
                            contentView.setText(charSequence);
                        }
                    }
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
