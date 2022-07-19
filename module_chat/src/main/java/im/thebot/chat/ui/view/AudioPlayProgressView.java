package im.thebot.chat.ui.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
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
    private final float minMove;
    private long timeDown;
    private boolean isDragging;
    private boolean isDraggingSet;
    private int colorSlider;

    private OnProgressChangedListener onProgressChangedListener;

    private float percent = 0;
    private float xLast;
    private ValueAnimator animatorSmooth;
    private float percentForDraw;
    private float xDown, yDown;
    private SlideInterrupter slideInterrupter;


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
        minMove = ViewConfiguration.get(context).getScaledTouchSlop();
        setPaddingRelative((int) radiusCircle, 0, (int) radiusCircle, 0);
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = (int) (Math.max(radiusCircle, dotRadius)) * 2 + getPaddingBottom() + getPaddingTop();
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
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
        if (percent >= 0 && percent <= 1 && !isDragging) {
            float percentBefore = this.percentForDraw;
            this.percent = percent;
            if (onProgressChangedListener != null) {
                onProgressChangedListener.onProgressChanged(percent, false);
            }
            if (animatorSmooth != null && animatorSmooth.isRunning()) {
                animatorSmooth.cancel();
            }
            if (animate) {
                float difference = percent - percentBefore;
                animatorSmooth = ValueAnimator.ofFloat(0, 1);
                animatorSmooth.setInterpolator(new DecelerateInterpolator());
                animatorSmooth.setDuration(200);
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (slideInterrupter != null && !slideInterrupter.canSlide()) {
            return super.onTouchEvent(event);
        }
        cancelAnimation();
        long now = SystemClock.elapsedRealtime();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                timeDown = now;
                xDown = event.getX();
                yDown = event.getY();
                isDraggingSet = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceMoveX = Math.abs(event.getX() - xDown);
                float distanceMoveY = Math.abs(event.getY() - yDown);
                if (!isDraggingSet) {
                    if ((distanceMoveX - distanceMoveY) > UiUtils.dipToPx(1)) {
                        isDraggingSet = true;
                        isDragging = true;
                    } else if ((distanceMoveY - distanceMoveX) > UiUtils.dipToPx(1)) {
                        isDraggingSet = true;
                        isDragging = false;
                    }
                    if (isDragging) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        if (onProgressChangedListener != null) {
                            onProgressChangedListener.onStartDragging();
                        }
                    }
                }
                if (isDragging) {
                    calculatePercent(event);
                    postInvalidate();
                    if (onProgressChangedListener != null) {
                        onProgressChangedListener.onDragging(percent);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                isDraggingSet = false;
                break;
            case MotionEvent.ACTION_UP:
                if (!isDragging && now - timeDown < 200) {
                    if (onProgressChangedListener != null) {
                        onProgressChangedListener.onStartDragging();
                    }
                    calculatePercent(event);
                    postInvalidate();
                }
                if (onProgressChangedListener != null) {
                    onProgressChangedListener.onProgressChanged(percent, true);
                }
                isDragging = false;
                isDraggingSet = false;
                break;
        }
        return true;
    }

    private void calculatePercent(MotionEvent event) {
        boolean isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        float right = getWidth() - radiusCircle - getPaddingRight();
        float left = getPaddingLeft() + radiusCircle;
        boolean overStart = isRtl ? event.getX() > right : event.getX() < left;
        boolean overEnd = isRtl ? event.getX() < left : event.getX() > right;
        if (overEnd) {
            percent = 1;
        } else if (overStart) {
            percent = 0;
        } else {
            percent = (isRtl ? (right - event.getX()) : (event.getX() - left)) / (right - left);
        }
        percentForDraw = percent;
    }

    public void setSlideInterrupter(SlideInterrupter slideInterrupter) {
        this.slideInterrupter = slideInterrupter;
    }
}
