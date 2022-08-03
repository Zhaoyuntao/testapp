package com.test.test3app.viewpager2;

import androidx.annotation.DrawableRes;

/**
 * created by zhaoyuntao
 * on 03/08/2022
 * description:
 */
public class ImageModel {
    public String id;
    @DrawableRes
    public int drawableRes;

    public ImageModel(String id, int drawableRes) {
        this.id = id;
        this.drawableRes = drawableRes;
    }
}
