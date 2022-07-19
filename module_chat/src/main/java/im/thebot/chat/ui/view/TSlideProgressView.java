package im.thebot.chat.ui.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.turbo.basetools.state.StateMachine;
import im.turbo.baseui.utils.UiUtils;

/**
 * Created by zhaoyuntao on 2017/5/25.
 */

public class TSlideProgressView extends View {

    private final float radiusCircle;
    private final float widthProgress;
    private final boolean roundProgress;
    private long timeDown;
    private boolean isDragging;
    private boolean isDraggingSet;
    private final Paint paint;
    private final RectF rectBack;
    private final RectF rectFore;
    private int colorProgress;
    private int colorSlider;
    private final int colorProgressBackground;

    private OnProgressChangedListener onProgressChangedListener;
    private final StateMachine<ProgressMode> stateMachine = new StateMachine<>();

    private float percent = 0;
    private float percentForDraw;
    private float xDown, yDown;
    private final int shadow;
    private final boolean showShadow;

    private ValueAnimator animatorSmooth;
    private SlideInterrupter slideInterrupter;

    public TSlideProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        rectBack = new RectF();
        rectFore = new RectF();
        float defaultWidthProgress = UiUtils.dipToPx(4);
        float defaultRadiusCircle = UiUtils.dipToPx(5);
        int defaultColorProgress = Color.WHITE;
        int defaultColorProgressBackground = Color.rgb(100, 100, 100);
        shadow = UiUtils.dipToPx(4);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TSlideProgressView);//
            roundProgress = typedArray.getBoolean(R.styleable.TSlideProgressView_TSlideProgressView_roundProgress, true);
            widthProgress = typedArray.getDimension(R.styleable.TSlideProgressView_TSlideProgressView_widthProgress, defaultWidthProgress);
            radiusCircle = typedArray.getDimension(R.styleable.TSlideProgressView_TSlideProgressView_radiusCircle, defaultRadiusCircle);
            colorSlider = typedArray.getColor(R.styleable.TSlideProgressView_TSlideProgressView_colorSlider, defaultColorProgress);
            colorProgress = typedArray.getColor(R.styleable.TSlideProgressView_TSlideProgressView_colorProgress, defaultColorProgress);
            showShadow = typedArray.getBoolean(R.styleable.TSlideProgressView_TSlideProgressView_showShadow, true);
            colorProgressBackground = typedArray.getColor(R.styleable.TSlideProgressView_TSlideProgressView_colorProgressBack, defaultColorProgressBackground);
            typedArray.recycle();
        } else {
            roundProgress = true;
            widthProgress = defaultWidthProgress;
            radiusCircle = defaultRadiusCircle;
            colorSlider = defaultColorProgress;
            colorProgress = defaultColorProgress;
            colorProgressBackground = defaultColorProgressBackground;
            showShadow = true;
        }
    }

    public void setCurrentMode(int mode) {
        if (stateMachine != null && stateMachine.setCurrentState(mode)) {
            _setCurrentMode(stateMachine.getCurrentState());
        }
    }

    public void setCurrentMode(@NonNull String mode) {
        if (stateMachine != null && stateMachine.setCurrentState(mode)) {
            _setCurrentMode(stateMachine.getCurrentState());
        }
    }

    private void _setCurrentMode(@Nullable ProgressMode viewMode) {
        if (viewMode != null) {
            invalidate();
        }
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int wView = getWidth();
        int hView = getHeight();
        if (wView == 0 || hView == 0) {
            return;
        }
        boolean isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        float radiusProgress = roundProgress ? widthProgress / 2f : 0;
        float yCenter = hView / 2f;
        float wRectBack = wView - getPaddingStart() - getPaddingEnd() - radiusCircle * 2 + radiusProgress * 2;
        float hRectBack = widthProgress;
        float leftRectBack = getPaddingStart() + radiusCircle - radiusProgress;
        float rightRectBack = leftRectBack + wRectBack;
        float topRectBack = yCenter - hRectBack / 2f;
        float bottomRectBack = topRectBack + hRectBack;
        float slideRange = wRectBack - radiusProgress * 2;
        float sliderPosition = isRtl ? rightRectBack - radiusProgress - slideRange * percentForDraw : leftRectBack + radiusProgress + slideRange * percentForDraw;

        rectBack.set(leftRectBack, topRectBack, rightRectBack, bottomRectBack);

        if (isRtl) {
            rectFore.set(sliderPosition, topRectBack, rightRectBack, bottomRectBack);
        } else {
            rectFore.set(leftRectBack, topRectBack, sliderPosition, bottomRectBack);
        }

        paint.setAntiAlias(true);
        paint.setColor(colorProgressBackground);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectBack, radiusProgress, radiusProgress, paint);

        paint.setColor(colorProgress);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectFore, radiusProgress, radiusProgress, paint);

        if (showShadow) {
            paint.setShadowLayer(shadow, shadow / 2f, shadow / 2f, Color.argb(40, 0, 0, 0));
        }
        paint.setColor(colorSlider);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(sliderPosition, yCenter, radiusCircle, paint);
        paint.clearShadowLayer();
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
            float difference = percent - percentBefore;
            if (animate) {
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

    private void cancelAnimation() {
        if (animatorSmooth != null && animatorSmooth.isRunning()) {
            animatorSmooth.cancel();
        }
    }

    public void setViewMode(@NonNull ProgressMode... viewModes) {
        if (stateMachine != null) {
            stateMachine.setStates(viewModes);
        }
    }

    public void setColorSlider(@ColorInt int color) {
        this.colorSlider = color;
        this.colorProgress = color;
        postInvalidate();
    }

    public void setSlideInterrupter(SlideInterrupter slideInterrupter) {
        this.slideInterrupter = slideInterrupter;
    }

    public void clearVideMode() {
        _setCurrentMode(null);
        if (stateMachine != null) {
            stateMachine.clear();
        }
    }

    @Override
    public void destroyDrawingCache() {
        clearVideMode();
        super.destroyDrawingCache();
    }
}
