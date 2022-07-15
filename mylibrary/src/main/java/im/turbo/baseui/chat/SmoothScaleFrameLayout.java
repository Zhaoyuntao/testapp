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

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * this button can share its long click event with AudioRecordView
 */
public class SmoothScaleFrameLayout extends FrameLayout {
    @ScaleDirection
    private int scaleDirection;

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

    @IntDef({
            ScaleDirection.all,
            ScaleDirection.horizontal,
            ScaleDirection.vertical,
    })
    public @interface ScaleDirection {

        int all = 0;
        int horizontal = 1;
        int vertical = 2;
    }

    private final int DURATION_ANIMATION = 300;
    private final float PERCENT_MIN_ANIMATION = 0.5f;
    private View defaultView;
    private View secondView;
    private boolean useAnimator;
    public static final int INDEX_DEFAULT = 0;

    public static final int INDEX_SECOND = 1;
    private final ValueAnimator animator;
    private int index = INDEX_DEFAULT;
    private float percent;
    private static final int DEFAULT_CHILD_GRAVITY = Gravity.CENTER;

    public SmoothScaleFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        animator = ValueAnimator.ofFloat(0, 1);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    child.setClickable(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (getIndex() == INDEX_DEFAULT) {
                    percent = 1;
                } else {
                    percent = 0;
                }
                initViewState();
                requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percentTmp = (float) animation.getAnimatedValue();
                percent = (getIndex() == INDEX_DEFAULT ? percentTmp : (1 - percentTmp));
                initViewState();
                requestLayout();
            }
        });
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmoothScaleFrameLayout);
            @InterpolatorType
            int interpolatorType = typedArray.getInt(R.styleable.SmoothScaleFrameLayout_SmoothScaleFrameLayout_animatorInterpolator, InterpolatorType.Linear);
            setInterpolator(interpolatorType);
            @ScaleDirection
            int scaleDirection = typedArray.getInt(R.styleable.SmoothScaleFrameLayout_SmoothScaleFrameLayout_scaleDirection, ScaleDirection.all);
            setScaleDirection(scaleDirection);
            useAnimator = typedArray.getBoolean(R.styleable.SmoothScaleFrameLayout_SmoothScaleFrameLayout_useAnimator, true);
            int duration = typedArray.getInt(R.styleable.SmoothScaleFrameLayout_SmoothScaleFrameLayout_animatorDuration, DURATION_ANIMATION);
            setDuration(duration);
            typedArray.recycle();
        } else {
            useAnimator = true;
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(DURATION_ANIMATION);
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

    public void setScaleDirection(@ScaleDirection int scaleDirection) {
        this.scaleDirection = scaleDirection;
    }

    public int getScaleDirection() {
        return scaleDirection;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("You have to put two child in this view group.");
        }
        secondView = getChildAt(0);
        defaultView = getChildAt(1);
        secondView.setVisibility(VISIBLE);
        defaultView.setVisibility(VISIBLE);
        percent = 1;
        initViewState();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;

        if (defaultView.getVisibility() != GONE) {
            int[] result = measureChild(defaultView, widthMeasureSpec, heightMeasureSpec, maxWidth, maxHeight, childState);
            maxWidth = result[0];
            maxHeight = result[1];
            childState = result[2];
        }
        if (secondView.getVisibility() != GONE) {
            int[] result = measureChild(secondView, widthMeasureSpec, heightMeasureSpec, maxWidth, maxHeight, childState);
            maxWidth = result[0];
            maxHeight = result[1];
            childState = result[2];
        }

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        int measureWidth = resolveSizeAndState(maxWidth, widthMeasureSpec, childState);
        int measureHeight = resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT);

        if (defaultView.getVisibility() != GONE) {
            float percentDefault = percent > PERCENT_MIN_ANIMATION ? percent : 0;
            int[] result = measureChildWithPercent(defaultView, percentDefault, widthMeasureSpec, heightMeasureSpec, measureWidth, measureHeight, 0, 0, childState);
            maxWidth = result[0];
            maxHeight = result[1];
        }
        if (secondView.getVisibility() != GONE) {
            float percentSecond = (1 - percent) > PERCENT_MIN_ANIMATION ? (1 - percent) : 0;
            int[] result = measureChildWithPercent(secondView, percentSecond, widthMeasureSpec, heightMeasureSpec, measureWidth, measureHeight, maxWidth, maxHeight, childState);
            maxWidth = result[0];
            maxHeight = result[1];
        }

        measureWidth = resolveSizeAndState(maxWidth, widthMeasureSpec, childState);
        measureHeight = resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int[] measureChild(View child, int widthMeasureSpec, int heightMeasureSpec, int maxWidth, int maxHeight, int childState) {
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
        maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
        childState = combineMeasuredStates(childState, child.getMeasuredState());
        return new int[]{maxWidth, maxHeight, childState};
    }

    private int[] measureChildWithPercent(View child, final float percent, int widthMeasureSpec, int heightMeasureSpec, int measureWidth, int measureHeight, int maxWidth, int maxHeight, int childState) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        final int childWidthMeasureSpec;
        final float percentWidth;
        if (scaleDirection == ScaleDirection.vertical) {
            percentWidth = 1;
        } else {
            percentWidth = percent;
        }
        if (lp.width == LayoutParams.MATCH_PARENT) {
            final int width = (int) (Math.max(0, measureWidth - getPaddingLeft() - getPaddingRight() - lp.leftMargin - lp.rightMargin) * percentWidth);
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        } else {
            childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, (int) (lp.width * percentWidth));
        }
        final int childHeightMeasureSpec;
        final float percentHeight;
        if (scaleDirection == ScaleDirection.horizontal) {
            percentHeight = 1;
        } else {
            percentHeight = percent;
        }
        if (lp.height == LayoutParams.MATCH_PARENT) {
            final int height = (int) (Math.max(0, measureHeight - getPaddingTop() - getPaddingBottom() - lp.topMargin - lp.bottomMargin) * percentHeight);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else {
            childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, (int) (lp.height * percentHeight));
        }
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
        maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
        return new int[]{maxWidth, maxHeight, childState};
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

    public void setDefaultView(View view) {
        if (view != null) {
            this.defaultView = view;
            this.defaultView.setVisibility(VISIBLE);
            addView(defaultView);
        }
    }

    public void setSecondView(View view) {
        if (view != null) {
            this.secondView = view;
            this.secondView.setVisibility(VISIBLE);
            addView(secondView);
        }
    }

    public void nextIndex() {
        if (index == INDEX_DEFAULT) {
            startAnim(INDEX_SECOND);
        } else {
            startAnim(INDEX_DEFAULT);
        }
    }

    public int getIndex() {
        return index;
    }

    public void switchIndex(int index) {
        switchIndex(index, true);
    }

    public void switchIndex(int index, boolean useAnimator) {
        this.useAnimator = useAnimator;
        startAnim(index);
    }

    private void startAnim(int index) {
        if (this.index == index || (index != INDEX_SECOND && index != INDEX_DEFAULT)) {
            return;
        }
        this.index = index;
        stopAnim();
        if (!useAnimator) {
            if (getIndex() == INDEX_DEFAULT) {
                percent = 1;
            } else {
                percent = 0;
            }
            initViewState();
            requestLayout();
            return;
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    private void initViewState() {
        defaultView.setClickable(percent == 1);
        defaultView.setAlpha(percent);
        secondView.setClickable(percent == 0);
        secondView.setAlpha(1 - percent);
        requestLayout();
    }

    private void stopAnim() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            animator.end();
        }
    }

    @Override
    public void destroyDrawingCache() {
        stopAnim();
        initViewState();
        super.destroyDrawingCache();
    }

}
