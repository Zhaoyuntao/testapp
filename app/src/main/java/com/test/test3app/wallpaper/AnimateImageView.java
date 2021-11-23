package com.test.test3app.wallpaper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * created by zhaoyuntao
 * on 19/10/2021
 * description:
 */
public class AnimateImageView extends AppCompatImageView {

    private final float minPercent = 0.3f;

    public AnimateImageView(Context context) {
        super(context);
    }

    public AnimateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
        shrink(resId);
    }

    public void shrink(int resId) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1, minPercent, 1, minPercent, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1, minPercent));
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(100);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimateImageView.super.setImageResource(resId);
                expend();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animationSet);
    }

    public void expend() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(minPercent, 1, minPercent, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(minPercent, 1));
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(100);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animationSet);
    }
}
