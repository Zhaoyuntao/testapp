package com.test.test3app.layoutmanager;

import android.os.Bundle;

import base.ui.BaseActivity;
import com.test.test3app.R;

public class LoopGalleryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_gallery);
        BaseLoopGallery alyLoopGallery = (BaseLoopGallery) findViewById(R.id.alyLoopGallery);
        alyLoopGallery.setDatasAndLayoutId();
    }
}
