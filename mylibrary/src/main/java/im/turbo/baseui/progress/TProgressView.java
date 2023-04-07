package im.turbo.baseui.progress;

import static android.animation.ValueAnimator.INFINITE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.doctor.mylibrary.R;

import im.turbo.basetools.state.StateMachine;
import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class TProgressView extends View {
    private int strokeColor;
    private int strokeColorBack;
    private float strokeWidth;
    private Paint paint;
    private ValueAnimator animatorRotate;
    private ValueAnimator animatorSmooth;
    private Rect rectDrawable;
    private RectF rectStroke;
    private float drawablePadding;
    private float totalProgress, currentProgress;
    private boolean strokeRound;
    private int strokeShadowColor;
    private float strokeShadowWidth;
    private final StateMachine<ViewMode> stateMachine = new StateMachine<>();
    private Drawable drawable;
    private boolean showProgress;

    private float progressOld;
    private float rotatePercent;
    private float wavePercentStart, wavePercentDelta;
    private float smoothPercent;

    public TProgressView(Context context) {
        super(context); init(null);
    }

    public TProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs); init(attrs);
    }

    public TProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); init(attrs);
    }

    public TProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes); init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        int defaultStrokeColor = Color.WHITE; int defaultStrokeColorBack = Color.argb(20, 0, 0, 0);
        int defaultShadowColor = Color.argb(55, 0, 0, 0);
        int defaultStrokeShadowWidth = UiUtils.dipToPx(2); if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TProgressView);
            drawablePadding = typedArray.getDimension(R.styleable.TProgressView_TProgressView_drawablePadding, 0);
            strokeColor = typedArray.getColor(R.styleable.TProgressView_TProgressView_strokeColor, defaultStrokeColor);
            strokeColorBack = typedArray.getColor(R.styleable.TProgressView_TProgressView_strokeColorBack, defaultStrokeColorBack);
            strokeWidth = typedArray.getDimension(R.styleable.TProgressView_TProgressView_strokeWidth, 0);
            strokeRound = typedArray.getBoolean(R.styleable.TProgressView_TProgressView_strokeRound, true);
            strokeShadowWidth = typedArray.getDimension(R.styleable.TProgressView_TProgressView_strokeShadowWidth, defaultStrokeShadowWidth);
            strokeShadowColor = typedArray.getColor(R.styleable.TProgressView_TProgressView_strokeShadowColor, defaultShadowColor);
            typedArray.recycle();
        } else {
            strokeColor = defaultStrokeColor; strokeColorBack = defaultStrokeColorBack;
            strokeShadowColor = defaultShadowColor; strokeShadowWidth = defaultStrokeShadowWidth;
            strokeRound = true;
        }
        paint = new Paint();
        rectStroke = new RectF();
        rectDrawable = new Rect();

        final float animateValue = 2f;
        animatorRotate = ValueAnimator.ofFloat(0, animateValue);
        animatorRotate.setDuration(2000);
        animatorRotate.setRepeatCount(INFINITE);
        animatorRotate.setInterpolator(new LinearInterpolator());
        animatorRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                rotatePercent = percent / animateValue;

                if (percent < animateValue / 2f) {
                    wavePercentStart = 0;
                    wavePercentDelta = percent;
                } else {
                    wavePercentStart = percent - 1;
                    wavePercentDelta = 1 - wavePercentStart;
                }
                postInvalidate();
            }
        });

        animatorSmooth = new ValueAnimator();
        animatorSmooth.setFloatValues(1, 0);
        animatorSmooth.setInterpolator(new DecelerateInterpolator());
        animatorSmooth.setDuration(300);
        animatorSmooth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                smoothPercent = (float) animation.getAnimatedValue(); postInvalidate();
            }
        });
        animatorSmooth.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                progressOld = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progressOld = 0;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int widthCanvas = right - left - getPaddingStart() - getPaddingEnd();
        int heightCanvas = bottom - top - getPaddingTop() - getPaddingBottom();
        if (strokeWidth == 0) {
            strokeWidth = (int) (Math.min(widthCanvas, heightCanvas) * 0.1f);
        }
        float halfStroke = strokeWidth / 2;
        float radiusStroke = Math.min(widthCanvas, heightCanvas) / 2f - halfStroke;
        float radiusDrawable = radiusStroke - halfStroke - drawablePadding;
        int xCenter = widthCanvas / 2 + getPaddingStart();
        int yCenter = heightCanvas / 2 + getPaddingTop();
        float leftOfStroke = xCenter - radiusStroke; float rightOfStroke = xCenter + radiusStroke;
        float topOfStroke = yCenter - radiusStroke; float bottomOfStroke = yCenter + radiusStroke;
        rectStroke.set(leftOfStroke, topOfStroke, rightOfStroke, bottomOfStroke);
        int leftOfDrawable = (int) (xCenter - radiusDrawable);
        int rightOfDrawable = (int) (xCenter + radiusDrawable);
        int topOfDrawable = (int) (yCenter - radiusDrawable);
        int bottomOfDrawable = (int) (yCenter + radiusDrawable);
        rectDrawable.set(leftOfDrawable, topOfDrawable, rightOfDrawable, bottomOfDrawable);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float angleStart, angleDelta;
        if (showProgress) {
            if (animatorRotate.isRunning()) {
                angleStart = (rotatePercent * 360 - 90) + (360 * wavePercentStart);
                angleDelta = Math.max(360 * wavePercentDelta, 0.1f);
            } else {
                angleStart = -90;
                angleDelta = 360 * ((currentProgress - (currentProgress - progressOld) * smoothPercent) / totalProgress);
            }
            paint.setColor(strokeColorBack); paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            paint.setStrokeCap(strokeRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);
            paint.setAntiAlias(true);
            canvas.drawArc(rectStroke, 0, 360, false, paint);
            paint.setColor(strokeColor);
            paint.setShadowLayer(strokeShadowWidth, 0, 0, strokeShadowColor);
            canvas.drawArc(rectStroke, angleStart, angleDelta, false, paint);
            paint.clearShadowLayer();
        }

        if (drawable != null) {
            drawable.setBounds(rectDrawable); drawable.draw(canvas);
        }
    }

    public void setProgress(float current, float total) {
        totalProgress = Math.max(0, total);
        float oldProgress = currentProgress;
        currentProgress = Math.min(Math.max(0, current), totalProgress);
        if (current >= oldProgress) {
            if (!animatorSmooth.isRunning()) {
                progressOld = oldProgress;
                animatorSmooth.start();
            }
        } else {
            smoothPercent = 0;
            progressOld = 0;
            postInvalidate();
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

    private void _setCurrentMode(@Nullable ViewMode viewMode) {
        if (viewMode == null) {
            setOnClickListener(null);
            this.drawable = null;
            this.showProgress = false;
            closeAllAnimation();
            this.rotatePercent = 0;
            this.wavePercentStart = 0;
            this.wavePercentDelta = 0;
        } else {
            setVisibility(viewMode.getVisible());
            setOnClickListener(viewMode.getListener());
            int drawableRes = viewMode.getDrawableRes();
            if (drawableRes != 0) {
                try {
                    this.drawable = ContextCompat.getDrawable(getContext(), drawableRes);
                } catch (Throwable ignore) {
                    this.drawable = null;
                }
            } else {
                this.drawable = null;
            }
            this.showProgress = viewMode.isShowProgress();
            if (showProgress) {
                if (viewMode.isRotate()) {
                    startRotateAnimator();
                } else {
                    stopRotateAnimator();
                }
            } else {
                this.rotatePercent = 0;
                this.wavePercentStart = 0;
                this.wavePercentDelta = 0;
            }
        }
        invalidate();
    }

    public void setViewMode(@NonNull ViewMode... viewModes) {
        if (stateMachine != null) {
            stateMachine.setStates(viewModes);
        }
    }

    private void startRotateAnimator() {
        currentProgress = 0;
        if (!animatorRotate.isRunning()) {
            animatorRotate.start();
        }
    }

    private void stopRotateAnimator() {
        if (animatorRotate.isRunning()) {
            animatorRotate.cancel();
        }
    }

    public void closeAllAnimation() {
        if (animatorSmooth.isRunning()) {
            animatorSmooth.cancel();
        }
        if (animatorRotate.isRunning()) {
            animatorRotate.cancel();
        }
    }

    public void clearVideMode() {
        _setCurrentMode(null);
        if (stateMachine != null) {
            stateMachine.clear();
        }
    }

    public void setStrokeColor(int strokeColor, int strokeColorBack) {
        this.strokeColor = strokeColor;
        this.strokeColorBack = strokeColorBack;
    }

    @Override
    protected void onDetachedFromWindow() {
        closeAllAnimation();
        clearVideMode();
        super.onDetachedFromWindow();
    }
}
