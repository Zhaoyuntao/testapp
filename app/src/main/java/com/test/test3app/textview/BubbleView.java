package com.test.test3app.textview;

import static com.test.test3app.textview.BubbleView.TailDirection.LEFT;
import static com.test.test3app.textview.BubbleView.TailDirection.LEFT_HIDE;
import static com.test.test3app.textview.BubbleView.TailDirection.RIGHT;
import static com.test.test3app.textview.BubbleView.TailDirection.RIGHT_HIDE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.test3app.fastrecordviewnew.UiUtils;

/**
 * created by zhaoyuntao
 * on 16/08/2021
 * description:
 */
public class BubbleView extends LinearLayout {
    private Path path;
    private Paint paint;
    private RectF bubbleRect;
    private float radiusConner;
    private int tailWidth, tailHeight;
    @TailDirection
    private int direction = LEFT_HIDE;
    private int padding;
    private int margin;
    private int shadow;
    private int backgroundColor;
    private int backgroundShadowColor;

    @IntDef({LEFT, RIGHT, LEFT_HIDE, RIGHT_HIDE})
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
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);
        path = new Path();
        paint = new Paint();
        bubbleRect = new RectF();
        radiusConner = UiUtils.dipToPx(6);
        tailWidth = UiUtils.dipToPx(10);
        tailHeight = UiUtils.dipToPx(12);
        margin = UiUtils.dipToPx(2);
        padding = UiUtils.dipToPx(8) + margin;
        shadow = UiUtils.dipToPx(1);
        backgroundColor = Color.WHITE;
        backgroundShadowColor = Color.argb(20, 0, 0, 0);

        resetPadding();
        setOrientation(VERTICAL);
    }

    private void resetPadding() {
        if (direction == RIGHT || direction == RIGHT_HIDE) {
            setPadding(padding, padding, (int) (padding + tailWidth), padding);
        } else {
            setPadding((int) (padding + tailWidth), padding, padding, padding);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (width > 0 && height > 0) {
            int widthOfBubble = width - margin * 2 - tailWidth;
            int heightOfBubble = height - margin * 2;
            path.reset();
            paint.reset();
            paint.setColor(backgroundColor);

            float xTail1;
            float xTail2;
            float xTail3;
            float yTail1 = margin + heightOfBubble;
            float yTail2 = margin + heightOfBubble;
            float yTail3 = margin + heightOfBubble - tailHeight;
            float radiusRight;
            float radiusLeft;
            if (direction == RIGHT) {
                radiusRight = 0;
                radiusLeft = radiusConner;
                xTail1 = margin + widthOfBubble + tailWidth;
                xTail2 = margin + widthOfBubble;
                xTail3 = margin + widthOfBubble;
                bubbleRect.set(margin, margin, margin + widthOfBubble, margin + heightOfBubble);
                path.moveTo(xTail1, yTail1);
                path.lineTo(xTail2, yTail2);
                path.lineTo(xTail3, yTail3);
            } else if (direction == LEFT) {
                radiusRight = radiusConner;
                radiusLeft = 0;
                xTail1 = margin;
                xTail2 = margin + tailWidth;
                xTail3 = margin + tailWidth;
                bubbleRect.set(margin + tailWidth, margin, margin + tailWidth + widthOfBubble, margin + heightOfBubble);
                path.moveTo(xTail1, yTail1);
                path.lineTo(xTail2, yTail2);
                path.lineTo(xTail3, yTail3);
            } else {
                radiusRight = radiusConner;
                radiusLeft = radiusConner;
                if (direction == RIGHT_HIDE) {
                    bubbleRect.set(margin, margin, margin + widthOfBubble, margin + heightOfBubble);
                } else {
                    bubbleRect.set(margin + tailWidth, margin, margin + tailWidth + widthOfBubble, margin + heightOfBubble);
                }
            }
            path.addRoundRect(bubbleRect, new float[]{radiusConner, radiusConner, radiusConner, radiusConner, radiusRight, radiusRight, radiusLeft, radiusLeft}, Path.Direction.CW);
            path.close();
            paint.setShadowLayer(shadow, shadow / 2f, shadow / 2f, backgroundShadowColor);
            canvas.drawPath(path, paint);
        }
        super.draw(canvas);
    }

    public void setTailDirection(@TailDirection int direction) {
        this.direction = direction;
        resetPadding();
        postInvalidate();
    }
}
