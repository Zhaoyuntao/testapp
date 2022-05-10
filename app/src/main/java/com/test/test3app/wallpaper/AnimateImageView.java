package com.test.test3app.wallpaper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.test.test3app.R;

/**
 * created by zhaoyuntao
 * on 19/10/2021
 * description:
 */
public class AnimateImageView extends AppCompatImageView {

    private static final int MODE_NON = 0;
    private static final int MODE_SCALE = 1;
    private static final int MODE_FLIP = 2;
    private int animationMode;

    public AnimateImageView(Context context) {
        super(context);
        init(null);
    }

    public AnimateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AnimateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AnimateImageView);
            animationMode = typedArray.getInt(R.styleable.AnimateImageView_AnimateImageView_animationMode, MODE_NON);
            typedArray.recycle();
        }
    }

    @Override
    public void setImageResource(int resId) {
        BaseAnimation animation;
        if (animationMode == MODE_SCALE) {
            animation = new ScaleAnimation();
        } else if (animationMode == MODE_FLIP) {
            animation = new FlipAnimation();
        } else {
            super.setImageResource(resId);
            return;
        }
        animation.setDuration(1000);
        animation.setUpdateListener(new AnimationProgressListener() {
            boolean set;

            @Override
            public void onUpdate(float percent) {
                if (percent == 0 && !set) {
                    set = true;
                    AnimateImageView.super.setImageResource(resId);
                }
            }
        });
        animation.setFillAfter(true);
        startAnimation(animation);
    }
}
