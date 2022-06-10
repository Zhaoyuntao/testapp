package im.thebot.chat.ui.cells.drawable;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 1/13/21
 * description: for message cell high light.
 */
public class HighLightDrawable extends ColorDrawable {
    private final int radius;

    public HighLightDrawable(int color) {
        super(color);
        radius = UiUtils.dipToPx(5);
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);
        Path path = new Path();
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.draw(canvas);
    }
}
