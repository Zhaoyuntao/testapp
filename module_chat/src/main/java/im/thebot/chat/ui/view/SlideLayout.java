package im.thebot.chat.ui.view;

import android.content.Context;
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

    private float x, xDown, xLast;
    private int rightBorder;
    private long timeDown;
    private boolean isLongClicking;
    private boolean isDistanceOver;
    private boolean isTimeOut;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        scrollerChild = new Scroller(context);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        longClickTimeOut = ViewConfiguration.getLongPressTimeout();
        setWillNotDraw(false);
    }

    @Override
    public SlideLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SlideLayoutParams(getContext(), attrs);
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
        int leftMain;
        int rightMain = widthView - layoutParams.rightMargin - paddingEnd;
        if ((absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT) {
            leftMain = paddingStart + layoutParams.leftMargin;
        } else {
            leftMain = rightMain - childWidth;
        }
        int topMain = paddingTop + layoutParams.topMargin;
        int bottomMain = topMain + childHeight;
        childView.layout(leftMain, topMain, rightMain, bottomMain);

        SlideLayoutParams tailLayoutParams = (SlideLayoutParams) hideTailView.getLayoutParams();
        int leftFollow = -hideTailView.getMeasuredWidth() - tailLayoutParams.getMarginEnd();
        int rightFollow = -tailLayoutParams.getMarginEnd();
        int topFollow = (int) ((getMeasuredHeight() - hideTailView.getMeasuredHeight()) / 2f);
        int bottomFollow = topFollow + hideTailView.getMeasuredHeight();
        hideTailView.layout(leftFollow, topFollow, rightFollow, bottomFollow);
        rightBorder = hideTailView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                xLast = xDown;
                break;
            case MotionEvent.ACTION_MOVE:
                x = ev.getRawX();
                xLast = x;
                if (canSlide() && Math.abs(x - xDown) > touchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long now = SystemClock.elapsedRealtime();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDistanceOver = false;
                isTimeOut = false;
                isLongClicking = false;
                timeDown = now;
                xLast = xDown;
                return true;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                isDistanceOver = isDistanceOver || Math.abs(x - xDown) > touchSlop;
                isTimeOut = (now - timeDown) > longClickTimeOut;
                if (canSlide() && !isLongClicking && isDistanceOver) {
                    int scrolledXDistance = (int) (xLast - x);
                    if (getScrollX() + scrolledXDistance > 0) {
                        scrollTo(0, 0);
                        childView.scrollTo(0, 0);
                        if (listener != null) {
                            listener.onSlideDistanceChange(0);
                            changeTailAlpha(0);
                        }
                    } else if (getScrollX() + scrolledXDistance < -rightBorder || childView.getScrollX() < 0) {
                        if (getScrollX() != -rightBorder) {
                            scrollTo(-rightBorder, 0);
                            VibratorUtil.vibrate(getContext());
                            if (listener != null) {
                                listener.onSlideDistanceChange(1);
                                listener.onSlideToRight();
                                changeTailAlpha(1);
                            }
                        }
                        childView.scrollBy(scrolledXDistance / 2, 0);
                    } else {
                        scrollBy(scrolledXDistance, 0);
                        childView.scrollTo(0, 0);
                        if (listener != null) {
                            float percent = Math.abs(getScrollX()) / (float) Math.abs(rightBorder);
                            listener.onSlideDistanceChange(percent);
                            changeTailAlpha(percent);
                        }
                    }
                    xLast = x;
                } else if (!isDistanceOver && !isLongClicking && isTimeOut) {
                    isLongClicking = true;
                    performLongClick();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isDistanceOver && !isTimeOut) {
                    performClick();
                }
                isDistanceOver = false;
                isTimeOut = false;
                isLongClicking = false;
                int scrollX = getScrollX();
                int scrollXMain = childView.getScrollX();
                scroller.startScroll(scrollX, 0, -scrollX, 0);
                scrollerChild.startScroll(scrollXMain, 0, -scrollXMain, 0);
                invalidate();
                childView.invalidate();
                if (listener != null) {
                    listener.onSlideDistanceChange(0);
                    changeTailAlpha(0);
                    listener.onFingerUp(getScrollX() <= -rightBorder || childView.getScrollX() < 0);
                }
                break;
        }
        return super.onTouchEvent(event);
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
        default void onSlideToRight() {
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
