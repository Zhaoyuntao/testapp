package com.test.test3app.recyclerview;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 2/2/21
 * description:
 */
public class CountryCodeText extends AppCompatTextView {
    public CountryCodeText(Context context) {
        super(context);
        init();
    }

    public CountryCodeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountryCodeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Context context = getContext();
        context.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(@NonNull Configuration newConfig) {
                S.s("onConfigurationChanged:" + getText());
            }

            @Override
            public void onLowMemory() {

            }
        });
    }

    @Override
    public void destroyDrawingCache() {
//        S.e("destroyDrawingCache:"+getText());
        super.destroyDrawingCache();
    }

    @Override
    protected void onAttachedToWindow() {
//        S.s("onAttachedToWindow:" + getText());
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
//        S.e("onDetachedFromWindow:" + getText());
        super.onDetachedFromWindow();
    }
}
