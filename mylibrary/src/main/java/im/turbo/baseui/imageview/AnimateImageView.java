package im.turbo.baseui.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.doctor.mylibrary.R;

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
    private int duration = 200;

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
            duration = typedArray.getInt(R.styleable.AnimateImageView_AnimateImageView_animationDuration, duration);
            typedArray.recycle();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    public void setImageResource(int resId, boolean animate) {
        if (!animate) {
            super.setImageResource(resId);
            return;
        }
        BaseAnimation animation;
        if (animationMode == MODE_SCALE) {
            animation = new ScaleAnimation();
        } else if (animationMode == MODE_FLIP) {
            animation = new FlipAnimation();
        } else {
            super.setImageResource(resId);
            return;
        }
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(duration);
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
