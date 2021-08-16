package com.test.test3app.faceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.regex.Pattern;

/**
 * created by zhaoyuntao
 * on 2020/7/14
 * description:
 */
public class BlueFaceDrawable extends Drawable {
    private static final Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");

    private final String name;
    private final int backgroundColor;
    private float nameTextSizePercent;

    // workspace
    private final Paint paint = new Paint();
    private final Rect workRect = new Rect();
    private final Point textOrigin = new Point();

    public BlueFaceDrawable(@NonNull Context context, @NonNull String name,
                            @ColorInt int backgroundColor, float percentText) {
        this.name = name;

        // '-1' (0xFFFFFFFF) means that we cannot support 'white' color as background color!
        if (backgroundColor == -1) {
            this.backgroundColor = DefaultFaceUtils.getNameBackgroundColor(context, name);
        } else {
            this.backgroundColor = backgroundColor;
        }

        nameTextSizePercent = (pattern.matcher(name).matches() || name.length() == 1) ? 1 / 2.2f : 1 / 2.8f;
        if (percentText > 0) {
            nameTextSizePercent = nameTextSizePercent * percentText;
        }

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        int size = Math.min(width, height);
        int x = (int) ((width - size) / 2f);
        int y = (int) ((height - size) / 2f);

        paint.setColor(backgroundColor);
        canvas.drawRect(x, y, x + size, y + size, paint);

        if (!TextUtils.isEmpty(name)) {
            float textSize = size * nameTextSizePercent;
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);

            workRect.set(x, y, x + size, y + size);
            TextMeasure.calculateTextOriginAtCenter(workRect, paint, name, textOrigin);
            canvas.drawText(name, textOrigin.x, textOrigin.y, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
