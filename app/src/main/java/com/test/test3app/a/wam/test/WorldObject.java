package com.test.test3app.a.wam.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.test.test3app.a.wam.base.WAMDrawObject;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.TextMeasure;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 20/05/2022
 * description:
 */
public class WorldObject extends WAMDrawObject {
    private List<WAMDrawObject> objects;
    public float f;
    public float fAir;
    public float g;
    public float powerTime;
    public float power;
    public float angle;
    public float powerMax;
    public float onePixel;


    public WorldObject(String id) {
        super(id);
    }

    public List<WAMDrawObject> getObjects() {
        return objects;
    }

    public void setObjects(List<WAMDrawObject> objects) {
        this.objects = objects;
    }

    @Override
    public void setWH(float w, float h) {
        super.setWH(w, h);
        powerMax = w * POWER_MAX;
        onePixel = 100;
        if (objects != null) {
            for (WAMDrawObject object : objects) {
                object.setXY(w / 2, h / 2);
                float size = w * 0.1f;
                object.setWH(size, size);
            }
        }
    }

    @Override
    public void calculate(float timeSeconds) {
        if (powerTime > 0) {
            powerTime -= timeSeconds;
            if (powerTime <= 0) {
                power = 0;
            }
        }
        if (power > powerMax) {
            power = powerMax;
        }
        float powerY = 0;//-(float) (power * Math.cos(Math.toRadians(angle)));
        float powerX = (float) (power * Math.sin(Math.toRadians(angle)));
        for (WAMDrawObject object : objects) {
            final float xChild = object.getX();
            final float yChild = object.getY();
            final float wChild = object.getW();
            final float hChild = object.getH();
            final float xSpeed = object.getSpeedX();
            final float ySpeed = object.getSpeedY();
            final float elasticity = object.getE();
            final float yGround = h - hChild;
            final float xWallRight = w - wChild;
            final float xWallLeft = 0;
            final boolean isInAirNow = yChild < yGround;
            float friction;
            if (isInAirNow) {
                friction = fAir;
            } else {
                friction = f;
            }

            float accX = powerX + friction;
            float speedXCurrent = xSpeed + accX;
            float currentDistanceX = speedXCurrent * timeSeconds;
            float xNext = xChild + currentDistanceX;
            if (xNext > xWallRight) {
                xNext = xWallRight;
                speedXCurrent *= -1;
            } else if (xNext < xWallLeft) {
                xNext = xWallLeft;
                speedXCurrent *= -1;
            }

            float accY = powerY + g;
            float speedYCurrent = ySpeed + accY;
            float currentDistanceY = speedYCurrent * timeSeconds;
            float yNext = yChild + currentDistanceY;
            if (yNext > yGround) {
                yNext = yGround;
            }
            if (yNext >= yGround) {
                speedYCurrent *= -elasticity;
            }
            object.setSpeedXY(speedXCurrent, speedYCurrent);
            object.setXY(xNext, yNext);
        }
    }

    public void draw(Canvas canvas, int width, int height, Paint paint) {
        if (objects == null) {
            return;
        }
        canvas.drawColor(Color.rgb(230, 230, 230));
        float speedX = 0;
        float speedY = 0;
        float heightOfChild = 0;
        for (WAMDrawObject object : objects) {
            speedX = object.getSpeedX();
            speedY = object.getSpeedY();
            object.draw(canvas, width, height, paint);
            heightOfChild = object.getY();
        }

        paint.setColor(Color.BLACK);
        int textSize = 50;
        paint.setTextSize(textSize);
        String text = "Speed x:" + S.formatNumber(speedX, "#.##") + " y:" + S.formatNumber(speedY) + " height:" + heightOfChild;
        float[] size = TextMeasure.measure(text, textSize);
        canvas.drawText(text, 0, size[1] * 1.5f, paint);
    }
}
