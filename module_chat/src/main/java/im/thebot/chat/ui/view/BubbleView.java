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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.ResourceUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 16/08/2021
 * description:
 */
public class BubbleView extends ChatViewGroup {
    private Path path;
    private Paint paint;
    private RectF bubbleRect;
    private RectF tailRect;
    private float radiusConner;
    private int tailWidth, tailHeight;
    private int gravity;
    private boolean showTail;
    private int shadow;
    private int backgroundColor;
    private int backgroundColorPressed;
    private int backgroundShadowColor;
    private int radiusTail;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

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
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG);
        radiusConner = UiUtils.dipToPx(12);
        radiusTail = UiUtils.dipToPx(2);
        tailWidth = UiUtils.dipToPx(10);
        tailHeight = UiUtils.dipToPx(12);
        shadow = UiUtils.dipToPx(1);
        backgroundShadowColor = Color.argb(20, 0, 0, 0);

        resetBackgroundColor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int maxWidth = 0;
        int maxHeight = 0;
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int measureCount = 0;
//        S.v(true, "BubbleView.onMeasure: --------------------------------------------------------> ", widthMeasureSpec, heightMeasureSpec, this);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                lp.width = LayoutParams.WRAP_CONTENT;
//                S.s(true, ">>>>>> [" + child.getClass().getSimpleName() + "].measure  -------> " + lp.width + " " + lp.height);
                measureChildWithMargins(child, widthMeasureSpec, heightMeasureSpec, lp);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
//                S.s(true, ">>>>>> [" + child.getClass().getSimpleName() + "].measure result w:" + childWidth + " h:" + childHeight + "  leftMargin:" + lp.leftMargin + " rightMargin:" + lp.rightMargin);
                maxWidth = Math.max(maxWidth, childWidth + lp.leftMargin + lp.rightMargin);
                maxHeight += (childHeight + lp.topMargin + lp.bottomMargin);
                measureCount++;
            }
        }
//        S.s(true, "first finish --------------------------------------------------------------------------------------------------------------- result: w:" + maxWidth + " h:" + maxHeight);
        maxWidth += (paddingStart + paddingEnd);
        int height;
        if (measureCount <= 1) {
            height = maxHeight;
        } else {
            height = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child != null && child.getVisibility() != GONE) {
                    MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                    lp.width = LayoutParams.MATCH_PARENT;
//                S.s(true, ">>>>>> [" + child.getClass().getSimpleName() + "].measure  -------> " + lp.width + " " + lp.height);
                    int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
                    measureChildWithMargins(child, childWidthMeasureSpec, heightMeasureSpec, lp);
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
//                S.s(true, "second >>>>>> [" + child.getClass().getSimpleName() + "].measure result w:" + childWidth + " h:" + childHeight + "  leftMargin:" + lp.leftMargin + " rightMargin:" + lp.rightMargin);
                    height += (childHeight + lp.topMargin + lp.bottomMargin);
                }
            }
        }
        height += (paddingTop + paddingBottom);
//        S.s(true, "second finish ------------------------------------------------- result: w:" + maxWidth + " h:" + height);
        setMeasuredDimension(maxWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int viewTop = paddingTop;
//        S.s("BubbleView.onLayout: -----------------------------------------------------------------------------------------> l:" + l + " r:" + r);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int left = paddingStart + lp.getMarginStart();
                int right = left + child.getMeasuredWidth();
                int top = viewTop + lp.topMargin;
                int bottom = top + child.getMeasuredHeight();
//                S.e(true, "[" + child.getClass().getSimpleName() + "].layout: left:" + left + "  right:" + right + "    BubbleView.paddingStart:" + getPaddingStart() + " paddingEnd:" + getPaddingEnd());
                child.layout(left, top, right, bottom);
                viewTop = bottom + lp.bottomMargin + paddingBottom;
            }
        }
    }

    private void resetBackgroundColor() {
        boolean isSelfBubble;
        int gravity = Gravity.getAbsoluteGravity(this.gravity, getLayoutDirection());
        if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            isSelfBubble = gravity == Gravity.LEFT;
        } else {
            isSelfBubble = gravity == Gravity.RIGHT;
        }
        if (isSelfBubble) {
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
        canvas.save();
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.drawPath(path, paint);
        canvas.restore();
        super.draw(canvas);
    }

    public void setPath(int width, int height) {
        if (width > 0 && height > 0) {
            int gravity = Gravity.getAbsoluteGravity(this.gravity, getLayoutDirection());
            int widthOfBubble = width - tailWidth;
            int heightOfBubble = height;
            path.reset();

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
//            S.s("gravity:" + gravity);
            if (showTail) {
                if (gravity == Gravity.RIGHT) {
                    bubbleLeft = 0;
                    bubbleRight = bubbleLeft + widthOfBubble;
                    radiusRight = 0;
                    radiusLeft = radiusConner;
                    xTail1 = width - tailWidth;
                    xTail2 = xTail1 + calculatePoint2X(tailWidth, radiusTail);
                    xTail3 = xTail1;
                    path.moveTo(xTail1, yTail1);
                    path.lineTo(xTail2, yTail2);
                    tailRect.set(xTail2 - radiusTail, yTail2, xTail2 + radiusTail, yTail2 + radiusTail * 2);
                    path.arcTo(tailRect, -90, 135);
                    path.lineTo(xTail3, yTail3);
                } else if (gravity == Gravity.LEFT) {
                    bubbleLeft = tailWidth;
                    bubbleRight = bubbleLeft + widthOfBubble;
                    radiusRight = radiusConner;
                    radiusLeft = 0;
                    xTail1 = tailWidth;
                    xTail2 = xTail1 - calculatePoint2X(tailWidth, radiusTail);
                    xTail3 = xTail1;
                    bubbleRect.set(tailWidth, 0, tailWidth + widthOfBubble, heightOfBubble);
                    path.moveTo(xTail1, yTail1);
                    path.lineTo(xTail2, yTail2);
                    tailRect.set(xTail2 - radiusTail, yTail2, xTail2 + radiusTail, yTail2 + radiusTail * 2);
                    path.arcTo(tailRect, -90, -135);
                    path.lineTo(xTail3, yTail3);
                } else {
                    bubbleLeft = tailWidth;
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
                    bubbleLeft = tailWidth;
                    bubbleRight = bubbleLeft + widthOfBubble;
                } else {
                    bubbleLeft = tailWidth;
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
            return super.getPaddingStart() + tailWidth;
        } else if (gravity == Gravity.RIGHT) {
            return super.getPaddingStart();
        } else {
            return super.getPaddingStart() + tailWidth;
        }
    }

    @Override
    public int getPaddingEnd() {
        int gravity = Gravity.getAbsoluteGravity(this.gravity, getLayoutDirection());
        if (gravity == Gravity.LEFT) {
            return super.getPaddingEnd();
        } else if (gravity == Gravity.RIGHT) {
            return super.getPaddingEnd() + tailWidth;
        } else {
            return super.getPaddingEnd() + tailWidth;
        }
    }

    private int calculatePoint2X(int tailWidth, int radiusTail) {
        return (int) (tailWidth - Math.tan(Math.toRadians(67.5f)) * radiusTail);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            drawable = getForeground();
        }
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

    final public int getGravity() {
        return gravity;
    }

    final public void setGravity(int gravity, boolean showTail) {
        this.gravity = gravity;
        this.showTail = showTail;
        postInvalidate();
    }
}
