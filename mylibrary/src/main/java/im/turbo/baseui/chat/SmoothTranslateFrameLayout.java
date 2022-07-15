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
public class SmoothTranslateFrameLayout extends FrameLayout {
    @TranslateDirection
    private int translateDirection;

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
            TranslateDirection.left,
            TranslateDirection.top,
            TranslateDirection.right,
            TranslateDirection.bottom,
    })
    public @interface TranslateDirection {

        int left = 0;
        int top = 1;
        int right = 2;
        int bottom = 3;
    }

    private final int DURATION_ANIMATION = 300;
    private View defaultView;
    private View secondView;
    private boolean useAnimator;
    public static final int INDEX_DEFAULT = 0;

    public static final int INDEX_SECOND = 1;
    private final ValueAnimator animator;
    private int index = INDEX_DEFAULT;
    private float percentDefault;
    private float percentSecond;
    private static final int DEFAULT_CHILD_GRAVITY = Gravity.CENTER;

    public SmoothTranslateFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        animator = ValueAnimator.ofFloat(0, 2);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                defaultView.setClickable(false);
                secondView.setClickable(false);
                defaultView.setVisibility(VISIBLE);
                secondView.setVisibility(VISIBLE);
                requestLayout();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetViewState();
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
                float percent = (float) animation.getAnimatedValue();
                if (getIndex() == INDEX_DEFAULT) {
                    if (percent < 1) {
                        percentDefault = 0;
                        percentSecond = Math.max(0, 1 - percent);
                    } else {
                        percentDefault = Math.max(0, percent - 1);
                        percentSecond = 0;
                    }
                } else {
                    if (percent < 1f) {
                        percentDefault = Math.max(0, 1 - percent);
                        percentSecond = 0;
                    } else {
                        percentDefault = 0;
                        percentSecond = Math.max(0, percent - 1);
                    }
                }
                requestLayout();
            }
        });
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmoothTranslateFrameLayout);
            @InterpolatorType
            int interpolatorType = typedArray.getInt(R.styleable.SmoothTranslateFrameLayout_SmoothTranslateFrameLayout_animatorInterpolator, InterpolatorType.Linear);
            setInterpolator(interpolatorType);
            @TranslateDirection
            int translateDirection = typedArray.getInt(R.styleable.SmoothTranslateFrameLayout_SmoothTranslateFrameLayout_translateDirection, TranslateDirection.bottom);
            setTranslateDirection(translateDirection);
            useAnimator = typedArray.getBoolean(R.styleable.SmoothTranslateFrameLayout_SmoothTranslateFrameLayout_useAnimator, true);
            int duration = typedArray.getInt(R.styleable.SmoothTranslateFrameLayout_SmoothTranslateFrameLayout_animatorDuration, DURATION_ANIMATION);
            setDuration(duration);
            typedArray.recycle();
        } else {
            useAnimator = true;
            translateDirection = TranslateDirection.bottom;
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

    public void setTranslateDirection(@TranslateDirection int scaleDirection) {
        this.translateDirection = scaleDirection;
    }

    public int getTranslateDirection() {
        return translateDirection;
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
        resetViewState();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        measureChildWithMargins(defaultView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        final LayoutParams lpDefault = (LayoutParams) defaultView.getLayoutParams();

        childState = combineMeasuredStates(childState, defaultView.getMeasuredState());

        measureChildWithMargins(secondView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        final LayoutParams lpSecond = (LayoutParams) secondView.getLayoutParams();
        childState = combineMeasuredStates(childState, secondView.getMeasuredState());

//        if (getTranslateDirection() == TranslateDirection.bottom || getTranslateDirection() == TranslateDirection.top) {
            int defaultViewHeightSpace = defaultView.getMeasuredHeight() + lpDefault.topMargin + lpDefault.bottomMargin;
            int secondViewHeightSpace = secondView.getMeasuredHeight() + lpSecond.topMargin + lpSecond.bottomMargin;
            if (getIndex() == INDEX_DEFAULT) {
                maxHeight = (int) (secondViewHeightSpace + (defaultViewHeightSpace - secondViewHeightSpace) * percentDefault);
            } else {
                maxHeight = (int) (defaultViewHeightSpace + (secondViewHeightSpace - defaultViewHeightSpace) * percentSecond);
            }
//            maxWidth = Math.max(maxWidth, secondView.getMeasuredWidth() + lpSecond.leftMargin + lpSecond.rightMargin);
//            maxWidth = Math.max(maxWidth, defaultView.getMeasuredWidth() + lpDefault.leftMargin + lpDefault.rightMargin);
//        } else {
            int defaultViewWidthSpace = defaultView.getMeasuredWidth() + lpDefault.leftMargin + lpDefault.rightMargin;
            int secondViewWidthSpace = secondView.getMeasuredWidth() + lpSecond.leftMargin + lpSecond.rightMargin;
            if (getIndex() == INDEX_DEFAULT) {
                maxWidth = (int) (secondViewWidthSpace + (defaultViewWidthSpace - secondViewWidthSpace) * percentDefault);
            } else {
                maxWidth = (int) (defaultViewWidthSpace + (secondViewWidthSpace - defaultViewWidthSpace) * percentSecond);
            }
//            maxHeight = Math.max(maxHeight, secondView.getMeasuredHeight() + lpSecond.topMargin + lpSecond.bottomMargin);
//            maxHeight = Math.max(maxHeight, defaultView.getMeasuredHeight() + lpDefault.topMargin + lpDefault.bottomMargin);
//        }
        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        int measureWidth = resolveSizeAndState(maxWidth, widthMeasureSpec, childState);
        int measureHeight = resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT);

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int[] layoutSizeDefault = calculateChildLayoutParam(defaultView, left, top, right, bottom, percentDefault);
        if (layoutSizeDefault != null) {
            defaultView.layout(layoutSizeDefault[0], layoutSizeDefault[1], layoutSizeDefault[2], layoutSizeDefault[3]);
        }
        int[] layoutSizeSecond = calculateChildLayoutParam(secondView, left, top, right, bottom, percentSecond);
        if (layoutSizeSecond != null) {
            secondView.layout(layoutSizeSecond[0], layoutSizeSecond[1], layoutSizeSecond[2], layoutSizeSecond[3]);
        }
    }

    private int[] calculateChildLayoutParam(View child, int left, int top, int right, int bottom, float percent) {
        if (child.getVisibility() == GONE) {
            return null;
        }
        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

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

        switch (translateDirection) {
            case TranslateDirection.left:
                childLeft = childLeft - (int) ((childLeft + width) * (1 - percent));
                break;
            case TranslateDirection.top:
                childTop = childTop - (int) ((childTop + height) * (1 - percent));
                break;
            case TranslateDirection.right:
                childLeft = childLeft + (int) ((getMeasuredWidth() - childLeft - lp.leftMargin) * (1 - percent));
                break;
            case TranslateDirection.bottom:
                childTop = childTop + (int) ((getMeasuredHeight() - childTop - lp.topMargin) * (1 - percent));
                break;
        }

        return new int[]{childLeft, childTop, childLeft + width, childTop + height};
    }

    public void setDefaultView(View view) {
        if (view != null) {
            this.defaultView = view;
            addView(defaultView);
        }
    }

    public void setSecondView(View view) {
        if (view != null) {
            this.secondView = view;
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
            resetViewState();
            requestLayout();
            return;
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    private void resetViewState() {
        if (getIndex() == INDEX_DEFAULT) {
            percentDefault = 1;
            percentSecond = 0;
            defaultView.setVisibility(VISIBLE);
            secondView.setVisibility(GONE);
        } else {
            percentDefault = 0;
            percentSecond = 1;
            secondView.setVisibility(VISIBLE);
            defaultView.setVisibility(GONE);
        }
        defaultView.setClickable(percentDefault == 1);
        secondView.setClickable(percentSecond == 1);
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
        resetViewState();
        super.destroyDrawingCache();
    }

}
