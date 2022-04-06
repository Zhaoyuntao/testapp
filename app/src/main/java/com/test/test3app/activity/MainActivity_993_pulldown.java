package com.test.test3app.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.fastrecordviewnew.UiUtils;
import com.test.test3app.scrollView.SlideAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * created by zhaoyuntao
 * on 2020-03-25
 * description:
 */

public class MainActivity_993_pulldown extends BaseActivity {
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
                int d = new Random().nextInt(10) + 1;
                for (int j = 0; j < d; j++) {
                    s.append("a");
                }
            }
            list.add("[" + String.valueOf(i) + "] " + s);
        }
        SlideAdapter adapter = new SlideAdapter(list);
        recyclerView.setAdapter(adapter);


        EditText editText = findViewById(R.id.edittext_slide);

        TextView textView = findViewById(R.id.slayouttext);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(s);
            }
        });

    }

}
