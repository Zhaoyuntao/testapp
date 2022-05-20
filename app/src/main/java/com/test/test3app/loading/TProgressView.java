package com.test.test3app.loading;

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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.test.test3app.R;
import com.test.test3app.faceview.Preconditions;
import com.test.test3app.fastrecordviewnew.UiUtils;
import com.test.test3app.observer.ObjectManager;

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
    private float angle;
    private long rotateDuration;
    private ValueAnimator animatorRotateCanvas;
    private ValueAnimator animatorWave;
    private ValueAnimator animatorSmooth;
    private Rect rectDrawable;
    private RectF rectStroke;
    private int xCenter;
    private int yCenter;
    private float drawablePadding;
    private boolean useWaveProgress;
    private float startAngle;
    private float progressAngleFinal;
    private float progressAngleForDraw;
    private boolean strokeRound;
    private int strokeShadowColor;
    private float strokeShadowWidth;
    private final ObjectManager<ViewMode> manager = new ObjectManager<>(false);
    private Drawable drawable;
    private boolean showProgress;
    private String currentMode;

    public TProgressView(Context context) {
        super(context);
        init(null);
    }

    public TProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public TProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        int defaultStrokeColor = Color.parseColor("#01a983");
        int defaultStrokeColorBack = Color.argb(20, 0, 0, 0);
        int defaultShadowColor = Color.argb(55, 0, 0, 0);
        int defaultDuration = 1000;
        int defaultStrokeShadowWidth = UiUtils.dipToPx(2);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TProgressView);
            drawablePadding = typedArray.getDimension(R.styleable.TProgressView_TProgressView_drawablePadding, 0);
            strokeColor = typedArray.getColor(R.styleable.TProgressView_TProgressView_strokeColor, defaultStrokeColor);
            strokeColorBack = typedArray.getColor(R.styleable.TProgressView_TProgressView_strokeColorBack, defaultStrokeColorBack);
            strokeWidth = typedArray.getDimension(R.styleable.TProgressView_TProgressView_strokeWidth, 0);
            strokeRound = typedArray.getBoolean(R.styleable.TProgressView_TProgressView_strokeRound, true);
            strokeShadowWidth = typedArray.getDimension(R.styleable.TProgressView_TProgressView_strokeShadowWidth, defaultStrokeShadowWidth);
            strokeShadowColor = typedArray.getColor(R.styleable.TProgressView_TProgressView_strokeShadowColor, defaultShadowColor);
            rotateDuration = typedArray.getInt(R.styleable.TProgressView_TProgressView_rotateDuration, defaultDuration);
            typedArray.recycle();
        } else {
            strokeColor = defaultStrokeColor;
            strokeColorBack = defaultStrokeColorBack;
            strokeShadowColor = defaultShadowColor;
            strokeShadowWidth = defaultStrokeShadowWidth;
            rotateDuration = defaultDuration;
            strokeRound = true;
        }
        paint = new Paint();
        rectStroke = new RectF();
        rectDrawable = new Rect();
        animatorRotateCanvas = ValueAnimator.ofFloat(0, 1);
        animatorRotateCanvas.setRepeatCount(INFINITE);
        animatorRotateCanvas.setInterpolator(new LinearInterpolator());
        animatorRotateCanvas.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                angle = percent * 360;
                postInvalidate();
            }
        });
        animatorRotateCanvas.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                angle = 0;
                postInvalidate();
            }
        });
        animatorWave = ValueAnimator.ofFloat(0, 2);
        animatorWave.setRepeatCount(INFINITE);
        animatorWave.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorWave.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                if (percent < 1f) {
                    startAngle = 0;
                    progressAngleForDraw = 360 * percent;
                } else {
                    startAngle = 360 * (percent - 1);
                    progressAngleForDraw = 360 - startAngle;
                }
                postInvalidate();
            }
        });
        animatorWave.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                startAngle = 0;
                progressAngleForDraw = 0;
                postInvalidate();
            }
        });
        setOnClickListener(null);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(v -> {
            String mode = TProgressView.this.currentMode;
            manager.notifyObjects(viewMode -> {
                if (TextUtils.equals(viewMode.getMode(), mode)) {
                    OnClickListener listener = viewMode.getListener();
                    if (listener != null) {
                        listener.onClick(v);
                    }
                    return true;
                } else {
                    return false;
                }
            });
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
        xCenter = widthCanvas / 2 + getPaddingStart();
        yCenter = heightCanvas / 2 + getPaddingTop();
        float leftOfStroke = xCenter - radiusStroke;
        float rightOfStroke = xCenter + radiusStroke;
        float topOfStroke = yCenter - radiusStroke;
        float bottomOfStroke = yCenter + radiusStroke;
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
        if (showProgress) {
            canvas.save();
            canvas.rotate(angle, xCenter, yCenter);
            paint.setColor(strokeColorBack);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            paint.setStrokeCap(strokeRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);
            paint.setAntiAlias(true);
            canvas.drawArc(rectStroke, 0, 360, false, paint);
            paint.setColor(strokeColor);
            paint.setShadowLayer(strokeShadowWidth, 0, 0, strokeShadowColor);
            canvas.drawArc(rectStroke, startAngle - 90, Math.max(progressAngleForDraw, 0.1f), false, paint);
            canvas.restore();
            paint.clearShadowLayer();
        }

        if (drawable != null) {
            drawable.setBounds(rectDrawable);
            drawable.draw(canvas);
        }
    }

    public void setProgress(float current, float total) {
        if (useWaveProgress) {
            throw new RuntimeException("Please don't set progress when set wave progress");
        }
        float totalProgress = Math.max(0, total);
        float currentProgress = Math.min(Math.max(0, current), totalProgress);
        progressAngleFinal = 360 * (currentProgress / totalProgress);
        startAngle = 0;
        if (animatorSmooth != null && animatorSmooth.isRunning()) {
            animatorSmooth.cancel();
        }
        float difference = progressAngleFinal - progressAngleForDraw;
        float startAngle = progressAngleForDraw;
        animatorSmooth = ValueAnimator.ofFloat(0, 1);
        animatorSmooth.setInterpolator(new DecelerateInterpolator());
        animatorSmooth.setDuration(300);
        animatorSmooth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                progressAngleForDraw = startAngle + difference * percent;
                postInvalidate();
            }
        });
        animatorSmooth.start();
    }

    public void setCurrentMode(int mode) {
        setCurrentMode(String.valueOf(mode));
    }

    public void setCurrentMode(@NonNull String mode) {
        Preconditions.checkNotEmpty(mode);
        if (TextUtils.equals(currentMode, mode)) {
            return;
        }
        ViewMode viewMode = manager.getObject(mode);
        setCurrentMode(viewMode);
    }

    private void setCurrentMode(ViewMode viewMode) {
        if (viewMode != null) {
            setVisibility(viewMode.getVisible());
            int drawableRes = viewMode.getDrawableRes();
            if (drawableRes != 0) {
                try {
                    this.drawable = ContextCompat.getDrawable(getContext(), drawableRes);
                } catch (Throwable ignore) {
                }
            } else {
                this.drawable = null;
            }
            this.currentMode = viewMode.getMode();
            this.showProgress = viewMode.isShowProgress();
            this.useWaveProgress = viewMode.useWaveProgress();
            this.rotateDuration = viewMode.getRotateDuration();
            if (showProgress) {
                if (viewMode.isRotate()) {
                    if (rotateDuration <= 0) {
                        rotateDuration = 1000;
                    }
                    animatorRotateCanvas.setDuration(rotateDuration);
                    animatorRotateCanvas.start();
                    animatorWave.setDuration(rotateDuration * 2);
                    if (useWaveProgress) {
                        animatorWave.start();
                    } else {
                        animatorWave.cancel();
                        angle = 0;
                    }
                } else {
                    animatorRotateCanvas.cancel();
                    animatorWave.cancel();
                    angle = 0;
                    invalidate();
                }
            } else {
                angle = 0;
                startAngle = 0;
                progressAngleFinal = 0;
                progressAngleForDraw = 0;
            }
            invalidate();
        }
    }

    public void setViewMode(@NonNull ViewMode... viewModes) {
        manager.clear();
        for (ViewMode viewMode : viewModes) {
            manager.addObject(viewMode.getMode(), viewMode);
        }
        if (viewModes.length > 0) {
            setCurrentMode(viewModes[0]);
        }
    }
}
