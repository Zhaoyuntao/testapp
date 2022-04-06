package com.test.test3app.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.test.test3app.R;
import com.test.test3app.fastrecordviewnew.UiUtils;
import com.test.test3app.threadpool.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 16/08/2021
 * description:
 */
public class BubbleView extends LinearLayout {
    private Path path;
    private Paint paint;
    private RectF bubbleRect;
    private RectF tailRect;
    private float radiusConner;
    private int tailWidth, tailHeight;
    @TailDirection
    private int direction = TailDirection.LEFT_HIDE;
    private int padding;
    private int margin;
    private int shadow;
    private int backgroundColor;
    private int backgroundColorPressed;
    private int backgroundShadowColor;
    private int radiusTail;

    @IntDef({TailDirection.LEFT, TailDirection.RIGHT, TailDirection.LEFT_HIDE, TailDirection.RIGHT_HIDE})
    public @interface TailDirection {
        int LEFT = 0;
        int RIGHT = 1;
        int LEFT_HIDE = 2;
        int RIGHT_HIDE = 3;
    }

    public BubbleView(@NonNull Context context) {
        super(context);
        init();
    }

    public BubbleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        setWillNotDraw(false);
        path = new Path();
        paint = new Paint();
        bubbleRect = new RectF();
        tailRect = new RectF();
        radiusConner = UiUtils.dipToPx(13);
        radiusTail = UiUtils.dipToPx(2);
        tailWidth = UiUtils.dipToPx(10);
        tailHeight = UiUtils.dipToPx(12);
        margin = UiUtils.dipToPx(1);
        padding = UiUtils.dipToPx(0) + margin;
        shadow = UiUtils.dipToPx(1);
        backgroundShadowColor = Color.argb(20, 0, 0, 0);

        resetBackgroundColor();
        resetPadding();
        setOrientation(VERTICAL);
    }

    private void resetPadding() {
        if (direction == TailDirection.RIGHT || direction == TailDirection.RIGHT_HIDE) {
            setPadding(padding, padding, (int) (padding + tailWidth), padding);
        } else {
            setPadding((int) (padding + tailWidth), padding, padding, padding);
        }
    }

    private void resetBackgroundColor() {
        if (direction == TailDirection.RIGHT || direction == TailDirection.RIGHT_HIDE) {
            backgroundColor = ResourceUtils.getColor(R.color.color_bubble_me);
            backgroundColorPressed = ResourceUtils.getColor(R.color.color_bubble_me_press);
        } else {
            backgroundColor = ResourceUtils.getColor(R.color.color_bubble_other);
            backgroundColorPressed = ResourceUtils.getColor(R.color.color_bubble_other_press);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        setPath(width, height);
        paint.reset();
        paint.setColor(isPressed() ? backgroundColorPressed : backgroundColor);
        paint.setShadowLayer(shadow, shadow / 2f, shadow / 2f, backgroundShadowColor);
        canvas.drawPath(path, paint);
        super.draw(canvas);
    }

    public void setPath(int width, int height) {

        if (width > 0 && height > 0) {
            int widthOfBubble = width - margin * 2 - tailWidth;
            int heightOfBubble = height - margin * 2;
            path.reset();

            float xTail1;
            float xTail2;
            float xTail3;
            float yTail1 = margin;
            float yTail2 = margin;
            float yTail3 = margin + tailHeight;
            float radiusRight;
            float radiusLeft;
            if (direction == TailDirection.RIGHT) {
                radiusRight = 0;
                radiusLeft = radiusConner;
                xTail1 = margin + widthOfBubble;
                xTail2 = xTail1 + calculatePoint2X(tailWidth, radiusTail);
                xTail3 = xTail1;
                bubbleRect.set(margin, margin, margin + widthOfBubble, margin + heightOfBubble);
                path.moveTo(xTail1, yTail1);
                path.lineTo(xTail2, yTail2);
                tailRect.set(xTail2 - radiusTail, yTail2, xTail2 + radiusTail, yTail2 + radiusTail * 2);
                path.arcTo(tailRect, -90, 135);
                path.lineTo(xTail3, yTail3);
            } else if (direction == TailDirection.LEFT) {
                radiusRight = radiusConner;
                radiusLeft = 0;
                xTail1 = margin + tailWidth;
                xTail2 = xTail1 - calculatePoint2X(tailWidth, radiusTail);
                xTail3 = xTail1;
                bubbleRect.set(margin + tailWidth, margin, margin + tailWidth + widthOfBubble, margin + heightOfBubble);
                path.moveTo(xTail1, yTail1);
                path.lineTo(xTail2, yTail2);
                tailRect.set(xTail2 - radiusTail, yTail2, xTail2 + radiusTail, yTail2 + radiusTail * 2);
                path.arcTo(tailRect, -90, -135);
                path.lineTo(xTail3, yTail3);
            } else {
                radiusRight = radiusConner;
                radiusLeft = radiusConner;
                if (direction == TailDirection.RIGHT_HIDE) {
                    bubbleRect.set(margin, margin, margin + widthOfBubble, margin + heightOfBubble);
                } else {
                    bubbleRect.set(margin + tailWidth, margin, margin + tailWidth + widthOfBubble, margin + heightOfBubble);
                }
            }
            path.addRoundRect(bubbleRect, new float[]{radiusLeft, radiusLeft, radiusRight, radiusRight, radiusConner, radiusConner, radiusConner, radiusConner}, Path.Direction.CW);
            path.close();
        }
    }

    private int calculatePoint2X(int tailWidth, int radiusTail) {
        return (int) (tailWidth - Math.tan(Math.toRadians(67.5f)) * radiusTail);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDrawForeground(Canvas canvas) {
        Drawable drawable = getForeground();
        ColorDrawable colorDrawable;
        if (drawable instanceof ColorDrawable) {
            colorDrawable = (ColorDrawable) drawable;
        } else {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        setPath(width, height);
        paint.reset();
        paint.setColor(colorDrawable.getColor());
        paint.setAlpha(colorDrawable.getAlpha());
        canvas.drawPath(path, paint);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        postInvalidate();
    }

    public void setTailDirection(@TailDirection int direction) {
        this.direction = direction;
        resetBackgroundColor();
        resetPadding();
        postInvalidate();
    }

    @TailDirection
    public int getTailDirection() {
        return direction;
    }
}
