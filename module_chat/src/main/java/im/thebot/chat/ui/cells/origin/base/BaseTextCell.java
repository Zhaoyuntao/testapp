package im.thebot.chat.ui.cells.origin.base;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import java.util.List;

import im.thebot.chat.api.chat.message.text.BaseTextMessageForUI;
import im.thebot.chat.ui.cells.longclick.TextMessageClickManager;
import im.thebot.common.ui.chat.CellTextView;
import im.thebot.common.ui.chat.mention.MentionTextUtil;
import im.turbo.basetools.clipboard.ClipBoardUtils;
import im.turbo.basetools.pattern.PatternSpannableBuilder;
import im.turbo.basetools.pattern.PatternUtils;
import im.turbo.basetools.span.ItemProcessor;
import im.turbo.baseui.toast.ToastUtil;
import im.turbo.callback.CommonDataCallback;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public abstract class BaseTextCell<T extends BaseTextMessageForUI> extends BaseMessageCell<T> {
    private CellTextView contentView;

    @CallSuper
    @Override
    final public void initView(@NonNull Context context) {
        contentView = findViewById(R.id.text_view_base_text_cell);
        initTextTypeView(context);
    }

    protected void initTextTypeView(@NonNull Context context) {

    }

    @CallSuper
    @Override
    final public void onMessageInit(@NonNull T messageBean) {
        initPatterns(messageBean);
        registerContactsNameChangeListener(messageBean);
        onTextTypeMessageInit(messageBean);
    }

    @Override
    final public void onMessageChanged(@NonNull T messageBean) {
        onTextTypeMessageChanged(messageBean);
    }

    protected abstract void onTextTypeMessageInit(@NonNull T messageBean);

    protected abstract void onTextTypeMessageChanged(@NonNull T messageBean);

    private void registerContactsNameChangeListener(@NonNull T messageBean) {
        List<String> mentionUidList = messageBean.getMentionUidList();
        if (mentionUidList == null || mentionUidList.size() == 0) {
            unRegisterContactsNameChangeListener();
            return;
        }
    }

    private void unRegisterContactsNameChangeListener() {
    }

    private void initPatterns(@NonNull T messageBean) {
        String content = messageBean.getContent();
        setContentViewText(messageBean.getCachedCharSequence());
        if (contentView == null || TextUtils.isEmpty(content)) {
            return;
        }
        PatternSpannableBuilder.newBuilder(content)
                .addPattern(new ItemProcessor(PatternUtils.PATTERN_MENTION) {
                    @Override
                    public boolean canClick(@NonNull View v) {
                        return isSupportClick();
                    }

                    @Override
                    public CharSequence onReplace(SpannableStringBuilder stringBuilder, int start, int end, String childContent) {
                        String uid = MentionTextUtil.removeMentionSymbol(childContent);
                            return "@\u2068" + uid + "\u2068";
                    }

                    @Override
                    public void onClick(@NonNull View v, String contentOrigin, CharSequence contentClick) {
//                        S.s("onClick:[" + contentOrigin + "][" + contentClick + "]");
                        if (isSupportClick()) {
                            String uid = MentionTextUtil.removeMentionSymbol(contentOrigin);
                            ToastUtil.show("uid:"+uid);
                        }
                    }
                }, new ItemProcessor("(@\u2068(?i)(All)\u2068)") {
                    @Override
                    public boolean canClick(@NonNull View v) {
                        return false;
                    }

                    @Override
                    public CharSequence onReplace(SpannableStringBuilder stringBuilder, int start, int end, String childContent) {
                        return "@\u2068" + "all" + "\u2068";
                    }
                }, new ItemProcessor(PatternUtils.PATTERN_EMAIL) {
                    @Override
                    public boolean canClick(@NonNull View v) {
                        return isSupportClick();
                    }

                    @Override
                    public void onClick(@NonNull View v, String contentOrigin, CharSequence contentClick) {
                        if (isSupportClick()) {
                            TextMessageClickManager.onEmailClicked(getContext(), contentOrigin);
                        }
                    }
                }, new ItemProcessor(PatternUtils.WEB_URL) {

                    @Override
                    public boolean canClick(@NonNull View v) {
                        return isSupportClick();
                    }

                    @Override
                    public void onClick(@NonNull View v, String contentOrigin, CharSequence contentClick) {
                        if (isSupportClick()) {
                            TextMessageClickManager.processThirdPartyUrlClick(getContext(), contentOrigin);
                        }
                    }

                    @Override
                    public boolean onLongClick(@NonNull View v, String contentOrigin, CharSequence contentClick) {
                        ClipBoardUtils.copy(contentClick, "copied");
                        return true;
                    }
                }
//                , new ItemProcessor(PatternUtils.PATTERN_PHONE_NUMBER) {
//                    @Override
//                    public boolean canClick(@NonNull View v) {
//                        return isSupportClick();
//                    }
//
//                    @Override
//                    public void onClick(@NonNull View v, String contentOrigin, CharSequence contentClick) {
//                        if (isSupportClick()) {
//                            TextMessageClickManager.onPhoneNumberClicked(getContext(), contentOrigin);
//                        }
//                    }
//                }
                )
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

    final protected void setContentViewTextSize(float textSize) {
        if (contentView != null) {
            contentView.setTextSize(textSize);
        }
    }

    final protected void setContentViewText(CharSequence text) {
        if (contentView != null) {
            contentView.setText(text);
        }
    }

    @Override
    final protected void onAttachedToCell(@Nullable T message) {
        if (message != null) {
            registerContactsNameChangeListener(message);
        }
        onAttachedToTextTypeCell(message);
    }

    @Override
    final protected void onDetachedFromCell(@Nullable T message) {
        unRegisterContactsNameChangeListener();
        onDetachedFromTextTypeCell(message);
    }

    protected void onAttachedToTextTypeCell(@Nullable T message) {
    }

    protected void onDetachedFromTextTypeCell(@Nullable T message) {
    }
}
