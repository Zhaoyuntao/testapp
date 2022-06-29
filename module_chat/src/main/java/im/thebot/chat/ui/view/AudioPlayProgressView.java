package im.thebot.chat.ui.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.example.module_chat.R;

import im.turbo.baseui.utils.UiUtils;

/**
 * Created by zhaoyuntao on 2017/5/25.
 */

public class AudioPlayProgressView extends AudioRecordingProgressView {

    private final Paint paint;
    private final int colorProgress;
    private final int colorProgressBackground;
    private final float radiusCircle;

    private int colorSlider;

    private OnProgressChangedListener onProgressChangedListener;

    private float percent = 0;
    private float xLast;
    private ValueAnimator animatorSmooth;
    private float percentForDraw;
    private boolean isFingerPressed;

    public AudioPlayProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AudioPlayProgressView);
            radiusCircle = typedArray.getDimension(R.styleable.AudioPlayProgressView_AudioPlayProgressView_sliderRadius, UiUtils.dipToPx(6));
            colorProgress = typedArray.getColor(R.styleable.AudioPlayProgressView_AudioPlayProgressView_progressColor, ContextCompat.getColor(getContext(), R.color.color_chat_record_draft_progress_foreground));
            colorProgressBackground = typedArray.getColor(R.styleable.AudioPlayProgressView_AudioPlayProgressView_progressBackColor, ContextCompat.getColor(getContext(), R.color.color_chat_record_draft_progress));
            colorSlider = typedArray.getColor(R.styleable.AudioPlayProgressView_AudioPlayProgressView_sliderColor, ContextCompat.getColor(getContext(), R.color.color_chat_record_draft_progress_slider));
            typedArray.recycle();
        } else {
            radiusCircle = UiUtils.dipToPx(6);
            colorProgress = ContextCompat.getColor(getContext(), R.color.color_chat_record_draft_progress_foreground);
            colorProgressBackground = ContextCompat.getColor(getContext(), R.color.color_chat_record_draft_progress);
            colorSlider = ContextCompat.getColor(getContext(), R.color.color_chat_record_draft_progress_slider);
        }
        setPaddingRelative((int) radiusCircle, 0, (int) radiusCircle, 0);
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int wView = getWidth();
        int hView = getHeight();
        if (wView == 0 || hView == 0) {
            return;
        }
        //Draw back;
        drawDot(canvas, colorProgressBackground, getDotSize(), 0, true);

        //Draw fore.
        drawDot(canvas, colorProgress, (int) (getDotSize() * percentForDraw), 0, true);

        //Draw slider.
        float yCenter = hView / 2f;
        float slideRange = wView - radiusCircle * 2;
        float sliderPosition = radiusCircle + slideRange * percentForDraw;
        paint.setAntiAlias(true);
        paint.setColor(colorSlider);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(sliderPosition, yCenter, radiusCircle, paint);
    }

    @Override
    protected void calculateInitData(float[] initVolumes, long timeDurationMills, long durationPerDot, int dotDrawCount) {
        clearData();
        float piece = (float) initVolumes.length / dotDrawCount;
        for (int i = 0; i < dotDrawCount; i++) {
            int indexInitData = Math.round(i * piece);
            if (indexInitData <= initVolumes.length - 1) {
                addVolumeValue(initVolumes[indexInitData]);
            }
        }
    }

    public void setPercent(float percent) {
        setPercent(percent, true);
    }

    public void setPercent(float percent, boolean animate) {
        if (percent >= 0 && percent <= 1 && !isFingerPressed) {
            float percentBefore = this.percentForDraw;
            this.percent = percent;
            if (onProgressChangedListener != null) {
                onProgressChangedListener.onProgressChanged(percent, ProgressAction.ACTION_NOT_TOUCH);
            }
            if (animatorSmooth != null && animatorSmooth.isRunning()) {
                animatorSmooth.cancel();
            }
            if (animate) {
                float difference = percent - percentBefore;
                animatorSmooth = ValueAnimator.ofFloat(0, 1);
                animatorSmooth.setInterpolator(new DecelerateInterpolator());
                animatorSmooth.setDuration(300);
                animatorSmooth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (!animation.isRunning()) {
                            return;
                        }
                        float percent = (float) animation.getAnimatedValue();
                        percentForDraw = percentBefore + difference * percent;
                        postInvalidate();
                    }
                });
                animatorSmooth.start();
            } else {
                percentForDraw = percent;
                postInvalidate();
            }
        }
    }

    private void cancelAnimation() {
        if (animatorSmooth != null && animatorSmooth.isRunning()) {
            animatorSmooth.cancel();
        }
    }

    public float getPercent() {
        return percent;
    }

    float min_move = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float max_position = getWidth() - radiusCircle - getPaddingEnd();
        float min_position = getPaddingStart() + radiusCircle;
        isFingerPressed = event.getAction() != MotionEvent.ACTION_CANCEL && event.getAction() != MotionEvent.ACTION_UP;
        cancelAnimation();
        @ProgressAction
        int action = ProgressAction.ACTION_NOT_TOUCH;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float xNow = event.getX();
                xLast = xNow;
                if (xNow > max_position) {
                    percent = 1;
                } else if (xNow < min_position) {
                    percent = 0;
                } else {
                    percent = (xNow - min_position) / (max_position - min_position);
                }
                action = ProgressAction.ACTION_PRESS_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                xNow = event.getX();
                float distance_move = xNow - xLast;
                if (Math.abs(distance_move) >= min_move) {
                    xLast = xNow;
                    if (xNow > max_position) {
                        percent = 1;
                    } else if (xNow < min_position) {
                        percent = 0;
                    } else {
                        percent = (xNow - min_position) / (max_position - min_position);
                    }
                    action = ProgressAction.ACTION_MOVE;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                xNow = event.getX();
                if (xNow > max_position) {
                    percent = 1;
                } else if (xNow < min_position) {
                    percent = 0;
                } else {
                    percent = (xNow - min_position) / (max_position - min_position);
                }
                action = ProgressAction.ACTION_UP;
                break;
        }
        percentForDraw = percent;
        postInvalidate();
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged(percent, action);
        }
        return true;
    }

    public void setColorSlider(@ColorInt int color) {
        this.colorSlider = color;
        postInvalidate();
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(float percent, @ProgressAction int action);
    }
}
