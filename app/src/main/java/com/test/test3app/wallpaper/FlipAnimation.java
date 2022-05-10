package com.test.test3app.wallpaper;

import android.graphics.Matrix;
import android.view.animation.DecelerateInterpolator;

/**
 * created by zhaoyuntao
 * on 19/10/2021
 * description:
 */
public class FlipAnimation extends BaseAnimation {
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setRepeatCount(1);
        setRepeatMode(REVERSE);
        setDuration(getDuration()/2);
        setInterpolator(new DecelerateInterpolator());
    }
    @Override
    protected float applyTransformation(float interpolatedTime, Matrix matrix, int xCenter, int yCenter) {
        interpolatedTime = 1 - interpolatedTime;
        matrix.postScale(interpolatedTime, 1, xCenter, yCenter);
        return interpolatedTime;
    }
}
