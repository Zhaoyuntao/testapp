package com.test.test3app.windowtransition;

import android.os.Bundle;
import android.view.View;

import androidx.core.app.SharedElementCallback;
import androidx.viewpager2.widget.ViewPager2;

import com.test.test3app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ui.BaseActivity;
import im.turbo.baseui.toast.ToastUtil;
import im.turbo.thread.ThreadPool;

public class ThirdActivity extends BaseActivity {
    public static ImageBean imageBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        ViewPager2 viewPager2 = findViewById(R.id.recycler_view_third);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        ThirdImageAdapter adapter = new ThirdImageAdapter();
        viewPager2.setAdapter(adapter);

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                View page0 = viewPager2.getChildAt(0);
                if (page0 != null) {
                    sharedElements.put("image0", page0);
                }
            }
        });

        getWindow().getSharedElementEnterTransition().setDuration(200);
        getWindow().getSharedElementExitTransition().setDuration(200);
        postponeEnterTransition();

        ThreadPool.runIoDelayed(500, new Runnable() {
            @Override
            public void run() {
                ToastUtil.show("db load finish");
                List<ImageBean> imageBeans = new ArrayList<>(4);
                imageBeans.add(new ImageBean("image0", R.drawable.image0));
                imageBeans.add(new ImageBean("image1", R.drawable.image1));
                imageBeans.add(new ImageBean("image2", R.drawable.image2));
                imageBeans.add(new ImageBean("image3", R.drawable.image3));

                int position = imageBeans.indexOf(imageBean);

                viewPager2.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.submitList(imageBeans);
                        viewPager2.post(new Runnable() {
                            @Override
                            public void run() {
                                viewPager2.setCurrentItem(position);
                                startPostponedEnterTransition();
                            }
                        });
                    }
                });
            }
        });
    }
}