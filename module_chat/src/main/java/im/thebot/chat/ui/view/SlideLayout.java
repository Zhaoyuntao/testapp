package im.thebot.chat.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.turbo.basetools.vibrate.VibratorUtil;

/**
 * created by zhaoyuntao
 * on 2020-03-25
 * description:
 */
public class SlideLayout extends ViewGroup {

    private final int touchSlop;
    private final long longClickTimeOut;
    private final Scroller scrollerChild;
    private final Scroller scroller;

    private View childView, hideTailView;
    private SlideListener listener;

    private float xDown;
    private long timeDown;
    private boolean isLongClicked;
    private boolean isDistanceOver;
    private boolean isTimeOut;

    private boolean hasPostSlideTouchBorder;
    private final boolean slideRightDirection;
    private int scrollRange;
    private OnLongClickListener longClickListener;
    private OnClickListener clickListener;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        scrollerChild = new Scroller(context);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        longClickTimeOut = ViewConfiguration.getLongPressTimeout();
        setWillNotDraw(false);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideLayout);
            slideRightDirection = typedArray.getInt(R.styleable.SlideLayout_SlideLayout_slideDirection, 0) == 1;
            typedArray.recycle();
        } else {
            slideRightDirection = false;
        }
    }

    @Override
    public SlideLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SlideLayoutParams(getContext(), attrs);
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener longClickListener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        this.longClickListener = longClickListener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener clickListener) {
        if (!isClickable()) {
            setClickable(true);
        }
        this.clickListener = clickListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("Please put two children in this layout");
        }
        hideTailView = getChildAt(0);
        childView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("You can only put two children in this layout");
        }
        int sizeWidthMax = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeightMax = MeasureSpec.getSize(heightMeasureSpec);
        int sizeHeight = 0;
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            SlideLayoutParams childLayoutParams = (SlideLayoutParams) child.getLayoutParams();
            final int childWidthMeasureSpec;
            if (childLayoutParams.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidthMax, MeasureSpec.EXACTLY);
            } else {
                childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingStart + paddingEnd + childLayoutParams.leftMargin + childLayoutParams.rightMargin, childLayoutParams.width);
            }
            final int childHeightMeasureSpec;
            if (childLayoutParams.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                final int childMaxWidth = Math.max(0, sizeHeightMax - paddingTop - paddingBottom - childLayoutParams.topMargin - childLayoutParams.bottomMargin);
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childMaxWidth, MeasureSpec.EXACTLY);
            } else {
                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingBottom + paddingBottom + childLayoutParams.topMargin + childLayoutParams.bottomMargin, childLayoutParams.height);
            }

            measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec);
            if (child.getMeasuredHeight() > sizeHeight) {
                sizeHeight = child.getMeasuredHeight();
            }
        }
        sizeHeight += (paddingTop + paddingBottom);
        if (sizeHeightMax > 0) {
            if (sizeHeight > sizeHeightMax || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                sizeHeight = sizeHeightMax;
            }
        }
        setMeasuredDimension(sizeWidthMax, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        SlideLayoutParams layoutParams = (SlideLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        final int layoutDirection = getLayoutDirection();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int widthView = getMeasuredWidth();
        final int absoluteGravity = Gravity.getAbsoluteGravity(layoutParams.gravity, layoutDirection);
        int left;
        int right = widthView - layoutParams.rightMargin - paddingEnd;
        if ((absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT) {
            left = paddingStart + layoutParams.leftMargin;
        } else {
            left = right - childWidth;
        }
        int top = paddingTop + layoutParams.topMargin;
        int bottom = top + childHeight;
        childView.layout(left, top, right, bottom);

        SlideLayoutParams tailLayoutParams = (SlideLayoutParams) hideTailView.getLayoutParams();
        int leftFollow = slideRightDirection ? (-hideTailView.getMeasuredWidth() - tailLayoutParams.getMarginEnd()) : (getMeasuredWidth() + tailLayoutParams.getMarginStart());
        int rightFollow = leftFollow + hideTailView.getMeasuredWidth();
        int topFollow = (int) ((getMeasuredHeight() - hideTailView.getMeasuredHeight()) / 2f);
        int bottomFollow = topFollow + hideTailView.getMeasuredHeight();
        hideTailView.layout(leftFollow, topFollow, rightFollow, bottomFollow);
        scrollRange = tailLayoutParams.getMarginStart() + hideTailView.getMeasuredWidth() + tailLayoutParams.getMarginEnd();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        long now = SystemClock.elapsedRealtime();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                timeDown = now;
                isDistanceOver = false;
                isTimeOut = false;
                isLongClicked = false;
                scroller.abortAnimation();
                scrollerChild.abortAnimation();
                scrollTo(0, 0);
                childView.scrollTo(0, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (canSlide() && Math.abs(event.getRawX() - xDown) > touchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long now = SystemClock.elapsedRealtime();
        int leftBorder = slideRightDirection ? 0 : -scrollRange;
        int rightBorder = slideRightDirection ? scrollRange : 0;
        int moveDistance = (int) (event.getRawX() - xDown);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                isDistanceOver = isDistanceOver || Math.abs(moveDistance) > touchSlop;
//                S.s("moveDistance:" + moveDistance + "  touchSlop:" + touchSlop + " isDistanceOver:" + isDistanceOver);
                isTimeOut = (now - timeDown) > longClickTimeOut;
                if (!isLongClicked && canSlide() && isDistanceOver) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (slideRightDirection) {
                        if (moveDistance < leftBorder) {
                            onTouchedStartBorder(leftBorder);
                        } else if (moveDistance > rightBorder) {
                            onTouchedEndBorder(rightBorder, moveDistance);
                        } else {
                            onSliding(moveDistance, scrollRange);
                        }
                    } else {
                        if (moveDistance > rightBorder) {
                            onTouchedStartBorder(rightBorder);
                        } else if (moveDistance < leftBorder) {
                            onTouchedEndBorder(leftBorder, moveDistance);
                        } else {
                            onSliding(moveDistance, scrollRange);
                        }
                    }
                } else if (!isDistanceOver && !isLongClicked && isTimeOut && longClickListener != null) {
                    isLongClicked = true;
                    boolean handled = longClickListener.onLongClick(this);
                    if (handled) {
                        VibratorUtil.vibrate(getContext());
                    }
                    return handled;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isLongClicked && clickListener != null) {
                    clickListener.onClick(this);
                }
            case MotionEvent.ACTION_CANCEL:
                hasPostSlideTouchBorder = false;
                isDistanceOver = false;
                isTimeOut = false;
                isLongClicked = false;
                int scrollX = getScrollX();
                int scrollXChild = childView.getScrollX();
                scroller.startScroll(scrollX, 0, -scrollX, 0);
                scrollerChild.startScroll(scrollXChild, 0, -scrollXChild, 0);
                invalidate();
                childView.invalidate();
                if (listener != null) {
                    if (slideRightDirection) {
                        listener.onFingerUp(moveDistance > rightBorder);
                    } else {
                        listener.onFingerUp(moveDistance < leftBorder);
                    }
                    listener.onSlideDistanceChange(0);
                }
        }
        return super.onTouchEvent(event);
    }

    private void onTouchedStartBorder(int border) {
        scrollTo(-border, 0);
        childView.scrollTo(0, 0);
        if (listener != null) {
            listener.onSlideDistanceChange(0);
            changeTailAlpha(0);
        }
    }

    private void onSliding(int moveDistance, int scrollRange) {
        scrollTo(-moveDistance, 0);
        childView.scrollTo(0, 0);
        if (listener != null) {
            float percent = Math.abs(getScrollX()) / (float) scrollRange;
            listener.onSlideDistanceChange(percent);
            changeTailAlpha(percent);
        }
    }

    private void onTouchedEndBorder(int border, int moveDistance) {
        if (!hasPostSlideTouchBorder) {
            hasPostSlideTouchBorder = true;
            scrollTo(-border, 0);
            VibratorUtil.vibrate(getContext());
            if (listener != null) {
                listener.onSlideDistanceChange(1);
                listener.onSlideTouchedBorder();
                changeTailAlpha(1);
            }
        }
        childView.scrollTo(-((moveDistance - border) / 2), 0);
    }

    private void changeTailAlpha(float percent) {
        hideTailView.setAlpha(percent);
    }

    private boolean canSlide() {
        if (listener != null) {
            return listener.canSlide();
        } else {
            return false;
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
            float percent = Math.abs(scroller.getCurrX()) / (float) scrollRange;
            changeTailAlpha(percent);
        }
        if (scrollerChild.computeScrollOffset()) {
            childView.scrollTo(scrollerChild.getCurrX(), scrollerChild.getCurrY());
            childView.invalidate();
        }
    }

    public void setSlideListener(SlideListener listener) {
        this.listener = listener;
    }

    public interface SlideListener {
        default void onSlideTouchedBorder() {
        }

        void onFingerUp(boolean slideToRight);

        void onSlideDistanceChange(float percent);

        boolean canSlide();
    }

    public static class SlideLayoutParams extends LinearLayout.LayoutParams {
        public SlideLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }
}
