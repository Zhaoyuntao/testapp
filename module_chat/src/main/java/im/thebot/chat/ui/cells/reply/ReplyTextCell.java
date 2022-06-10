package im.thebot.chat.ui.cells.reply;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import java.util.List;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.api.chat.message.TextMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseReplyCell;
import im.thebot.common.ui.chat.CellTextView;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public class ReplyTextCell extends BaseReplyCell<TextMessageForUI> {
    private CellTextView contentView;

    public ReplyTextCell(Context context) {
        super(context);
    }

    @Override
    protected int setReplyLayout() {
        return R.layout.layout_reply_chat_cell_text;
    }

    @Override
    protected void initReplyView(Context context) {
        contentView = findViewById(R.id.reply_cell_text);
//        contentView.addPattern(PatternUtils.PATTERN_URL_MEETING, new BlueSpanClickListener() {
//            @Override
//            public void onClick(View view, String matchContent, List<BluePatternGroupItemBean> groups) {
//            }
//        });
//        contentView.addPattern(PatternUtils.PATTERN_EMAIL, new BlueSpanClickListener() {
//            @Override
//            public void onClick(View view, String matchContent, List<BluePatternGroupItemBean> groups) {
//            }
//        });
//        contentView.addPattern(PatternUtils.WEB_URL, new BlueSpanClickListener() {
//            @Override
//            public void onClick(View view, String matchContent, List<BluePatternGroupItemBean> groups) {
//            }
//        });
//        contentView.addPattern(PatternUtils.PATTERN_PHONE_NUMBER, new BlueSpanClickListener() {
//            @Override
//            public void onClick(View view, String matchContent, List<BluePatternGroupItemBean> groups) {
//            }
//        });
    }

    @Override
    protected boolean useDefaultReplySnapshotView() {
        return false;
    }

    @Override
    public void onReplyDataInit(@NonNull TextMessageForUI messageReply) {
            contentView.setText(messageReply.getContent());
    }

    private void initMentionInfo(TextMessageForUI messageBean) {
    }

    private void setReferenceName(@NonNull MessageBeanForUI messageBean, List<String> hidList) {
        //todo lv3
//        UserSdkFactory.getSdk().getContactsInfo(hidList, new ContactInfoService.ContactsInfoCallBack() {
//            @Override
//            public void result(List<ContactInfo> contactInfoList) {
//                ThreadPool.runUi(new SafeRunnable(getContext()) {
//                    @Override
//                    protected void runSafely() {
//                        if (!TextUtils.equals(contactsChangeListener.getTag(), (String) ReplyTextCell.this.getTag())) {
//                            return;
//                        }
//                        ReferenceRender.renderReferenceBackgroundColor(contentView, messageBean.getContent(), contentView.getCurrentTextColor(),
//                                messageBean.getReferenceBeans(), ReferenceUtils.convertTo(contactInfoList), null);
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void onAttachedToCell(@Nullable TextMessageForUI message) {
    }

    @Override
    protected void onDetachedFromCell(@Nullable TextMessageForUI message) {
    }
}
