package com.test.test3app.fastrecordviewnew;

import android.view.MotionEvent;

/**
 * created by zhaoyuntao
 * on 2019-10-24
 * description:
 */
public interface TouchEvent {

    void whenActionDown();

    void whenActionUp();

    void whenLongClickDown(float x, float y);

    void whenLongClickUp(MotionEvent event);

    void whenPressAndMove(MotionEvent event);
}
