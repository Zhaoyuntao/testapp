package com.test.test3app.pagerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public class ZPagerView extends ViewPager {
    private List<OnScrollListener> onScrollListeners;

    public ZPagerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ZPagerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOffscreenPageLimit(3);
    }

    public void addOnScrollListener(OnScrollListener onScrollListener) {
        if (onScrollListeners == null) {
            onScrollListeners = new ArrayList<>();
        }
        this.onScrollListeners.add(onScrollListener);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int scrollXOld, int scrollYOld) {
        super.onScrollChanged(scrollX, scrollY, scrollXOld, scrollYOld);
        if (onScrollListeners != null && onScrollListeners.size() > 0) {
            for (OnScrollListener onScrollListener : onScrollListeners) {
                if (onScrollListener == null) {
                    continue;
                }
                onScrollListener.onScrollChange(this, scrollX, scrollY, scrollXOld, scrollYOld);
            }
        }
    }

    public interface OnScrollListener {
        void onScrollChange(ViewPager scrollView, int x, int y, int oldx, int oldy);
    }
}
