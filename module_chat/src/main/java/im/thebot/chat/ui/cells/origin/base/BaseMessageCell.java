package im.thebot.chat.ui.cells.origin.base;

import android.view.Gravity;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public abstract class BaseMessageCell<M extends MessageBeanForUI> extends BaseCell<M> {

    public boolean showMessageStatusView() {
        return true;
    }

    public int setForwardViewState(@NonNull M message) {
        return GONE;
    }

    public boolean showForwardView() {
        return false;
    }

    public boolean showAsSender() {
        return true;
    }

    public boolean isMaxWidth() {
        return false;
    }

    public boolean needElevation() {
        return false;
    }

    @ColorRes
    public int getTimestampColorRes(@NonNull M message) {
        return R.color.white;
    }

    public void onSetTimestamp(@NonNull M messageBean, String timeString) {

    }

    public void onSetStatusDrawable(@NonNull M messageBean, int visible, @DrawableRes int drawableRes, boolean animate) {
    }

    public int getMarginStartPX() {
        return UiUtils.dipToPx(60);
    }

    public int getBubbleColor(@NonNull M message) {
        return ResourceUtils.getColor(message.isSelf() ? R.color.color_bubble_me : R.color.color_bubble_other);
    }

    public int getBubbleClickColor(@NonNull M message) {
        return ResourceUtils.getColor(message.isSelf() ? R.color.color_bubble_me_press : R.color.color_bubble_other_press);
    }

    public boolean needDrawBubble() {
        return true;
    }

    public int getMaxWidth() {
        return 0;
    }

    public boolean canSelect(@NonNull M messageBean) {
        return true;
    }

    public boolean canBeReplied(@NonNull M messageBean) {
        return false;
    }
}
