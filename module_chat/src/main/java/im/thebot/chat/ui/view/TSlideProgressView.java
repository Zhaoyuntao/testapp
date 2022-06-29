package im.thebot.chat.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_chat.R;

import im.turbo.basetools.state.StateMachine;
import im.turbo.baseui.utils.UiUtils;

/**
 * Created by zhaoyuntao on 2017/5/25.
 */

public class TSlideProgressView extends View {
    private static final int STYLE_LINE = 0;
    private static final int STYLE_DOTTED = 1;

    private final float radiusCircle;
    private final float widthProgress;
    private final boolean roundProgress;

    private final Paint paint;
    private final RectF rectBack;
    private final RectF rectFore;
    private final int colorProgress;
    private final int colorSlider;
    private final int colorProgressBackground;

    private OnProgressChangedListener onProgressChangedListener;
    private final StateMachine<ProgressMode> stateMachine = new StateMachine<>();

    private int style = STYLE_LINE;
    private float percent = 0;
    private float xLast;

    public TSlideProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        rectBack = new RectF();
        rectFore = new RectF();
        float defaultWidthProgress = UiUtils.dipToPx(4);
        float defaultRadiusCircle = UiUtils.dipToPx(5);
        int defaultColorProgress = Color.WHITE;
        int defaultColorProgressBackground = Color.rgb(100, 100, 100);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TSlideProgressView);//
            roundProgress = typedArray.getBoolean(R.styleable.TSlideProgressView_TSlideProgressView_roundProgress, true);
            widthProgress = typedArray.getDimension(R.styleable.TSlideProgressView_TSlideProgressView_widthProgress, defaultWidthProgress);
            radiusCircle = typedArray.getDimension(R.styleable.TSlideProgressView_TSlideProgressView_radiusCircle, defaultRadiusCircle);
            colorSlider = typedArray.getColor(R.styleable.TSlideProgressView_TSlideProgressView_colorSlider, defaultColorProgress);
            colorProgress = typedArray.getColor(R.styleable.TSlideProgressView_TSlideProgressView_colorProgress, defaultColorProgress);
            style = typedArray.getInt(R.styleable.TSlideProgressView_TSlideProgressView_style, STYLE_LINE);
            colorProgressBackground = typedArray.getColor(R.styleable.TSlideProgressView_TSlideProgressView_colorProgressBack, defaultColorProgressBackground);
            typedArray.recycle();
        } else {
            roundProgress = true;
            widthProgress = defaultWidthProgress;
            radiusCircle = defaultRadiusCircle;
            colorSlider = defaultColorProgress;
            colorProgress = defaultColorProgress;
            colorProgressBackground = defaultColorProgressBackground;
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
        float radiusProgress = roundProgress ? widthProgress / 2f : 0;
        float yCenter = hView / 2f;
        float wRectBack = wView - getPaddingStart() - getPaddingEnd() - radiusCircle * 2 + radiusProgress * 2;
        float hRectBack = widthProgress;
        float leftRectBack = getPaddingStart() + radiusCircle - radiusProgress;
        float rightRectBack = leftRectBack + wRectBack;
        float topRectBack = yCenter - hRectBack / 2f;
        float bottomRectBack = topRectBack + hRectBack;
        float slideRange = wRectBack - radiusProgress * 2;
        float sliderPosition = leftRectBack + radiusProgress + slideRange * percent;

        rectBack.set(leftRectBack, topRectBack, rightRectBack, bottomRectBack);
        rectFore.set(leftRectBack, topRectBack, sliderPosition, bottomRectBack);

        paint.setAntiAlias(true);
        paint.setColor(colorProgressBackground);
        paint.setStyle(Paint.Style.FILL);
        if (style == STYLE_LINE) {
            canvas.drawRoundRect(rectBack, radiusProgress, radiusProgress, paint);
        } else {
            for (float dotX = rectBack.left; dotX < rectBack.right; dotX += (widthProgress * 2)) {
                canvas.drawCircle(dotX, yCenter, radiusProgress, paint);
            }
        }

        paint.setColor(colorProgress);
        paint.setStyle(Paint.Style.FILL);
        if (style == STYLE_LINE) {
            canvas.drawRoundRect(rectFore, radiusProgress, radiusProgress, paint);
        } else {
            for (float dotX = rectFore.left; dotX < rectFore.right; dotX += (widthProgress * 2)) {
                canvas.drawCircle(dotX, yCenter, radiusProgress, paint);
            }
        }

        paint.setColor(colorSlider);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(sliderPosition, yCenter, radiusCircle, paint);
    }

    public void setPercent(float percent) {
        if (percent >= 0 && percent <= 1) {
            this.percent = percent;
            if (onProgressChangedListener != null) {
                onProgressChangedListener.onProgressChanged(percent, false);
            }
            postInvalidate();
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
                if (onProgressChangedListener != null) {
                    onProgressChangedListener.onProgressChanged(percent, true);
                }
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
                    if (onProgressChangedListener != null) {
                        onProgressChangedListener.onProgressChanged(percent, true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                xNow = event.getX();
                if (xNow > max_position) {
                    percent = 1;
                } else if (xNow < min_position) {
                    percent = 0;
                } else {
                    percent = (xNow - min_position) / (max_position - min_position);
                }
                if (onProgressChangedListener != null) {
                    onProgressChangedListener.onProgressChanged(getPercent(), true);
                }
                break;
        }
        postInvalidate();
        return true;
    }

    public void setViewMode(@NonNull ProgressMode... viewModes) {
        if (stateMachine != null) {
            stateMachine.setStates(viewModes);
        }
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(float percent, boolean dragByUser);
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
