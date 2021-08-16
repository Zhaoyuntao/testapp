package com.test.test3app.fastrecordview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * this button can share its long click event with AudioRecordView
 */
public class DoubleSwitchView extends FrameLayout {

    private View defaultView;
    private View secondView;
    private int width, height;

    public static final int INDEX_DEFAULT = 0;
    public static final int INDEX_SECOND = 1;
    private ValueAnimator animator;
    private int index = INDEX_DEFAULT;

    private LayoutParams layoutParamsDefault;
    private LayoutParams layoutParamsSecond;

    public DoubleSwitchView(Context context) {
        super(context);
        init(null);
    }

    public DoubleSwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DoubleSwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public int getIndex() {
        return index;
    }

    public void switchIndex(int index) {
        if (this.index == index) {
            return;
        }
        if (index == INDEX_DEFAULT) {
            this.index = index;
        } else if (index == INDEX_SECOND) {
            this.index = index;
        }
        startAnim();
    }

    private void startAnim() {
        if (defaultView == null || secondView == null) {
            return;
        }
        stopAnim();
        if (animator == null) {
            animator = ValueAnimator.ofFloat(1000, 0);
            animator.setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
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
                        secondView.setLayoutParams(layoutParamsSecond);
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
                        secondView.setLayoutParams(layoutParamsSecond);
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
                    if (layoutParamsDefault == null || layoutParamsSecond == null) {
                        layoutParamsDefault = (LayoutParams) defaultView.getLayoutParams();
                        layoutParamsSecond = (LayoutParams) secondView.getLayoutParams();
                    }
                    percentDisappear = value / 1000;
                    if (percentDisappear < 0.5) {
                        percentDisappear = 0;
                    }
                    percentAppear = 1 - value / 1000;
                    if (percentAppear < 0.5) {
                        percentAppear = 0;
                    }

                    if (getIndex() == INDEX_SECOND) {
                        defaultView.setClickable(false);
                        secondView.setClickable(true);
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
                        defaultView.setClickable(true);
                        secondView.setClickable(false);
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

    private void stopAnim() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            animator.end();
        }
    }

    @Override
    public void destroyDrawingCache() {
        stopAnim();
        super.destroyDrawingCache();
    }

}
