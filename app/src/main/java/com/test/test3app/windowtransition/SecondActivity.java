package com.test.test3app.windowtransition;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import com.test.test3app.recyclerview.ChatLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        int position = getIntent().getIntExtra("position", -1);
        List<ImageBean> imageBeans = new ArrayList<>(4);
        imageBeans.add(new ImageBean("image0", R.drawable.image0));
        imageBeans.add(new ImageBean("image1", R.drawable.image1));
        imageBeans.add(new ImageBean("image2", R.drawable.image2));
        imageBeans.add(new ImageBean("image3", R.drawable.image3));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_second);

        ImageLayoutManager layoutManager = new ImageLayoutManager(recyclerView.getContext());
        if (position < 3) {
            layoutManager.setScrollToPosition(position, 0);
        }
        recyclerView.setLayoutManager(layoutManager);
        SecondImageAdapter adapter = new SecondImageAdapter();
        adapter.submitList(imageBeans);
        recyclerView.setAdapter(adapter);

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                SecondImageHolder secondImageHolder0 = position == 0 || position == 3 ? (SecondImageHolder) recyclerView.findViewHolderForAdapterPosition(0) : null;
                SecondImageHolder secondImageHolder1 = position == 1 || position == 3 ? (SecondImageHolder) recyclerView.findViewHolderForAdapterPosition(1) : null;
                SecondImageHolder secondImageHolder2 = position == 2 || position == 3 ? (SecondImageHolder) recyclerView.findViewHolderForAdapterPosition(2) : null;
                if (secondImageHolder0 != null) {
                    sharedElements.put("image0", secondImageHolder0.itemView);
                }
                if (secondImageHolder1 != null) {
                    sharedElements.put("image1", secondImageHolder1.itemView);
                }
                if (secondImageHolder2 != null) {
                    sharedElements.put("image2", secondImageHolder2.itemView);
                }
            }
        });
    }
}