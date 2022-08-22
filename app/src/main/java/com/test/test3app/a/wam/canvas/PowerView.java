package com.test.test3app.a.wam.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 20/05/2022
 * description:
 */
public class PowerView extends View {
    private Paint paint;
    private float x, y, angle;
    private float xCenter;
    private float yCenter;
    private float radius;
    private Callback callback;
    private float lengthPercent;
    private String content;
    private float textSize;

    public PowerView(Context context) {
        super(context);
        init(null);
    }

    public PowerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public PowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        textSize = UiUtils.dipToPx(30);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        xCenter = width / 2f;
        yCenter = (height - textSize) / 2f + textSize;
        radius = Math.min(width, height - textSize) / 2f;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (xCenter == 0 || yCenter == 0) {
            return;
        }
        paint.setColor(Color.parseColor("#ff0000"));
        canvas.drawCircle(xCenter, yCenter, radius, paint);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        canvas.drawLine(xCenter, yCenter, x, y, paint);
        if (content != null) {
            paint.setColor(Color.parseColor("#ff0000"));
            canvas.drawRect(0, 0, getWidth(), textSize, paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            canvas.drawText(content, 0, textSize, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        float angle;

        if (x < xCenter) {
            angle = (float) Math.toDegrees(Math.atan((Math.abs(y - yCenter)) / Math.abs(x - xCenter)));
            if (y < yCenter) {
                angle = 270 + angle;
            } else if (y > yCenter) {
                angle = 270 - angle;
            } else {
                angle = 270;
            }
        } else if (x > xCenter) {
            angle = (float) Math.toDegrees(Math.atan((Math.abs(y - yCenter)) / Math.abs(x - xCenter)));
            if (y < yCenter) {
                angle = 90 - angle;
            } else if (y > yCenter) {
                angle = 90 + angle;
            } else {
                angle = 90;
            }
        } else {
            if (y > yCenter) {
                angle = 180;
            } else {
                angle = 0;
            }
        }

        float lengthPercent = Math.min(1, (float) Math.sqrt(Math.pow(Math.abs(x - xCenter), 2) + Math.pow(Math.abs(y - yCenter), 2)) / radius);

        if (angle != this.angle || lengthPercent != this.lengthPercent) {
            this.lengthPercent = lengthPercent;
            this.angle = angle;
            if (callback != null) {
                content = callback.onTouch(this.angle, this.lengthPercent);
            }
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (callback != null) {
            content = callback.onTouch(this.angle, this.lengthPercent);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        String onTouch(float angle, float lengthPercent);
    }
}
