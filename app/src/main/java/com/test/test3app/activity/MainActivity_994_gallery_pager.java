package com.test.test3app.activity;

import android.os.Bundle;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.layoutmanager.GalleryActivity;
import com.test.test3app.layoutmanager.LoopGalleryActivity;
import com.test.test3app.layoutmanager.TanTanAvatarActivity;

public class MainActivity_994_gallery_pager extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity994_gallery_pager);

//        RecyclerView recyclerView = findViewById(R.id.view_pager_cash_gift_theme);
//        GalleryLayoutManager layoutManager1 = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
//        layoutManager1.attach(viewPager, 0);
//        layoutManager1.setItemTransformer(new ScaleTransformer());
//        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
//        recyclerView.setAdapter(
//                InfiniteScrollAdapter.wrap(
//                        new CashGiftThemeAdapter()
//                )
//        );
//        recyclerView.setItemTransitionTimeMillis(150);
//        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
//                .setMinScale(1f)
//                .setMaxScale(2f)
//                .build());
//        recyclerView.setAdapter(new CommonAdapter(100));
//        goToActivity(LoopGalleryActivity.class);
//        goToActivity(GalleryActivity.class);
    }
}