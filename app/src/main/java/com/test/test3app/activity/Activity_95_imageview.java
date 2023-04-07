package com.test.test3app.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.ChooseFileUtils;

import base.ui.BaseActivity;
import im.turbo.basetools.image.ImageUtils;
import im.turbo.basetools.utils.TakePictureUtils;
import im.turbo.utils.ResourceUtils;

public class Activity_95_imageview extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_95_imageview);

        ImageView image1 = findViewById(R.id.imageview_0);
        ImageView image2 = findViewById(R.id.imageview_1);
        SimpleDraweeView image3=findViewById(R.id.imageview_3);
        image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image3.setImageBitmap(ImageUtils.drawableToBitmap(ResourceUtils.getDrawable(R.drawable.wallpaper2)));
    }
}
