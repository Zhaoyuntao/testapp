package com.test.test3app.a.wam.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.test.test3app.a.wam.base.WAMDrawObject;
import im.turbo.utils.log.S;
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
        float power, powerX, powerY;
        if (this.power > 0) {
            power = this.power;
            if (power > powerMax) {
                power = powerMax;
            } else if (power < 0) {
                power = -1;
            }
            powerY = -(float) (power * Math.cos(Math.toRadians(angle)));
            powerX = (float) (power * Math.sin(Math.toRadians(angle)));
        } else {
            power = 0;
            powerX = 0;
            powerY = 0;
        }
        final float ySky = 0;
        fAir = f * 0.1f;
        for (WAMDrawObject object : objects) {
            final float xChild = object.getX();
            final float yChild = object.getY();
            final float wChild = object.getW();
            final float hChild = object.getH();
            final float speedXChild = object.getSpeedX();
            final float speedYChild = object.getSpeedY();
            final float elasticity = object.getE();
            final float yGround = h - hChild;
            final float xWallRight = w - wChild;
            final float xWallLeft = 0;
            final boolean isInAirNow = yChild < yGround;
            float friction = isInAirNow ? fAir : f;
            float speedXAndCordsX[] = speedAndCords(powerX, speedXChild, friction, 0, elasticity, xChild, timeSeconds, xWallLeft, xWallRight, false);
            float speedYAndCordsY[] = speedAndCords(powerY, speedYChild, friction, g, elasticity, yChild, timeSeconds, ySky, yGround, true);

            float speedX = speedXAndCordsX[0];
            float xNext = speedXAndCordsX[1];
            float speedY = speedYAndCordsY[0];
            float yNext = speedYAndCordsY[1];
            object.setSpeedXY(speedX, speedY);
            object.setXY(xNext, yNext);
        }
    }

    private float[] speedAndCords(final float power, final float speedBefore, final float friction, final float extraForce, final float elasticity, final float cordChild, final float timeSeconds, final float cordStart, final float cordEnd, boolean open) {
        float acc;
        if (speedBefore > 0) {
            acc = -friction + extraForce;
        } else if (speedBefore < 0) {
            acc = friction + extraForce;
        } else {
            if (extraForce > 0) {
                if (cordChild < cordEnd) {
                    acc = extraForce;
                } else {
                    acc = 0;
                }
            } else if (extraForce < 0) {
                if (cordChild > cordStart) {
                    acc = extraForce;
                } else {
                    acc = 0;
                }
            } else {
                acc = 0;
            }
        }
        acc += power;
        float speedNext = speedBefore + acc;
        float distanceMove = speedNext * timeSeconds;
        if (Math.abs(speedNext) < 10) {
            speedNext = 0;
        }
        float cordNext = cordChild + distanceMove;
        if (cordNext >= cordEnd) {
            speedNext *= -elasticity;
            cordNext = cordEnd;
        } else if (cordNext <= cordStart) {
            speedNext *= -elasticity;
            cordNext = cordStart;
        }
        if (open) {
            if (speedNext > 0) {
                S.s("acc:" + acc + "   speedNext:" + speedNext + " ");
            }
        }
        return new float[]{speedNext, cordNext};
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
