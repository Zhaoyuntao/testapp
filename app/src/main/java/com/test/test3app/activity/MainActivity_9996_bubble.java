package com.test.test3app.activity;

import android.os.Bundle;
import android.view.Gravity;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.mention.ChatEditText;

import im.thebot.chat.ui.view.BubbleView;


public class MainActivity_9996_bubble extends BaseActivity {
    ChatEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9996_bubble);

        BubbleView bubbleView1 = findViewById(R.id.bubble_view_1);
        bubbleView1.setGravity(Gravity.START, true);

    }
}
