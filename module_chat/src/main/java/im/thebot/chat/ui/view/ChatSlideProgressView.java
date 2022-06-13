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

import com.example.module_chat.R;

import im.turbo.baseui.utils.UiUtils;

/**
 * Created by zhaoyuntao on 2017/5/25.
 */

public class ChatSlideProgressView extends View {
    private static final int STYLE_LINE = 0;
    private static final int STYLE_DOTTED = 1;

    private float percent = 0;
    private float radiusCircle;
    private int wView, hView;
    private float wLine = 4;
    private int style = STYLE_LINE;

    private float widthProgress;
    private boolean roundProgress;
    private float sliderPosition;
    private Paint paint;

    private CallBack callBack;

    private float xNow, xLast;
    private RectF rectBack;
    private RectF rectFore;
    private int colorProgress;

    private int colorProgressBackground;

    public ChatSlideProgressView(Context context) {
        super(context);
        init(null);
    }

    public ChatSlideProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChatSlideProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        rectBack = new RectF();
        rectFore = new RectF();
        float defaultWidthProgress = UiUtils.dipToPx(4);
        float defaultRadiusCircle = UiUtils.dipToPx(5);
        int defaultColorProgress = Color.WHITE;
        int defaultColorProgressBackground = Color.rgb(100, 100, 100);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ChatSlideProgressView);//
            roundProgress = typedArray.getBoolean(R.styleable.ChatSlideProgressView_ChatSlideProgressView_roundProgress, true);
            widthProgress = typedArray.getDimension(R.styleable.ChatSlideProgressView_ChatSlideProgressView_widthProgress, defaultWidthProgress);
            radiusCircle = typedArray.getDimension(R.styleable.ChatSlideProgressView_ChatSlideProgressView_radiusCircle, defaultRadiusCircle);
            colorProgress = typedArray.getColor(R.styleable.ChatSlideProgressView_ChatSlideProgressView_colorProgress, defaultColorProgress);
            style = typedArray.getInt(R.styleable.ChatSlideProgressView_ChatSlideProgressView_style, STYLE_LINE);
            colorProgressBackground = typedArray.getColor(R.styleable.ChatSlideProgressView_ChatSlideProgressView_colorProgressBack, defaultColorProgressBackground);
            typedArray.recycle();
        } else {
            roundProgress = true;
            widthProgress = defaultWidthProgress;
            radiusCircle = defaultRadiusCircle;
            colorProgress = defaultColorProgress;
            colorProgressBackground = defaultColorProgressBackground;
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        wView = getWidth();
        hView = getHeight();
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
        sliderPosition = leftRectBack + radiusProgress + slideRange * percent;

        rectBack.set(leftRectBack, topRectBack, rightRectBack, bottomRectBack);
        rectFore.set(leftRectBack, topRectBack, sliderPosition, bottomRectBack);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(wLine);

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

        paint.setColor(colorProgress);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(sliderPosition, yCenter, radiusCircle, paint);
    }

    public void setPercent(float percent) {
        if (percent >= 0 && percent <= 1) {
            this.percent = percent;
            if (callBack != null) {
                callBack.whenDragging(percent);
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
        float max_position = wView - radiusCircle;
        float min_position = radiusCircle;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.xNow = event.getX();
                xLast = this.xNow;
                if (xNow > max_position) {
                    percent = 1;
                } else if (xNow < min_position) {
                    percent = 0;
                } else {
                    percent = (xNow - min_position) / (max_position - min_position);
                }
                if (callBack != null) {
                    callBack.whenStartDragging(percent);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                this.xNow = event.getX();
                float distance_move = this.xNow - xLast;
                if (Math.abs(distance_move) >= min_move) {
                    xLast = this.xNow;
                    if (xNow > max_position) {
                        percent = 1;
                    } else if (xNow < min_position) {
                        percent = 0;
                    } else {
                        percent = (xNow - min_position) / (max_position - min_position);
                    }
                    if (callBack != null) {
                        callBack.whenDragging(percent);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                this.xNow = event.getX();
                if (xNow > max_position) {
                    percent = 1;
                } else if (xNow < min_position) {
                    percent = 0;
                } else {
                    percent = (xNow - min_position) / (max_position - min_position);
                }
                if (callBack != null) {
                    callBack.whenStopDragging(getPercent());
                }
                break;
        }
        postInvalidate();
        return true;
    }


    public interface CallBack {
        void whenStartDragging(float percent);

        void whenDragging(float percent);

        void whenStopDragging(float percent);

    }
}
