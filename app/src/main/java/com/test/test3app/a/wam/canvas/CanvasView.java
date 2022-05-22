package com.test.test3app.a.wam.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.test3app.a.wam.test.WorldObject;

/**
 * created by zhaoyuntao
 * on 20/05/2022
 * description:
 */
public class CanvasView extends View {
    private WorldObject worldObject;
    private int width, height;
    private Paint paint;
    private Thread thread;
    private volatile boolean run;
    private volatile long timeMills;
    private float slowPercent;

    public CanvasView(Context context) {
        super(context);
        init(null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
    }

    private void start() {
        stop();
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (run) {
                    long now = SystemClock.elapsedRealtime();
                    if (timeMills == 0) {
                        timeMills = now;
                        continue;
                    }
                    int timeMillsPass = (int) (now - timeMills);
                    float timePass = timeMillsPass / 1000f * slowPercent;
                    timeMills = now;
                    calculate(timePass);
                    postInvalidate();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignore) {
                    }
                }
            }
        });
        run = true;
        thread.start();
    }

    private void calculate(float timeMills) {
        if (worldObject == null) {
            return;
        }
        worldObject.calculate(timeMills);
    }

    private void stop() {
        run = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void setWorldObject(WorldObject worldObject) {
        this.worldObject = worldObject;
        initWorldSize(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        initWorldSize(width, height);
    }

    private void initWorldSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (worldObject != null) {
            worldObject.setWH(width, height);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (worldObject == null) {
            return;
        }
        if (width == 0 || height == 0) {
            return;
        }
        worldObject.draw(canvas, width, height, paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    public void destroyDrawingCache() {
        stop();
        super.destroyDrawingCache();
    }

    public void setSlowPercent(float slowPercent) {
        this.slowPercent = slowPercent;
    }
}
