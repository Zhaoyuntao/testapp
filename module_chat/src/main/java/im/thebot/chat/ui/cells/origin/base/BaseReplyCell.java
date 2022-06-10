package im.thebot.chat.ui.cells.origin.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.common.UserNameView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public abstract class BaseReplyCell<M extends MessageBeanForUI> extends BaseCell<M> {
    private UserNameView userNameView;

    public BaseReplyCell(Context context) {
        super();
    }

    @Override
    final public boolean showAsSender() {
        return false;
    }

    @Override
    final public int setLayout() {
        return setReplyLayout();
    }

    @Override
    final public void initView(@NonNull Context context) {
        userNameView = findViewById(getSenderNameViewResId());
        initReplyView(context);
    }

    public int getSenderNameViewResId() {
        return R.id.reply_cell_sender_name;
    }

    @Override
    final protected void onMessageInit(@NonNull M messageReply) {
        if (userNameView != null) {
            userNameView.setVisibility(showTitle(messageReply) ? VISIBLE : GONE);
            userNameView.bindUid(messageReply.getSenderUid());
        }
        onReplyDataInit(messageReply);
    }

    /**
     * Set a layout resource to customViewContainer.
     *
     * @return if 0, layout will not set.
     */
    protected int setReplyLayout() {
        return 0;
    }

    /**
     * If use default snapshot view.
     *
     * @return If true means using default snapshot view.customViewContainer will be displayed.
     */
    protected boolean useDefaultReplySnapshotView() {
        return true;
    }

    /**
     * When origin replied message is set to view.
     *
     * @param messageReply MessageBean replied.
     */
    protected abstract void onReplyDataInit(@NonNull M messageReply);

    /**
     * If show sender detail title.
     *
     * @param messageReply
     */
    protected boolean showTitle(@NonNull M messageReply) {
        return true;
    }

    /**
     * Init custom view here.
     */
    protected abstract void initReplyView(Context context);

    @Override
    protected void onAttachedToCell(@Nullable M message) {
    }

    @Override
    protected void onDetachedFromCell(@Nullable M message) {
    }
}
