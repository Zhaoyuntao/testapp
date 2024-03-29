package com.test.test3app.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import base.ui.BaseActivity;
import com.test.test3app.R;
import im.turbo.utils.log.S;
import im.turbo.baseui.utils.UiUtils;
import com.test.test3app.scrollView.SlideAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * created by zhaoyuntao
 * on 2020-03-25
 * description:
 */

public class Activity_993_slide extends BaseActivity {
//    private PullLoadMoreView pullLoadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pull_refresh);

        RecyclerView recyclerView = findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private float x;
            private float xLast, xDown;

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent ev) {

                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDown = ev.getRawX();
                        xLast = xDown;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = ev.getRawX();
                        xLast = x;
                        // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                        if (Math.abs(x - xDown) > UiUtils.dipToPx(10)) {
                            recyclerView.requestDisallowInterceptTouchEvent(true);
                        } else {
                            recyclerView.requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        int count = 100;
        List<String> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            StringBuilder s = new StringBuilder();
            if (i > 10) {
                int d = 50;//new Random().nextInt(10) + 1;
                for (int j = 0; j < d; j++) {
                    s.append("a");
                }
            }
            list.add("[" + String.valueOf(i) + "] " + s);
        }
        SlideAdapter adapter = new SlideAdapter(list);
        adapter.setOnItemClickListener(new SlideAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String String) {
                S.s("click item");
            }
        });
        recyclerView.setAdapter(adapter);


        EditText editText = findViewById(R.id.edittext_slide);

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
