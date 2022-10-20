package com.test.test3app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import com.test.test3app.windowtransition.ImageAdapter;
import com.test.test3app.windowtransition.ImageBean;
import com.test.test3app.windowtransition.ListViewAdapter;
import com.test.test3app.windowtransition.ThirdActivity;

import java.util.ArrayList;
import java.util.List;

import base.ui.BaseActivity;

public class Activity_9992_windowContentTransition extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9992_window_content_transitions);

//        prepareTransitions();
        List<ImageBean> imageBeans = new ArrayList<>(4);
        imageBeans.add(new ImageBean("0", 0));
        imageBeans.add(new ImageBean("1", 1));
        imageBeans.add(new ImageBean("2", 2));
        imageBeans.add(new ImageBean("3", 3));
        imageBeans.add(new ImageBean("4", 4));
        imageBeans.add(new ImageBean("5", 5));
        imageBeans.add(new ImageBean("6", 6));
        imageBeans.add(new ImageBean("7", 7));
        imageBeans.add(new ImageBean("8", 8));

        RecyclerView recyclerView = findViewById(R.id.window_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        ImageAdapter commonAdapter = new ImageAdapter();
        commonAdapter.submitList(imageBeans);
        commonAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onClick(view, position);
            }
        });

        recyclerView.setAdapter(commonAdapter);


        ListView listView = findViewById(R.id.window_list_view);
        ListViewAdapter viewAdapter = new ListViewAdapter();
        viewAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onClick(view, position);
            }
        });
        viewAdapter.setData(imageBeans);

        listView.setAdapter(viewAdapter);

        ViewGroup containerRecycler = findViewById(R.id.container_recycler);
        ViewGroup containerList = findViewById(R.id.container_list_view);

        findViewById(R.id.recycler_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerRecycler.setVisibility(View.VISIBLE);
                containerList.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.list_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerRecycler.setVisibility(View.GONE);
                containerList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onClick(View view, int position) {
        Intent intent = new Intent(activity(), ThirdActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity(), view, "image0").toBundle();
        intent.putExtra("position", position);
        startActivity(intent, bundle);
    }
}
