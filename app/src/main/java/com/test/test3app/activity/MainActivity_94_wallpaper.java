package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.BaseActivity;
import com.test.test3app.CommonAdapter;
import com.test.test3app.R;
import com.test.test3app.wallpaper.AdapterImageView;
import com.test.test3app.wallpaper.RippleFrameLayout;

public class MainActivity_94_wallpaper extends BaseActivity {

    private AdapterImageView adapterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_94_wallpaper);

        adapterImageView = findViewById(R.id.wall);

        ImageView imageView = findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            boolean a = false;

            @Override
            public void onClick(View v) {
                a = !a;
                imageView.setImageResource(a ? R.drawable.a1 : R.drawable.a2);
            }
        });

        boolean reverseLayout = true;
        boolean stackFromEnd = false;

        RecyclerView recyclerView1 = findViewById(R.id.recycler_view1);
        RecyclerView recyclerView2 = findViewById(R.id.recycler_view2);
//        recyclerView.setAdapter(new CommonAdapter(4));
//        recyclerView.setAdapter(new CommonAdapter(6));
        recyclerView1.setAdapter(new CommonAdapter(6));

        recyclerView2.setAdapter(new CommonAdapter(400));

//        GifDrawable gifDrawable=null;
//        GifDrawableBuilder builder = new GifDrawableBuilder();
//
//        try {
//            gifDrawable = builder.from(getResources(),R.raw.bbbb).setRenderingTriggeredOnDraw(true).build();
//            adapterImageView.setImageDrawable(gifDrawable);
//        } catch (Exception e) {
//            e.printStackTrace();
//            S.e(e);
//        }

        RippleFrameLayout rippleFrameLayout = findViewById(R.id.scale);
        TextView showButton = findViewById(R.id.show);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleFrameLayout.setVisibility(rippleFrameLayout.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
            }
        });
//
//        findViewById(R.id.image1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setVisibility(rippleFrameLayout.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
//            }
//        });
//        findViewById(R.id.image2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setVisibility(rippleFrameLayout.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
//            }
//        });
//        findViewById(R.id.image3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setVisibility(rippleFrameLayout.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
//            }
//        });
    }
}
