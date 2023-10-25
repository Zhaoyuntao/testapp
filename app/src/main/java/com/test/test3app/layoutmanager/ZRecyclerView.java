package com.test.test3app.layoutmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 05/12/2021
 * description:
 */
public class ZRecyclerView extends RecyclerView {
    private Interpolator interpolator;
    private static final int POW = 2;

    public ZRecyclerView(@NonNull Context context) {
        super(context);
        createInterpolator();
    }

    public ZRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createInterpolator();
    }

    public ZRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createInterpolator();
    }

    private void createInterpolator() {
        interpolator = new Interpolator() {
            @Override
            public float getInterpolation(float t) {
                S.s("t:"+t);
                t = Math.abs(t - 1.0f);
                return (float) (1.0f - Math.pow(t, POW));
            }
        };
    }

//    @Override
//    public void smoothScrollBy(int dx, int dy) {
//        super.smoothScrollBy(dx, dy, interpolator);
//    }


//    @Override
//    public boolean fling(int velocityX, int velocityY) {
//        S.s("vel:"+velocityX);
//        velocityX=velocityX/10;
//        velocityY=velocityY/10;
//        if(velocityX<100){
//            return false;
//        }
//        return super.fling(velocityX, velocityY);
//    }
}
