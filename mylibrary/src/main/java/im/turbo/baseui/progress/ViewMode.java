package im.turbo.baseui.progress;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import im.turbo.basetools.state.State;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
final public class ViewMode extends State<ViewMode> {
    @DrawableRes
    private int drawableRes;
    private boolean showProgress;
    private boolean rotate;
    private View.OnClickListener listener;
    private int visible = View.VISIBLE;

    public ViewMode(int tag) {
        super(tag);
    }

    public ViewMode(@NonNull String tag) {
        super(tag);
    }


    public ViewMode drawable(@DrawableRes int drawableRes) {
        this.drawableRes = drawableRes;
        return this;
    }

    public ViewMode showProgress(boolean showProgress) {
        this.showProgress = showProgress;
        return this;
    }

    public ViewMode rotate(boolean rotate) {
        this.rotate = rotate;
        return this;
    }

    public ViewMode listener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public boolean isRotate() {
        return rotate;
    }

    @DrawableRes
    final public int getDrawableRes() {
        return drawableRes;
    }

    final public String getMode() {
        return getTag();
    }

    final public boolean isShowProgress() {
        return showProgress;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public ViewMode visible(int visible) {
        this.visible = visible;
        return this;
    }

    public int getVisible() {
        return visible;
    }

    @Override
    public String toString() {
        return "ViewMode{" +
                "mode='" + getMode() + '\'' +
                '}';
    }
}
