package com.test.test3app.pagerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public class MainTabPagerAdapter extends PagerAdapter {
    private PagerInflater pagerInflater;

    public MainTabPagerAdapter(PagerInflater pagerInflater) {
        this.pagerInflater = pagerInflater;
    }

    @Override
    public int getCount() {
        return pagerInflater.getCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = pagerInflater.getPageLayout(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface PagerInflater {
        int getCount();

        View getPageLayout(int position);
    }
}
