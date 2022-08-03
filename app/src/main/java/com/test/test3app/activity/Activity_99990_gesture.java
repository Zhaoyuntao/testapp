package com.test.test3app.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.test.test3app.R;
import com.test.test3app.viewpager2.ImageModel;
import com.test.test3app.viewpager2.ViewPager2Adapter;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;

import base.ui.BaseActivity;

public class Activity_99990_gesture extends BaseActivity {

    private ViewPager2 viewPager2;
    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_99990_gesture);

        viewPager2 = findViewById(R.id.view_pager2_gesture);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        ViewPager2Adapter adapter = new ViewPager2Adapter();
        viewPager2.setAdapter(adapter);

        List<ImageModel> imageModels = new ArrayList<>();
        imageModels.add(new ImageModel("5", R.drawable.wallpaper2));
        adapter.submitList(imageModels);

        findViewById(R.id.text_view_reload_gesture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ImageModel> imageModels = new ArrayList<>();
                imageModels.add(new ImageModel("0", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("1", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("2", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("3", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("4", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("5", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("6", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("7", R.drawable.wallpaper2));
                imageModels.add(new ImageModel("8", R.drawable.wallpaper2));
                adapter.submitList(imageModels);
                viewPager2.post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager2.setCurrentItem(5);
                    }
                });
            }
        });

        gestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                S.s("fling");
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!super.dispatchTouchEvent(ev)) {
            return gestureDetectorCompat.onTouchEvent(ev);
        } else {
            return true;
        }
    }
}
