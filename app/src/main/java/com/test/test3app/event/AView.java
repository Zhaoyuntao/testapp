package com.test.test3app.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * created by zhaoyuntao
 * on 06/06/2022
 * description:
 */
public class AView extends View implements ViewInterface {
    private long time;
    private String content;

    public AView(Context context) {
        super(context);
    }

    public AView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                s("View dispatchTouchEvent down");
                break;
            case MotionEvent.ACTION_MOVE:
//                s("View dispatchTouchEvent move");
                break;
            case MotionEvent.ACTION_UP:
//                s("View dispatchTouchEvent up");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                s("View onTouchEvent down");
//                return true;
                break;
            case MotionEvent.ACTION_MOVE:
//                s("View onTouchEvent move");
                break;
            case MotionEvent.ACTION_UP:
//                s("View onTouchEvent up");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String a) {
        this.content = a;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }
}
