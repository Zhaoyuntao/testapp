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
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.test3app.fastrecordviewnew.UiUtils;
import com.zhaoyuntao.androidutils.tools.S;

/**
 * created by zhaoyuntao
 * on 21/12/2021
 * description:
 */
public class RippleFrameLayout extends LinearLayout {
    private float percent = 1;
    private float radius;
    private final long duration = 300;
    private float x, y;

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
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = r - l;
        int height = b - t;
        x = width * 0.7f;
        y = height;
        radius = (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
    }

    public void shrink() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
//                S.s("percent: " + percent);
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisible(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    public void expend() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
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
                setVisible(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.clipOutRect(new Rect(0,0,100,100));
        Path p = new Path();
        p.addCircle(x, y, radius * percent, Path.Direction.CCW);
        canvas.clipPath(p);
        canvas.drawColor(Color.WHITE);
        super.onDraw(canvas);
    }


    @Override
    public void setVisibility(int visibility) {
        int visibilityOld = getVisibility();
        if (visibilityOld == visibility) {
            return;
        }
        if (visibility == View.VISIBLE) {
            expend();
        } else {
            shrink();
        }
    }

    private void setVisible(int visible) {
        super.setVisibility(visible);
        int count = getChildCount();
        S.s("count:" + count);
        long time = 100;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(visible);
                }
            }, time += 30);
        }
    }
}
