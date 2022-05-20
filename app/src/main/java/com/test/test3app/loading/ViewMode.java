package com.test.test3app.loading;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.test.test3app.faceview.Preconditions;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
final public class ViewMode {
    private final String mode;
    @DrawableRes
    private int drawableRes;
    private long rotateDuration;
    private boolean showProgress;
    private boolean useWaveProgress;
    private boolean rotate;
    private View.OnClickListener listener;
    private int visible = View.VISIBLE;

    public ViewMode(int mode) {
        this.mode = String.valueOf(mode);
    }

    public ViewMode(@NonNull String mode) {
        Preconditions.checkNotEmpty(mode);
        this.mode = mode;
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

    public ViewMode useWaveProgress(boolean useWaveProgress) {
        this.useWaveProgress = useWaveProgress;
        return this;
    }

    public ViewMode duration(long rotateDuration) {
        this.rotateDuration = rotateDuration;
        return this;
    }

    public ViewMode listener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public boolean isUseWaveProgress() {
        return useWaveProgress;
    }

    public boolean isRotate() {
        return rotate;
    }

    @DrawableRes
    final public int getDrawableRes() {
        return drawableRes;
    }

    final public String getMode() {
        return mode;
    }

    final public boolean isShowProgress() {
        return showProgress;
    }

    public boolean useWaveProgress() {
        return useWaveProgress;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public long getRotateDuration() {
        return rotateDuration;
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
                "mode='" + mode + '\'' +
                '}';
    }
}
