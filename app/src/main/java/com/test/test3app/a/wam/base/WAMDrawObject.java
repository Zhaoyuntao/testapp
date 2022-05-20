package com.test.test3app.a.wam.base;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * created by zhaoyuntao
 * on 20/05/2022
 * description:
 */
public abstract class WAMDrawObject extends WAMObject {
    private float speedX;
    private float speedY;

    public WAMDrawObject(String id) {
        super(id);
    }

    public abstract void draw(Canvas canvas, int width, int height, Paint paint);

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedXY(float speedX, float speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public abstract void calculate(float timeSeconds);

    public float getSpeedY() {
        return speedY;
    }

}
