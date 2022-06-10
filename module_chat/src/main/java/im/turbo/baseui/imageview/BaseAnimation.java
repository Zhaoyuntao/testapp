package im.turbo.baseui.imageview;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import androidx.annotation.CallSuper;

/**
 * created by zhaoyuntao
 * on 29/04/2022
 * description:
 */
public abstract class BaseAnimation extends Animation {

    private int xCenter, yCenter;
    private AnimationProgressListener listener;

    @CallSuper
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        xCenter = width / 2;
        yCenter = height / 2;
        setInterpolator(new DecelerateInterpolator());
    }

    final public void setUpdateListener(AnimationProgressListener listener) {
        this.listener = listener;
    }

    @Override
    final protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float percent = applyTransformation(interpolatedTime, transformation.getMatrix(), xCenter, yCenter);
        if (listener != null) {
            listener.onUpdate(percent);
        }
    }

    protected abstract float applyTransformation(float interpolatedTime, Matrix matrix, int xCenter, int yCenter);
}
