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
import im.turbo.utils.log.S;

public class ThirdActivity extends BaseActivity {
    public static ImageBean imageBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        prepareTransitions();

        ViewPager2 viewPager2 = findViewById(R.id.recycler_view_third);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        ThirdImageAdapter adapter = new ThirdImageAdapter();
        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(3);

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

//        S.s("first time load ----------------->");
        List<ImageBean> imageBeans = new ArrayList<>(4);
        imageBeans.add(new ImageBean("image1", R.drawable.wallpaper2));
        adapter.submitList(imageBeans);


        viewPager2.post(new Runnable() {
            @Override
            public void run() {
                startPostponedEnterTransition();
            }
        });

        ThreadPool.runIoDelayed(1000, new Runnable() {
            @Override
            public void run() {
//                ToastUtil.show("db load finish");
//                S.s("second time load ----------------->");
                List<ImageBean> imageBeans = new ArrayList<>(4);
                imageBeans.add(new ImageBean("image0", R.drawable.image1));
                imageBeans.add(new ImageBean("image1", R.drawable.wallpaper2));
                imageBeans.add(new ImageBean("image2", R.drawable.image2));
                imageBeans.add(new ImageBean("image3", R.drawable.image3));

                int position = imageBeans.indexOf(imageBean);
                adapter.submitList(imageBeans);
            }
        });
    }

    private void prepareTransitions() {
        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                sharedElements.clear();
            }
        });
//        setExitSharedElementCallback(
//                new SharedElementCallback() {
//                    @Override
//                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//                        S.s("sharedElements:"+sharedElements);
//                        sharedElements.clear();
////                        // Locate the ViewHolder for the clicked position.
////                        RecyclerView.ViewHolder selectedViewHolder = recyclerView
////                                .findViewHolderForAdapterPosition(MainActivity.currentPosition);
////                        if (selectedViewHolder == null) {
////                            return;
////                        }
////
////                        // Map the first shared element name to the child ImageView.
////                        sharedElements
////                                .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.card_image));
//                    }
//                });
    }
}