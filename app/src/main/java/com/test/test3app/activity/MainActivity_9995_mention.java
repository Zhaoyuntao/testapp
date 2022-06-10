package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.mention.ChatEditText;
import com.test.test3app.mention.MentionListener;
import com.test.test3app.mention.TMention;
import com.test.test3app.mention.TTextWatcher;

import im.thebot.common.ui.chat.AutoSizeTextView;
import im.turbo.basetools.utils.InputMethodUtils;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.List;


public class MainActivity_9995_mention extends BaseActivity {
    ChatEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9995_mention);

        editText = findViewById(R.id.edit_text_mention);
        AutoSizeTextView addMentionView = findViewById(R.id.text_mention);
        AutoSizeTextView popView = findViewById(R.id.text_pop);

        TTextWatcher textWatcher = new TTextWatcher(editText);
        textWatcher.setMentionListener(new MentionListener() {
            @Override
            public void onSearchPeople(String contentAfterReferenceSymbol) {

            }

            @Override
            public void onStartMentionPeople() {
                addMentionView.setBackgroundColor(Color.parseColor("#ff0000"));
                addMentionView.setEnabled(true);
            }

            @Override
            public void onStopMentionPeople() {
                addMentionView.setBackgroundColor(Color.parseColor("#222222"));
                addMentionView.setEnabled(false);
            }
        });

        addMentionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textWatcher.addMention("12345", "Hello");
            }
        });
        addMentionView.setEnabled(false);

        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMention mention = textWatcher.popMention();
                String text = mention.getText().toString();
                List<String> uidList = mention.getUidList();
                S.s("pop text:[" + text + "], uidList:" + uidList);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodUtils.forceOpenInputKeyboard(editText);
                editText.setText("@");
            }
        }, 200);
    }
}
