package com.test.test3app.wallpaper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 19/10/2021
 * description:
 */
public class AnimateViewGroup extends LinearLayout {

    private final float minPercent = 0.3f;
    private final long duration=300;

    public AnimateViewGroup(Context context) {
        super(context);
    }

    public AnimateViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimateViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void shrink() {
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(1,0.5f);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimateViewGroup.super.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent= (float) animation.getAnimatedValue();
                AnimateViewGroup.this.setScaleX(percent);
                AnimateViewGroup.this.setScaleY(percent);
            }
        });
        valueAnimator.start();
    }

    public void expend() {
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0.5f,1);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                AnimateViewGroup.super.setVisibility(View.VISIBLE);
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
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent= (float) animation.getAnimatedValue();
                AnimateViewGroup.this.setScaleX(percent);
                AnimateViewGroup.this.setScaleY(percent);
            }
        });
        valueAnimator.start();
    }

    @Override
    public void setVisibility(int visibility) {
        S.s("1111");
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
}
