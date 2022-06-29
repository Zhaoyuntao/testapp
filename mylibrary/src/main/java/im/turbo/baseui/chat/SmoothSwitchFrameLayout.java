package im.turbo.baseui.chat;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * this button can share its long click event with AudioRecordView
 */
public class SmoothSwitchFrameLayout extends FrameLayout {

    private final long DURATION_ANIMATION = 300;
    private final float PERCENT_MIN_ANIMATION = 0.5f;
    private View defaultView;
    private View secondView;
    private int width, height;

    public static final int INDEX_DEFAULT = 0;
    public static final int INDEX_SECOND = 1;

    private OnChildSwitchListener onChildSwitchListener;

    @IntDef({
            INDEX_DEFAULT,
            INDEX_SECOND
    })
    public @interface Index {

    }

    private ValueAnimator animator;
    @Index
    private int index = INDEX_DEFAULT;

    private LayoutParams layoutParamsDefault;
    private LayoutParams layoutParamsSecond;

    public SmoothSwitchFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public SmoothSwitchFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SmoothSwitchFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        setPaddingRelative(0, 0, 0, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() >= 2) {
            secondView = getChildAt(0);
            defaultView = getChildAt(1);

            secondView.setVisibility(GONE);
            defaultView.setVisibility(VISIBLE);
            secondView.setAlpha(0);
            defaultView.setAlpha(1);
            if (layoutParamsDefault == null) {
                layoutParamsDefault = (LayoutParams) defaultView.getLayoutParams();
            }
            if (layoutParamsSecond == null) {
                layoutParamsSecond = (LayoutParams) secondView.getLayoutParams();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w_max = MeasureSpec.getSize(widthMeasureSpec);
        int h_max = MeasureSpec.getSize(heightMeasureSpec);
        if (defaultView != null) {
            measureChild(defaultView, widthMeasureSpec, heightMeasureSpec);
        }
        if (secondView != null) {
            measureChild(secondView, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(w_max, h_max);
        width = w_max;
        height = h_max;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (defaultView != null) {
            int width = defaultView.getWidth();
            int height = defaultView.getHeight();
            int leftInParent = ((right - left) - width) / 2;
            int rightInParent = leftInParent + width;
            int topInParent = ((bottom - top) - height) / 2;
            int bottomInParent = topInParent + height;
            defaultView.layout(leftInParent, topInParent, rightInParent, bottomInParent);
        }
        if (secondView != null) {
            int width = secondView.getWidth();
            int height = secondView.getHeight();
            int leftInParent = ((right - left) - width) / 2;
            int rightInParent = leftInParent + width;
            int topInParent = ((bottom - top) - height) / 2;
            int bottomInParent = topInParent + height;
            secondView.layout(leftInParent, topInParent, rightInParent, bottomInParent);
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
            this.secondView.setVisibility(GONE);
            addView(secondView);
        }
    }

    public void nextIndex() {
        if (index == INDEX_DEFAULT) {
            index = INDEX_SECOND;
        } else {
            index = INDEX_DEFAULT;
        }
        startAnim();
    }

    @Index
    public int getIndex() {
        return index;
    }

    public void switchIndex(@Index int index) {
        switchIndex(index, true);
    }

    public void switchIndex(@Index int index, boolean animate) {
        if (this.index == index || (index != INDEX_SECOND && index != INDEX_DEFAULT)) {
            return;
        }
        this.index = index;
        if (animate) {
            startAnim();
        } else {
            resetState();
        }
        if (onChildSwitchListener != null) {
            if (index == INDEX_DEFAULT) {
                onChildSwitchListener.onSwitchDefault();
            } else {
                onChildSwitchListener.onSwitchSecond();
            }
        }
    }

    private void startAnim() {
        if (defaultView == null || secondView == null) {
            return;
        }
        stopAnim();
        if (animator == null) {
            animator = ValueAnimator.ofFloat(1, 0);
            animator.setDuration(DURATION_ANIMATION);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    defaultView.setClickable(false);
                    secondView.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    resetState();
                    if (onChildSwitchListener != null) {
                        onChildSwitchListener.onSwitchAnimationEnd(index == INDEX_DEFAULT);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percentDisappear;
                    float percentAppear;
                    float value = (float) animation.getAnimatedValue();

                    percentDisappear = value;
                    if (percentDisappear < PERCENT_MIN_ANIMATION) {
                        percentDisappear = 0;
                    }
                    percentAppear = 1 - value;
                    if (percentAppear < PERCENT_MIN_ANIMATION) {
                        percentAppear = 0;
                    }

                    if (getIndex() == INDEX_SECOND) {
                        layoutParamsDefault.width = (int) (width * percentDisappear);
                        layoutParamsDefault.height = (int) (height * percentDisappear);
                        defaultView.setLayoutParams(layoutParamsDefault);
                        defaultView.setAlpha(percentDisappear);
                        if (percentDisappear == 0) {
                            defaultView.setVisibility(GONE);
                        } else {
                            defaultView.setVisibility(VISIBLE);
                        }

                        layoutParamsSecond.width = (int) (width * percentAppear);
                        layoutParamsSecond.height = (int) (height * percentAppear);
                        secondView.setLayoutParams(layoutParamsSecond);
                        secondView.setAlpha(percentAppear);
                        if (percentAppear == 0) {
                            secondView.setVisibility(GONE);
                        } else {
                            secondView.setVisibility(VISIBLE);
                        }
                    } else {
                        layoutParamsDefault.width = (int) (width * percentAppear);
                        layoutParamsDefault.height = (int) (height * percentAppear);
                        defaultView.setLayoutParams(layoutParamsDefault);
                        defaultView.setAlpha(percentAppear);
                        if (percentAppear == 0) {
                            defaultView.setVisibility(GONE);
                        } else {
                            defaultView.setVisibility(VISIBLE);
                        }

                        layoutParamsSecond.width = (int) (width * percentDisappear);
                        layoutParamsSecond.height = (int) (height * percentDisappear);
                        secondView.setLayoutParams(layoutParamsSecond);
                        secondView.setAlpha(percentDisappear);
                        if (percentDisappear == 0) {
                            secondView.setVisibility(GONE);
                        } else {
                            secondView.setVisibility(VISIBLE);
                        }
                    }
                }
            });
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    private void resetState() {
        if (getIndex() == INDEX_DEFAULT) {
            defaultView.setVisibility(VISIBLE);
            defaultView.setAlpha(1);
            layoutParamsDefault.width = width;
            layoutParamsDefault.height = height;
            defaultView.setLayoutParams(layoutParamsDefault);

            secondView.setVisibility(GONE);
            secondView.setAlpha(0);
            layoutParamsSecond.width = 0;
            layoutParamsSecond.height = 0;

            secondView.setClickable(false);
            defaultView.setClickable(true);
        } else {
            defaultView.setVisibility(GONE);
            defaultView.setAlpha(0);
            layoutParamsDefault.width = 0;
            layoutParamsDefault.height = 0;
            defaultView.setLayoutParams(layoutParamsDefault);

            secondView.setVisibility(VISIBLE);
            secondView.setAlpha(1);
            layoutParamsSecond.width = width;
            layoutParamsSecond.height = height;

            secondView.setClickable(true);
            defaultView.setClickable(false);
        }
        secondView.setLayoutParams(layoutParamsSecond);
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
        resetState();
        super.destroyDrawingCache();
    }

    public interface OnChildSwitchListener {
        void onSwitchDefault();

        void onSwitchSecond();

        void onSwitchAnimationEnd(boolean isDefault);
    }

    public void addOnChildSwitchListener(OnChildSwitchListener onChildSwitchListener) {
        this.onChildSwitchListener = onChildSwitchListener;
    }

}
