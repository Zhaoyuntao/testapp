package im.thebot.chat.ui.view;

import android.graphics.Canvas;

/**
 * created by zhaoyuntao
 * on 22/06/2022
 * description:
 */
public abstract class AnimateFrame {
    private final float start;
    private final float duration;
    private final String name;

    public AnimateFrame(float start, float duration) {
        this(null, start, duration);
    }

    public AnimateFrame(String name, float start, float duration) {
        this.name = name;
        this.start = start;
        this.duration = duration;
    }

    public float getStart() {
        return start;
    }

    public float getDuration() {
        return duration;
    }

    public void draw(Canvas canvas, float timeLine) {
        float end = start + duration;
        if (timeLine >= start && timeLine < end) {
            onDraw(canvas, (timeLine - start) / duration);
        }
    }

    protected abstract void onDraw(Canvas canvas, float percent);

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ""+name;
    }
}
