package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import base.ui.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.stickerreply.StickerRepliedParticipantItemBean;
import com.test.test3app.stickerreply.TestStickerAdapter;

public class Activity_98_sticker_reply extends BaseActivity {

    private TestStickerAdapter stickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sticker_reply);

        findViewById(R.id.button_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                init();
            }
        });
        findViewById(R.id.button_add1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add("\uD83D\uDE0A");
            }
        });
        findViewById(R.id.button_add2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add("\uD83D\uDE04");
            }
        });
        findViewById(R.id.button_add3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add("\uD83D\uDC4C");
            }
        });
        findViewById(R.id.button_add4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add("\uD83D\uDC36");
            }
        });
        findViewById(R.id.button_add5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add("⬆️");
            }
        });

        stickerAdapter = new TestStickerAdapter();
        RecyclerView recyclerView = findViewById(R.id.sticker_test_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));
        recyclerView.setAdapter(stickerAdapter);
    }

    private void add(String sticker) {
        StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean = new StickerRepliedParticipantItemBean();
        stickerRepliedParticipantItemBean.setSticker(sticker);
        stickerRepliedParticipantItemBean.setName("abcd");
        stickerRepliedParticipantItemBean.setUid("abcd");
        stickerRepliedParticipantItemBean.setTime(System.currentTimeMillis());
        stickerAdapter.debugAdd(stickerRepliedParticipantItemBean);
    }

}