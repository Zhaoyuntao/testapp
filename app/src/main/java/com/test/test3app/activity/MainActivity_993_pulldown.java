package com.test.test3app.activity;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.test.test3app.BaseActivity;
import com.test.test3app.CommonPagerAdapter;
import com.test.test3app.R;


/**
 * Created by qinxue on 2018/8/25.
 */

public class MainActivity_993_pulldown extends BaseActivity {
//    private PullLoadMoreView pullLoadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pull_refresh);

//        pullLoadMoreView = findViewById(R.id.pullLoadMoreView);
//
//        //添加头部布局
//        pullLoadMoreView.addHeadView(R.layout.top_layout);
//        //添加监听open/close
//        pullLoadMoreView.setViewStateListener(new PullLoadMoreView.ViewStateListener() {
//            @Override
//            public void onViewState(PullLoadMoreView.VIewState viewState) {
//                if (viewState == PullLoadMoreView.VIewState.OPEN) {
//                    Toast.makeText(activity(), "Open", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(activity(), "Close", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
//        recyclerView.setAdapter(new CommonAdapter(100));
//        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));

        ViewPager viewPager = findViewById(R.id.viewpager2);
        viewPager.setAdapter(new CommonPagerAdapter(3));
    }

}
