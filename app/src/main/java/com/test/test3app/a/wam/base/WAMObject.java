package com.test.test3app.a.wam.base;

/**
 * created by zhaoyuntao
 * on 20/05/2022
 * description:
 */
public class WAMObject {
    protected final String id;
    private String description;
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected float e;//elasticity
    public static final float POWER_MAX = 10;

    public WAMObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setWH(float w, float h) {
        this.w = w;
        this.h = h;
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public float getE() {
        return e;
    }

    public void setE(float e) {
        this.e = e;
    }
}
