package com.test.test3app.interpolator;

import android.view.animation.Interpolator;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:customize BounceInterpolator for animator
 */
public class BounceInterpolator implements Interpolator {
    public BounceInterpolator() {
    }

    private static float bounce(float t) {
        return t * t * 2.6f;
    }

    public float getInterpolation(float t) {
        if (t < 0.62f) return bounce(t);
        else if (t < 1) return bounce(t - 0.812f) + 0.899f;
        else return 1;
    }

}
