package com.test.test3app.gallerypager;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

/**
 * Created by chensuilun on 2016/12/16.
 */
public class ScaleTransformer implements GalleryLayoutManager.ItemTransformer {

    private static final String TAG = "CurveTransformer";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        float maxScaleValue = 1;
        float minScaleValue = 0.5f;
        if (fraction < 0.5f) {
            item.setTranslationZ(10);
        } else {
            item.setTranslationZ(0);
        }
        item.setPivotX(item.getWidth() / 2.0f);
        item.setPivotY(item.getHeight() / 2.0f);
        float scale = (maxScaleValue - minScaleValue) * (1 - Math.abs(fraction)) + minScaleValue;
        item.setScaleX(scale);
        item.setScaleY(scale);
    }
}
