package im.thebot.chat.ui.cells.origin.base;

import android.view.Gravity;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.mvp.presenter.ChatPresenter;
import im.turbo.basetools.preconditions.Preconditions;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public abstract class BaseMessageCell<M extends MessageBeanForUI> extends BaseCell<M> {

    private boolean showTimeTitle;
    private boolean showTopSpace;
    private boolean showSenderName;
    private boolean showNewMessageLine;
    private int gravity;

    public BaseMessageCell() {
        super();
    }

    public boolean showMessageStatusView() {
        return true;
    }

    public boolean showMessageForwardView() {
        return false;
    }

    final public void setShowTimeTitle(boolean showTimeTitle) {
        this.showTimeTitle = showTimeTitle;
    }

    final public boolean needShowTimeTitle() {
        return showTimeTitle;
    }

    final public void setShowTopSpace(boolean showTopSpace) {
        this.showTopSpace = showTopSpace;
    }

    final public boolean needShowTopSpace() {
        return showTopSpace;
    }

    final public void setShowSenderName(boolean showSenderName) {
        this.showSenderName = showSenderName;
    }

    final public boolean needShowSenderName() {
        return showSenderName;
    }

    final public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    final public int getGravity() {
        return showAsSender() ? gravity : Gravity.CENTER;
    }

    public boolean needElevation() {
        return false;
    }

    public void setShowNewMessageLine(boolean showNewMessageLine) {
        this.showNewMessageLine = showNewMessageLine;
    }

    public boolean needShowNewMessageLine() {
        return showNewMessageLine;
    }

    @IdRes
    public int getSendTimeViewResId() {
        return R.id.message_send_time_text_view;
    }

    @IdRes
    public int getSendStatusViewResId() {
        return R.id.message_status_view_send;
    }

    @ColorRes
    public int getTimestampColor() {
        return R.color.white;
    }

    @Override
    public boolean isSupportClick() {
        return !presenter.isSelecting();
    }

    public void onSetTimestamp(@NonNull M messageBean, String timeString) {

    }

    public void onSetStatusDrawable(@NonNull M messageBean, int visible, @DrawableRes int drawableRes, boolean animate) {
    }
}
