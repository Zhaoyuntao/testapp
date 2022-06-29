package com.test.test3app.activity;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import base.ui.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.fastrecordview.AudioRecordView;
import com.test.test3app.fastrecordview.DoubleSwitchView;
import com.test.test3app.fastrecordview.FastRecordView;
import com.test.test3app.fastrecordviewnew.ZImageButton;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;

public class Activity_2_record extends BaseActivity {

    DoubleSwitchView changeIconButton;
    AudioRecordView audioRecordView;
    FastRecordView fastRecordView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        listView = findViewById(R.id.listview);
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        listView.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(activity());
                textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setText(list.get(position));
                return textView;
            }
        });
        findViewById(R.id.testclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                audioRecordView.stopRecord();
                changeIconButton.nextIndex();
            }
        });

//        audioRecordView = findViewById(R.id.fastrecord);
        fastRecordView = findViewById(R.id.fastrecord);
//        audioRecordView.setAutoUpdateTime(true);

        changeIconButton = findViewById(R.id.button);
        changeIconButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                S.s("click");
            }
        });
        ZImageButton imageButton = new ZImageButton(this);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageButton.setImageResource(R.drawable.ic_voice_message_record);
        ImageButton imageButton2 = new ImageButton(this);
        imageButton2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageButton2.setImageResource(R.drawable.ic_messages_delete);
        changeIconButton.setDefaultView(imageButton);
        changeIconButton.setSecondView(imageButton2);


        imageButton.setTouchConnection(fastRecordView);

//        audioRecordView.seztCallBack(new AudioRecordView.CallBack() {
//            @Override
//            public void whenStartRecord() {
//                S.s("started");
//            }
//
//            @Override
//            public void whenStopRecord(boolean needSend) {
//                S.s("stopped --------");
//                if (needSend) {
//                    audioRecordView.showSendAndDeleteBar();
//                }
//            }
//
//            @Override
//            public void whenCancelRecord() {
//                S.s("canceled --------");
//            }
//
//            @Override
//            public void whenSendClick() {
//                S.s("sent");
//            }
//
//            @Override
//            public void whenActionDown() {
//                S.s("down");
//            }
//
//            @Override
//            public void whenActionUp() {
//                S.s("up");
//            }
//
//            @Override
//            public void whenAbandonedVoice() {
//                S.s("abandoned ---");
//            }
//        })
    }

}
