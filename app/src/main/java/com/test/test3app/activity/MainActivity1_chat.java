package com.test.test3app.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.mvp.presenter.ChatPresenter;
import im.thebot.chat.ui.adapter.ChatAdapter;
import im.turbo.baseui.chat.ChatLayoutManager;

public class MainActivity1_chat extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        RecyclerView chatView = findViewById(R.id.chat_view);
        chatView.setItemAnimator(null);
        chatView.setLayoutManager(new ChatLayoutManager(activity()));
        ChatPresenter chatPresenter = new ChatPresenter();
        ChatAdapter chatAdapter = new ChatAdapter(chatPresenter);
        chatAdapter.setMessageCallback(new ChatAdapter.OnMessageClickListener() {
            @Override
            public void onMessageBindToAdapter(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public boolean canClickMessage(@NonNull MessageBeanForUI messageBean) {
                return false;
            }

            @Override
            public void onClickReplyMessage(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public boolean onLongClickReplyMessage(@NonNull MessageBeanForUI messageBean) {
                return false;
            }

            @Override
            public void onNameClick(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public void onClickForwardButton(@NonNull MessageBeanForUI message) {

            }

            @Override
            public void onSlideMessage(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public void onSelectItem(@NonNull MessageBeanForUI messageBean, boolean selected) {

            }
        });
        chatView.setAdapter(chatAdapter);

        EditText editText1 = findViewById(R.id.edittext1);
        EditText editText2 = findViewById(R.id.edittext2);

//        String text1 = "0000000";
//        String text2 = "000\n000021\n9938\n8121";
//        editText1.setText(text1);
//        editText2.setText(text2);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                chatPresenter.setText(s.toString(), 0);
                chatAdapter.notifyItemChanged(0);
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                chatPresenter.setText(s.toString(), 1);
                chatAdapter.notifyItemChanged(1);
            }
        });

    }
}
