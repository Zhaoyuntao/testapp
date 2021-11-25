package com.test.test3app.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.test.test3app.BaseActivity;
import com.test.test3app.CashGiftThemeAdapter;
import com.test.test3app.CommonAdapter;
import com.test.test3app.CommonPagerAdapter;
import com.test.test3app.R;
import com.test.test3app.gallerypager.ScaleTransformer;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class MainActivity_994_gallery_pager extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity994_gallery_pager);

        RecyclerView viewPager = findViewById(R.id.view_pager_cash_gift_theme);
        GalleryLayoutManager layoutManager1 = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        layoutManager1.attach(viewPager, 0);
        layoutManager1.setItemTransformer(new ScaleTransformer());

        viewPager.setAdapter(new CashGiftThemeAdapter());
    }
}