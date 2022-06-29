package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import base.ui.BaseActivity;
import com.test.test3app.CommonAdapter;
import com.test.test3app.CommonBean;
import com.test.test3app.R;
import com.test.test3app.wallpaper.AdapterImageView;
import com.test.test3app.wallpaper.SessionLayoutManager;

import im.turbo.baseui.chat.ChatRecyclerView;

public class Activity_94_wallpaper extends BaseActivity {

    private AdapterImageView adapterImageView;
    int lastOffset;
    int lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_94_wallpaper);

        adapterImageView = findViewById(R.id.wall);

        View actionbar = findViewById(R.id.actionbar);
        actionbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        boolean reverseLayout = true;
        boolean stackFromEnd = false;


        ChatRecyclerView chatView = findViewById(R.id.recycler_view1);
        ChatRecyclerView chatView2 = findViewById(R.id.recycler_view2);
        CommonAdapter commonAdapter = new CommonAdapter(0);
        chatView.setAdapter(commonAdapter);
        CommonAdapter commonAdapter2 = new CommonAdapter(8);
//        chatView2.setAdapter(commonAdapter2);

        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CommonBean commonBean, int position) {
                commonAdapter.selectItem(commonBean, position);
            }
        });


        TextView add6 = findViewById(R.id.add6);
        TextView add100 = findViewById(R.id.add100);
        TextView jump = findViewById(R.id.jump);
        TextView change = findViewById(R.id.change);
        add6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                commonAdapter.initData(6);
            }
        });
        add100.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chatView.setScrollToPosition(12, 0);
                commonAdapter.initData(100);
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chatView.setScrollToPosition(12, 0);
            }
        });


        RecyclerView chatView3 = findViewById(R.id.recycler_view3);
        LinearLayoutManager linearLayoutManager = new SessionLayoutManager(chatView3.getContext());
//        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
        chatView3.setItemAnimator(null);
        chatView3.setLayoutManager(linearLayoutManager);
        int fromP = 1;
        int toP = 0;
        CommonAdapter commonAdapter3 = new CommonAdapter(20);
        commonAdapter3.setColor(String.valueOf(fromP), Color.parseColor("#dd009900"));
        commonAdapter3.setColor(String.valueOf(toP), Color.parseColor("#dd000099"));
        chatView3.setAdapter(commonAdapter3);
        change.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
//                initPositionAndOffset();
//                List<CommonBean> list = new ArrayList<>(commonAdapter3.getCurrentList());
//                CommonBean c0 = list.remove(fromP);
//                list.add(toP, c0);
//                commonAdapter3.submitList(list);
            }
        });
        chatView.setScrollToPosition(12, 0);
        commonAdapter.initData(8);

        EditText editText = findViewById(R.id.testtest2);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            boolean add;

            @Override
            public void onClick(View v) {
                if (add) {
                    add = false;
                    commonAdapter.add(new CommonBean("hello", 0));
                } else {
                    add = true;
                    commonAdapter.remove();
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
