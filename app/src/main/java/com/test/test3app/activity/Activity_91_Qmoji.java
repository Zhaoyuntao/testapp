package com.test.test3app.activity;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import base.ui.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.qmoji.QmojiSelectView;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.T;


public class Activity_91_Qmoji extends BaseActivity {

    TextView button_mute;
    TextView button_switch;
    TextView button_retouch;
    TextView button_qmoji;
    TextView button_hangup;
    TextView abc;
    QmojiSelectView qmojiSelectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity91__qmoji);
//        FontRequest fontRequest = null;
//            fontRequest = new FontRequest(
//                    "com.example.fontprovider",
//                    "com.example",
//                    "emoji compat Font Query",R.array.com_google_android_gms_fonts_certs);
//        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest).registerInitCallback(new EmojiCompat.InitCallback() {
//            @Override
//            public void onInitialized() {
//                super.onInitialized();
//                S.s("emoji");
//
//            }
//        });
//        EmojiCompat.init(config);

        abc=findViewById(R.id.abc);
        Typeface typeface = ResourcesCompat.getFont(this,R.font.abcc);
        abc.setTypeface(typeface);

//        CharSequence processed = EmojiCompat.get().process("\uD83E\uDDBE");
        abc.setText("\uD83E\uDDBE");

        button_mute = findViewById(R.id.btn_mute);
        button_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        button_switch = findViewById(R.id.btn_facing);
        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        button_retouch = findViewById(R.id.btn_beauty);
        button_retouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        button_qmoji = findViewById(R.id.btn_qmoji);
        button_qmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("1111");
                show();
            }
        });
        button_hangup = findViewById(R.id.btn_hangup);
        button_hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        qmojiSelectView=findViewById(R.id.qmojiSelectView);
        qmojiSelectView.addQmojiItem(0,R.drawable.ic_send);
        qmojiSelectView.addQmojiItem(1,R.drawable.ic_messages_delete);
        qmojiSelectView.addQmojiItem(2,R.drawable.ic_mic);
        qmojiSelectView.addQmojiItem(3,R.drawable.ic_lock_btm);
        qmojiSelectView.addQmojiItem(4,R.drawable.ic_lock_top);
        qmojiSelectView.addQmojiItem(5,R.drawable.ic_messages_delete);
        qmojiSelectView.addQmojiItem(6,R.drawable.ic_messages_delete);
        qmojiSelectView.addQmojiItem(7,R.drawable.ic_messages_delete);
        qmojiSelectView.addQmojiItem(8,R.drawable.ic_messages_delete);
        qmojiSelectView.addQmojiItem(9,R.drawable.ic_messages_delete);
        qmojiSelectView.setOnItemClickListener(new QmojiSelectView.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                T.t(activity(),"Qmoji:"+index);
            }
        });
    }

    public void show() {
        qmojiSelectView.setVisibility(View.VISIBLE);
    }
}
