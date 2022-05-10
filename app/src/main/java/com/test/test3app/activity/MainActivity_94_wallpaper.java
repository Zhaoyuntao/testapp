package com.test.test3app.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.test3app.BaseActivity;
import com.test.test3app.CommonAdapter;
import com.test.test3app.CommonBean;
import com.test.test3app.R;
import com.test.test3app.recyclerview.ChatView;
import com.test.test3app.wallpaper.AdapterImageView;

public class MainActivity_94_wallpaper extends BaseActivity {

    private AdapterImageView adapterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_94_wallpaper);

        adapterImageView = findViewById(R.id.wall);

        View actionbar = findViewById(R.id.actionbar);

        boolean reverseLayout = true;
        boolean stackFromEnd = false;

        ChatView chatView = findViewById(R.id.recycler_view1);
        ChatView chatView2 = findViewById(R.id.recycler_view2);
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
        add6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                commonAdapter.initData(6);
            }
        });
        add100.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chatView.setPreScrollPosition(12, 0);
                commonAdapter.initData(100);
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chatView.scrollToPositionWithOffset(12, 0);
            }
        });
        chatView.setPreScrollPosition(12, 0);
        commonAdapter.initData(50);

        EditText editText = findViewById(R.id.testtest2);

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
