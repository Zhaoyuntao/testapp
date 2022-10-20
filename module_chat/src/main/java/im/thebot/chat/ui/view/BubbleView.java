package im.thebot.chat.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 16/08/2021
 * description:
 */
public class BubbleView extends ChatViewGroup {
    private final Path path;
    private final Paint paint;
    private final RectF bubbleRect;
    private final RectF tailRect;
    private final float radiusConner;
    private final int tailWidth, tailMargin;
    private int gravity;
    private boolean showTail;
    private final int shadow;
    private int backgroundColor;
    private int backgroundColorPressed;
    private final int backgroundShadowColor;
    private final int radiusTail;
    private final PaintFlagsDrawFilter paintFlagsDrawFilter;
    private final float angleOfTail;
    private boolean needDrawBubble;
    private int maxWidth;

    public BubbleView(@NonNull Context context) {
        super(context);
        setClickable(true);
        setWillNotDraw(false);
        path = new Path();
        paint = new Paint();
        bubbleRect = new RectF();
        tailRect = new RectF();
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG);
        radiusConner = UiUtils.dipToPx(6);
        radiusTail = UiUtils.dipToPx(1.6f);
        tailWidth = UiUtils.dipToPx(10);
        tailMargin = UiUtils.dipToPx(6);
        shadow = UiUtils.dipToPx(1);
        backgroundShadowColor = Color.argb(20, 0, 0, 0);
        angleOfTail = 47;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int selfMaxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (maxWidth > 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(selfMaxWidth, maxWidth), mode);
            selfMaxWidth = Math.min(selfMaxWidth, maxWidth);
        }
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int maxHeight = 0;
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int measureCount = 0;
        boolean useMaxWidth = mode != MeasureSpec.AT_MOST;
        int childMaxWidth = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (mode == MeasureSpec.EXACTLY) {
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
                measureChildWithMargins(child, widthMeasureSpec, heightMeasureSpec, lp);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                childMaxWidth = Math.max(childMaxWidth, childWidth + lp.leftMargin + lp.rightMargin);
                maxHeight += (childHeight + lp.topMargin + lp.bottomMargin);
                measureCount++;
            }
        }
        childMaxWidth = childMaxWidth + (paddingStart + paddingEnd);
        if (maxWidth > 0) {
            childMaxWidth = Math.min(childMaxWidth, maxWidth);
        }
        int height;
        if (useMaxWidth || measureCount <= 1) {
            height = maxHeight;
        } else {
            height = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child != null && child.getVisibility() != GONE) {
                    MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childMaxWidth, MeasureSpec.EXACTLY);
                    measureChildWithMargins(child, childWidthMeasureSpec, heightMeasureSpec, lp);
                    int childHeight = child.getMeasuredHeight();
                    height += (childHeight + lp.topMargin + lp.bottomMargin);
                }
            }
        }
        height += (paddingTop + paddingBottom);
        setMeasuredDimension(useMaxWidth ? selfMaxWidth : childMaxWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int viewTop = paddingTop;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int left = paddingStart + lp.getMarginStart();
                int right = left + child.getMeasuredWidth();
                int top = viewTop + lp.topMargin;
                int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
                viewTop = bottom + lp.bottomMargin + paddingBottom;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (Gravity.getAbsoluteGravity(gravity, getLayoutDirection()) != Gravity.CENTER && needDrawBubble) {
            int width = getWidth();
            int height = getHeight();
            setPath(width, height);
            paint.reset();
            //Disable click state for temporary.
//            paint.setColor(isPressed() ? backgroundColorPressed : backgroundColor);
            paint.setColor(backgroundColor);
            paint.setShadowLayer(shadow, shadow / 2f, shadow / 2f, backgroundShadowColor);
            canvas.save();
            canvas.setDrawFilter(paintFlagsDrawFilter);
            canvas.drawPath(path, paint);
            canvas.restore();
        }
        super.draw(canvas);
    }

    public void setPath(int width, int height) {
        if (width > 0 && height > 0) {
            int gravity = Gravity.getAbsoluteGravity(this.gravity, getLayoutDirection());
            int widthOfBubble = width - ((gravity == Gravity.LEFT || gravity == Gravity.RIGHT) ? (tailMargin + tailWidth) : 0);
            int heightOfBubble = height;
            path.reset();
            float tailHeight = (float) Math.tan(Math.toRadians(angleOfTail)) * tailWidth;
            float xTail1;
            float xTail2;
            float xTail3;
            float yTail1 = 0;
            float yTail2 = 0;
            float yTail3 = tailHeight;
            float radiusRight;
            float radiusLeft;

            int bubbleLeft;
            int bubbleRight;
            if (showTail) {
                if (gravity == Gravity.RIGHT) {
                    bubbleLeft = 0;
                    bubbleRight = bubbleLeft + widthOfBubble;
                    radiusRight = 0;
                    radiusLeft = radiusConner;
                    xTail1 = width - tailWidth - tailMargin;
                    xTail2 = xTail1 + calculatePoint2X(tailWidth, radiusTail);
                    xTail3 = xTail1;
                    path.moveTo(xTail1, yTail1);
                    path.lineTo(xTail2, yTail2);
                    tailRect.set(xTail2 - radiusTail, yTail2, xTail2 + radiusTail, yTail2 + radiusTail * 2);
                    path.arcTo(tailRect, -90, 90 + angleOfTail);
                    path.lineTo(xTail3, yTail3);
                } else if (gravity == Gravity.LEFT) {
                    bubbleLeft = tailWidth + tailMargin;
                    bubbleRight = bubbleLeft + widthOfBubble;
                    radiusRight = radiusConner;
                    radiusLeft = 0;
                    xTail1 = tailWidth + tailMargin;
                    xTail2 = xTail1 - calculatePoint2X(tailWidth, radiusTail);
                    xTail3 = xTail1;
                    bubbleRect.set(tailWidth, 0, tailWidth + widthOfBubble, heightOfBubble);
                    path.moveTo(xTail1, yTail1);
                    path.lineTo(xTail2, yTail2);
                    tailRect.set(xTail2 - radiusTail, yTail2, xTail2 + radiusTail, yTail2 + radiusTail * 2);
                    path.arcTo(tailRect, -90, -90 - angleOfTail);
                    path.lineTo(xTail3, yTail3);
                } else {
                    bubbleLeft = 0;
                    bubbleRight = bubbleLeft + widthOfBubble;

                    radiusRight = radiusConner;
                    radiusLeft = radiusConner;
                    bubbleRect.set(0, 0, widthOfBubble, heightOfBubble);
                }
            } else {
                radiusRight = radiusConner;
                radiusLeft = radiusConner;
                if (gravity == Gravity.RIGHT) {
                    bubbleLeft = 0;
                    bubbleRight = bubbleLeft + widthOfBubble;
                } else if (gravity == Gravity.LEFT) {
                    bubbleLeft = tailWidth + tailMargin;
                    bubbleRight = bubbleLeft + widthOfBubble;
                } else {
                    bubbleLeft = tailWidth + tailMargin;
                    bubbleRight = bubbleLeft + widthOfBubble - tailWidth;
                }
            }
            int bubbleTop = 0;
            int bubbleBottom = bubbleTop + heightOfBubble;
            bubbleRect.set(bubbleLeft, bubbleTop, bubbleRight, bubbleBottom);
            path.addRoundRect(bubbleRect, new float[]{radiusLeft, radiusLeft, radiusRight, radiusRight, radiusConner, radiusConner, radiusConner, radiusConner}, Path.Direction.CW);
            path.close();
        }
    }

    @Override
    public int getPaddingStart() {
        int gravity = Gravity.getAbsoluteGravity(this.gravity, getLayoutDirection());
        if (gravity == Gravity.LEFT) {
            return super.getPaddingStart() + tailWidth + tailMargin;
        } else if (gravity == Gravity.RIGHT) {
            return super.getPaddingStart();
        } else {
            return super.getPaddingStart() + tailWidth + tailMargin;
        }
    }

    @Override
    public int getPaddingEnd() {
        int gravity = Gravity.getAbsoluteGravity(this.gravity, getLayoutDirection());
        if (gravity == Gravity.LEFT) {
            return super.getPaddingEnd();
        } else if (gravity == Gravity.RIGHT) {
            return super.getPaddingEnd() + tailWidth + tailMargin;
        } else {
            return super.getPaddingEnd() + tailWidth + tailMargin;
        }
    }

    private int calculatePoint2X(int tailWidth, int radiusTail) {
        return (int) (tailWidth - Math.tan(Math.toRadians((180 - angleOfTail) / 2)) * radiusTail);
    }

    final public int getGravity() {
        return gravity;
    }

    final public void setBubbleGravity(int gravity, boolean needDrawBubble) {
        this.gravity = gravity;
        this.needDrawBubble = needDrawBubble;
    }

    final public void setBubbleColor(int backgroundColor, int backgroundColorPressed, boolean showTail) {
        this.backgroundColor = backgroundColor;
        this.backgroundColorPressed = backgroundColorPressed;
        this.showTail = showTail;
        postInvalidate();
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }
}
