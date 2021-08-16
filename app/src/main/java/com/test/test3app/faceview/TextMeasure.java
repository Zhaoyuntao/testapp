package com.test.test3app.faceview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

/**
 * Created by zhaoyuntao on 2018/7/4.
 */

public class TextMeasure {

    public static float[] measure(String text, float textsize) {
        Paint p = new Paint();
        p.setTextSize(textsize);
        float textWidth = p.measureText(text);
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        float top = Math.abs(fontMetrics.top);
        float ascent = Math.abs(fontMetrics.ascent);
        float descent = fontMetrics.descent;
        float bottom = fontMetrics.bottom;
        float w_text = textWidth;
        float h_text = Math.abs(descent - ascent);
        if (w_text < 0) {
            w_text = 0;
        }
        return new float[]{w_text, h_text};
    }

    /**
     * Calculate the origin point for drawing text in center of a rect
     * when using {@link Canvas#drawText(String, float, float, Paint)} Canvas.drawText
     *
     * @param rect The rect to draw the text inside it.
     *             The rect will be reused inside this method to avoid object allocation.
     * @param paint The {@link Paint#setTextAlign(Paint.Align)} will be invoked on it with
     *              {@link Paint.Align#LEFT}
     * @param text The text will be drawn
     * @param origin The calculated origin point for output
     */
    public static void calculateTextOriginAtCenter(@NonNull Rect rect, @NonNull Paint paint,
                                                   @NonNull String text, @NonNull Point origin) {
        // Ref: https://stackoverflow.com/a/32081250
        int rectLeft = rect.left;
        int rectTop = rect.top;
        int rectWidth = rect.width();
        int rectHeight = rect.height();

        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), rect);
        origin.x = rectLeft + rectWidth / 2 - rect.width() / 2 - rect.left;
        origin.y = rectTop + rectHeight / 2 + rect.height() / 2 - rect.bottom;
    }
}
