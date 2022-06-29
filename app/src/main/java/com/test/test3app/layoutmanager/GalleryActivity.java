package com.test.test3app.layoutmanager;

import android.os.Bundle;

import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import base.ui.BaseActivity;
import com.test.test3app.CommonImageAdapter;
import com.test.test3app.R;


public class GalleryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ZRecyclerView recyclerView = (ZRecyclerView) findViewById(R.id.rv);

        recyclerView.setAdapter(new CommonImageAdapter());

        recyclerView.setLayoutManager(new GalleryLayoutManagerNew());
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity(),RecyclerView.HORIZONTAL,false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setItemViewCacheSize(0);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }
}
