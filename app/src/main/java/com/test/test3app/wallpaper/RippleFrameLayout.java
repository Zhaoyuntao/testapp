package com.test.test3app.wallpaper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.turbo.baseui.utils.UiUtils;
import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 21/12/2021
 * description:
 */
public class RippleFrameLayout extends ViewGroup {
    private final long duration = 300;
    private final long durationChild = 300;
    private final long durationChildOffset = 30;
    private final float percentChildMin = 0.3f;
    private final float percentChildMax = 1f;
    private float percentChild = percentChildMax;
    private ValueAnimator valueAnimator;
    private float percent = 1;
    private float radius;
    private float childMargin;
    private float xScalePosition, yScalePosition;
    private int widthMin;
    private final Path path = new Path();

    public RippleFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public RippleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), UiUtils.dipToPx(30));
            }
        });
        widthMin = UiUtils.dipToPx(100);
        childMargin = UiUtils.dipToPx(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        layout(l, t, r, b);
        S.s("l:" + l + " t:" + t + " r:" + r + " b:" + b);
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = r - l - paddingStart - paddingEnd;
        int countHorizontal = width / widthMin;
        int count = getChildCount();
        int height = b - t;
        xScalePosition = width * 0.7f;
        yScalePosition = height;
        radius = (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));

        int wChildSpace = (int) (width / (float) countHorizontal);
        int hChildSpace = wChildSpace;

        S.s("count:" + count + " countHorizontal:" + countHorizontal);
        S.lll();
        int wChild = (int) ((wChildSpace - childMargin * 2) * percentChild);
        int hChild = wChild;
        int xChildStartSpace = (int) ((wChildSpace - wChild) / 2f);
        int yChildStartSpace = (int) ((hChildSpace - hChild) / 2f);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int xChild = paddingStart + (i % countHorizontal) * wChildSpace + xChildStartSpace;
            int yChild = paddingTop + (i / countHorizontal) * hChildSpace + yChildStartSpace;
            S.s("[" + i + "]child:x:" + xChild + " y:" + yChild + " w:" + wChildSpace + " h:" + hChildSpace);
            child.layout(xChild, yChild, xChild + wChild, yChild + hChild);
        }
    }

    public void startAnimation(boolean open) {
        valueAnimator = open ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (open) {
                    setVisible(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!open) {
                    setVisible(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ValueAnimator valueAnimator2 = open ? ValueAnimator.ofFloat(percentChildMin, percentChildMax) : ValueAnimator.ofFloat(percentChildMax, percentChildMin);
        valueAnimator2.setInterpolator(open ? new OvershootInterpolator() : new DecelerateInterpolator());
        valueAnimator2.setDuration(durationChild);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percentChild = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        valueAnimator2.start();
        valueAnimator.start();

    }

    private boolean isAnimationRunning() {
        return (valueAnimator != null && valueAnimator.isRunning());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.reset();
        path.addCircle(xScalePosition, yScalePosition, radius * percent, Path.Direction.CCW);
        canvas.clipPath(path);
        canvas.drawColor(Color.WHITE);
        super.onDraw(canvas);
    }

    @Override
    public void setVisibility(int visibility) {
        if (isAnimationRunning()) {
            return;
        }
        int visibilityOld = getVisibility();
        if (visibilityOld == visibility) {
            return;
        }
        startAnimation(visibility == VISIBLE);
    }

    private void setVisible(int visible) {
        super.setVisibility(visible);
    }

}
