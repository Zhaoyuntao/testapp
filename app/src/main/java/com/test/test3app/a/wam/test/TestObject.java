package com.test.test3app.a.wam.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.test.test3app.a.wam.base.WAMDrawObject;

/**
 * created by zhaoyuntao
 * on 20/05/2022
 * description:
 */
public class TestObject extends WAMDrawObject {
    public TestObject(String id) {
        super(id);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, Paint paint) {
        paint.setColor(Color.BLACK);
        float radius = w / 2f;
        canvas.drawCircle(x + radius, y + radius, radius, paint);
    }

    @Override
    public void calculate(float timeSeconds) {
    }

}
