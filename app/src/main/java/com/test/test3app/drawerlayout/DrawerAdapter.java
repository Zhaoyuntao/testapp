package com.test.test3app.drawerlayout;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Random;


/**
 * created by zhaoyuntao
 * on 2019-11-12
 * description:
 */
public class DrawerAdapter extends PagerAdapter {

    public DrawerAdapter() {
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView textView = new TextView(container.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
        textView.setBackgroundColor(Color.rgb(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)));
        textView.setText(String.valueOf(position));
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        container.addView(textView);
        return textView;
    }
}
