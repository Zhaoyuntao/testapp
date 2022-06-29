package im.turbo.baseui.progress;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import im.turbo.basetools.state.State;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
final public class SlideMode extends State<SlideMode> {
    @ColorInt
    private int colorSlider;

    public SlideMode(int tag) {
        super(tag);
    }

    public SlideMode(@NonNull String tag) {
        super(tag);
    }

    public SlideMode colorSlide(@ColorInt int color) {
        this.colorSlider = color;
        return this;
    }

    public int getColorSlider() {
        return colorSlider;
    }

    final public String getMode() {
        return getTag();
    }

    @Override
    public String toString() {
        return "ChatProgressViewMode{" +
                "mode='" + getMode() + '\'' +
                '}';
    }
}
