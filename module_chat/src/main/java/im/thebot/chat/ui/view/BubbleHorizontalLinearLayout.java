package im.thebot.chat.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import im.turbo.utils.log.S;

import java.lang.reflect.Field;


/**
 * created by zhaoyuntao
 * on 14/03/2023
 */
public class BubbleHorizontalLinearLayout extends LinearLayout {

    public BubbleHorizontalLinearLayout(Context context) {
        super(context);
        init();
    }

    public BubbleHorizontalLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleHorizontalLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() <= 1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = 0;
        View bubbleLayoutChild = null;
        int childCount = getChildCount();
        int allChildWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                LayoutParams lpOrigin = (LayoutParams) child.getLayoutParams();
                if (lpOrigin instanceof BubbleHorizontalLayoutParams) {
                    bubbleLayoutChild = child;
                } else {
                    measureChildWithMargins(child, widthMeasureSpec, heightMeasureSpec, lpOrigin);
                    allChildWidth += (child.getMeasuredWidth() + lpOrigin.leftMargin + lpOrigin.rightMargin);
                    measureHeight = Math.max(measureHeight, child.getMeasuredHeight() + lpOrigin.topMargin + lpOrigin.bottomMargin);
                }
            }
        }

        if (bubbleLayoutChild != null) {
            int restSpace = Math.max(0, maxWidth - allChildWidth);
            ViewGroup.LayoutParams lpOrigin = bubbleLayoutChild.getLayoutParams();
            BubbleHorizontalLayoutParams lp = (BubbleHorizontalLayoutParams) lpOrigin;
            int childWanted = lp.maxWidth + lp.getMarginStart() + lp.getMarginEnd();
            int parentWidthMeasureSize = lp.maxWidth > 0 ? Math.min(restSpace, childWanted) : restSpace;
            int parentWidthMeasureMode = lp.width == ViewGroup.LayoutParams.MATCH_PARENT ? MeasureSpec.EXACTLY : MeasureSpec.AT_MOST;
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidthMeasureSize, parentWidthMeasureMode);
            measureChildWithMargins(bubbleLayoutChild, childWidthMeasureSpec, heightMeasureSpec, lp);
            measureHeight = Math.max(measureHeight, bubbleLayoutChild.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
        }
        measureHeight += (paddingTop + paddingBottom);
        setMeasuredDimension(maxWidth, measureHeight);
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSize, int parentHeightMeasureSize, MarginLayoutParams lp) {
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSize, getPaddingStart() + getPaddingEnd() + lp.getMarginStart() + lp.getMarginEnd(), lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSize, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() <= 1) {
            super.onLayout(changed, left, top, right, bottom);
            return;
        }
        final int count = getChildCount();
        int mTotalLength = 0;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != GONE) {
                final int childWidth = view.getMeasuredWidth();
                final LayoutParams lp = (LayoutParams) view.getLayoutParams();
                mTotalLength += (childWidth + lp.getMarginStart() + lp.getMarginEnd());
            }
        }
        final boolean isLayoutRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        final int paddingTop = getPaddingTop();

        int gravity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            gravity = getGravity();
        } else {
            try {
                @SuppressLint("DiscouragedPrivateApi")
                Field staticField = LinearLayout.class.getDeclaredField("mGravity");
                staticField.setAccessible(true);
                gravity = staticField.getInt(this);
            } catch (NoSuchFieldException | IllegalArgumentException |
                     IllegalAccessException ignore) {
                gravity = Gravity.START | Gravity.CENTER;
            }
        }

        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        final int height = bottom - top;
        int childBottom = height - paddingBottom;
        int childSpace = height - paddingTop - paddingBottom;
        final int majorGravity = gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        final int minorGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        final int layoutDirection = getLayoutDirection();
        int x;
        switch (Gravity.getAbsoluteGravity(majorGravity, layoutDirection)) {
            case Gravity.RIGHT:
                x = getMeasuredWidth() - mTotalLength - paddingRight;
                break;
            case Gravity.CENTER_HORIZONTAL:
                x = (getMeasuredWidth() - mTotalLength) / 2;
                break;
            case Gravity.LEFT:
            default:
                x = paddingLeft;
                break;
        }
        int start = 0;
        int dir = 1;
        if (isLayoutRtl) {
            start = count - 1;
            dir = -1;
        }
        for (int i = 0; i < count; i++) {
            final int childIndex = start + dir * i;
            final View child = getChildAt(childIndex);
            if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childGravity = lp.gravity;
                if (childGravity < 0) {
                    childGravity = minorGravity;
                }
                int childTop;
                switch (childGravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        childTop = paddingTop + lp.topMargin;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = paddingTop + ((childSpace - childHeight) / 2) + lp.topMargin - lp.bottomMargin;
                        break;
                    case Gravity.BOTTOM:
                        childTop = childBottom - childHeight - lp.bottomMargin;
                        break;
                    default:
                        childTop = paddingTop;
                        break;
                }
                x += lp.leftMargin;
                child.layout(x, childTop, x + childWidth, childTop + childHeight);
                x += childWidth + lp.rightMargin;
            }
        }
    }

    public static class BubbleHorizontalLayoutParams extends LinearLayout.LayoutParams {
        private final int maxWidth;

        public BubbleHorizontalLayoutParams(int maxWidth, boolean useMaxWidth) {
            super(useMaxWidth ? MATCH_PARENT : WRAP_CONTENT, WRAP_CONTENT);
            this.maxWidth = maxWidth;
        }
    }
}
