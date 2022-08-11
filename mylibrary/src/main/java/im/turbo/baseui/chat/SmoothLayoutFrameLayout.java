package im.turbo.baseui.chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doctor.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 06/07/2022
 * description:
 */
public class SmoothLayoutFrameLayout extends FrameLayout {

    private final int minHeight, minWidth;

    @IntDef({
            InterpolatorType.Linear,
            InterpolatorType.Accelerate,
            InterpolatorType.Decelerate,
            InterpolatorType.AccelerateDecelerate,
            InterpolatorType.Overshoot,
    })
    public @interface InterpolatorType {
        int Linear = 0;
        int Accelerate = 1;
        int Decelerate = 2;
        int AccelerateDecelerate = 3;
        int Overshoot = 4;
    }

    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;
    private final LayoutAnimator animator;
    private float scalePercent;
    private int sizeDelta;
    private int heightAnimatorStart, heightLastMeasure;
    private final List<View> matchParentChildren = new ArrayList<>(1);

    public SmoothLayoutFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        animator = LayoutAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                scalePercent = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmoothLayoutFrameLayout);
            @InterpolatorType
            int interpolatorType = typedArray.getInt(R.styleable.SmoothLayoutFrameLayout_SmoothLayoutFrameLayout_animatorInterpolator, InterpolatorType.Linear);
            setInterpolator(interpolatorType);
            int duration = typedArray.getInt(R.styleable.SmoothLayoutFrameLayout_SmoothLayoutFrameLayout_animatorDuration, 150);
            minHeight = typedArray.getDimensionPixelSize(R.styleable.SmoothLayoutFrameLayout_SmoothLayoutFrameLayout_minHeight, 0);
            minWidth = typedArray.getDimensionPixelSize(R.styleable.SmoothLayoutFrameLayout_SmoothLayoutFrameLayout_minWidth, 0);
            setDuration(duration);
            typedArray.recycle();
        } else {
            minHeight = 0;
            minWidth = 0;
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(150);
        }
    }

    private Interpolator getInterpolator(@InterpolatorType int type) {
        switch (type) {
            case InterpolatorType.Accelerate:
                return new AccelerateInterpolator();
            case InterpolatorType.AccelerateDecelerate:
                return new AccelerateDecelerateInterpolator();
            case InterpolatorType.Decelerate:
                return new DecelerateInterpolator();
            case InterpolatorType.Overshoot:
                return new OvershootInterpolator();
            case InterpolatorType.Linear:
            default:
                return new LinearInterpolator();
        }
    }

    public void setInterpolator(@NonNull Interpolator interpolator) {
        animator.setInterpolator(interpolator);
    }

    public void setInterpolator(@InterpolatorType int type) {
        setInterpolator(getInterpolator(type));
    }

    public void setDuration(int duration) {
        animator.setDuration(duration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        final boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        matchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            childState = combineMeasuredStates(childState, child.getMeasuredState());
            if (measureMatchParentChildren) {
                if (lp.width == LayoutParams.MATCH_PARENT || lp.height == LayoutParams.MATCH_PARENT) {
                    matchParentChildren.add(child);
                }
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        int measureWidth = resolveSizeAndState(maxWidth, widthMeasureSpec, childState);
        int measureHeight = resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT);

        boolean sizeChanged = heightLastMeasure != measureHeight;
        if (sizeChanged) {
            sizeDelta = measureHeight - heightLastMeasure;
            heightAnimatorStart = heightLastMeasure;
            heightLastMeasure = measureHeight;
            if (animator.isRunning()) {
                animator.cancel();
            }
            animator.start();
        }
        if (animator.isInLayout()) {
            measureHeight = heightAnimatorStart + (int) (sizeDelta * scalePercent);
        }
        if (minWidth > 0) {
            measureWidth = Math.max(measureWidth, minWidth);
        }
        if (minHeight > 0) {
            measureHeight = Math.max(measureHeight, minHeight);
        }
        setMeasuredDimension(measureWidth, measureHeight);

        count = matchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = matchParentChildren.get(i);
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                final int childWidthMeasureSpec;
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
        matchParentChildren.clear();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            int childLeft;
            int childTop;

            int gravity = lp.gravity;
            if (gravity == -1) {
                gravity = DEFAULT_CHILD_GRAVITY;
            }

            final int layoutDirection = getLayoutDirection();
            final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = parentLeft + (parentRight - parentLeft - width) / 2 + lp.leftMargin - lp.rightMargin;
                    break;
                case Gravity.RIGHT:
                    childLeft = parentRight - width - lp.rightMargin;
                    break;
                case Gravity.LEFT:
                default:
                    childLeft = parentLeft + lp.leftMargin;
            }

            switch (verticalGravity) {
                case Gravity.TOP:
                    childTop = parentTop + lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    childTop = parentTop + (parentBottom - parentTop - height) / 2 + lp.topMargin - lp.bottomMargin;
                    break;
                case Gravity.BOTTOM:
                    childTop = parentBottom - height - lp.bottomMargin;
                    break;
                default:
                    childTop = parentTop + lp.topMargin;
            }

            child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }
    }

    public static class LayoutAnimator extends ValueAnimator {
        private boolean isInLayout;

        public boolean isInLayout() {
            return isInLayout;
        }

        public static LayoutAnimator ofFloat(float... values) {
            LayoutAnimator anim = new LayoutAnimator();
            anim.setFloatValues(values);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    anim.isInLayout = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    anim.isInLayout = false;
                }
            });
            return anim;
        }

        @Override
        public void start() {
            isInLayout = true;
            super.start();
        }

        @Override
        public void cancel() {
            isInLayout = false;
            super.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        animator.cancel();
        super.onDetachedFromWindow();
    }
}
